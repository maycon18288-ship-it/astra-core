package astra.core.api.titles;

import astra.core.Core;
import astra.core.player.Account;
import astra.core.utils.AnimatedString;
import astra.core.utils.StringUtils;
import com.comphenix.packetwrapper.WrapperPlayServerEntityDestroy;
import com.comphenix.packetwrapper.WrapperPlayServerEntityMetadata;
import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class TitleController {

  @Getter
  public Player owner;
  private WrappedDataWatcher watcher;
  private boolean disabled = true;
  private final int entityId;
  private final Title title;
  private BukkitTask animationTask;

  public TitleController(Player owner, Title title) {
    this.owner = owner;
    this.title = title;
    this.watcher = new WrappedDataWatcher();
    this.watcher.setObject(0, (byte) 0x20);
    this.watcher.setObject(2, title.getTitle());
    this.watcher.setObject(3, (byte) 1);
    this.watcher.setObject(5, (byte) 1);
    this.watcher.setObject(10, (byte) (0x01 | 0x08 | 0x10));
    this.entityId = generateEntityId();
    this.startAnimationTask();
  }

  private void startAnimationTask() {
    if (this.animationTask != null) {
      this.animationTask.cancel();
    }

    switch (this.title.getAnimation()) {
      case NONE: {
        if (!title.getTitle().contains("{")) break; // Don't run task if there are no placeholders
        this.animationTask = new BukkitRunnable() {
          @Override
          public void run() {
            if (owner == null || !owner.isOnline()) {
              cancel();
              return;
            }
            Account profile = Account.getAccount(owner.getName());
            if (profile == null) return;
            String updatedTitle = title.getTitle()
                    .replace("{levelsw}", StringUtils.formatNumber(profile.getDataContainer("SkyWars", "level").getAsInt()))
                    .replace("{levelbw}", StringUtils.formatNumber(profile.getDataContainer("BedWars", "level").getAsInt()))
                    ;

            setName(updatedTitle);
          }
        }.runTaskTimer(Core.getInstance(), 0L, 20L);
        break;
      }
      case RAINBOW: {
        this.animationTask = new BukkitRunnable() {
          @Override
          public void run() {
            if (owner == null || !owner.isOnline()) {
              cancel();
              return;
            }
            setName(TitleManager.getRainbowTitle(title.getName()));
          }
        }.runTaskTimer(Core.getInstance(), 0L, 3L);
        break;
      }
      case FADE: {
        AnimatedString wave = new AnimatedString(title.getName(), "§f§l", "§e§l", "§6§l");
        this.animationTask = new BukkitRunnable() {
          @Override
          public void run() {
            if (owner == null || !owner.isOnline()) {
              cancel();
              return;
            }
            setName("§6✯ " + wave.next() + "§r§6 ✯");
          }
        }.runTaskTimer(Core.getInstance(), 0L, 2L);
        break;
      }
      case ASTRA: {
        AnimatedString wave = new AnimatedString(title.getName(), "§f§l", "§d§l", "§5§l");
        this.animationTask = new BukkitRunnable() {
          @Override
          public void run() {
            if (owner == null || !owner.isOnline()) {
              cancel();
              return;
            }
            setName("§e✯ " + wave.next() + "§r§e ✯");
          }
        }.runTaskTimer(Core.getInstance(), 0L, 2L);
        break;
      }
    }
  }

  private int generateEntityId() {
    return (int) (Math.random() * Integer.MAX_VALUE);
  }

  public void setName(String name) {
    if (this.watcher.getString(2).equals("disabled")) {
      this.watcher.setObject(2, name);
      Account.listProfiles().forEach(profile -> {
        Player player = profile.getPlayer();
        if (player != null && player.canSee(this.owner)) {
          showToPlayer(player);
        }
      });
      return;
    }

    this.watcher.setObject(2, name);
    if (name.equals("disabled")) {
      Account.listProfiles().forEach(profile -> {
        Player player = profile.getPlayer();
        if (player != null && player.canSee(this.owner)) {
          hideToPlayer(player);
        }
      });
      return;
    }

    WrapperPlayServerEntityMetadata metadata = new WrapperPlayServerEntityMetadata();
    metadata.setEntityId(this.entityId);
    metadata.setEntityMetadata(this.watcher.getWatchableObjects());

    Account.listProfiles().forEach(profile -> {
      Player player = profile.getPlayer();
      if (player != null && player.canSee(this.owner)) {
        metadata.sendPacket(player);
      }
    });
  }

  public void destroy() {
    this.disable();
    if (this.animationTask != null) {
      this.animationTask.cancel();
      this.animationTask = null;
    }
    this.owner = null;
    this.watcher = null;
  }

  public void enable() {
    if (!this.disabled) {
      return;
    }

    this.disabled = false;
    Account.listProfiles().forEach(profile -> {
      Player player = profile.getPlayer();
      if (player != null && player.canSee(this.owner)) {
        this.showToPlayer(player);
      }
    });
  }

  public void disable() {
    if (this.disabled) {
      return;
    }

    Account.listProfiles().forEach(profile -> {
      Player player = profile.getPlayer();
      if (player != null && player.canSee(this.owner)) {
        this.hideToPlayer(player);
      }
    });
    this.disabled = true;
  }

  void showToPlayer(Player player) {
    if (!this.disabled && !this.watcher.getString(2).equals("disabled")) {
      WrapperPlayServerSpawnEntityLiving spawn = new WrapperPlayServerSpawnEntityLiving();
      spawn.setType(EntityType.ARMOR_STAND);
      spawn.setEntityID(this.entityId);
      spawn.setMetadata(this.watcher);
      spawn.setX(this.owner.getLocation().getX());
      spawn.setY(this.owner.getLocation().getY() + 2.0);
      spawn.setZ(this.owner.getLocation().getZ());

      spawn.sendPacket(player);
    }
  }

  void hideToPlayer(Player player) {
    if (!this.disabled) {
      WrapperPlayServerEntityDestroy destroy = new WrapperPlayServerEntityDestroy();
      destroy.setEntities(new int[]{this.entityId});

      destroy.sendPacket(player);
    }
  }

  public void updatePosition() {
    if (!this.disabled && this.owner != null && !this.watcher.getString(2).equals("disabled")) {
      PacketPlayOutEntityTeleport teleport = new PacketPlayOutEntityTeleport(
              this.entityId,
              (int) (this.owner.getLocation().getX() * 32.0),
              (int) ((this.owner.getLocation().getY() + 2.3) * 32.0),
              (int) (this.owner.getLocation().getZ() * 32.0),
              (byte) 0,
              (byte) 0,
              false
      );

      for (Player player : this.owner.getWorld().getPlayers()) {
        if (player != null && player.canSee(this.owner)) {
          ((CraftPlayer) player).getHandle().playerConnection.sendPacket(teleport);
        }
      }
    }
  }
}
