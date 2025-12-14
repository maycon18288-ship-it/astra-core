package astra.core;

import astra.core.api.npclib.ShopNPC;
import astra.core.api.profile.Mojang;
import astra.core.api.rank.Rank;
import astra.core.api.titles.Title;
import astra.core.api.titles.TitleCategory;
import astra.core.bukkit.utils.ServerUtils;
import astra.core.redis.RedisManager;
import astra.core.utils.JsonHandler;
import com.comphenix.protocol.ProtocolLibrary;

import astra.core.plugin.AuroraPlugin;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import astra.core.bukkit.commands.Commands;
import astra.core.mysql.Database;
import astra.core.protocollib.FakeAdapter;
import astra.core.protocollib.HologramAdapter;
import astra.core.protocollib.NPCAdapter;
import astra.core.api.holograms.HologramLibrary;
import astra.core.api.npclib.NPCLibrary;
import astra.core.bukkit.listeners.Listeners;
import astra.core.bukkit.listeners.PluginMessageListener;
import astra.core.nms.NMS;
import astra.core.player.Account;
import astra.core.player.fake.FakeManager;
import astra.core.servers.ServerItem;
import astra.core.redis.SpigotServerManager;

public class Core extends AuroraPlugin {

  @Getter private static Core instance;
  public static boolean validInit;
  @Getter private static JsonHandler jsonHandler;
  @Getter private static RedisManager redisManager;
  @Getter private static Mojang mojangAPI;

  @Override
  public void start() {
    instance = this;
  }

  @Override
  public void load() {}

  @Override
  public void enable() {
    if (!NMS.setupNMS()) {
      this.setEnabled(false);
      return;
    }

    saveDefaultConfig();
    lobby = Bukkit.getWorlds().get(0).getSpawnLocation();

    for (TitleCategory category : TitleCategory.values()) {
      Title.listTitles().addAll(category.getTitles());
    }
    getLogger().info("Carregados " + Title.listTitles().size() + " títulos.");

    if (Bukkit.getSpawnRadius() != 0) {
      Bukkit.setSpawnRadius(0);
    }

    Database.setupDatabase(
      getConfig().getString("database.tipo"),
      getConfig().getString("database.mysql.host"),
      getConfig().getString("database.mysql.porta"),
      getConfig().getString("database.mysql.nome"),
      getConfig().getString("database.mysql.usuario"),
      getConfig().getString("database.mysql.senha"),
      getConfig().getBoolean("database.mysql.mariadb", false),
      getConfig().getString("database.mongodb.url", "")
    );

    try {
      redisManager = new RedisManager(getConfig(), getLogger());
      if (!redisManager.isConnected()) {
        getLogger().severe("Não foi possível conectar ao Redis. O plugin será desativado.");
        setEnabled(false);
        return;
      }
      
      new SpigotServerManager(redisManager, getLogger());
      
    } catch (Exception e) {
      getLogger().severe("Erro ao inicializar conexão com Redis: " + e.getMessage());
      if (e.getCause() != null) {
        getLogger().severe("Causa: " + e.getCause().getMessage());
      }
      setEnabled(false);
      return;
    }

    NPCLibrary.setupNPCs(this);
    HologramLibrary.setupHolograms(this);
    Rank.loadRanks();
    FakeManager.setupFake();
    ServerItem.setupServers();

    Commands.setupCommands();
    Listeners.setupListeners();
    ProtocolLibrary.getProtocolManager().addPacketListener(new FakeAdapter());
    ProtocolLibrary.getProtocolManager().addPacketListener(new NPCAdapter());
    ProtocolLibrary.getProtocolManager().addPacketListener(new HologramAdapter());

    getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    getServer().getMessenger().registerOutgoingPluginChannel(this, "aCore");
    getServer().getMessenger().registerIncomingPluginChannel(this, "aCore", new PluginMessageListener());
    ShopNPC.setupNPCs();

    ServerUtils.init(this);

    validInit = true;
    this.getLogger().info("O plugin foi ativado.");
  }

  @Override
  public void disable() {
    if (validInit) {
      Bukkit.getOnlinePlayers().forEach(player -> {
        Account profile = Account.unloadAccount(player.getName());
        if (profile != null) {
          profile.saveSync();
          this.getLogger().info("Saved " + profile.getName() + "!");
          profile.destroy();
        }
      });
      
      if (SpigotServerManager.getInstance() != null) {
        SpigotServerManager.getInstance().shutdown();
      }
      
      Database.getInstance().close();
    }
    ShopNPC.listNPCs().forEach(ShopNPC::destroy);
    this.getLogger().info("O plugin foi desativado.");
  }


  @Setter
  @Getter private static Location lobby;

}
