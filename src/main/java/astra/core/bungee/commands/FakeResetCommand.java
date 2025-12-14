package astra.core.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import astra.core.bungee.Bungee;

public class FakeResetCommand extends Commands {

  public FakeResetCommand() {
    super("faker");
  }

  @Override
  public void perform(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText("§cApenas jogadores podem utilizar este comando."));
      return;
    }

    ProxiedPlayer player = (ProxiedPlayer) sender;
    if (!player.hasPermission("aCore.cmd.fake")) {
      player.sendMessage(TextComponent.fromLegacyText("§cVocê não possui permissão para utilizar este comando."));
      return;
    }

    if (!Bungee.isFake(player.getName())) {
      player.sendMessage(TextComponent.fromLegacyText("§cVocê não está utilizando um nickname falso."));
      return;
    }

    Bungee.removeFake(player);
  }
}
