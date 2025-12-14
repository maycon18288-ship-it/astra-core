package astra.core.menus.account;

import astra.core.Core;
import astra.core.player.Account;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class NicknameMenu {
    private final Player player;
    private final Account profile;
    private int containerId;
    private AnvilContainer container;

    public NicknameMenu(Account profile) {
        this.player = profile.getPlayer();
        this.profile = profile;
        openAnvil();
    }

    private void openAnvil() {
        EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        
        container = new AnvilContainer(nmsPlayer);
        containerId = nmsPlayer.nextContainerCounter();
        
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta meta = paper.getItemMeta();
        meta.setDisplayName("Digite seu apelido");
        paper.setItemMeta(meta);
        container.getSlot(0).set(CraftItemStack.asNMSCopy(paper));
        
        nmsPlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerId, "minecraft:anvil", new ChatMessage("Reparar & Renomear")));
        nmsPlayer.activeContainer = container;
        nmsPlayer.activeContainer.windowId = containerId;
        nmsPlayer.activeContainer.addSlotListener(nmsPlayer);
        
        Core.getInstance().getServer().getPluginManager().registerEvents(new AnvilListener(), Core.getInstance());
    }

    private static class AnvilContainer extends ContainerAnvil {
        public AnvilContainer(EntityHuman entity) {
            super(entity.inventory, entity.world, new BlockPosition(0, 0, 0), entity);
        }

        @Override
        public boolean a(EntityHuman entityhuman) {
            return true;
        }
    }

    private class AnvilListener implements Listener {
        @EventHandler
        public void onClick(InventoryClickEvent event) {
            if (event.getWhoClicked() != player) return;
            if (!(event.getInventory() instanceof org.bukkit.inventory.AnvilInventory)) return;
            
            event.setCancelled(true);
            
            ItemStack item = event.getCurrentItem();
            if (event.getRawSlot() == 2 && item != null && item.hasItemMeta()) {
                String nickname = item.getItemMeta().getDisplayName();
                if (!nickname.equals("Digite seu apelido")) {
                    player.closeInventory();
                    openConfirmationMenu(nickname);
                }
            }
        }

        @EventHandler
        public void onClose(InventoryCloseEvent event) {
            if (event.getPlayer() != player) return;
            if (!(event.getInventory() instanceof org.bukkit.inventory.AnvilInventory)) return;
            
            HandlerList.unregisterAll(this);
        }
    }

    private void openConfirmationMenu(String nickname) {
        Inventory confirmMenu = org.bukkit.Bukkit.createInventory(null, 27, "Confirmar Apelido");
        
        ItemStack confirmPaper = new ItemStack(Material.PAPER);
        ItemMeta confirmMeta = confirmPaper.getItemMeta();
        confirmMeta.setDisplayName("§aSeu novo apelido será:");
        confirmMeta.setLore(Arrays.asList("§7➟ §f" + nickname, "", "§7Clique nos botões ao lado", "§7para confirmar ou cancelar."));
        confirmPaper.setItemMeta(confirmMeta);
        confirmMenu.setItem(13, confirmPaper);
        
        ItemStack confirmWool = new ItemStack(Material.WOOL, 1, (short) 5);
        ItemMeta confirmWoolMeta = confirmWool.getItemMeta();
        confirmWoolMeta.setDisplayName("§aConfirmar");
        confirmWoolMeta.setLore(Arrays.asList("§7Clique para confirmar", "§7seu novo apelido!"));
        confirmWool.setItemMeta(confirmWoolMeta);
        confirmMenu.setItem(11, confirmWool);
        
        ItemStack cancelWool = new ItemStack(Material.WOOL, 1, (short) 14);
        ItemMeta cancelWoolMeta = cancelWool.getItemMeta();
        cancelWoolMeta.setDisplayName("§cCancelar");
        cancelWoolMeta.setLore(Arrays.asList("§7Clique para cancelar", "§7a alteração do apelido."));
        cancelWool.setItemMeta(cancelWoolMeta);
        confirmMenu.setItem(15, cancelWool);

        Core.getInstance().getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onInventoryClick(InventoryClickEvent event) {
                if (!event.getView().getTitle().equals("Confirmar Apelido")) return;
                if (!(event.getWhoClicked() instanceof Player)) return;
                
                event.setCancelled(true);
                if (profile.getDataContainer("Account", "apelido").getAsString().equals(nickname)) {
                    player.closeInventory();
                    player.sendMessage("§c§lERRO! §cVocê já está com o apelido §7" + nickname + "§c definido!");
                    return;
                }

                if (event.getRawSlot() == 11) {
                    profile.getDataContainer("Account","apelido").set(nickname);
                    player.sendMessage("§a§lYAY! §aVocê alterou seu apelido para: " + nickname);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                    player.closeInventory();
                    HandlerList.unregisterAll(this);
                } else if (event.getRawSlot() == 15) {
                    player.sendMessage("§cOperação cancelada!");
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 1.0f, 1.0f);
                    player.closeInventory();
                    HandlerList.unregisterAll(this);
                }
            }

            @EventHandler
            public void onInventoryClose(InventoryCloseEvent event) {
                if (event.getView().getTitle().equals("Confirmar Apelido")) {
                    HandlerList.unregisterAll(this);
                }
            }
        }, Core.getInstance());

        player.openInventory(confirmMenu);
    }
} 