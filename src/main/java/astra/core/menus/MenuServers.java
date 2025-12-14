package astra.core.menus;

import astra.core.redis.SpigotServerManager;
import astra.core.redis.IServerManager.ServerStatus;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import astra.core.Core;
import astra.core.api.menu.UpdatablePlayerMenu;
import astra.core.player.Account;
import astra.core.utils.BukkitUtils;
import astra.core.utils.StringUtils;

public class MenuServers extends UpdatablePlayerMenu {

  private final SpigotServerManager serverManager;

  public MenuServers(Account profile) {
    super(profile.getPlayer(), "Servidores", 4);
    this.serverManager = SpigotServerManager.getInstance();
    
    if (this.serverManager == null) {
      player.sendMessage("§cO sistema de servidores está temporariamente indisponível.");
      player.closeInventory();
      return;
    }
    
    this.update();
    this.register(Core.getInstance(), 20L);
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

            for (ServerStatus serverStatus : serverManager.getServers().values()) {
              if (serverStatus.isShowInMenu() && serverStatus.getSlot() == evt.getSlot()) {
                serverManager.connectToGameServer(profile.getPlayer().getName(), serverStatus.getType());
                this.player.closeInventory();
                break;
              }
            }
          }
        }
      }
    }
  }

  public void update() {
    if (this.serverManager == null) {
      return;
    }
    
    for (ServerStatus serverStatus : serverManager.getServers().values()) {
      if (serverStatus.isShowInMenu()) {
        String icon = serverStatus.getIcon().replace("{players}", StringUtils.formatNumber(serverStatus.getOnlinePlayers()));
        this.setItem(serverStatus.getSlot(), BukkitUtils.deserializeItemStack(icon));
      }
    }
  }

  public void cancel() {
    super.cancel();
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
