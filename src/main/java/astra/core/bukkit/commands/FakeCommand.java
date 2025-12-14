package astra.core.bukkit.commands;

import astra.core.Manager;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import astra.core.player.Account;
import astra.core.player.fake.FakeManager;
import astra.core.api.rank.Rank;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static astra.core.player.fake.FakeManager.ALEX;
import static astra.core.player.fake.FakeManager.STEVE;

public class FakeCommand extends Commands {

  public FakeCommand() {
    super("fake", "faker", "fakel");
  }

  @Override
  public void perform(CommandSender sender, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("§cApenas jogadores podem utilizar este comando.");
      return;
    }

    Player player = (Player) sender;
    if (!player.hasPermission("cmd.fake") || (label.equalsIgnoreCase("fakel") && !player.hasPermission("cmd.fakelist"))) {
      player.sendMessage("§cVocê não possui permissão para utilizar este comando.");
      return;
    }

    Account profile = Account.getAccount(player.getName());
    if (label.equalsIgnoreCase("fake")) {
      if (profile != null && profile.playingGame()) {
        player.sendMessage("§cVocê não pode utilizar este comando no momento.");
        return;
      }

      if (FakeManager.getRandomNicks().stream().noneMatch(FakeManager::isUsable)) {
        player.sendMessage(" \n §c§lALTERAR NICKNAME\n \n §cNenhum nickname está disponível para uso no momento.\n ");
        return;
      }

      if (args.length == 0) {
        FakeManager.sendRole(player);
        return;
      }

      String roleName = args[0];
      if (!FakeManager.isFakeRole(roleName)) {
        player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
        FakeManager.sendRole(player);
        return;
      }

      if (Rank.getRoleByName(roleName) == null) {
        player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
        FakeManager.sendRole(player);
        return;
      }

      if (args.length == 1) {
        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
        FakeManager.sendSkin(player, roleName);
        return;
      }

      String skin = args[1];
      if (!skin.equalsIgnoreCase("alex") && !skin.equalsIgnoreCase("steve") && !skin.equalsIgnoreCase("you")) {
        player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
        FakeManager.sendSkin(player, roleName);
        return;
      }

      List<String> enabled = FakeManager.getRandomNicks().stream().filter(FakeManager::isUsable).collect(Collectors.toList());
      String fakeName = enabled.isEmpty() ? null : enabled.get(ThreadLocalRandom.current().nextInt(enabled.size()));
      if (fakeName == null) {
        player.sendMessage(" \n §c§lALTERAR NICKNAME\n \n §cNenhum nickname está disponível para uso no momento.\n ");
        return;
      }

      enabled.clear();
      FakeManager.applyFake(player, fakeName, roleName, skin.equalsIgnoreCase("steve") ? STEVE : skin.equalsIgnoreCase("you") ? (Manager.getSkin(player.getName(), "value") + ":" + Manager.getSkin(player.getName(), "signature")) : ALEX);
    } else if (label.equalsIgnoreCase("faker")) {
      if (profile != null && profile.playingGame()) {
        player.sendMessage("§cVocê não pode utilizar este comando no momento.");
        return;
      }

      if (!FakeManager.isFake(player.getName())) {
        player.sendMessage("§cVocê não está utilizando um nickname falso.");
        return;
      }

      FakeManager.removeFake(player);
    } else {
      List<String> nicked = FakeManager.listNicked();
      StringBuilder sb = new StringBuilder();
      for (int index = 0; index < nicked.size(); index++) {
        sb.append("§c").append(nicked.get(index)).append(" §fé na verdade ").append("§aacore-corefakereal:").append(nicked.get(index)).append(index + 1 == nicked.size() ? "" : "\n");
      }

      nicked.clear();
      if (sb.length() == 0) {
        sb.append("§cNão há nenhum usuário utilizando um nickname falso.");
      }

      player.sendMessage(" \n§eLista de nicknames falsos:\n \n" + sb + "\n ");
    }
  }
}