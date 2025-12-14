package astra.core.api.bossbar;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class CustomBossbar {

    private final EntityWither wither;

    public CustomBossbar(Location location) {
        WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
        this.wither = new EntityWither(world);

        wither.setInvisible(true);
        wither.setCustomName("§5§lAURORA");
        wither.setCustomNameVisible(true);
        wither.setHealth(wither.getMaxHealth());
        wither.setLocation(location.getX(), location.getY(), location.getZ(), 0, 0);
    }

    public PacketPlayOutSpawnEntityLiving getSpawnPacket() {
        return new PacketPlayOutSpawnEntityLiving(wither);
    }

    public PacketPlayOutEntityMetadata getMetaPacket() {
        return new PacketPlayOutEntityMetadata(wither.getId(), wither.getDataWatcher(), true);
    }

    public PacketPlayOutEntityDestroy getDestroyPacket() {
        return new PacketPlayOutEntityDestroy(wither.getId());
    }

    public PacketPlayOutEntityTeleport getTeleportPacket() {
        return new PacketPlayOutEntityTeleport(wither);
    }

    public void updateLocation(Location location) {
        wither.setLocation(location.getX(), location.getY(), location.getZ(), 0, 0);
    }

    public void setMessage(String message) {
        wither.setCustomName(message);
    }

    public int getEntityId() {
        return wither.getId();
    }
}
