package astra.core.utils;

import astra.core.player.fake.FakeManager;
import astra.core.Core;
import astra.core.api.rank.AnimatedRank;
import astra.core.api.rank.Rank;
import astra.core.api.enums.Medal;
import astra.core.mysql.data.DataContainer;
import astra.core.player.Account;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

@SuppressWarnings("deprecation")
public class TagUtils {
  private static final Map<String, FakeTeam> TEAMS = new HashMap<>();
  private static final Map<String, FakeTeam> CACHED_FAKE_TEAMS = new HashMap<>();
  private static Map<Player, AnimatedManager> playersSchedulers = new HashMap<>();


  public static void setTag(Player player) {
    if (FakeManager.isFake(player.getName())) {
      Rank fakeRole = FakeManager.getRole(player.getName());
      setTag(player.getName(), fakeRole.getPrefix(), "", fakeRole.getId());
      return;
    }
    
    Rank role = Rank.getRank(player);
    String suffix = "";

    resetTask(player);
    setTag(player.getName(), role.getPrefix(), suffix, role.getId());
  }

  private static void refreshAnimatedPlayers() {
    Set<Player> serverPlayers = new HashSet<>(Bukkit.getOnlinePlayers());
    playersSchedulers.forEach((p, s) -> {
      if (!serverPlayers.contains(p)) {
        playersSchedulers.remove(p);
      }
    });
  }

  private static Map<Player, AnimatedManager> getAnimatedPlayers() {
    refreshAnimatedPlayers();
    return playersSchedulers;
  }


  public static void setMedal(Player player, Medal medal) {
    Account profile = Account.getAccount(player.getName());
    String role;
    String suffix = "";

    if (profile != null) {
      role = profile.getDataContainer("Account", "role").getAsString();
      Rank currentPlayerRole = Rank.getRoleByName(role);

      DataContainer medalContainer = profile.getDataContainer("Account", "medalha");
      String medalSuffix;
      if (medalContainer != null) {
        medalSuffix = medalContainer.getAsString();
      }

      medalSuffix = currentPlayerRole != null ? currentPlayerRole.getPrefix() : "";
      setTag(player.getName(), medalSuffix, suffix);
    }

    role = Rank.getRoleByName(profile.getDataContainer("Account", "tag").getAsString()).getPrefix();
    setTag(player.getName(), role, suffix);
  }

  public static void setTag(Player player, Rank role) {
    if (role instanceof AnimatedRank) {
      setTag(player, (AnimatedRank) role, ((AnimatedRank) role).getInterval());
      return;
    }
    String suffix = "";

    resetTask(player);
    setTag(player.getName(), role.getPrefix(), suffix, role.getId());
  }

  public static void setTag(Player player, AnimatedRank role, long interval) {
    resetTask(player);
    Map<Player, AnimatedManager> animatedManagerMap = getAnimatedPlayers();

    animatedManagerMap.put(player, new AnimatedManager(Bukkit.getScheduler().runTaskTimer(Core.getInstance(), new AnimatedRunnable(role, player), 0L, interval)));
    playersSchedulers = animatedManagerMap;
  }

  public static void resetTask(Player player) {
    Map<Player, AnimatedManager> players = getAnimatedPlayers();
    if (players.containsKey(player)) {
      AnimatedManager manager = players.get(player);
      if (manager != null) {
        manager.scheduler.cancel();
        players.remove(player);
        playersSchedulers = players;
      }
    }
  }

  public static void setTag(String player, String prefix, String suffix) {
    resetTask(Bukkit.getPlayer(player));
    setTag(player, prefix, suffix, -1);
  }

  public static void setTag(String player, String prefix, String suffix, int sortPriority) {
    addPlayerToTeam(player, prefix != null ? prefix : "", suffix != null ? suffix : "", sortPriority);
  }

  public static void sendTeams(Player player) {
    for (FakeTeam fakeTeam : TEAMS.values()) {
      (new Wrapper(fakeTeam.getName(), fakeTeam.getPrefix(), fakeTeam.getSuffix(), 0, fakeTeam.getMembers())).send(player);
    }
  }

  public static void reset() {

    for (FakeTeam fakeTeam : TEAMS.values()) {
      removePlayerFromTeamPackets(fakeTeam, fakeTeam.getMembers());
      removeTeamPackets(fakeTeam);
    }

    CACHED_FAKE_TEAMS.clear();
    TEAMS.clear();
  }

  public static FakeTeam reset(String player) {
    return reset(player, decache(player));
  }

  private static FakeTeam decache(String player) {
    return CACHED_FAKE_TEAMS.remove(player);
  }

