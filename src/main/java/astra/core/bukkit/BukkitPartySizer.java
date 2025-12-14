package astra.core.bukkit;

import org.bukkit.entity.Player;
import astra.core.Core;
import astra.core.plugin.AuroraConfig;

import java.util.LinkedHashMap;
import java.util.Map;

public class BukkitPartySizer {

  private static final AuroraConfig CONFIG;
  private static final Map<String, Integer> SIZES;

  static {
    CONFIG = Core.getInstance().getConfig("utils");
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

    CONFIG.set("party.size." + role, size);

    SIZES = new LinkedHashMap<>();
    for (String key : CONFIG.getSection("party.size").getKeys(false)) {
      SIZES.put(key.replace("_", "."), CONFIG.getInt("party.size." + key));
    }
  }

  public static int getPartySize(Player player) {
    for (Map.Entry<String, Integer> entry : SIZES.entrySet()) {
      if (player.hasPermission(entry.getKey())) {
        return entry.getValue();
      }
    }

    return 3;
  }
}
