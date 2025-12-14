package astra.core.servers;

import astra.core.servers.balancer.type.*;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;
import astra.core.Core;
import astra.core.player.Account;
import astra.core.plugin.AuroraConfig;
import astra.core.servers.balancer.BaseBalancer;
import astra.core.servers.balancer.Server;
import astra.core.servers.balancer.type.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class ServerItem {

  private final String key;
  private final int slot;
  private final String icon;
  private final BaseBalancer<Server> balancer;
  private final List<Server> fallbackServers;
  private final Map<String, Integer> serverWeights;

  public ServerItem(String key, int slot, String icon, String balancerType) {
    this.key = key != null ? key : "unknown";
    this.slot = slot;
    this.icon = icon != null ? icon : "STONE";
    this.fallbackServers = new ArrayList<>();
    this.serverWeights = new ConcurrentHashMap<>();
    
    String type = balancerType != null ? balancerType.toLowerCase() : "leastconnection";
    switch (type) {
      case "roundrobin":
        this.balancer = new RoundRobin();
        break;
      case "weightedroundrobin":
        this.balancer = new WeightedRoundRobin();
        break;
      case "leastresponse":
        this.balancer = new LeastResponse();
        break;
      case "healthaware":
        this.balancer = new HealthAware();
        break;
      default:
        this.balancer = new LeastConnection<>();
    }
  }

  public void connect(Account profile) {
    Server server = balancer.next();
    
    // Try fallback servers if primary fails
    if (server == null && !fallbackServers.isEmpty()) {
      for (Server fallback : fallbackServers) {
        if (fallback.canBeSelected()) {
          server = fallback;
          break;
        }
      }
    }
    
    if (server != null) {
      Account.sendServer(profile, server.getName());
    }
  }

  public void addServer(String serverInfo, boolean isFallback) {
    if (serverInfo.split(" ; ").length < 2) {
      return;
    }

    String[] parts = serverInfo.split(" ; ");
    Server server = new Server(parts[0], parts[1], CONFIG.getInt("items." + key + ".max-players"));
    
    if (isFallback) {
      fallbackServers.add(server);
    } else {
      balancer.add(parts[1], server);
    }

    // Add weight if specified
    if (parts.length > 2) {
      try {
        int weight = Integer.parseInt(parts[2]);
        serverWeights.put(parts[1], weight);
        if (balancer instanceof WeightedRoundRobin) {
          ((WeightedRoundRobin) balancer).setWeight(server, weight);
        }
      } catch (NumberFormatException ignored) {}
    }
  }

  private static final List<ServerItem> SERVERS = new ArrayList<>();
  public static final AuroraConfig CONFIG = Core.getInstance().getConfig("servers");
  public static final List<Integer> DISABLED_SLOTS = CONFIG.getIntegerList("disabled-slots");
  public static final Map<String, Integer> SERVER_COUNT = new ConcurrentHashMap<>();

  public static void setupServers() {

    if (CONFIG == null) {
      return;
    }
    
    if (CONFIG.getSection("items") == null) {
      return;
    }

    // Clear existing servers before reloading
    SERVERS.clear();
    SERVER_COUNT.clear();

    for (String key : CONFIG.getSection("items").getKeys(false)) {
      try {
        Core.getInstance().getLogger().info("Processando servidor: " + key);
        
        String icon = CONFIG.getString("items." + key + ".icon");
        String balancerType = CONFIG.getString("items." + key + ".balancer-type");
        int slot = CONFIG.getInt("items." + key + ".slot", 0);
        int maxPlayers = CONFIG.getInt("items." + key + ".max-players", 500);

        ServerItem si = new ServerItem(key, slot, icon, balancerType);
        SERVERS.add(si);

        // Add primary servers
        List<String> serverNames = CONFIG.getStringList("items." + key + ".servernames");
        if (serverNames != null && !serverNames.isEmpty()) {
          for (String server : serverNames) {
            try {
              si.addServer(server, false);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        } else {
        }

        // Add fallback servers if configured
        List<String> fallbackServers = CONFIG.getStringList("items." + key + ".fallback-servers");
        if (fallbackServers != null && !fallbackServers.isEmpty()) {
          for (String server : fallbackServers) {
            try {
              si.addServer(server, true);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }


    // Schedule health checks
    new BukkitRunnable() {
      @Override
      public void run() {
        SERVERS.forEach(server -> {
          server.getBalancer().getList().forEach(s -> {
            boolean isHealthy = s.canBeSelected();
          });
          server.getFallbackServers().forEach(s -> {
            boolean isHealthy = s.canBeSelected();
          });
        });
      }
    }.runTaskTimerAsynchronously(Core.getInstance(), 0, 40);
  }

  public static Collection<ServerItem> listServers() {
    return SERVERS;
  }

  public static ServerItem getServerItem(String key) {
    return SERVERS.stream()
        .filter(si -> si.getKey().equals(key))
        .findFirst()
        .orElse(null);
  }

  public static boolean alreadyQuerying(String servername) {
    return SERVERS.stream().anyMatch(si -> 
        si.getBalancer().keySet().contains(servername) ||
        si.getFallbackServers().stream().anyMatch(s -> s.getName().equals(servername)));
  }

  public static int getServerCount(ServerItem serverItem) {
    return serverItem.getBalancer().getTotalNumber();
  }

  public static int getServerCount(String servername) {
    return SERVER_COUNT.getOrDefault(servername, 0);
  }

  public static void reloadConfiguration() {
    Core.getInstance().reloadConfig();
    CONFIG.reload();
    setupServers();
  }
}
