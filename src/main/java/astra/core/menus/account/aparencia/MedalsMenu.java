package astra.core.menus.account.aparencia;

import astra.core.Core;
import astra.core.api.enums.Medal;
import astra.core.api.menu.PagedPlayerMenu;
import astra.core.api.rank.Rank;
import astra.core.menus.account.AccountPageMenu;
import astra.core.player.Account;
import astra.core.utils.BukkitUtils;
import astra.core.utils.StringUtils;
import astra.core.utils.TagUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedalsMenu extends PagedPlayerMenu {
    private final Map<ItemStack, Medal> medals = new HashMap<>();

    public MedalsMenu(Account profile) {
        super(profile.getPlayer(), "Medalhas", 6);
        this.previousPage = 45;
        this.nextPage = 53;
        this.onlySlots(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34);

        String currentMedal = profile.getDataContainer("Account", "medalha").getAsString();
        List<ItemStack> items = new ArrayList<>();

        for (Medal medal : Medal.values()) {
            boolean hasPermission = medal.has(profile.getPlayer()) || profile.getPlayer().isOp();
            boolean isSelected = medal.getName().equals(currentMedal);
            Rank rank = Rank.getRoleByName(profile.getDataContainer("Account", "tag").getAsString());
            
            List<String> description = new ArrayList<>();
            description.add("&7↳ Informações:");
            description.add(" ");
            description.add("&7▪ Status: " + (isSelected ? "§aAtivada!" : "§cDesativada"));
            description.add("&7▪ Permissão: " + (hasPermission ? "§aSim" : "§cNão"));
            description.add("&7▪ Símbolo: " + medal.getSuffix());
            description.add(" ");
            description.add("§bExemplo:");
            description.add(medal.getSuffix() + " " + rank.getPrefix() + player.getName());
            description.add(" ");
            
            if (hasPermission) {
                description.add("&eClique para " + (isSelected ? "desativar" : "ativar") + "!");
            } else {
                description.add("&cVocê não tem permissão para usar esta medalha!");
            }

            String glassData = hasPermission ? "STAINED_GLASS:5" : "STAINED_GLASS:15";
            String itemName = (hasPermission ? "§a" : "§c") + medal.getName();
            
            ItemStack icon = BukkitUtils.deserializeItemStack(glassData + " : 1 : nome>" + itemName + " : desc>" + String.join("\n", description));
            items.add(icon);
            this.medals.put(icon, medal);
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
                if (profile == null) {
                    this.player.closeInventory();
                    return;
                }

                if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getCurrentInventory())) {
                    ItemStack item = evt.getCurrentItem();
                    if (item != null && item.getType() != Material.AIR) {
                        if (evt.getSlot() == 49) {
                            new PrincipalAparenciaMenu(profile);
                            return;
                        }

                        if (evt.getSlot() == 45) {
                            this.openPrevious();
                            return;
                        }

                        if (evt.getSlot() == 53) {
                            this.openNext();
                            return;
                        }

                        Medal medal = this.medals.get(item);
                        if (medal != null) {
                            if (!medal.has(profile.getPlayer()) && !profile.getPlayer().isOp()) {
                                player.sendMessage("§cVocê não tem permissão para usar esta medalha!");
                                player.playSound(player.getLocation(), Sound.NOTE_BASS, 1.0f, 1.0f);
                                return;
                            }
                            
                            String currentMedal = profile.getDataContainer("Account", "medalha").getAsString();
                            if (medal.getName().equals(currentMedal)) {
                                profile.getDataContainer("Account", "medalha").set("");
                                profile.getPlayer().sendMessage("§bMedalha: §cVocê desativou a medalha " + medal.getName() + "§c.");
                            } else {
                                profile.getDataContainer("Account", "medalha").set(StringUtils.stripColors(medal.getName()));
                                profile.getPlayer().sendMessage("§bMedalha: §aVocê ativou a medalha " + medal.getName() + "§a.");
                            }
                            profile.save();
                            TagUtils.setMedal(player, medal);
                            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                            new MedalsMenu(profile);
                        }
                    }
                }
            }
        }
    }

    public void cancel() {
        HandlerList.unregisterAll(this);
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