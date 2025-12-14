package astra.core.menus.account;

import astra.core.Core;
import astra.core.api.menu.PlayerMenu;
import astra.core.menus.account.aparencia.PrincipalAparenciaMenu;
import astra.core.menus.account.social.SocialMenu;
import astra.core.player.Account;
import astra.core.utils.BukkitUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class AccountEditMenu extends PlayerMenu {

  public AccountEditMenu(Account profile) {
    super(profile.getPlayer(), "Editar perfil", 5);

    this.setItem(0, BukkitUtils.putProfileOnSkull(this.player, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§aVisualizar sua conta")));

    this.setItem(2, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§aEditar seu perfil : desc>§7Altere o seu perfil! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTFmNTExZDY4NmJlNzA3YTEwMTJmYzc1NjJlMzEzNTQ5YzdiZmE0YmFmZDEzODg3NGEwMmQzYmFiMTRmMjJkYSJ9fX0="));
    this.setItem(3, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§aPreferências : desc>§7Configure suas preferências! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmVhMGY3YTVmMTVmYmNmMjg1MWU5NzA3YzdlNTI3ZTFkOWNiYjg5NDhiOGZmZTMzMTI3ODhjY2I1YWNhNzZkYiJ9fX0="));
    this.setItem(4, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§aConfigurar aparência : desc>§7Atualize sua skin, medalha\n§7tags e muito mais! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjI5NmIzMWIwNTMwZWU2YmYzNzYwZjgzOTBkZGM0ZTBkMDkwOThlNWExYmE2ZWE2NjJmN2U2YWFhYTRkY2QyMiJ9fX0="));
    this.setItem(5, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§aRedes sociais : desc>§7Atualize suas redes sociais! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDQ1NTg2MDY1ZjAyZTg1NTMwMTI1ZjQ2NmRmMTA3YzFjOGQzZTE0MzEwMTAzMmMyYjQ3ODA3MTE1YmJkYTJmMCJ9fX0="));
    this.setItem(6, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§aSua credibilidade : desc>§cEm desenvolvimento... : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmFkZjlkNzEzNjdjZDZlNTA1ZmI0OGNhYWE1YWNkY2RmZjJhMDlmNjZjNDg4ZGFmMDRkMDQ1ZWUwYmY1MjhlMSJ9fX0="));
    this.setItem(7, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§aPresentes : desc>§cEm desenvolvimento... : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGMyZjVjNWJlNmI1NzUwMTY3NzM3MzU5NzRjOWNhODgwN2UzMDI1ZTAxYjBlNjc2Y2E3MTQ3ZTAwNTQ1YjUxMCJ9fX0="));

    this.setItem(9, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));
    this.setItem(10, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));
    this.setItem(11, BukkitUtils.deserializeItemStack("160:5 : 1 : nome>§aCategoria atual"));
    this.setItem(12, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));
    this.setItem(13, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));
    this.setItem(14, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));
    this.setItem(15, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));
    this.setItem(16, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));
    this.setItem(17, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));

    this.setItem(30, BukkitUtils.putProfileOnSkull(this.player, BukkitUtils.deserializeItemStack("NAME_TAG : 1 : nome>&aAlterar apelido : desc>&7Você pode alterar o seu apelido\n§7no servidor, assim todos os\n§7jogadores visualizarão seu\n§7apelido no chat!\n\n§eClique para alterar!")));
    this.setItem(32, BukkitUtils.putProfileOnSkull(this.player, BukkitUtils.deserializeItemStack("BARRIER : 1 : nome>&cRemover apelido")));

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
              case 0:
                new AccountPageMenu(profile);
                break;
              case 3:
                new AccountPreferencesMenu(profile);
                break;
              case 4:
                new PrincipalAparenciaMenu(profile);
                break;
              case 5:
                new SocialMenu(profile);
                break;
              case 30:
                new NicknameMenu(profile);
                break;
              case 32:
                if (profile.getDataContainer("Account", "apelido").getAsString().isEmpty()) {
                  player.closeInventory();
                  player.sendMessage("§c§lERRO! §cVocê não tem nenhum apelido definido para remover.");
                } else {
                  profile.getDataContainer("Account", "apelido").set("");
                  player.closeInventory();
                  player.sendMessage("§aApelido removido com sucesso!");
                }
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
