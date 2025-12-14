package astra.core.redis;

import astra.core.Core;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class SpigotServerManager implements IServerManager {
    
    @Getter
    private static SpigotServerManager instance;
    
    @Getter
    private final RedisManager redis;
    private final Logger logger;
    private final Gson gson;
    @Getter
    private final Map<String, ServerStatus> servers;
    private Thread listenerThread;
    private JedisPubSub pubSub;
    
    public SpigotServerManager(RedisManager redis, Logger logger) {
        instance = this;
        this.redis = redis;
        this.logger = logger;
        this.gson = new GsonBuilder().create();
        this.servers = new ConcurrentHashMap<>();
        
        startListening();
    }
    
    private void startListening() {
        this.listenerThread = new Thread(() -> {
            try {
                this.pubSub = new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        if (channel.equals("astra:server:status")) {
                            try {
                                ServerStatus status = gson.fromJson(message, ServerStatus.class);
                                servers.put(status.getName(), status);
                            } catch (Exception e) {
                                logger.warning("Failed to parse server status: " + e.getMessage());
                            }
                        }
                    }
                };
                redis.subscribe(this.pubSub, "astra:server:status");
            } catch (Exception e) {
                logger.severe("Error in Redis listener thread: " + e.getMessage());
                e.printStackTrace();
            }
        }, "Redis-Server-Listener");
        this.listenerThread.start();
    }
    
    @Override
    public void connectToGameServer(String playerName, String gameType) {
        Player player = Core.getInstance().getServer().getPlayer(playerName);
        if (player == null || !player.isOnline()) {
            return;
        }
        
        for (ServerStatus server : servers.values()) {
            if (server.getType().equalsIgnoreCase(gameType) && 
                server.getState().equals("ONLINE") && 
                server.getOnlinePlayers() < server.getMaxPlayers()) {
                
                try (Jedis jedis = redis.getPool().getResource()) {
                    ConnectRequest request = new ConnectRequest(playerName, server.getName());
                    String json = gson.toJson(request);
                    jedis.publish("astra:server:connect", json);
                    return;
                } catch (Exception e) {
                    logger.warning("Failed to send connect request via Redis: " + e.getMessage());
                    player.sendMessage("§cErro ao conectar ao servidor. Tente novamente.");
                }
                return;
            }
        }
        
        player.sendMessage("§cNão há conexões disponíveis.");
    }
    
    private static class ConnectRequest {
        private final String playerName;
        private final String targetServer;

        public ConnectRequest(String playerName, String targetServer) {
            this.playerName = playerName;
            this.targetServer = targetServer;
        }
    }
    
    public void shutdown() {
        if (this.pubSub != null) {
            this.pubSub.unsubscribe();
        }
        if (this.listenerThread != null) {
            this.listenerThread.interrupt();
        }
    }

}