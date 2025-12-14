package astra.core.redis;

import astra.core.Core;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.logging.Logger;

public class RedisManager {
    
    @Getter
    private static RedisManager instance;
    
    @Getter
    private JedisPool pool;
    private final String host;
    private final int port;
    private final String password;
    private final int timeout;
    private final int database;
    private final Logger logger;
    
    public RedisManager(FileConfiguration config, Logger logger) {
        instance = this;
        this.logger = logger;
        
        // Load configuration
        this.host = config.getString("redis.host", "localhost");
        this.port = config.getInt("redis.port", 6379);
        this.password = config.getString("redis.password", "");
        this.timeout = config.getInt("redis.timeout", 5000);
        this.database = config.getInt("redis.database", 0);

        Core.getInstance().getLogger().info("Iniciando conexão com Redis em " + host + ":" + port);
        if (!password.isEmpty()) {
            logger.info("Usando autenticação Redis");
        }
        
        connect();
    }

    private void connect() {
        try {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(8);
            poolConfig.setMaxIdle(8);
            poolConfig.setMinIdle(0);
            poolConfig.setTestOnBorrow(true);
            poolConfig.setTestOnReturn(true);
            poolConfig.setTestWhileIdle(true);
            
            if (password != null && !password.isEmpty()) {
                pool = new JedisPool(poolConfig, host, port, timeout, password, database);
            } else {
                pool = new JedisPool(poolConfig, host, port, timeout, null, database);
            }
            
            // Test the connection
            try (Jedis jedis = pool.getResource()) {
                String pong = jedis.ping();
                if ("PONG".equalsIgnoreCase(pong)) {
                    Core.getInstance().getLogger().info("✓ Conexão Redis estabelecida com sucesso!");
                }
            }
        } catch (JedisConnectionException e) {
            logger.severe("✗ Falha ao conectar ao servidor Redis em " + host + ":" + port);
            logger.severe("✗ Erro: " + e.getMessage());
            if (e.getCause() != null) {
                logger.severe("✗ Causa: " + e.getCause().getMessage());
            }
            throw e; // Re-throw to signal connection failure
        } catch (Exception e) {
            logger.severe("✗ Erro inesperado ao conectar ao Redis");
            e.printStackTrace();
            throw e; // Re-throw to signal connection failure
        }
    }
    
    public void disconnect() {
        if (pool != null && !pool.isClosed()) {
            pool.close();
            logger.info("Disconnected from Redis server");
        }
    }
    
    public boolean isConnected() {
        if (pool == null || pool.isClosed()) {
            return false;
        }
        
        try (Jedis jedis = pool.getResource()) {
            return "PONG".equalsIgnoreCase(jedis.ping());
        } catch (Exception e) {
            return false;
        }
    }
    
    public void set(String key, String value) {
        try (Jedis jedis = pool.getResource()) {
            jedis.set(key, value);
        } catch (Exception e) {
            logger.warning("Failed to set Redis key: " + key);
            logger.warning("Error: " + e.getMessage());
        }
    }
    
    public String get(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.get(key);
        } catch (Exception e) {
            logger.warning("Failed to get Redis key: " + key);
            logger.warning("Error: " + e.getMessage());
            return null;
        }
    }
    
    public void delete(String key) {
        try (Jedis jedis = pool.getResource()) {
            jedis.del(key);
        } catch (Exception e) {
            logger.warning("Failed to delete Redis key: " + key);
            logger.warning("Error: " + e.getMessage());
        }
    }
    
    public void publish(String channel, String message) {
        try (Jedis jedis = pool.getResource()) {
            jedis.publish(channel, message);
        } catch (Exception e) {
            logger.warning("Failed to publish message to channel: " + channel);
            logger.warning("Error: " + e.getMessage());
        }
    }
    
    public void subscribe(JedisPubSub pubSub, String... channels) {
        try (Jedis jedis = pool.getResource()) {
            jedis.subscribe(pubSub, channels);
        } catch (Exception e) {
            logger.warning("Failed to subscribe to channels");
            logger.warning("Error: " + e.getMessage());
        }
    }
} 