package astra.core.redis;

import astra.core.Core;
import astra.core.bungee.Bungee;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.net.InetSocketAddress;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ServerManager {

    private static final String CHANNEL_SERVER_STATUS = "astra:server:status";
    private static final String CHANNEL_SERVER_CONNECT = "astra:server:connect";
    private static final String KEY_SERVERS = "astra:servers";
    private static final String KEY_SERVER_PLAYERS = "astra:server:players:";
    
    @Getter
    private static ServerManager instance;
    private final BungeeRedisManager redis;
    private final Logger logger;
    private final Gson gson;
    @Getter
    private final Map<String, ServerStatus> servers;
    private final Map<String, ServerInfo> serverInfoCache;
    
    public ServerManager(BungeeRedisManager redis, Logger logger) {
        instance = this;
        this.redis = redis;
        this.logger = logger;
        this.gson = new GsonBuilder().create();
        this.servers = new ConcurrentHashMap<>();
        this.serverInfoCache = new ConcurrentHashMap<>();
        
        loadServersFromConfig();
        startListening();
        startServerStatusUpdater();
    }
    
    private void loadServersFromConfig() {
        Configuration config = Bungee.getInstance().getServersConfig();
        if (config == null) {
            return;
        }

        Configuration items = config.getSection("items");
        if (items == null) {
            return;
        }

        for (String key : items.getKeys()) {
            if (key.equals("fecharmenu") || key.equals("info")) continue;
            
            Configuration serverConfig = items.getSection(key);
            List<String> serverNames = serverConfig.getStringList("servernames");
            String icon = serverConfig.getString("icon", "STONE");
            int slot = serverConfig.getInt("slot", 0);
            boolean showInMenu = serverConfig.getBoolean("show_in_menu", true);
            
            if (serverNames.isEmpty()) {
                logger.warning("✗ Nenhum servidor configurado para o tipo: " + key);
                continue;
            }
            
            for (String serverInfo : serverNames) {
                String[] parts = serverInfo.split(";");
                if (parts.length != 2) {
                    logger.warning("✗ Formato inválido para servidor '" + serverInfo + "'. Formato correto: 'ip:porta ; nome'");
                    continue;
                }
                
                String address = parts[0].trim();
                String name = parts[1].trim();

                ServerStatus status = new ServerStatus(
                    name,
                    key,
                    serverConfig.getInt("max-players", 500),
                    0,
                    "OFFLINE",
                    icon,
                    slot,
                    showInMenu
                );
                
                servers.put(name, status);
                
                try {
                    String[] addressParts = address.split(":");
                    serverInfoCache.put(name, Bungee.getInstance().getProxy().constructServerInfo(
                        name,
                        new InetSocketAddress(
                            addressParts[0],
                            Integer.parseInt(addressParts[1])
                        ),
                        "",
                        false
                    ));
                    Bungee.getInstance().getLogger().info("✓ Servidor " + name + " registrado com sucesso!");
                    
                    try (Jedis jedis = redis.getPool().getResource()) {
                        String statusJson = gson.toJson(status);
                        jedis.hset(KEY_SERVERS, name, statusJson);
                        jedis.publish(CHANNEL_SERVER_STATUS, statusJson);
                    } catch (Exception e) {
                        logger.warning("✗ Erro ao publicar status do servidor " + name + " no Redis: " + e.getMessage());
                    }
                } catch (Exception e) {
                    logger.severe("✗ Erro ao registrar servidor " + name + ": " + e.getMessage());
                }
            }
        }
        
        Bungee.getInstance().getLogger().info("Total: " + servers.size() + " servidores carregados.");
    }
    
    private void startServerStatusUpdater() {
        Bungee.getInstance().getProxy().getScheduler().schedule(
            Bungee.getInstance(),
            () -> {
                try (Jedis jedis = redis.getPool().getResource()) {
                    for (Map.Entry<String, ServerStatus> entry : servers.entrySet()) {
                        String serverName = entry.getKey();
                        ServerInfo serverInfo = Bungee.getInstance().getProxy().getServerInfo(serverName);
                        
                        if (serverInfo != null) {
                            int players = serverInfo.getPlayers().size();
                            jedis.hset(KEY_SERVER_PLAYERS, serverName, String.valueOf(players));
                            
                            ServerStatus oldStatus = entry.getValue();
                            ServerStatus newStatus = new ServerStatus(
                                oldStatus.getName(),
                                oldStatus.getType(),
                                oldStatus.getMaxPlayers(),
                                players,
                                "ONLINE",
                                oldStatus.getIcon(),
                                oldStatus.getSlot(),
                                oldStatus.isShowInMenu()
                            );
                            
                            servers.put(serverName, newStatus);
                            jedis.publish(CHANNEL_SERVER_STATUS, gson.toJson(newStatus));
                        }
                    }
                } catch (Exception e) {
                    logger.warning("Erro ao atualizar status dos servidores: " + e.getMessage());
                }
            },
            1,
            1,
            TimeUnit.SECONDS
        );
    }
    
    private void startListening() {
        new Thread(() -> {
            try {
                try (Jedis jedis = redis.getPool().getResource()) {
                    jedis.subscribe(new JedisPubSub() {
                        @Override
                        public void onMessage(String channel, String message) {
                            if (channel.equals(CHANNEL_SERVER_STATUS)) {
                                handleServerStatus(message);
                            } else if (channel.equals(CHANNEL_SERVER_CONNECT)) {
                                handleServerConnect(message);
                            }
                        }
                    }, CHANNEL_SERVER_STATUS, CHANNEL_SERVER_CONNECT);
                }
            } catch (Exception e) {
                logger.severe("Erro ao iniciar listener do Redis: " + e.getMessage());
            }
        }, "Redis-Listener").start();
    }
    
    private void handleServerStatus(String message) {
        try {
            ServerStatus status = gson.fromJson(message, ServerStatus.class);
            servers.put(status.getName(), status);
            
            try (Jedis jedis = redis.getPool().getResource()) {
                jedis.hset(KEY_SERVERS, status.getName(), message);
            }
        } catch (Exception ignored) {
        }
    }
    
    private void handleServerConnect(String message) {
        try {
            ConnectRequest request = gson.fromJson(message, ConnectRequest.class);
            ProxiedPlayer player = Bungee.getInstance().getProxy().getPlayer(request.getPlayerName());
            
            if (player != null && player.isConnected()) {
                ServerInfo server = Bungee.getInstance().getProxy().getServerInfo(request.getTargetServer());
                if (server != null) {
                    player.connect(server);
                }
            }
        } catch (Exception ignored) {
        }
    }
    
    public void connectToServer(ProxiedPlayer player, String serverName) {
        try {
            ServerInfo serverInfo = serverInfoCache.get(serverName);
            if (serverInfo == null) {
                serverInfo = Bungee.getInstance().getProxy().getServerInfo(serverName);
            }
            
            if (serverInfo != null) {
                ConnectRequest request = new ConnectRequest(player.getName(), serverName);
                try (Jedis jedis = redis.getPool().getResource()) {
                    jedis.publish(CHANNEL_SERVER_CONNECT, gson.toJson(request));
                }
                
                player.connect(serverInfo);
            }
        } catch (Exception ignored) {
        }
    }
    
    public void connectToGameServer(String playerName, String gameType) {
        List<ServerStatus> availableServers = getServersByType(gameType).stream()
            .filter(server -> server.getOnlinePlayers() < server.getMaxPlayers())
            .collect(Collectors.toList());
            
        if (!availableServers.isEmpty()) {
            ServerStatus bestServer = availableServers.get(0);
            try (Jedis jedis = redis.getPool().getResource()) {
                ConnectRequest request = new ConnectRequest(playerName, bestServer.getName());
                jedis.publish(CHANNEL_SERVER_CONNECT, gson.toJson(request));
            } catch (Exception e) {
                logger.warning("Erro ao enviar pedido de conexão para " + playerName + " ao servidor " + bestServer.getName());
            }
        }
    }
    
    public List<ServerStatus> getServersByType(String type) {
        return servers.values().stream()
                .filter(server -> server.getType().equalsIgnoreCase(type))
                .sorted(Comparator.comparingInt(ServerStatus::getOnlinePlayers))
                .collect(Collectors.toList());
    }
    
    @Getter
    public static class ServerStatus {
        private final String name;
        private final String type;
        private final int maxPlayers;
        private final int onlinePlayers;
        private final String state;
        private final long lastUpdate;
        private final String icon;
        private final int slot;
        private final boolean showInMenu;
        
        public ServerStatus(String name, String type, int maxPlayers, int onlinePlayers, String state, String icon, int slot, boolean showInMenu) {
            this.name = name;
            this.type = type;
            this.maxPlayers = maxPlayers;
            this.onlinePlayers = onlinePlayers;
            this.state = state;
            this.lastUpdate = System.currentTimeMillis();
            this.icon = icon;
            this.slot = slot;
            this.showInMenu = showInMenu;
        }
    }
    
    @Getter
    private static class ConnectRequest {
        private final String playerName;
        private final String targetServer;
        
        public ConnectRequest(String playerName, String targetServer) {
            this.playerName = playerName;
            this.targetServer = targetServer;
        }
    }
} 