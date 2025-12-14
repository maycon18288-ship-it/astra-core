package astra.core.menus.shop;

import astra.core.Core;
import astra.core.api.menu.PlayerMenu;
import astra.core.player.Account;
import astra.core.utils.BukkitUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class PrincipalShopMenu extends PlayerMenu {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("d 'de' MMMM. yyyy 'às' HH:mm", Locale.forLanguageTag("pt-BR"));

    public PrincipalShopMenu(Account profile) {
        super(profile.getPlayer(), "Loja do Servidor", 6);
        this.setItem(1, BukkitUtils.deserializeItemStack("EMERALD : 1 : nome>§aRanks : desc>§eVisualizando"));
        this.setItem(2, BukkitUtils.deserializeItemStack("NAME_TAG : 1 : nome>§aTags : desc>§aClique para visualizar!"));
        this.setItem(3, BukkitUtils.deserializeItemStack("175 : 1 : nome>§aMedalhas : desc>§aClique para visualizar!"));

        this.setItem(9, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categorias : desc>§7↓ Produtos"));
        this.setItem(10, BukkitUtils.deserializeItemStack("160:5 : 1 : nome>§7↑ Categorias : desc>§7↓ Produtos"));
        this.setItem(11, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categorias : desc>§7↓ Produtos"));
        this.setItem(12, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categorias : desc>§7↓ Produtos"));
        this.setItem(13, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categorias : desc>§7↓ Produtos"));
        this.setItem(14, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categorias : desc>§7↓ Produtos"));
        this.setItem(15, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categorias : desc>§7↓ Produtos"));
        this.setItem(16, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categorias : desc>§7↓ Produtos"));
        this.setItem(17, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categorias : desc>§7↓ Produtos"));

        this.setItem(28, BukkitUtils.deserializeItemStack("351:10 : 1 : nome>§aVIP §7(Eterno) : desc>§8Produto vitalício\n\n§a✔ §7Medalha Positivo §a✔\n§a✔ §7Títulos dos minigames\n§a✔ §7Mensagens de entrada\n§a✔ §7Limite de party: §d15\n\n§7Preço: §aR$24,90\n\n§eClique para comprar!"));
        this.setItem(30, BukkitUtils.deserializeItemStack("351:1 : 1 : nome>§cSpark §7(Eterno) : desc>§8Produto vitalício\n\n§a✔ §7Criação de clans\n§a✔ §7Medalhas exclusivas\n§a✔ §7Títulos exclusivos\n§a✔ §7Mensagens de entrada\n§a✔ §7Limite de party: §d20\n\n§7Preço: §aR$34,90\n\n§eClique para comprar!"));
        this.setItem(32, BukkitUtils.deserializeItemStack("351:12 : 1 : nome>§bPrime §7(Eterno) : desc>§8Produto vitalício\n\n§a✔ §7Criação de clans\n§a✔ §7Medalhas exclusivas\n§a✔ §7Títulos §d§lAURORA NO TOPO\n§a✔ §7Títulos personalizados\n§a✔ §7Mensagens de entrada\n§a✔ §7Limite de party: §d25\n\n§7Preço: §aR$54,90\n\n§eClique para comprar!"));
        this.setItem(34, BukkitUtils.deserializeItemStack("351:6 : 1 : nome>§3Prime+ §7(Eterno) : desc>§8Produto vitalício\n\n§a✔ §7Criação de clans\n§a✔ §7Medalhas exclusivas\n§a✔ §7Títulos animados\n§a✔ §7Títulos personalizados\n§a✔ §7Mensagens de entrada\n§a✔ §7Limite de party: §d35\n\n§7Preço: §aR$74,90\n\n§eClique para comprar!"));

        this.setItem(37, BukkitUtils.deserializeItemStack("PAPER : 1 : nome>§aBenefícios do VIP : desc>§7Clique para visualizar!"));
        this.setItem(39, BukkitUtils.deserializeItemStack("PAPER : 1 : nome>§cBenefícios do Spark : desc>§7Clique para visualizar!"));
        this.setItem(41, BukkitUtils.deserializeItemStack("PAPER : 1 : nome>§bBenefícios do Prime : desc>§7Clique para visualizar!"));
        this.setItem(43, BukkitUtils.deserializeItemStack("PAPER : 1 : nome>§3Benefícios do Prime+ : desc>§7Clique para visualizar!"));

        this.register(Core.getInstance());
        this.open();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent evt) {
        if (evt.getInventory().equals(this.getInventory())) {
            evt.setCancelled(true);
            if (evt.getWhoClicked().equals(this.player)) {
                Account profile = Account.getAccount(this.player.getName());
                if (profile == null) {
                    this.player.closeInventory();
                    return;
                }

                if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getInventory())) {
                    ItemStack item = evt.getCurrentItem();
                    if (item != null && item.getType() != Material.AIR) {
                        switch (evt.getSlot()) {
                            case 37:
                                ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                                BookMeta meta = (BookMeta) book.getItemMeta();
                                meta.setTitle("Benefícios VIP");
                                meta.setAuthor("astra Server");
                                meta.addPage("§a§lBENEFÍCIOS VIP\n\n§a✔ §0Medalha Positivo §a✔\n§a✔ §0Títulos dos minigames\n§a✔ §0Mensagens de entrada\n§a✔ §0Limite de party: 15\n§a✔ §0Seleção de mapas nos minigames\n§a✔ §0Destaque no chat do servidor");
                                meta.addPage("§a§lBENEFÍCIOS VIP\n\n§a✔ §0Cosméticos limitados\n§a✔ §0Skins personalizadas");

                                book.setItemMeta(meta);
                                BukkitUtils.openBook(player, book);
                                break;

                            case 39:
                                book = new ItemStack(Material.WRITTEN_BOOK);
                                meta = (BookMeta) book.getItemMeta();
                                meta.setTitle("Benefícios Spark");
                                meta.setAuthor("astra Server");
                                meta.addPage("§c§lBENEFÍCIOS SPARK\n\n§a✔ §0Medalhas exclusivas\n§a✔ §0Títulos dos minigames\n§a✔ §0Mensagens de entrada\n§a✔ §0Limite de party: 20\n§a✔ §0Seleção de mapas nos minigames\n§a✔ §0Destaque no chat do servidor");
                                meta.addPage("§c§lBENEFÍCIOS SPARK\n\n§a✔ §0Cosméticos limitados\n§a✔ §0Skins personalizadas\n§a✔ §0Espectar partidas\n§a✔ §0Permissão de cores no chat\n§a✔ §0Criação de clans");

                                book.setItemMeta(meta);
                                BukkitUtils.openBook(player, book);
                                break;

                            case 41:
                                book = new ItemStack(Material.WRITTEN_BOOK);
                                meta = (BookMeta) book.getItemMeta();
                                meta.setTitle("Benefícios Prime");
                                meta.setAuthor("astra Server");
                                meta.addPage("§b§lBENEFÍCIOS PRIME\n\n§a✔ §0Medalhas exclusivas\n§a✔ §0Títulos dos minigames\n§a✔ §0Mensagens de entrada\n§a✔ §0Limite de party: 25\n§a✔ §0Seleção de mapas nos minigames\n§a✔ §0Destaque no chat do servidor");
                                meta.addPage("§b§lBENEFÍCIOS PRIME\n\n§a✔ §0Cosméticos limitados\n§a✔ §0Skins personalizadas\n§a✔ §0Espectar partidas\n§a✔ §0Permissão de cores no chat\n§a✔ §0Criação de clans\n§a✔ §0Cores de clan: §dRosa§0, §aVerde§0 e §cVermelha");

                                book.setItemMeta(meta);
                                BukkitUtils.openBook(player, book);
                                break;

                            case 43:
                                book = new ItemStack(Material.WRITTEN_BOOK);
                                meta = (BookMeta) book.getItemMeta();
                                meta.setTitle("Benefícios Prime+");
                                meta.setAuthor("astra Server");
                                meta.addPage("§3§lBENEFÍCIOS PRIME+\n\n§a✔ §0Diversas medalhas\n§a✔ §0Títulos dos minigames\n§a✔ §0Mensagens de entrada\n§a✔ §0Limite de party: 35\n§a✔ §0Seleção de mapas nos minigames\n§a✔ §0Destaque no chat do servidor");
                                meta.addPage("§3§lBENEFÍCIOS PRIME+\n\n§a✔ §0Cosméticos ilimitados\n§a✔ §0Skins personalizadas\n§a✔ §0Espectar partidas\n§a✔ §0Permissão de cores no chat\n§a✔ §0Criação de clans\n§a✔ §0Todas as cores de clan");
                                meta.addPage("§3§lBENEFÍCIOS PRIME+\n\n§a✔ §0Cores do §3+\n§a✔ §0Títulos animados e coloridos\n§a✔ §0Tag RGB\n§a✔ §0");

                                book.setItemMeta(meta);
                                BukkitUtils.openBook(player, book);
                                break;
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
        if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getInventory())) {
            this.cancel();
        }
    }
}