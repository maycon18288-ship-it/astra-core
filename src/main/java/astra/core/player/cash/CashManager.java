package astra.core.player.cash;

import org.bukkit.entity.Player;
import astra.core.Core;
import astra.core.player.Account;
import astra.core.plugin.AuroraConfig;

public class CashManager {

  private static final AuroraConfig CONFIG;
  public static final boolean CASH;

  static {
    CONFIG = Core.getInstance().getConfig("utils");
    if (!CONFIG.contains("cash")) {
      CONFIG.set("cash", true);
    }

    CASH = CONFIG.getBoolean("cash");
  }

  public static void addCash(Account profile, long amount) throws CashException {
    if (profile == null) {
      throw new CashException("O usuário precisa estar conectado para alterar o cash");
    }

    profile.setStats("Account", profile.getStats("Account", "cash") + amount, "cash");
  }

  public static void addCash(Player player, long amount) throws CashException {
    addCash(player.getName(), amount);
  }

  public static void addCash(String name, long amount) throws CashException {
    addCash(Account.getAccount(name), amount);
  }

  public static void removeCash(Account profile, long amount) throws CashException {
    if (profile == null) {
      throw new CashException("O usuário precisa estar conectado para alterar o cash");
    }

    profile.setStats("Account", profile.getStats("Account", "cash") - amount, "cash");
  }

  public static void removeCash(Player player, long amount) throws CashException {
    removeCash(player.getName(), amount);
  }

  public static void removeCash(String name, long amount) throws CashException {
    removeCash(Account.getAccount(name), amount);
  }

  public static void setCash(Account profile, long amount) throws CashException {
    if (profile == null) {
      throw new CashException("O usuário precisa estar conectado para alterar o cash");
    }

    profile.setStats("Account", amount, "cash");
  }

  public static void setCash(Player player, long amount) throws CashException {
    setCash(player.getName(), amount);
  }

  public static void setCash(String name, long amount) throws CashException {
    setCash(Account.getAccount(name), amount);
  }

  public static long getCash(Account profile) {
    long cash = 0L;
    if (profile != null) {
      cash = profile.getStats("Account", "cash");
    }

    return cash;
  }

  public static long getCash(Player player) {
    return getCash(player.getName());
  }

  public static long getCash(String player) {
    return getCash(Account.getAccount(player));
  }
}
