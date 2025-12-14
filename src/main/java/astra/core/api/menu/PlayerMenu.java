package astra.core.api.menu;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import astra.core.Core;
import org.bukkit.event.inventory.InventoryClickEvent;

@Getter
public abstract class PlayerMenu extends Menu implements Listener {

  protected Player player;

  public PlayerMenu(Player player, String title, int rows) {
    super(title, rows);
    this.player = player;
  }

  public void register(Core plugin) {
    Bukkit.getPluginManager().registerEvents(this, plugin);
  }

  public void open() {
    this.player.openInventory(getInventory());
  }

    public abstract void onInventoryClick(InventoryClickEvent event);
}
