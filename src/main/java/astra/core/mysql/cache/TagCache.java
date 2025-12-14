package astra.core.mysql.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TagCache {

    private static final Map<String, CacheData> CACHE = new HashMap<>();

    private static class CacheData {
        long expirationTime;
        String role;
        String realName;

        CacheData(long expirationTime, String role, String realName) {
            this.expirationTime = expirationTime;
            this.role = role;
            this.realName = realName;
        }
    }

    public static void setCache(String playerName, String role, String realName) {
        long expirationTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30);
        CacheData data = new CacheData(expirationTime, role, realName);
        CACHE.put(playerName.toLowerCase(), data);
    }

    public static String get(String playerName) {
        CacheData data = CACHE.get(playerName.toLowerCase());
        if (data == null || isExpired(data)) {
            return null;
        }
        return data.role + " : " + data.realName;
    }

    public static boolean isPresent(String playerName) {
        CacheData data = CACHE.get(playerName.toLowerCase());
        return data != null && !isExpired(data);
    }

    private static boolean isExpired(CacheData data) {
        return data.expirationTime < System.currentTimeMillis();
    }

    public static TimerTask clearCache() {
        return new TimerTask() {
            @Override
            public void run() {
                CACHE.entrySet().removeIf(entry -> isExpired(entry.getValue()));
            }
        };
    }
}

