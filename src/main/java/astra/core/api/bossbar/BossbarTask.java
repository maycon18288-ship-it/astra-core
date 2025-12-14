package astra.core.api.bossbar;

import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BossbarTask extends BukkitRunnable {

    private final Player player;
    private final CustomBossbar bossbar;

    public BossbarTask(Player player, CustomBossbar bossbar) {
        this.player = player;
        this.bossbar = bossbar;
    }

    @Override
    public void run() {
        if (!player.isOnline()) {
            cancel();
            return;
        }

        Location eyeLoc = player.getEyeLocation();
        Location inFront = eyeLoc.add(eyeLoc.getDirection().normalize().multiply(40)); // 40 blocos à frente

        bossbar.updateLocation(inFront);

        PacketPlayOutEntityTeleport teleport = bossbar.getTeleportPacket();
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(teleport);
    }
}
