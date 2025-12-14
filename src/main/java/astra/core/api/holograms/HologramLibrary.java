package astra.core.api.holograms;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import astra.core.Core;
import astra.core.api.holograms.api.Hologram;
import astra.core.api.holograms.api.HologramLine;
import astra.core.nms.NMS;
import astra.core.plugin.AuroraLogger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HologramLibrary {

  public static final AuroraLogger LOGGER = ((AuroraLogger) Core.getInstance().getLogger()).getModule("HOLOGRAMS");
  private static final int POOL_SIZE = 100; // Adjust based on typical server usage
  
  private static Plugin plugin;
  private static final Map<UUID, Hologram> holograms = new ConcurrentHashMap<>();
  private static final Queue<HologramLine> linePool = new LinkedList<>();
  private static final Map<Integer, Entity> entityCache = new ConcurrentHashMap<>();

  static {
    // Pre-populate the line pool
    for (int i = 0; i < POOL_SIZE; i++) {
      linePool.offer(new HologramLine());
    }
  }

  public static Hologram createHologram(Location location, List<String> lines) {
    return createHologram(location, lines.toArray(new String[0]));
  }

  public static Hologram createHologram(Location location, String... lines) {
    return createHologram(location, true, lines);
  }

  public static Hologram createHologram(Location location, boolean spawn, String... lines) {
    Hologram hologram = new Hologram(location, lines);
    if (spawn) {
      hologram.spawn();
    }
    holograms.put(hologram.getUniqueId(), hologram);
    return hologram;
  }

  public static void removeHologram(Hologram hologram) {
    if (hologram == null) return;
    
    // Return lines to pool
    hologram.getLines().forEach(line -> {
      line.despawn();
      recycleLine(line);
    });
    
    holograms.remove(hologram.getUniqueId());
    hologram.despawn();
  }

  public static void unregisterAll() {
    holograms.values().forEach(Hologram::despawn);
    holograms.clear();
    entityCache.clear();
    plugin = null;
  }

  public static Entity getHologramEntity(int entityId) {
    // Check cache first
    Entity cachedEntity = entityCache.get(entityId);
    if (cachedEntity != null && !cachedEntity.isDead()) {
      return cachedEntity;
    }

    // If not in cache or entity is dead, search and update cache
    for (Hologram hologram : listHolograms()) {
      if (hologram.isSpawned()) {
        for (HologramLine line : hologram.getLines()) {
          if (line.getArmor() != null && line.getArmor().getId() == entityId) {
            Entity entity = line.getArmor().getEntity();
            entityCache.put(entityId, entity);
            return entity;
          }
        }
      }
    }

    entityCache.remove(entityId);
    return null;
  }

  public static HologramLine obtainLine() {
    HologramLine line = linePool.poll();
    return line != null ? line : new HologramLine();
  }

  private static void recycleLine(HologramLine line) {
    if (linePool.size() < POOL_SIZE) {
      line.reset();
      linePool.offer(line);
    }
  }

  public static Hologram getHologram(Entity entity) {
    return NMS.getHologram(entity);
  }

  public static boolean isHologramEntity(Entity entity) {
    return NMS.isHologramEntity(entity);
  }

  public static Collection<Hologram> listHolograms() {
    return ImmutableList.copyOf(holograms.values());
  }

  public static void setupHolograms(Core pl) {
    if (plugin != null) {
      return;
    }

    plugin = pl;
    Bukkit.getPluginManager().registerEvents(new HologramListeners(), plugin);
    
    // Schedule periodic cache cleanup
    Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
      entityCache.entrySet().removeIf(entry -> 
        entry.getValue() == null || entry.getValue().isDead()
      );
    }, 20 * 60, 20 * 60); // Run every minute
  }

  public static Plugin getPlugin() {
    return plugin;
  }
}
