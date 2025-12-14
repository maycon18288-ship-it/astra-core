package astra.core.api.holograms.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import astra.core.api.holograms.HologramLibrary;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Hologram {
  private final UUID uniqueId;
  private String attached;
  private boolean spawned;
  private Location location;
  private final Map<Integer, HologramLine> lines;
  private final Object lock = new Object();

  public Hologram(Location location, String... lines) {
    this.uniqueId = UUID.randomUUID();
    this.location = location.clone();
    this.lines = new ConcurrentHashMap<>();
    this.spawned = false;

    int current = 0;
    for (String line : lines) {
      HologramLine hologramLine = HologramLibrary.obtainLine();
      hologramLine.init(this, location.clone().add(0, 0.33 * ++current, 0), line);
      this.lines.put(current, hologramLine);
    }
  }

  public Hologram spawn() {
    synchronized(lock) {
      if (spawned) {
        return this;
      }

      this.lines.values().forEach(HologramLine::spawn);
      this.spawned = true;
      return this;
    }
  }

  public Hologram despawn() {
    synchronized(lock) {
      if (!spawned) {
        return this;
      }

      this.lines.values().forEach(HologramLine::despawn);
      this.spawned = false;
      return this;
    }
  }

  public Hologram withLine(String line) {
    synchronized(lock) {
      int l = 1;
      while (this.lines.containsKey(l)) {
        l++;
      }

      HologramLine hologramLine = HologramLibrary.obtainLine();
      hologramLine.init(this, this.location.clone().add(0, 0.33 * l, 0), line);
      this.lines.put(l, hologramLine);
      
      if (spawned) {
        hologramLine.spawn();
      }

      return this;
    }
  }

  public Hologram updateLine(int id, String line) {
    synchronized(lock) {
      HologramLine hologramLine = this.lines.get(id);
      if (hologramLine != null) {
        hologramLine.setLine(line);
      }
      return this;
    }
  }

  public void setLocation(Location newLocation) {
    synchronized(lock) {
      if (newLocation == null) return;
      
      Location loc = newLocation.clone();
      this.location = loc;
      
      this.lines.forEach((id, line) -> {
        Location lineLoc = loc.clone().add(0, 0.33 * id, 0);
        line.setLocation(lineLoc);
      });
    }
  }

  public void setAttached(String player) {
    this.attached = player;
  }

  public boolean canSee(Player player) {
    return this.attached == null || this.attached.equals(player.getName());
  }

  public UUID getUniqueId() {
    return uniqueId;
  }
  
  public boolean isSpawned() {
    return this.spawned;
  }
  
  public Location getLocation() {
    return this.location.clone();
  }

  public HologramLine getLine(int id) {
    return this.lines.get(id);
  }
  
  public Collection<HologramLine> getLines() {
    return Collections.unmodifiableCollection(this.lines.values());
  }

  public Map<Integer, HologramLine> getLinesMap() {
    return Collections.unmodifiableMap(this.lines);
  }
}
