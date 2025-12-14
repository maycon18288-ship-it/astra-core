package astra.core.game;

import java.util.List;
import org.bukkit.entity.Player;
import astra.core.player.Account;

public interface Game<T extends GameTeam> {
  
  void broadcastMessage(String message);
  
  void broadcastMessage(String message, boolean spectators);
  
  void join(Account profile, boolean ignoreLeader);
  
  void leave(Account profile, Game<?> game);
  
  void kill(Account profile, Account killer);
  
  void killLeave(Account profile, Account killer);
  
  void start();
  
  void stop(T winners);
  
  void reset();
  
  String getGameName();
  
  GameState getState();
  
  boolean isSpectator(Player player);
  
  int getOnline();
  
  int getMaxPlayers();
  
  T getTeam(Player player);
  
  List<T> listTeams();
  
  List<Player> listPlayers();
  
  List<Player> listPlayers(boolean spectators);
}
