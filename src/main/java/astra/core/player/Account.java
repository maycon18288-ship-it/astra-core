package astra.core.player;

import astra.core.api.titles.TitleManager;
import astra.core.mysql.data.container.PreferencesContainer;
import astra.core.mysql.data.container.SelectedContainer;
import astra.core.mysql.data.container.SkinsContainer;
import astra.core.redis.SpigotServerManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import astra.core.Core;
import astra.core.mysql.Database;
import astra.core.mysql.data.DataContainer;
import astra.core.mysql.data.interfaces.AbstractContainer;
import astra.core.mysql.exception.ProfileLoadException;
import astra.core.game.Game;
import astra.core.game.GameTeam;
import astra.core.player.hotbar.Hotbar;
import astra.core.api.rank.Rank;
import astra.core.player.scoreboard.MScoreboard;
import astra.core.utils.BukkitUtils;
import astra.core.utils.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Account {

  @Getter
  private String name;

  private Game<? extends GameTeam> game;
  @Getter
  @Setter
  private Hotbar hotbar;
  @Getter
  private MScoreboard scoreboard;

  private Map<String, Long> lastHit = new HashMap<>();
  @Getter
  private Map<String, Map<String, DataContainer>> tableMap;

  public Account(String name) throws ProfileLoadException {
    this.name = name;
    this.tableMap = Database.getInstance().load(name);
    this.getDataContainer("Account", "lastlogin").set(System.currentTimeMillis());
  }

  public static void sendServer(Account profile, String name) {
    if (!Core.getInstance().isEnabled()) {
      return;
    }

    Player player = profile.getPlayer();

    if (player != null) {
      Bukkit.getScheduler().runTask(Core.getInstance(), () -> {
        if (player.isOnline()) {
          player.closeInventory();
          player.sendMessage("§aConectando...");
          SpigotServerManager.getInstance().connectToGameServer(profile.getPlayer().getName(), name);
        }
      });
    }
  }

  public void setGame(Game<? extends GameTeam> game) {
    this.game = game;
    this.lastHit.clear();
    if (this.game != null) {
      TitleManager.leaveLobby(this);
    }
  }

  public void setHit(String name) {
    this.lastHit.put(name, System.currentTimeMillis() + 8000);
  }

  public void setScoreboard(MScoreboard scoreboard) {
    if (this.scoreboard != null) {
      this.scoreboard.destroy();
    }
    this.scoreboard = scoreboard;
  }

  public void update() {
    this.scoreboard.update();
  }

  public void refresh() {
    Player player = this.getPlayer();
    if (player == null) {
      return;
    }

    player.setMaxHealth(20.0);
    player.setHealth(20.0);
    player.setFoodLevel(20);
    player.setExhaustion(0.0f);
    player.setExp(0.0f);
    player.setLevel(0);
    player.setAllowFlight(false);
    player.closeInventory();
    player.spigot().setCollidesWithEntities(true);
    for (PotionEffect pe : player.getActivePotionEffects()) {
      player.removePotionEffect(pe.getType());
    }

    if (!playingGame()) {
      player.setGameMode(GameMode.ADVENTURE);

      player.setAllowFlight(player.hasPermission("aCore.fly"));
      this.getDataContainer("Account", "role").set(StringUtils.stripColors(Rank.getRank(player, true).getName()));
    }

    if (this.hotbar != null) {
      this.hotbar.apply(this);
    }
    this.refreshPlayers();
  }

  public void refreshPlayers() {
    Player player = this.getPlayer();
    if (player == null) {
      return;
    }

    if (this.hotbar != null) {
      this.hotbar.getButtons().forEach(button -> {
        if (button.getAction().getValue().equalsIgnoreCase("jogadores")) {
          player.getInventory().setItem(button.getSlot(), BukkitUtils.deserializeItemStack(button.getIcon()));
        }
      });
    }

    if (!this.playingGame()) {
      for (Player players : Bukkit.getOnlinePlayers()) {
        Account profile = Account.getAccount(players.getName());
        if (profile != null) {
          if (!profile.playingGame()) {
            if ((this.getPreferencesContainer().getPlayerVisibility() == PlayerProfileEnums.PlayerVisibility.TODOS || Rank.getRank(players).isAlwaysVisible())) {
              player.showPlayer(players);
            } else {
              player.hidePlayer(players);
            }

            if ((profile.getPreferencesContainer().getPlayerVisibility() == PlayerProfileEnums.PlayerVisibility.TODOS || Rank.getRank(player).isAlwaysVisible())) {
              players.showPlayer(player);
            } else {
              players.hidePlayer(player);
            }
          } else {
            player.hidePlayer(players);
            players.hidePlayer(player);
          }
        }
      }
    }
  }

  public void save() {
    if (this.name == null || this.tableMap == null) {
      return;
    }
    Database.getInstance().save(this.name, this.tableMap);
  }

  public void saveSync() {
    if (this.name == null || this.tableMap == null) {
      return;
    }
    Database.getInstance().saveSync(this.name, this.tableMap);
  }

  public void destroy() {
    this.name = null;
    this.game = null;
    this.hotbar = null;
    this.scoreboard = null;
    this.lastHit.clear();
    this.lastHit = null;
    this.tableMap.values().forEach(containerMap -> {
      containerMap.values().forEach(DataContainer::gc);
      containerMap.clear();
    });
    this.tableMap.clear();
    this.tableMap = null;
  }

  public boolean isOnline() {
    return this.name != null && isOnline(this.name);
  }

  private Player player;

  public void setPlayer(Player player) {
    this.player = player;
    UUID_CACHE.put(this.name.toLowerCase(), player.getUniqueId());
  }

  public Player getPlayer() {
    if (this.player == null) {
      this.player = this.name == null ? null : Bukkit.getPlayerExact(this.name);
    }

    return this.player;
  }

  public Game<?> getGame() {
    return this.getGame(Game.class);
  }

  @SuppressWarnings("unchecked")
  public <T extends Game<?>> T getGame(Class<T> gameClass) {
    return this.game != null && gameClass.isAssignableFrom(this.game.getClass()) ? (T) this.game : null;
  }

  public boolean playingGame() {
    return this.game != null;
  }

  public List<Account> getLastHitters() {
    List<Account> hitters = this.lastHit.entrySet().stream()
            .filter(entry -> entry.getValue() > System.currentTimeMillis() && isOnline(entry.getKey()))
            .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
            .map(entry -> getAccount(entry.getKey()))
            .collect(Collectors.toList());
    // limpar após uso
    this.lastHit.clear();
    return hitters;
  }

  public void addStats(String table, String... keys) {
    this.addStats(table, 1, keys);
  }

  public void addStats(String table, long amount, String... keys) {
    for (String key : keys) {
      this.getDataContainer(table, key).addLong(amount);
    }
  }

  public void setStats(String table, long amount, String... keys) {
    for (String key : keys) {
      this.getDataContainer(table, key).set(amount);
    }
  }

  public void updateDailyStats(String table, String date, long amount, String key) {
    long currentExpire = this.getStats(table, date);
    this.setStats(table, System.currentTimeMillis(), date);
    if (amount == 0 || (this.getStats(table, key) > 0 && !COMPARE_SDF.format(System.currentTimeMillis()).equals(COMPARE_SDF.format(currentExpire)))) {
      this.setStats(table, 0, key);
      return;
    }

    this.addStats(table, amount, key);
  }

  public void addCoins(String table, double amount) {
    this.getDataContainer(table, "Coins").addDouble(amount);
  }

  public void addCoinsWM(String table, double amount) {
    amount = this.calculateWM(amount);
    this.addCoins(table, amount);
  }

  public double calculateWM(double amount) {
    double add = 0.0D;
    return amount > 0.0 ? amount : add;
  }

  public void removeCoins(String table, double amount) {
    this.getDataContainer(table, "Coins").removeDouble(amount);
  }

  public long getStats(String table, String... keys) {
    long stat = 0;
    for (String key : keys) {
      stat += this.getDataContainer(table, key).getAsLong();
    }

    return stat;
  }

  public long getDailyStats(String table, String date, String key) {
    long currentExpire = this.getStats(table, date);
    if (!COMPARE_SDF.format(System.currentTimeMillis()).equals(COMPARE_SDF.format(currentExpire))) {
      this.setStats(table, 0, key);
    }

    this.setStats(table, System.currentTimeMillis(), date);
    return this.getStats(table, key);
  }

  public double getCoins(String table) {
    return this.getDataContainer(table, "Coins").getAsDouble();
  }

  public PreferencesContainer getPreferencesContainer() {return this.getAbstractContainer("Account", "preferencias", PreferencesContainer.class);}

  public SkinsContainer getSkinsContainer() {
    return this.getAbstractContainer("Skins", "info", SkinsContainer.class);
  }

  public DataContainer getDataContainer(String table, String key) {
    return this.tableMap.get(table).get(key);
  }

  public SelectedContainer getSelectedContainer() {return this.getAbstractContainer("Account", "selected", SelectedContainer.class);}

  public <T extends AbstractContainer> T getAbstractContainer(String table, String key, Class<T> containerClass) {
    return this.getDataContainer(table, key).getContainer(containerClass);
  }

  private static final Map<String, UUID> UUID_CACHE = new HashMap<>();
  private static final Map<String, Account> PROFILES = new HashMap<>();
  private static final SimpleDateFormat COMPARE_SDF = new SimpleDateFormat("yyyy/MM/dd");

  public static void create(String playerName) throws ProfileLoadException {
    Account profile = PROFILES.getOrDefault(playerName.toLowerCase(), null);
    if (profile == null) {
      profile = new Account(playerName);
      PROFILES.put(playerName.toLowerCase(), profile);
    }
  }

  public static Account load(String playerName) throws ProfileLoadException {
    Account profile = PROFILES.getOrDefault(playerName.toLowerCase(), null);
    if (profile == null) {
      playerName = Database.getInstance().exists(playerName);
      if (playerName != null) {
        profile = new Account(playerName);
      }
    }
    return profile;
  }

  public static Account getAccount(String playerName) {
    return PROFILES.get(playerName.toLowerCase());
  }

  public static Account unloadAccount(String playerName) {
    UUID_CACHE.remove(playerName.toLowerCase());
    return PROFILES.remove(playerName.toLowerCase());
  }

  public static Player findCached(String playerName) {
    UUID uuid = UUID_CACHE.get(playerName.toLowerCase());
    return uuid == null ? null : Bukkit.getPlayer(uuid);
  }

  public static boolean isOnline(String playerName) {
    return PROFILES.containsKey(playerName.toLowerCase());
  }

  public static Collection<Account> listProfiles() {
    return PROFILES.values();
  }

  public void applyHotbarAsync() {
    Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), () -> {
      // Preparação de dados pesados (se necessário)
      Bukkit.getScheduler().runTask(Core.getInstance(), () -> {
        if (this.hotbar != null) {
          this.hotbar.apply(this);
        }
      });
    });
  }

  public void applyScoreboardAsync() {
    Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), () -> {
      // Preparação de dados pesados (se necessário)
      Bukkit.getScheduler().runTask(Core.getInstance(), () -> {
        if (this.scoreboard != null) {
          this.scoreboard.update();
        }
      });
    });
  }
}
