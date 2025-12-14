package astra.core.api.titles;

import astra.core.Core;
import astra.core.player.Account;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class TitleManager {

  private final Map<String, TitleController> controllers = new HashMap<>();
  private static int tickCounter = 0;

  private TitleManager() {
    startUpdateTask();
  }

  private void startUpdateTask() {
    new BukkitRunnable() {
      @Override
      public void run() {
        controllers.values().forEach(TitleController::updatePosition);
        tickCounter++;
      }
    }.runTaskTimer(Core.getInstance(), 0L, 1L);
  }

  public void onJoinLobby(Account profile) {
    if (profile.getName() == null) {
      return;
    }

    Player player = profile.getPlayer();
    if (player != null) {
      this.controllers.values().forEach(controller -> {
        if (controller.getOwner() != null && player.canSee(controller.getOwner())) {
          controller.showToPlayer(player);
        }
      });
    }

    Title title = profile.getSelectedContainer().getTitle();
    if (title != null) {
      applyTitle(profile, title);
    }
  }

  public void onLeaveLobby(Account profile) {
    TitleController controller = this.getTitleController(profile);
    if (controller != null) {
      controller.disable();
    }

    Player player = profile.getPlayer();
    if (player != null) {
      this.controllers.values().forEach(c -> {
        if (c.getOwner() != null && player.canSee(c.getOwner())) {
          c.hideToPlayer(player);
        }
      });
    }
  }

  public void onLeaveServer(Account profile) {
    TitleController controller = this.controllers.remove(profile.getName());
    if (controller != null) {
      controller.destroy();
    }
  }

  public void onLobbyShow(Account profile, Account target) {
    Player player = profile.getPlayer();
    TitleController controller = this.getTitleController(target);
    if (controller != null) {
      Bukkit.getScheduler().scheduleSyncDelayedTask(Core.getInstance(), () -> {
        if (controller.getOwner() != null && player.isOnline() && player.canSee(controller.getOwner())) {
          controller.showToPlayer(player);
        }
      }, 10);
    }
  }

  public void onLobbyHide(Account profile, Account target) {
    Player player = profile.getPlayer();
    TitleController controller = this.getTitleController(target);
    if (controller != null) {
      controller.hideToPlayer(player);
    }
  }

  public void onSelectTitle(Account profile, Title title) {
    profile.getSelectedContainer().setTitle(title);
    profile.save();

    applyTitle(profile, title);
  }

  public void onDeselectTitle(Account profile) {
    TitleController controller = this.getTitleController(profile);
    if (controller == null) {
      return;
    }

    controller.setName("disabled");
  }

  private void applyTitle(Account profile, Title title) {
    TitleController oldController = this.controllers.remove(profile.getName());
    if (oldController != null) {
      oldController.destroy();
    }

    TitleController newController = new TitleController(profile.getPlayer(), title);
    newController.enable();
    this.controllers.put(profile.getName(), newController);
  }

  public TitleController getTitleController(Account profile) {
    return this.controllers.get(profile.getName());
  }

  private static final TitleManager TITLE_MANAGER = new TitleManager();

  public static void joinLobby(Account profile) {
    TITLE_MANAGER.onJoinLobby(profile);
  }

  public static void leaveLobby(Account profile) {
    TITLE_MANAGER.onLeaveLobby(profile);
  }

  public static void leaveServer(Account profile) {
    TITLE_MANAGER.onLeaveServer(profile);
  }

  public static void show(Account profile, Account target) {
    TITLE_MANAGER.onLobbyShow(profile, target);
  }

  public static void hide(Account profile, Account target) {
    TITLE_MANAGER.onLobbyHide(profile, target);
  }

  public static void select(Account profile, Title title) {
    TITLE_MANAGER.onSelectTitle(profile, title);
  }

  public static void deselect(Account profile) {
    TITLE_MANAGER.onDeselectTitle(profile);
  }

  public static String getRainbowTitle(String title) {
    ChatColor[] colors = {
            ChatColor.DARK_RED,
            ChatColor.RED,
            ChatColor.GOLD,
            ChatColor.YELLOW,
            ChatColor.GREEN,
            ChatColor.DARK_GREEN,
            ChatColor.BLUE,
            ChatColor.DARK_AQUA,
            ChatColor.LIGHT_PURPLE
    };

    StringBuilder rainbowTitle = new StringBuilder();
    int colorLength = colors.length;
    int titleLength = title.length();

    for (int i = 0; i < titleLength; i++) {
      int colorIndex = (colorLength + (tickCounter - i) % colorLength) % colorLength;
      rainbowTitle.append(colors[colorIndex]).append(ChatColor.BOLD).append(title.charAt(i));
    }

    return rainbowTitle.toString();
  }
}
