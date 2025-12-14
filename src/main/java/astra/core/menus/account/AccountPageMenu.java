package astra.core.menus.account;

import astra.core.Core;
import astra.core.api.menu.PlayerMenu;
import astra.core.menus.account.aparencia.PrincipalAparenciaMenu;
import astra.core.menus.account.social.SocialMenu;
import astra.core.player.Account;
import astra.core.player.cash.CashManager;
import astra.core.api.rank.Rank;
import astra.core.utils.BukkitUtils;
import astra.core.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AccountPageMenu extends PlayerMenu {
  private static final SimpleDateFormat SDF = new SimpleDateFormat("d 'de' MMMM. yyyy 'às' HH:mm", Locale.forLanguageTag("pt-BR"));

  public AccountPageMenu(Account profile) {
    super(profile.getPlayer(), "Sua conta", 5);

    this.setItem(0, BukkitUtils.putProfileOnSkull(this.player, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§aVisualizar sua conta")));

    this.setItem(2, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§aEditar seu perfil : desc>§7Altere o seu perfil! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTFmNTExZDY4NmJlNzA3YTEwMTJmYzc1NjJlMzEzNTQ5YzdiZmE0YmFmZDEzODg3NGEwMmQzYmFiMTRmMjJkYSJ9fX0="));
    this.setItem(3, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§aPreferências : desc>§7Configure suas preferências! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmVhMGY3YTVmMTVmYmNmMjg1MWU5NzA3YzdlNTI3ZTFkOWNiYjg5NDhiOGZmZTMzMTI3ODhjY2I1YWNhNzZkYiJ9fX0="));
    this.setItem(4, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§aConfigurar aparência : desc>§7Atualize sua skin, medalha\n§7tags e muito mais! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjI5NmIzMWIwNTMwZWU2YmYzNzYwZjgzOTBkZGM0ZTBkMDkwOThlNWExYmE2ZWE2NjJmN2U2YWFhYTRkY2QyMiJ9fX0="));
    this.setItem(5, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§aRedes sociais : desc>§7Atualize suas redes sociais! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDQ1NTg2MDY1ZjAyZTg1NTMwMTI1ZjQ2NmRmMTA3YzFjOGQzZTE0MzEwMTAzMmMyYjQ3ODA3MTE1YmJkYTJmMCJ9fX0="));
    this.setItem(6, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§aSua credibilidade : desc>§cEm desenvolvimento... : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmFkZjlkNzEzNjdjZDZlNTA1ZmI0OGNhYWE1YWNkY2RmZjJhMDlmNjZjNDg4ZGFmMDRkMDQ1ZWUwYmY1MjhlMSJ9fX0="));
    this.setItem(7, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§aPresentes : desc>§cEm desenvolvimento... : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGMyZjVjNWJlNmI1NzUwMTY3NzM3MzU5NzRjOWNhODgwN2UzMDI1ZTAxYjBlNjc2Y2E3MTQ3ZTAwNTQ1YjUxMCJ9fX0="));

    this.setItem(9, BukkitUtils.deserializeItemStack("160:5 : 1 : nome>§aCategoria atual"));
    this.setItem(10, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));
    this.setItem(11, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));
    this.setItem(12, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));
    this.setItem(13, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));
    this.setItem(14, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));
    this.setItem(15, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));
    this.setItem(16, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));
    this.setItem(17, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));

    this.setItem(31, BukkitUtils.putProfileOnSkull(this.player, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>&aSua conta : desc>&7▪ Grupo: " + Rank.getRoleByName(profile.getDataContainer("Account", "role").getAsString()).getName() + "\n&7▪ Cash: &6" + StringUtils.formatNumber(CashManager.getCash(profile)) + "\n \n&7▪ Conta criada em: &e" + SDF.format(profile.getDataContainer("Account", "created").getAsLong()) + "\n&7▪ Última entrada: &e" + SDF.format(profile.getDataContainer("Account", "lastlogin").getAsLong()) + "\n\n§7▪ Credibilidade: §a1\n§7▪ Telefone: §cNenhum\n§7▪ E-mail: §cNenhum\n§7▪ Fórum: §cNão vinculado")));

    this.register(Core.getInstance());
    this.open();
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(this.getInventory())) {
      evt.setCancelled(true);
      if (evt.getWhoClicked().equals(this.player)) {
        Account profile = Account.getAccount(this.player.getName());

        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getInventory())) {
          ItemStack item = evt.getCurrentItem();
          if (item != null && item.getType() != Material.AIR) {
            switch (evt.getSlot()) {
              case 2:
                new AccountEditMenu(profile);
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

