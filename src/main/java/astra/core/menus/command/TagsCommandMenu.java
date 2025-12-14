package astra.core.menus.command;

import astra.core.Core;
import astra.core.api.menu.PagedPlayerMenu;
import astra.core.api.rank.Rank;
import astra.core.menus.account.aparencia.PrincipalAparenciaMenu;
import astra.core.mysql.data.DataContainer;
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

public class TagsCommandMenu extends PagedPlayerMenu {
    private final Map<ItemStack, Rank> ranks = new HashMap<>();

    public TagsCommandMenu(Account profile) {
        super(profile.getPlayer(), "Tags", 6);
        this.previousPage = 45;
        this.nextPage = 53;
        this.onlySlots(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34);

        String currentTag = profile.getDataContainer("Account", "tag").getAsString();
        List<ItemStack> items = new ArrayList<>();

        for (Rank rank : Rank.listRoles()) {
            boolean hasPermission = rank.has(profile.getPlayer()) || profile.getPlayer().isOp();
            boolean isSelected = rank.getName().equals(currentTag);
            Rank playerRank = Rank.getRoleByName(profile.getDataContainer("Account", "tag").getAsString());

            List<String> description = new ArrayList<>();
            description.add("&7↳ Informações:");
            description.add(" ");
            description.add("&7▪ Status: " + (isSelected ? "§aAtivada!" : "§cDesativada"));
            description.add("&7▪ Permissão: " + (hasPermission ? "§aSim" : "§cNão"));
            description.add("&7▪ Prefixo: " + rank.getPrefix() + player.getName());
            description.add(" ");

            if (hasPermission) {
                if (playerRank != null && playerRank.getName().equals(rank.getName())) {
                    description.add("&cVocê já está usando esta tag!");
                } else {
                    description.add("&eClique para " + (isSelected ? "desativar" : "ativar") + "!");
                }
            } else {
                description.add("&cVocê não tem permissão para usar esta tag!");
            }

            String glassData;
            if (playerRank != null && playerRank.getName().equals(rank.getName())) {
                glassData = "STAINED_GLASS:4";
            } else {
                glassData = hasPermission ? "STAINED_GLASS:5" : "STAINED_GLASS:15";
            }
            
            String itemName = (hasPermission ? "§a" : "§c") + rank.getName();
            
            ItemStack icon = BukkitUtils.deserializeItemStack(glassData + " : 1 : nome>" + itemName + " : desc>" + String.join("\n", description));
            items.add(icon);
            this.ranks.put(icon, rank);
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
                            this.player.closeInventory();
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

                        Rank rank = this.ranks.get(item);
                        if (rank != null) {
                            if (!rank.has(profile.getPlayer()) && !profile.getPlayer().isOp()) {
                                player.sendMessage("§cVocê não tem permissão para usar esta tag!");
                                player.playSound(player.getLocation(), Sound.NOTE_BASS, 1.0f, 1.0f);
                                return;
                            }

                            Rank currentRank = Rank.getRoleByName(profile.getDataContainer("Account", "tag").getAsString());
                            if (currentRank != null && currentRank.getName().equals(rank.getName())) {
                                player.sendMessage("§cVocê já está usando esta tag!");
                                player.playSound(player.getLocation(), Sound.NOTE_BASS, 1.0f, 1.0f);
                                return;
                            }
                            
                            String currentTag = profile.getDataContainer("Account", "tag").getAsString();
                            if (rank.getName().equals(currentTag)) {
                                profile.getDataContainer("Account", "tag").set("");
                                profile.getPlayer().sendMessage("§bTag: §cVocê desativou a tag " + rank.getName() + "§c.");
                            } else {
                                TagUtils.setTag(player, rank);
                                DataContainer container = profile.getDataContainer("Account", "tag");
                                container.set(StringUtils.stripColors(rank.getName()));
                                profile.save();
                                profile.getPlayer().sendMessage("§aVocê selecionou a tag " + rank.getName() + "§a.");
                            }
                            profile.save();
                            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                            new TagsCommandMenu(profile);
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