package astra.core.api.bossbar;

import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class BossbarMessageTask extends BukkitRunnable {

    private final Player player;
    private final CustomBossbar bossbar;
    private final List<String> messages;
    private int index = 0;

    public BossbarMessageTask(Player player, CustomBossbar bossbar, List<String> messages) {
        this.player = player;
        this.bossbar = bossbar;
        this.messages = messages;
    }

    @Override
    public void run() {
        if (!player.isOnline()) {
            cancel();
            return;
        }

        String message = messages.get(index);
        bossbar.setMessage(message);

        PacketPlayOutEntityMetadata meta = bossbar.getMetaPacket();
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(meta);

        index = (index + 1) % messages.size(); // Loop infinito
    }
}
