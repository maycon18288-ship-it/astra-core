package astra.core.menus.account;

import astra.core.menus.account.aparencia.PrincipalAparenciaMenu;
import astra.core.menus.account.social.SocialMenu;
import astra.core.player.PlayerProfileEnums;
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

  public class AccountPreferencesMenu extends PlayerMenu {
  public AccountPreferencesMenu(Account profile) {
    super(profile.getPlayer(), "Preferências", 5);

    this.setItem(0, BukkitUtils.putProfileOnSkull(this.player, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§aVisualizar sua conta")));

    this.setItem(2, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§aEditar seu perfil : desc>§7Altere o seu perfil! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTFmNTExZDY4NmJlNzA3YTEwMTJmYzc1NjJlMzEzNTQ5YzdiZmE0YmFmZDEzODg3NGEwMmQzYmFiMTRmMjJkYSJ9fX0="));
    this.setItem(3, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§aPreferências : desc>§7Configure suas preferências! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmVhMGY3YTVmMTVmYmNmMjg1MWU5NzA3YzdlNTI3ZTFkOWNiYjg5NDhiOGZmZTMzMTI3ODhjY2I1YWNhNzZkYiJ9fX0="));
    this.setItem(4, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§aConfigurar aparência : desc>§7Atualize sua skin, medalha\n§7tags e muito mais! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjI5NmIzMWIwNTMwZWU2YmYzNzYwZjgzOTBkZGM0ZTBkMDkwOThlNWExYmE2ZWE2NjJmN2U2YWFhYTRkY2QyMiJ9fX0="));
    this.setItem(5, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§aRedes sociais : desc>§7Atualize suas redes sociais! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDQ1NTg2MDY1ZjAyZTg1NTMwMTI1ZjQ2NmRmMTA3YzFjOGQzZTE0MzEwMTAzMmMyYjQ3ODA3MTE1YmJkYTJmMCJ9fX0="));
    this.setItem(6, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§aSua credibilidade : desc>§cEm desenvolvimento... : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmFkZjlkNzEzNjdjZDZlNTA1ZmI0OGNhYWE1YWNkY2RmZjJhMDlmNjZjNDg4ZGFmMDRkMDQ1ZWUwYmY1MjhlMSJ9fX0="));
    this.setItem(7, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§aPresentes : desc>§cEm desenvolvimento... : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGMyZjVjNWJlNmI1NzUwMTY3NzM3MzU5NzRjOWNhODgwN2UzMDI1ZTAxYjBlNjc2Y2E3MTQ3ZTAwNTQ1YjUxMCJ9fX0="));

    this.setItem(9, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));
    this.setItem(10, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));
    this.setItem(11, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));
    this.setItem(12, BukkitUtils.deserializeItemStack("160:5 : 1 : nome>§aCategoria atual"));
    this.setItem(13, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));
    this.setItem(14, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));
    this.setItem(15, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));
    this.setItem(16, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));
    this.setItem(17, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));

    this.setItem(29, BukkitUtils.deserializeItemStack("347 : 1 : nome>§aBate-papo : desc>§7Clique para atualizar as\n§7preferências do chat!"));
    this.setItem(31, BukkitUtils.deserializeItemStack("DIAMOND_SWORD : 1 : nome>§aPedidos : desc>§7Clique para atualizar as\n§7preferências de pedidos!"));
    this.setItem(33, BukkitUtils.deserializeItemStack("COMPASS : 1 : nome>§aOutros : desc>§7Clique para atualizar as\n§7preferências diversas!"));


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
              case 0:
                new AccountPageMenu(profile);
                break;
              case 2:
                new AccountEditMenu(profile);
                break;
              case 4:
                new PrincipalAparenciaMenu(profile);
                break;
              case 5:
                new SocialMenu(profile);
                break;
              case 29:
                new ChatPreferences(profile);
                break;
              case 31:
                new PedidosPreferences(profile);
                break;
              case 33:
                new OthersPreferences(profile);
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

class ChatPreferences extends PlayerMenu {
  public ChatPreferences(Account profile) {
    super(profile.getPlayer(), "Configurações de Bate-papo", 5);

    PlayerProfileEnums.AutoGG autoGG = profile.getPreferencesContainer().getAutoGG();
    PlayerProfileEnums.Chat chat = profile.getPreferencesContainer().getChat();
    PlayerProfileEnums.Notificacao notificacao = profile.getPreferencesContainer().getNotificacao();
    PlayerProfileEnums.Mencao mencao = profile.getPreferencesContainer().getMencao();

    this.setItem(10, BukkitUtils.deserializeItemStack("FIREWORK : 1 : nome>§aAuto GG"));
    this.setItem(19, BukkitUtils.deserializeItemStack("351:" + autoGG.getItem() + " : 1 : nome>" + autoGG.getName()));

    this.setItem(12, BukkitUtils.deserializeItemStack("PAPER : 1 : nome>§aChat"));
    this.setItem(21, BukkitUtils.deserializeItemStack("351:" + chat.getItem() + " : 1 : nome>" + chat.getName()));

    this.setItem(14, BukkitUtils.deserializeItemStack("345 : 1 : nome>§aNotificações"));
    this.setItem(23, BukkitUtils.deserializeItemStack("351:" + notificacao.getItem() + " : 1 : nome>" + notificacao.getName()));

    this.setItem(16, BukkitUtils.deserializeItemStack("BEACON : 1 : nome>§aMenções no bate-papo"));
    this.setItem(25, BukkitUtils.deserializeItemStack("351:" + mencao.getItem() + " : 1 : nome>" + mencao.getName()));

    this.setItem(40, BukkitUtils.deserializeItemStack("ARROW : 1 : nome>§cVoltar"));

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
              case 19:
                profile.getPreferencesContainer().changeAutoGG();
                new ChatPreferences(profile);
                break;

              case 21:
                profile.getPreferencesContainer().changeChat();
                new ChatPreferences(profile);
                break;

              case 23:
                profile.getPreferencesContainer().changeNotificacao();
                new ChatPreferences(profile);
                break;

              case 25:
                profile.getPreferencesContainer().changeMencao();
                new ChatPreferences(profile);
                break;

              case 40:
                new AccountPreferencesMenu(profile);
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

class PedidosPreferences extends PlayerMenu {
  public PedidosPreferences(Account profile) {
    super(profile.getPlayer(), "Configurações de pedidos", 5);

    PlayerProfileEnums.ClanRequest clan = profile.getPreferencesContainer().getClan();
    PlayerProfileEnums.PartyRequest party = profile.getPreferencesContainer().getParty();
    PlayerProfileEnums.FriendRequest friend = profile.getPreferencesContainer().getFriend();

    this.setItem(11, BukkitUtils.deserializeItemStack("DIAMOND_SWORD : 1 : nome>§aPedidos de clan"));
    this.setItem(20, BukkitUtils.deserializeItemStack("351:" + clan.getItem() + " : 1 : nome>" + clan.getName()));

    this.setItem(13, BukkitUtils.deserializeItemStack("PAPER : 1 : nome>§aPedidos de party"));
    this.setItem(22, BukkitUtils.deserializeItemStack("351:" + party.getItem() + " : 1 : nome>" + party.getName()));

    this.setItem(15, BukkitUtils.deserializeItemStack("345 : 1 : nome>§aPedidos de amizade"));
    this.setItem(24, BukkitUtils.deserializeItemStack("351:" + friend.getItem() + " : 1 : nome>" + friend.getName()));

    this.setItem(40, BukkitUtils.deserializeItemStack("ARROW : 1 : nome>§cVoltar"));

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
              case 20:
                profile.getPreferencesContainer().changeClan();
                new PedidosPreferences(profile);
                break;
              case 22:
                profile.getPreferencesContainer().changeParty();
                new PedidosPreferences(profile);
                break;
              case 24:
                profile.getPreferencesContainer().changeFriendRequest();
                new PedidosPreferences(profile);
                break;
              case 40:
                new AccountPreferencesMenu(profile);
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

class OthersPreferences extends PlayerMenu {
  public OthersPreferences(Account profile) {
    super(profile.getPlayer(), "Configurações diversas", 5);

    PlayerProfileEnums.PlayerVisibility playerv = profile.getPreferencesContainer().getPlayerVisibility();
    PlayerProfileEnums.ProtectionLobby pl = profile.getPreferencesContainer().getProtectionLobby();
    PlayerProfileEnums.Scoreboard scoreboard = profile.getPreferencesContainer().getScoreboard();
    PlayerProfileEnums.PartidaAutomatica pa = profile.getPreferencesContainer().getPartidaAutomatica();
    PlayerProfileEnums.Apelido ap = profile.getPreferencesContainer().getApelido();


    this.setItem(10, BukkitUtils.deserializeItemStack("NETHER_STAR : 1 : nome>§aVisibilidade de jogadores"));
    this.setItem(19, BukkitUtils.deserializeItemStack("351:" + playerv.getItem() + " : 1 : nome>" + playerv.getName()));

    this.setItem(11, BukkitUtils.deserializeItemStack("385 : 1 : nome>§aConfirmação no /lobby"));
    this.setItem(20, BukkitUtils.deserializeItemStack("351:" + pl.getItem() + " : 1 : nome>" + pl.getName()));

    this.setItem(12, BukkitUtils.deserializeItemStack("389 : 1 : nome>§aScoreboard"));
    this.setItem(21, BukkitUtils.deserializeItemStack("351:" + scoreboard.getItem() + " : 1 : nome>" + scoreboard.getName()));

    this.setItem(13, BukkitUtils.deserializeItemStack("TNT : 1 : nome>§aPartida automática : desc>§7Esta opção serve para quando\n§7você clicar em um mapa para\n§7jogar novamente, você será\n§7redirecionado automaticamente para\n§7uma partida."));
    this.setItem(22, BukkitUtils.deserializeItemStack("351:" + pa.getItem() + " : 1 : nome>" + pa.getName()));

    this.setItem(14, BukkitUtils.deserializeItemStack("NAME_TAG : 1 : nome>§aApelido : desc>§7Ative ou desative o seu apelido que\n§7você pode esboçar nos principais\n§7chats do servidor!"));
    this.setItem(23, BukkitUtils.deserializeItemStack("351:" + ap.getItem() + " : 1 : nome>" + ap.getName()));

    this.setItem(40, BukkitUtils.deserializeItemStack("ARROW : 1 : nome>§cVoltar"));

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
              case 19:
                profile.getPreferencesContainer().changePlayerVisibility();
                new OthersPreferences(profile);
                break;
              case 20:
                profile.getPreferencesContainer().changeProtectionLobby();
                new OthersPreferences(profile);
                break;
              case 21:
                profile.getPreferencesContainer().changeScoreboard();
                new OthersPreferences(profile);
                break;
              case 22:
                profile.getPreferencesContainer().changePartidaAutomatica();
                new OthersPreferences(profile);
                break;
              case 23:
                profile.getPreferencesContainer().changeApelido();
                new OthersPreferences(profile);
                break;
              case 40:
                new AccountPreferencesMenu(profile);
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