  public static FakeTeam getFakeTeam(String player) {
    return CACHED_FAKE_TEAMS.get(player);
  }

  private static void cache(String player, FakeTeam fakeTeam) {
    CACHED_FAKE_TEAMS.put(player, fakeTeam);
  }

  private static FakeTeam reset(String player, FakeTeam fakeTeam) {
    if (fakeTeam != null && fakeTeam.getMembers().remove(player)) {
      Player removing = Bukkit.getPlayerExact(player);
      boolean delete;
      if (removing != null) {
        delete = removePlayerFromTeamPackets(fakeTeam, removing.getName());
      } else {
        OfflinePlayer toRemoveOffline = Bukkit.getOfflinePlayer(player);
        delete = removePlayerFromTeamPackets(fakeTeam, toRemoveOffline.getName());
      }

      if (delete) {
        removeTeamPackets(fakeTeam);
        TEAMS.remove(fakeTeam.getName());
      }
    }

    return fakeTeam;
  }

  private static void addPlayerToTeam(String player, String prefix, String suffix, int sortPriority) {
    reset(player);
    FakeTeam joining = getTeam(prefix, suffix);
    if (joining != null) {
      joining.addMember(player);
    } else {
      joining = new FakeTeam(prefix, suffix, getNameFromInput(sortPriority));
      joining.addMember(player);
      TEAMS.put(joining.getName(), joining);
      addTeamPackets(joining);
    }

    Player adding = Bukkit.getPlayerExact(player);
    if (adding != null) {
      addPlayerToTeamPackets(joining, adding.getName());
      cache(adding.getName(), joining);
    } else {
      OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
      addPlayerToTeamPackets(joining, offlinePlayer.getName());
      cache(offlinePlayer.getName(), joining);
    }

  }

  public static Rank getTag(Player player) {
    FakeTeam fakeTeam = CACHED_FAKE_TEAMS.get(player.getName());
    if (fakeTeam != null) {
      String prefix = fakeTeam.getPrefix();

      for (Rank role : Rank.listRoles()) {
        if (role.getPrefix().equals(prefix)) {
          return role;
        }
      }
    }

    return null;
  }

  private static FakeTeam getTeam(String prefix, String suffix) {
    Iterator<FakeTeam> var2 = TEAMS.values().iterator();

    FakeTeam team;
    do {
      if (!var2.hasNext()) {
        return null;
      }

      team = (FakeTeam)var2.next();
    } while(!team.isSimilar(prefix, suffix));

    return team;
  }

  private static String getNameFromInput(int input) {
    return input < 0 ? "" : String.valueOf((char)(input + 65));
  }

  private static void removeTeamPackets(FakeTeam fakeTeam) {
    (new Wrapper(fakeTeam.getName(), fakeTeam.getPrefix(), fakeTeam.getSuffix(), 1, new ArrayList<>())).send();
  }

  private static boolean removePlayerFromTeamPackets(FakeTeam fakeTeam, String... players) {
    return removePlayerFromTeamPackets(fakeTeam, Arrays.asList(players));
  }

  private static boolean removePlayerFromTeamPackets(FakeTeam fakeTeam, List<String> players) {
    (new Wrapper(fakeTeam.getName(), 4, players)).send();
    fakeTeam.getMembers().removeAll(players);
    return fakeTeam.getMembers().isEmpty();
  }

  private static void addTeamPackets(FakeTeam fakeTeam) {
    (new Wrapper(fakeTeam.getName(), fakeTeam.getPrefix(), fakeTeam.getSuffix(), 0, fakeTeam.getMembers())).send();
  }

  private static void addPlayerToTeamPackets(FakeTeam fakeTeam, String player) {
    (new Wrapper(fakeTeam.getName(), 3, Collections.singletonList(player))).send();
  }
}

class AnimatedManager {
  public BukkitTask scheduler;

  public AnimatedManager(BukkitTask scheduler) {
    this.scheduler = scheduler;
  }
}

class AnimatedRunnable implements Runnable {
  public AnimatedRank animatedGroup;
  public Player player;
  public int lastIndex;

  public AnimatedRunnable(AnimatedRank animatedGroup, Player player) {
    this.player = player;
    this.animatedGroup = animatedGroup;
    this.lastIndex = 0;
  }

  @Override
  public void run() {
    TagUtils.setTag(player.getName(), animatedGroup.getAnimatedprefix().get(lastIndex), "", animatedGroup.getId());
    if (animatedGroup.getAnimatedprefix().size()-1 <= lastIndex) {
      lastIndex = 0;
    } else {
      lastIndex += 1;
    }
  }
}