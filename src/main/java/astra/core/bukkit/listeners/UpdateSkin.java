package astra.core.bukkit.listeners;

import astra.core.Core;
import astra.core.mysql.data.container.SkinsContainer;
import astra.core.player.Account;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.GameMode;

import java.util.HashMap;
import java.util.Map;

public class UpdateSkin {

    private static final Map<String, Long> lastUpdateTimestamps = new HashMap<>();
    private static final long COOLDOWN_TIME = 30 * 1000;

    public static boolean updateSkin(Player player, String value, String signature) {
        if (value == null || signature == null) {
            return false;
        }

        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        GameProfile profile = entityPlayer.getProfile();
        Account account = Account.getAccount(player.getName());
        SkinsContainer container = account.getSkinsContainer();

        Property currentSkin = profile.getProperties().get("textures").stream().findFirst().orElse(null);

        if (currentSkin != null && currentSkin.getValue().equals(value) && currentSkin.getSignature().equals(signature)) {
            player.sendMessage("§eVocê já está utilizando essa skin.");
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.NOTE_PIANO, 2.0f, 2.0f);
            return false;
        }

        long currentTime = System.currentTimeMillis();
        Long lastUpdate = lastUpdateTimestamps.get(player.getName());

        if (lastUpdate != null && currentTime - lastUpdate < COOLDOWN_TIME) {
            long timeRemaining = (COOLDOWN_TIME - (currentTime - lastUpdate)) / 1000;
            player.sendMessage("§cAguarde " + timeRemaining + "s para alterar novamente.");
            player.playSound(player.getLocation(), Sound.NOTE_PIANO, 2.0f, 2.0f);
            return false;
        }

        Location location = player.getLocation();
        ItemStack[] inventory = player.getInventory().getContents();
        ItemStack[] armor = player.getInventory().getArmorContents();
        int heldItemSlot = player.getInventory().getHeldItemSlot();
        GameMode gameMode = player.getGameMode();
        float exp = player.getExp();
        int level = player.getLevel();
        double health = player.getHealth();
        int foodLevel = player.getFoodLevel();
        
        profile.getProperties().clear();
        profile.getProperties().put("textures", new Property("textures", value, signature));

        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            EntityPlayer onlineEntityPlayer = ((CraftPlayer) onlinePlayer).getHandle();
            
            onlineEntityPlayer.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(
                    PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
        });

        Bukkit.getScheduler().runTaskLater(Core.getInstance(), () -> {
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                EntityPlayer onlineEntityPlayer = ((CraftPlayer) onlinePlayer).getHandle();
                onlineEntityPlayer.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(
                        PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));
            });

            entityPlayer.playerConnection.sendPacket(new PacketPlayOutRespawn(
                    entityPlayer.getWorld().worldProvider.getDimension(),
                    entityPlayer.getWorld().getDifficulty(),
                    entityPlayer.getWorld().worldData.getType(),
                    entityPlayer.playerInteractManager.getGameMode()));

            player.teleport(location);
            player.getInventory().setContents(inventory);
            player.getInventory().setArmorContents(armor);
            player.getInventory().setHeldItemSlot(heldItemSlot);
            player.setGameMode(gameMode);
            player.setExp(exp);
            player.setLevel(level);
            player.setHealth(health);
            player.setFoodLevel(foodLevel);
            player.updateInventory();

            Bukkit.getOnlinePlayers().stream()
                    .filter(onlinePlayer -> !onlinePlayer.equals(player))
                    .forEach(onlinePlayer -> {
                        EntityPlayer onlineEntityPlayer = ((CraftPlayer) onlinePlayer).getHandle();
                        onlineEntityPlayer.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(player.getEntityId()));
                        onlineEntityPlayer.playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(entityPlayer));
                    });

            entityPlayer.updateAbilities();
        }, 2L);

        container.setSkin(container.getSkin(), value, signature);
        account.save();

        player.sendMessage("§aSkin atualizada com sucesso!");
        lastUpdateTimestamps.put(player.getName(), currentTime);
        
        return true;
    }
}