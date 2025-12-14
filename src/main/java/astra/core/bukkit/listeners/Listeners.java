package astra.core.bukkit.listeners;

import astra.core.api.profile.Mojang;
import astra.core.api.titles.TitleManager;
import astra.core.mysql.data.DataContainer;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import astra.core.api.bossbar.BossbarListener;
import astra.core.api.tablist.TabListAPI;
import astra.core.player.PlayerProfileEnums;
import lombok.var;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.WatchdogThread;
import astra.core.Core;
import astra.core.Manager;
import astra.core.mysql.data.container.SkinsContainer;
import astra.core.mysql.exception.ProfileLoadException;
import astra.core.player.Account;
import astra.core.player.fake.FakeManager;
import astra.core.player.hotbar.HotbarButton;
import astra.core.api.rank.Rank;
import astra.core.reflection.Accessors;
import astra.core.reflection.acessors.FieldAccessor;
import astra.core.utils.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Listeners implements Listener {

  public static final Map<String, Long> DELAY_PLAYERS = new HashMap<>();
  private static final Map<String, Long> PROTECTION_LOBBY = new HashMap<>();
  public static final List<Player> APELIDO = new ArrayList<>();

  private static final FieldAccessor<Map> COMMAND_MAP = Accessors.getField(SimpleCommandMap.class, "knownCommands", Map.class);
  private static final SimpleCommandMap SIMPLE_COMMAND_MAP = (SimpleCommandMap) Accessors.getMethod(Bukkit.getServer().getClass(), "getCommandMap").invoke(Bukkit.getServer());

  public static void setupListeners() {
    Bukkit.getPluginManager().registerEvents(new Listeners(), Core.getInstance());
    Bukkit.getPluginManager().registerEvents(new BossbarListener(), Core.getInstance());
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent evt) {
    if (evt.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
      try {
        Account.create(evt.getName());
      } catch (ProfileLoadException ignored) {
      }
    }
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerLoginMonitor(PlayerLoginEvent evt) {
    Account profile = Account.getAccount(evt.getPlayer().getName());
    SkinsContainer container = profile.getSkinsContainer();
    String skinv = container.getValue();
    String skinss = container.getSignature();
    if (skinv.equals("ewogICJ0aW1lc3RhbXAiIDogMTYxMjU1OTc5ODMwOCwKICAicHJvZmlsZUlkIiA6ICJmZDYwZjM2ZjU4NjE0ZjEyYjNjZDQ3YzJkODU1Mjk5YSIsCiAgInByb2ZpbGVOYW1lIiA6ICJSZWFkIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2ZiOTAzMjBjN2FlOGFmNjE0ZTFmNmRiM2Y5MDdmNDBmNjdlNjY4MGUxZWI4N2U5MDNlYmI5YjI2Zjk0YzljMjQiCiAgICB9CiAgfQp9") && skinss.equals("LjxNQagEnWdrNSVk9bM0iR/Sa/3xNOZwTxH61Ky5DSa+fRHmXvA8bYRu5usLIRWr9O89ObC3kiOLy8MbQFikERcXqgpZMgHh0GGQW+SEo4JyjI3iE3fT5v2YV1JFeSmGBYRy0v38osV+JfLapps39PKddwkY++19IlDWUQqskyDVyin2JdcNK7naeFxubxEX6R4QLqa5NHOdLU6Qdmw+i8kwJXw7ah/s8RhvBQlP/vm3URrunG9SvcYNfzw+c/7mHmKECe89xNjGpM9PvCFP8iZJd5XJIwLqBltuZaVHDyL7sMMBuZ853qjGt7cvGqqyDBgrdvkbKFCdV2GHIUE4pj6lbDmli/BBQtOP4GCkg/XSZB/J9gu00VgmVFJYSntHqRoHhh1rFM01hm91UpNzHOG13rqUXGdTl8QxxMaj1205h6JKRjNFJqDjJFJ/VmQ9zBMMDRZhRPFMwCnXYriJJbsaWVpKxs26Krz5oRIufqNdbhDH0BCwsA2b1gjPNBPl3hPcH5oVb8hYy9G9dbpnSIP5dbGCHHFX65idP2Dq3HvKA9gO5rWhqY+UVdlduzaOJwFLUPiVL4nTpeLmP+ya+LYYnkhY4FrjFAEzaVBpnWil+gCyz0radMpon1wrV7ytBFQR9fj1piYCMvvhgVTGCPAMPgXf1hME7LgX2G1ofyQ=")) {
      try {
        String id = Mojang.getUUID(profile.getName());
        if (id != null) {
          String textures = Mojang.getSkinProperty(id);
          if (textures != null) {
            skinv = textures.split(" : ")[1];
            skinss = textures.split(" : ")[2];
          }
        }
      } catch (Exception ignored) {
      }
    }

    String skinName = container.getSkin();
    container.setValue(container.getValue());
    container.setSignature(container.getSignature());
    if (skinName != null && container.getValue() != null && container.getSignature() != null && !skinName.equals("none")) {
      GameProfile gameProfile = (GameProfile)Accessors.getMethod(evt.getPlayer().getClass(), GameProfile.class, "getProfile").invoke(evt.getPlayer());
      gameProfile.getProperties().clear();
      gameProfile.getProperties().put("textures", new Property("textures", skinv, skinss));
    }

    profile.setPlayer(evt.getPlayer());
  }

  @EventHandler
  public void onAchievement(PlayerAchievementAwardedEvent event) {
    event.setCancelled(true);
  }

  @EventHandler
  public void onWeatherChange(WeatherChangeEvent event) {
    if (event.toWeatherState()) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerJoin(PlayerJoinEvent evt) throws InvocationTargetException {
    var player = evt.getPlayer();
    var profile = Account.getAccount(player.getName());

    TabListAPI.setHeaderAndFooter(player,
            "\n§e§l✬ §5§lREDE ASTRA §e§l✬\n",
            "\n     §eLoja: §floja.redeastra.com     \n     §eDiscord: §fdiscord.gg/redeastra    \n     §eFórum: §fredeastra.com     \n");

    Bukkit.getScheduler().runTaskLater(Core.getInstance(), () -> {
      if (profile.isOnline() && !profile.playingGame()) {
        TitleManager.joinLobby(profile);
      }
    }, 20L);
  }

  private static final FieldAccessor<WatchdogThread> RESTART_WATCHDOG = Accessors.getField(WatchdogThread.class, "instance", WatchdogThread.class);
  private static final FieldAccessor<Boolean> RESTART_WATCHDOG_STOPPING = Accessors.getField(WatchdogThread.class, "stopping", boolean.class);

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerQuit(PlayerQuitEvent evt) {
    Account profile = Account.unloadAccount(evt.getPlayer().getName());
    if (profile != null) {
      TitleManager.leaveServer(profile);
      if (profile.getGame() != null) {
        profile.getGame().leave(profile, profile.getGame());
      }
      if (!((CraftServer) Bukkit.getServer()).getHandle().getServer().isRunning() || RESTART_WATCHDOG_STOPPING.get(RESTART_WATCHDOG.get(null))) {
        profile.saveSync();
        Core.getInstance().getLogger().info("Perfil " + profile.getName() + " salvo!");
      } else {
        profile.save();
      }
      profile.destroy();
    }

    FakeManager.fakeNames.remove(evt.getPlayer().getName());
    FakeManager.fakeRoles.remove(evt.getPlayer().getName());
    FakeManager.fakeSkins.remove(evt.getPlayer().getName());
    DELAY_PLAYERS.remove(evt.getPlayer().getName());
    PROTECTION_LOBBY.remove(evt.getPlayer().getName().toLowerCase());
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent evt) {
    if (evt.isCancelled()) {
      return;
    }

    Player player = evt.getPlayer();
    Account profile = Account.getAccount(player.getName());

    if (profile != null) {
      String[] args = evt.getMessage().replace("/", "").split(" ");

      if (args.length > 0) {
        String command = args[0];

        switch (command) {
          case "lobby":
            if (COMMAND_MAP.get(SIMPLE_COMMAND_MAP).containsKey("lobby") && profile.getPreferencesContainer()
                    .getProtectionLobby() == PlayerProfileEnums.ProtectionLobby.ATIVADO) {
              long last = PROTECTION_LOBBY.getOrDefault(player.getName().toLowerCase(), 0L);
              if (last > System.currentTimeMillis()) {
                PROTECTION_LOBBY.remove(player.getName().toLowerCase());
                return;
              }

              evt.setCancelled(true);
              PROTECTION_LOBBY.put(player.getName().toLowerCase(), System.currentTimeMillis() + 3000);
              player.sendMessage("§eUtilize /lobby novamente para confirmar.");
            }
            break;
          case "tell":
            if (args.length > 1 && !args[1].equalsIgnoreCase(player.getName())) {
              profile = Account.getAccount(args[1]);
              if (profile != null && profile.getPreferencesContainer().getPrivateMessages() != PlayerProfileEnums.PrivateMessages.TODOS) {
                evt.setCancelled(true);
                player.sendMessage("§cEste usuário desativou as mensagens privadas.");
              }
            }
            break;
          default:
            break;
        }
      }
    }
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onAsyncPlayerChat(AsyncPlayerChatEvent evt) {
    Player player = evt.getPlayer();
    String originalMessage = evt.getMessage();
    String current = Manager.getCurrent(player.getName());
    Rank role = Rank.getRank(player);
    Account account = Account.getAccount(player.getName());

    if (APELIDO.contains(player)) {
      evt.setCancelled(true);
      String message = StringUtils.formatColors(evt.getMessage());
      if (message.equals("titlep:cancelchangename")) {
        APELIDO.remove(player);
        player.sendMessage("§cDefinição de apelido cancelada.");
        return;
      }

      if (message.length() < 2) {
        player.sendMessage("§cApelido muito curto, utilize pelo menos 4 caracteres.");
        return;
      }

      if (message.length() > 16) {
        player.sendMessage("§cApelido muito longo, utilize até 16 caracteres.");
        return;
      }

      if (StringUtils.stripColors(message).equals("puta") || StringUtils.stripColors(message).equals("desgraça") || StringUtils.stripColors(message).equals("caralho") || StringUtils.stripColors(message).equals("cu") || StringUtils.stripColors(message).equals("pênis") || StringUtils.stripColors(message).equals("penis") || StringUtils.stripColors(message).equals("vagina") || StringUtils.stripColors(message).equals("xereca") || StringUtils.stripColors(message).equals("xoxota") || StringUtils.stripColors(message).equals("piroca") || StringUtils.stripColors(message).equals("pinto")) {
        player.sendMessage("§cNão insira palavrões no seu apelido!");
        return;
      }

      if (StringUtils.stripColors(message).equals("&")) {
        player.sendMessage("§cNão utilize caracteres no seu apelido!");
      }


      DataContainer container2 = account.getDataContainer("Account", "apelido");
      container2.set(message);
      account.save();
      player.sendMessage("§aVocê definiu seu apelido para: §e" + message);
      APELIDO.remove(player);
    }

    if (!evt.isCancelled()) {
      Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();

      for (Player online : onlinePlayers) {
        String name = online.getName();
        if (originalMessage.contains(name)) {
          originalMessage = originalMessage.replaceAll("\\b" + name + "\\b", "§e@" + name);
        }
      }

      String formatted = String.format(evt.getFormat(), player.getName(), originalMessage);

      TextComponent component = new TextComponent("");
      BaseComponent[] componentsArray = TextComponent.fromLegacyText(formatted);

      for (BaseComponent part : componentsArray) {
        part.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell " + current + " "));
        part.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                TextComponent.fromLegacyText(StringUtils.getLastColor(role.getPrefix()) + current +
                        "\n§fGrupo: " + role.getName() +
                        "\n \n§eClique para enviar uma mensagem privada.")));
        component.addExtra(part);
      }

      evt.setCancelled(true);

      for (Player recipient : evt.getRecipients()) {
        if (recipient != null) {
          recipient.spigot().sendMessage(component);
        }
      }
    }
  }


  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent evt) {
    Player player = evt.getPlayer();
    Account profile = Account.getAccount(player.getName());

    if (profile != null && profile.getHotbar() != null) {
      ItemStack item = player.getItemInHand();
      if (evt.getAction().name().contains("CLICK") && item != null && item.hasItemMeta()) {
        HotbarButton button = profile.getHotbar().compareButton(player, item);
        if (button != null) {
          evt.setCancelled(true);
          button.getAction().execute(profile);
        }
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent evt) {

    if (evt.getRightClicked() instanceof ArmorStand) {
      if (evt.getRightClicked().getEntityId() >= 200000) {
        return;
      }
      if (evt.getPlayer().getGameMode() == GameMode.ADVENTURE) {
        evt.setCancelled(true);
      }
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onInventoryClick(InventoryClickEvent evt) {
    if (!(evt.getWhoClicked() instanceof Player)) return;

    Player player = (Player) evt.getWhoClicked();
    Account profile = Account.getAccount(player.getName());
    if (profile == null || profile.getHotbar() == null) return;

    ItemStack item = evt.getCurrentItem();
    if (item == null || item.getType() == Material.AIR || !item.hasItemMeta()) return;

    if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(player.getInventory())) {
      HotbarButton button = profile.getHotbar().compareButton(player, item);
      if (button != null) {
        evt.setCancelled(true);
        button.getAction().execute(profile);
      }
    }
  }
}
