package astra.core.menus;

import astra.core.Core;
import astra.core.api.menu.PagedPlayerMenu;
import astra.core.bukkit.listeners.UpdateSkin;
import astra.core.mysql.data.container.SkinsContainer;
import astra.core.player.Account;
import astra.core.player.skin.SkinCategory;
import astra.core.player.skin.SkinManager;
import astra.core.utils.BukkitUtils;
import java.util.ArrayList;
import java.util.HashMap;import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class SkinCategoryMenu extends PagedPlayerMenu {
    private final Map<ItemStack, SkinManager> skins = new HashMap<>();

    public SkinCategoryMenu(Account profile, SkinCategory category) {
        super(profile.getPlayer(), "Categoria: " + category.getDisplayName(), 6);
        this.previousPage = 45;
        this.nextPage = 53;
        this.onlySlots(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34);

        SkinsContainer skinsContainer = profile.getSkinsContainer();
        String selectedSkin = skinsContainer == null ? "none" : skinsContainer.getSkin();
        List<ItemStack> items = new ArrayList<>();

        for (SkinManager skin : category.getSkins()) {
            boolean isSelected = skin.getName().equals(selectedSkin);
            ItemStack icon = BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : skin>" + skin.getValue() + " : nome>&a" + skin.getName() + " : desc>&7Altere sua skin para " + skin.getName() + ".\n \n" + (isSelected ? "§eSelecionado." : "§eClique para alterar!"));
            items.add(icon);
            this.skins.put(icon, skin);
        }

        this.removeSlotsWith(BukkitUtils.deserializeItemStack("ARROW : 1 : nome>&cVoltar"), 49);
        this.setItems(items);
        this.register(Core.getInstance());
        this.open();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent evt) {
        if (evt.getInventory().equals(this.getCurrentInventory())) {
            evt.setCancelled(true);
            if (evt.getWhoClicked().equals(this.player)) {
                Account profile = Account.getAccount(this.player.getName());
                ItemStack item = evt.getCurrentItem();
                if (item != null && item.getType() != Material.AIR) {
                    switch(evt.getSlot()) {
                        case 45:
                            this.openPrevious();
                            break;
                        case 49:
                            new MenuCategories(profile);
                            break;
                        case 53:
                            this.openNext();
                            break;
                        default:
                            SkinManager selected = this.skins.get(item);
                            if (selected != null) {
                                String value = selected.getValue();
                                String signature = selected.getSignature();

                                if (UpdateSkin.updateSkin(player, value, signature)) {
                                    this.player.closeInventory();
                                }
                            }
                    }
                }
            }
        }
    }

    public void cancel() {
        HandlerList.unregisterAll(this);
        this.skins.clear();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent evt) {
        if (evt.getPlayer().equals(this.player)) {
            this.cancel();
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent evt) {
        if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getCurrentInventory())) {
            this.cancel();
        }
    }
}