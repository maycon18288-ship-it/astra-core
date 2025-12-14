package astra.core.player.skin;

import java.util.HashMap;
import java.util.Map;

public class SkinCooldown {
    private static final Map<String, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_TIME = 30 * 1000; // 30 segundos em milissegundos

    public static boolean hasCooldown(String playerName) {
        if (!cooldowns.containsKey(playerName)) {
            return false;
        }
        
        long lastUsed = cooldowns.get(playerName);
        if (System.currentTimeMillis() - lastUsed >= COOLDOWN_TIME) {
            cooldowns.remove(playerName);
            return false;
        }
        
        return true;
    }

    public static void addCooldown(String playerName) {
        cooldowns.put(playerName, System.currentTimeMillis());
    }

    public static String getRemainingTime(String playerName) {
        if (!cooldowns.containsKey(playerName)) {
            return "0s";
        }

        long lastUsed = cooldowns.get(playerName);
        long remaining = (lastUsed + COOLDOWN_TIME - System.currentTimeMillis()) / 1000;
        
        if (remaining <= 0) {
            cooldowns.remove(playerName);
            return "0s";
        }

        long minutes = remaining / 60;
        long seconds = remaining % 60;
        
        if (minutes > 0) {
            return minutes + "m " + seconds + "s";
        }
        return seconds + "s";
    }
} 