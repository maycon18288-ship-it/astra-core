package astra.core.menus;

import astra.core.Core;
import astra.core.api.menu.PlayerMenu;
import astra.core.bukkit.listeners.UpdateSkin;
import astra.core.menus.account.AccountPageMenu;
import astra.core.player.Account;
import astra.core.player.skin.SkinCategory;
import astra.core.utils.BukkitUtils;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MenuCategories extends PlayerMenu {
    private final Map<ItemStack, SkinCategory> buttons = new HashMap<>();

    public MenuCategories(Account profile) {
        super(profile.getPlayer(), "Categorias de Skin", 4);
        SkinCategory[] var2 = SkinCategory.values();

        for (SkinCategory category : var2) {
            int slot = category.getSlotdomenu();
            ItemStack icon = BukkitUtils.deserializeItemStack(category.getItem() + " : 1 : nome>§a" + category.getDisplayName() + " : desc>&7Visualize a categoria: §e" + category.getDisplayName());
            this.setItem(slot, icon);
            this.buttons.put(icon, category);
        }

        this.setItem(31, BukkitUtils.deserializeItemStack("ARROW : 1 : nome>§cVoltar"));
        this.setItem(35, BukkitUtils.deserializeItemStack("ENDER_PEARL : 1 : nome>§aAtualizar sua skin : desc>&7Utilize esta opção para atualizar\n&7a skin original da sua conta.\n \n&eClique para atualizar!"));
        this.register(Core.getInstance());
        this.open();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().equals(this.getInventory())) {
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();
            if (item != null && item.getType() != Material.AIR) {
                switch(event.getSlot()) {
                    case 31:
                        new AccountPageMenu(Account.getAccount(this.player.getName()));
                        break;
                    case 35:
                        this.updateSkin(this.player, Account.getAccount(this.player.getName()));
                }

                SkinCategory category = this.buttons.get(item);
                if (category != null) {
                    Account profile = Account.getAccount(this.player.getName());
                    if (profile != null) {
                        new SkinCategoryMenu(profile, category);
                    }
                }

            }
        }
    }

    private void updateSkin(Player player, Account profile) {
        this.updateSkin(player, profile, player.getName());
    }

    private void updateSkin(Player player, Account profile, String name) {
        profile.getSkinsContainer().addSkin(name);
        profile.getSkinsContainer().setOriginalSkin(name);
        UpdateSkin.updateSkin(player, profile.getSkinsContainer().getValue(), profile.getSkinsContainer().getSignature());
        player.sendMessage("§aSkin atualizada!");
    }
}