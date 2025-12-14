package astra.core.bungee.party;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import astra.core.bungee.Bungee;

import java.util.LinkedHashMap;
import java.util.Map;

public class BungeePartySizer {

  private static final Configuration CONFIG;
  private static final Map<String, Integer> SIZES;

  static {
    CONFIG = Bungee.getInstance().getConfig();
    String role = "role_master"; // Exemplo de papel
    int size;

    switch (role) {
      case "role_master":
        size = 20;
        break;
      case "role_youtuber":
        size = 15;
        break;
      case "role_mvpplus":
        size = 10;
        break;
      case "role_mvp":
        size = 5;
        break;
      default:
        size = 0;
        break;
    }

    SIZES = new LinkedHashMap<>();
    for (String key : CONFIG.getSection("party.size").getKeys()) {
      SIZES.put(key.replace("_", "."), CONFIG.getInt("party.size." + key));
    }
  }

  public static int getPartySize(ProxiedPlayer player) {
    for (Map.Entry<String, Integer> entry : SIZES.entrySet()) {
      if (player.hasPermission(entry.getKey())) {
        return entry.getValue();
      }
    }

    return 3;
  }
}
