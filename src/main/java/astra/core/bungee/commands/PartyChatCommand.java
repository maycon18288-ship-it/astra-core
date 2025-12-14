package astra.core.bungee.commands;

import astra.core.mysql.Database;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import astra.core.bungee.party.BungeeParty;
import astra.core.bungee.party.BungeePartyManager;
import astra.core.api.rank.Rank;
import astra.core.utils.StringUtils;

public class PartyChatCommand extends Commands {

  public PartyChatCommand() {
    super("p");
  }

  @Override
  public void perform(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText("§cApenas jogadores podem utilizar este comando."));
      return;
    }
    ProxiedPlayer player = (ProxiedPlayer) sender;
    if (args.length == 0) {
      player.sendMessage(TextComponent.fromLegacyText("§cUtilize /p [mensagem] para conversar com a sua Party."));
      return;
    }

    BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
    if (party == null) {
      player.sendMessage(TextComponent.fromLegacyText("§cVocê não pertence a uma Party."));
      return;
    }

    String string = Database.getInstance().getTagAndName(player.getName());
    Rank tagRole = Rank.getRoleByName(string.split(" : ")[0]);
    party.broadcast("§d[§aP§ea§br§ct§6y§d] " + tagRole.getPrefix() + player.getName() + "§f: " + StringUtils.join(args, " "));
  }
}
