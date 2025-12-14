package astra.core.bukkit;

import com.google.common.collect.ImmutableList;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import astra.core.Core;
import astra.core.player.fake.FakeManager;
import astra.core.redis.RedisManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class BukkitPartyManager {

  private static BukkitTask CLEAN_PARTIES;
  private static final List<BukkitParty> BUKKIT_PARTIES = new ArrayList<>();
  private static final Gson GSON = new Gson();

  public static BukkitParty createParty(Player leader) {
    return createParty(leader.getName(), BukkitPartySizer.getPartySize(leader));
  }

  public static BukkitParty createParty(String leader, int size) {
    BukkitParty bp = new BukkitParty(leader, size);
    BUKKIT_PARTIES.add(bp);
    savePartyToRedis(bp);
    if (CLEAN_PARTIES == null && !FakeManager.isBungeeSide()) {
      CLEAN_PARTIES = new BukkitRunnable() {
        @Override
        public void run() {
          ImmutableList.copyOf(BUKKIT_PARTIES).forEach(BukkitParty::update);
        }
      }.runTaskTimer(Core.getInstance(), 0L, 40L);
    }

    return bp;
  }

  public static BukkitParty getLeaderParty(String player) {
    BukkitParty party = getPartyFromRedis(player);
    if (party != null) return party;
    return BUKKIT_PARTIES.stream().filter(bp -> bp.isLeader(player)).findAny().orElse(null);
  }

  public static BukkitParty getMemberParty(String player) {
    for (BukkitParty party : getAllPartiesFromRedis()) {
      if (party.isMember(player)) return party;
    }
    return BUKKIT_PARTIES.stream().filter(bp -> bp.isMember(player)).findAny().orElse(null);
  }

  public static void savePartyToRedis(BukkitParty party) {
    String key = "party:" + party.getLeader().toLowerCase();
    RedisManager.getInstance().set(key, GSON.toJson(party));
  }

  public static BukkitParty getPartyFromRedis(String leader) {
    String key = "party:" + leader.toLowerCase();
    String json = RedisManager.getInstance().get(key);
    if (json != null) {
      try {
        return GSON.fromJson(json, BukkitParty.class);
      } catch (Exception ignored) {}
    }
    return null;
  }

  public static List<BukkitParty> getAllPartiesFromRedis() {
    return BUKKIT_PARTIES;
  }

  public static void removePartyFromRedis(String leader) {
    String key = "party:" + leader.toLowerCase();
    RedisManager.getInstance().delete(key);
  }

  public static List<BukkitParty> listParties() {
    return BUKKIT_PARTIES;
  }
}
