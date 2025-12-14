package astra.core.api.bossbar;

import astra.core.Core;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Arrays;

public class BossbarListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Location loc = player.getEyeLocation().add(player.getEyeLocation().getDirection().normalize().multiply(40));

        CustomBossbar bossbar = new CustomBossbar(loc);

        sendPacket(player, bossbar.getSpawnPacket());
        sendPacket(player, bossbar.getMetaPacket());

        new BossbarTask(player, bossbar).runTaskTimer(Core.getInstance(), 0L, 2L);
        new BossbarMessageTask(player, bossbar, Arrays.asList(
                "§aAdquira VIP, tags e medalhas! §7» §bloja.redeastra.com",
                "§eAdquira VIP, tags e medalhas! §7» §bloja.redeastra.com",
                "§6Adquira VIP, tags e medalhas! §7» §bloja.redeastra.com",
                "§cAdquira VIP, tags e medalhas! §7» §bloja.redeastra.com",
                "§9Adquira VIP, tags e medalhas! §7» §bloja.redeastra.com",
                "§5Adquira VIP, tags e medalhas! §7» §bloja.redeastra.com",
                "§dAdquira VIP, tags e medalhas! §7» §bloja.redeastra.com",
                "§3Adquira VIP, tags e medalhas! §7» §bloja.redeastra.com"

                )).runTaskTimer(Core.getInstance(), 0L, 20L);
    }

    private void sendPacket(Player player, Packet<?> packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}

