package astra.core.api.holograms.api;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;
import astra.core.nms.NMS;
import astra.core.nms.interfaces.entity.IArmorStand;
import astra.core.nms.interfaces.entity.IItem;
import astra.core.nms.interfaces.entity.ISlime;
import astra.core.utils.StringUtils;

public class HologramLine {
  private Location location;
  private IArmorStand armor;
  private ISlime slime;
  private IItem item;
  private TouchHandler touch;
  private PickupHandler pickup;
  private String line;
  private Hologram hologram;
  private boolean isSpawned;
  private final Object lock = new Object();

  public HologramLine() {
    // Empty constructor for object pooling
  }

  public void init(Hologram hologram, Location location, String line) {
    this.line = StringUtils.formatColors(line);
    this.location = location.clone(); // Clone to prevent external modifications
    this.hologram = hologram;
    this.isSpawned = false;
  }

  public void spawn() {
    synchronized(lock) {
      if (isSpawned) return;
      
      if (this.armor == null) {
        this.armor = NMS.createArmorStand(location, line, this);
        
        if (this.touch != null) {
          this.setTouchable(this.touch);
        }
      }
      
      isSpawned = true;
    }
  }

  public void despawn() {
    synchronized(lock) {
      if (!isSpawned) return;
      
      if (this.armor != null) {
        this.armor.killEntity();
        this.armor = null;
      }
      if (this.slime != null) {
        this.slime.killEntity();
        this.slime = null;
      }
      if (this.item != null) {
        this.item.killEntity();
        this.item = null;
      }
      
      isSpawned = false;
    }
  }

  public void setTouchable(TouchHandler touch) {
    synchronized(lock) {
      if (touch == null) {
        if (this.slime != null) {
          this.slime.killEntity();
          this.slime = null;
        }
        this.touch = null;
        return;
      }

      if (armor != null) {
        if (this.slime == null) {
          this.slime = NMS.createSlime(location, this);
        }

        if (this.slime != null) {
          this.slime.setPassengerOf(this.armor.getEntity());
        }

        this.touch = touch;
      }
    }
  }

  public void setItem(ItemStack item, PickupHandler pickup) {
    if (pickup == null) {
      this.item.killEntity();
      this.item = null;
      this.pickup = null;
      return;
    }

    if (armor != null) {
      this.item = this.item == null ? NMS.createItem(location, item, this) : this.item;

      if (this.item != null) {
        this.item.setPassengerOf(this.armor.getEntity());
      }

      this.pickup = pickup;
    }
  }

  public void setLine(String newLine) {
    synchronized(lock) {
      if (newLine == null) return;
      this.line = StringUtils.formatColors(newLine);
      if (this.armor != null) {
        this.armor.setName(this.line);
      }
    }
  }

  public void setLocation(Location newLocation) {
    synchronized(lock) {
      if (newLocation == null) return;
      this.location = newLocation.clone();
      if (this.armor != null) {
        this.armor.setLocation(location.getX(), location.getY(), location.getZ());
      }
    }
  }

  public void reset() {
    synchronized(lock) {
      despawn();
      this.location = null;
      this.line = null;
      this.hologram = null;
      this.touch = null;
      this.pickup = null;
    }
  }

  // Getters
  public Location getLocation() {
    return location != null ? location.clone() : null;
  }

  public IArmorStand getArmor() {
    return armor;
  }

  public ISlime getSlime() {
    return slime;
  }

  public IItem getItem() {
    return item;
  }

  public TouchHandler getTouchHandler() {
    return touch;
  }

  public PickupHandler getPickupHandler() {
    return pickup;
  }

  public String getLine() {
    return line;
  }

  public Hologram getHologram() {
    return hologram;
  }

  public boolean isSpawned() {
    return isSpawned;
  }
}
