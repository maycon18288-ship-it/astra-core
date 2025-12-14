package astra.core.game;

import org.bukkit.entity.Player;
import astra.core.player.Account;

import java.util.ArrayList;
import java.util.List;

public class FakeGame implements Game<GameTeam> {

  public static final FakeGame FAKE_GAME = new FakeGame();

  private FakeGame() {}

  private final List<Player> emptyList = new ArrayList<>(0);

  @Override
  public void broadcastMessage(String s) { }

  @Override
  public void broadcastMessage(String s, boolean b) {}

  @Override
  public void join(Account profile, boolean ignoreLeader) {}

  @Override
  public void leave(Account profile, Game<?> game) {}

  @Override
  public void kill(Account profile, Account profile1) {}

  @Override
  public void killLeave(Account profile, Account profile1) {}

  @Override
  public void start() {}

  @Override
  public void stop(GameTeam gameTeam) { }

  @Override
  public void reset() {}

  @Override
  public String getGameName() {
    return "FakeGame";
  }

  @Override
  public GameState getState() {
    return GameState.AGUARDANDO;
  }

  @Override
  public boolean isSpectator(Player player) {
    return false;
  }

  @Override
  public int getOnline() {
    return 0;
  }

  @Override
  public int getMaxPlayers() {
    return 0;
  }

  @Override
  public GameTeam getTeam(Player player) {
    return null;
  }

  @Override
  public List<GameTeam> listTeams() {
    return null;
  }

  @Override
  public List<Player> listPlayers() {
    return emptyList;
  }

  @Override
  public List<Player> listPlayers(boolean b) {
    return emptyList;
  }
}
