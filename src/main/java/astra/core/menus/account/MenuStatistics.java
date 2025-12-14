package astra.core.menus.account;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import astra.core.Core;
import astra.core.api.menu.PlayerMenu;
import astra.core.player.Account;
import astra.core.utils.BukkitUtils;
import astra.core.utils.StringUtils;

public class MenuStatistics extends PlayerMenu {

  private static final int SKY_WARS_SLOT = 10;
  private static final int BEDWARS_SLOT = 11;
  private static final int THE_BRIDGE_SLOT = 12;
  private static final int MURDER_SLOT = 13;
  private static final int EGG_SLOT = 14;

  public MenuStatistics(Account profile) {
    super(profile.getPlayer(), "Estatísticas", 4);

    this.setItem(SKY_WARS_SLOT, createGameStatsItem("GRASS", "&aSky Wars",
            "&eSolo:\n " +
                    "&8▪ &fAbates: &7" + profile.getStats("SkyWars", "1v1kills") + "\n " +
                    "&8▪ &fMortes: &7" + profile.getStats("SkyWars", "1v1deaths") + "\n " +
                    "&8▪ &fVitórias: &7" + profile.getStats("SkyWars", "1v1wins") + "\n " +
                    "&8▪ &fPartidas: &7" + profile.getStats("SkyWars", "1v1games") + "\n " +
                    "&8▪ &fAssistências: &7" + profile.getStats("SkyWars", "1v1assists") + "\n\n" +

                    "&eRanqueado:\n &8▪ " +
                    "&fAbates: &7" + profile.getStats("SkyWars", "rankedkills") + "\n " +
                    "&8▪ &fMortes: &7" + + profile.getStats("SkyWars", "rankeddeaths") + "\n " +
                    "&8▪ &fVitórias: &7" + profile.getStats("SkyWars", "rankedwins") + "\n " +
                    "&8▪ &fPartidas: &7" + profile.getStats("SkyWars", "rankedgames") + "\n " +
                    "&8▪ &fAssistências: &7" + profile.getStats("SkyWars", "rankedassists") + "\n" +
                    "\n" +
                    "&fCoins: &6" + StringUtils.formatNumber(profile.getCoins("SkyWars"))));

    this.setItem(BEDWARS_SLOT, createGameStatsItem("BED", "&aBed Wars",
            "&eGeral:\n" +
                    " &8▪ &fPartidas: &7" + profile.getStats("BedWars", "1v1games", "2v2games", "3v3games", "4v4games") + "\n" +
                    " &8▪ &fAbates: &7" + profile.getStats("BedWars", "1v1kills", "2v2games", "3v3games", "4v4games") + "\n" +
                    " &8▪ &fMortes: &7" + profile.getStats("BedWars", "1v1deaths", "2v2deaths", "3v3deaths", "4v4deaths") + "\n" +
                    " &8▪ &fAbates Finais: &7" + profile.getStats("BedWars", "1v1finalkills", "2v2finalkills", "3v3finalkills", "4v4finalkills") + "\n" +
                    " &8▪ &fMortes Finais: &7" + profile.getStats("BedWars", "1v1finaldeaths", "2v2finaldeaths", "3v3finaldeaths", "4v4finaldeaths") + "\n" +
                    " &8▪ &fVitórias: &7" + profile.getStats("BedWars", "1v1wins", "2v2wins", "3v3wins", "4v4wins") + "\n" +
                    " &8▪ &fCamas destruídas: &7" + profile.getStats("BedWars", "1v1bedsdestroyeds", "2v2bedsdestroyeds", "3v3bedsdestroyeds", "4v4bedsdestroyeds") + "\n" +
                    " &8▪ &fCamas perdidas: &7" + profile.getStats("BedWars", "1v1bedslosteds", "2v2bedslosteds", "3v3bedslosteds", "4v4bedslosteds") + "\n" +
                    " \n" +
                    "&fCoins: &6" + StringUtils.formatNumber(profile.getCoins("BedWars"))));

    this.setItem(THE_BRIDGE_SLOT, createGameStatsItem("STAINED_CLAY:11", "&aThe Bridge",
            "&e1v1:\n &8▪ &fAbates: &7" + profile.getStats("TheBridge", "1v1kills") + "\n &8▪ &fMortes: &7" + profile.getStats("TheBridge", "1v1deaths") + "\n &8▪ &fPontos: &7" + profile.getStats("TheBridge", "1v1points") + "\n &8▪ &fVitórias: &7" + profile.getStats("TheBridge", "1v1wins") + "\n &8▪ &fPartidas: &7" + profile.getStats("TheBridge", "1v1games") + "\n" +
                    "\n&e2v2:\n &8▪ &fAbates: &7" + profile.getStats("TheBridge", "2v2kills") + "\n &8▪ &fMortes: &7" + profile.getStats("TheBridge", "2v2deaths") + "\n &8▪ &fPontos: &7" + profile.getStats("TheBridge", "2v2points") + "\n &8▪ &fVitórias: &7" + profile.getStats("TheBridge", "2v2wins") + "\n &8▪ &fPartidas: &7" + profile.getStats("TheBridge", "2v2games") + "\n" +
                    "\n&eWinstreak:\n &8▪ &fDiário: &7" + profile.getStats("TheBridge", "winstreak") + "\n" +
                    "\n&fCoins: &6" + StringUtils.formatNumber(profile.getCoins("TheBridge"))));

    // Murder stats
    this.setItem(MURDER_SLOT, createGameStatsItem("BOW", "&aMurder",
            "&eClássico:\n &8▪ &fAbates: &7" + profile.getStats("Murder", "clkills") + "\n &8▪ &fVitórias: &7" + profile.getStats("Murder", "clwins") + "\n" +
                    "\n&eAssassinos:\n &8▪ &fAbates: &7" + profile.getStats("Murder", "askills") + "\n &8▪ &fVitórias: &7" + profile.getStats("Murder", "aswins") + "\n" +
                    "\n&fCoins: &6" + StringUtils.formatNumber(profile.getCoins("Murder"))));

    this.setItem(EGG_SLOT, createGameStatsItem("DRAGON_EGG", "&aEgg Wars",
            "&eGeral:\n" +
                    " &8▪ &fPartidas: &7" + profile.getStats("EggWars", "1v1games", "2v2games", "3v3games", "4v4games") + "\n" +
                    " &8▪ &fAbates: &7" + profile.getStats("EggWars", "1v1kills", "2v2games", "3v3games", "4v4games") + "\n" +
                    " &8▪ &fMortes: &7" + profile.getStats("EggWars", "1v1deaths", "2v2deaths", "3v3deaths", "4v4deaths") + "\n" +
                    " &8▪ &fAbates Finais: &7" + profile.getStats("EggWars", "1v1finalkills", "2v2finalkills", "3v3finalkills", "4v4finalkills") + "\n" +
                    " &8▪ &fMortes Finais: &7" + profile.getStats("EggWars", "1v1finaldeaths", "2v2finaldeaths", "3v3finaldeaths", "4v4finaldeaths") + "\n" +
                    " &8▪ &fVitórias: &7" + profile.getStats("EggWars", "1v1wins", "2v2wins", "3v3wins", "4v4wins") + "\n" +
                    " &8▪ &fOvos destruídos: &7" + profile.getStats("EggWars", "1v1eggsdestroyeds", "2v2eggsdestroyeds", "3v3eggsdestroyeds", "4v4eggsdestroyeds") + "\n" +
                    " &8▪ &fOvos perdidos: &7" + profile.getStats("EggWars", "1v1eggslosteds", "2v2eggslosteds", "3v3eggslosteds", "4v4eggslosteds") + "\n" +
                    " \n" +
                    "&fCoins: &6" + StringUtils.formatNumber(profile.getCoins("EggWars"))));

    this.setItem(31, BukkitUtils.deserializeItemStack("Arrow : 1 : nome>&cVoltar"));

    this.register(Core.getInstance());
    this.open();
  }

  private ItemStack createGameStatsItem(String material, String name, String description) {
    return BukkitUtils.deserializeItemStack(material + " : 1 : nome>" + name + " : desc>" + description);
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


              case 31:
                new AccountPageMenu(profile);
                break;


              case SKY_WARS_SLOT:
              case THE_BRIDGE_SLOT:
              case MURDER_SLOT:
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
