package astra.core.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import astra.core.bungee.Bungee;
import astra.core.mysql.Database;
import astra.core.api.rank.Rank;
import astra.core.utils.StringUtils;

public class ReplyCommand extends Commands {
    public ReplyCommand() {
        super("r", new String[] { "reply" });
    }

    public void perform(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            if (args.length == 0) {
                sender.sendMessage(TextComponent.fromLegacyText("§cUtilize /r [mensagem]"));
            } else {
                ProxiedPlayer player = (ProxiedPlayer)sender;
                ProxiedPlayer target = Bungee.tell.get(player);
                if (target != null && target.isConnected()) {
                    String join = StringUtils.join((Object[])args, " ");
                    if (player.hasPermission("rf.tell.color")) {
                        join = StringUtils.formatColors(join);
                    }

                    if (!Database.getInstance().getPreference(target.getName(), "pm", true)) {
                        player.sendMessage(TextComponent.fromLegacyText("§cEste jogador desativou o recebimento de tells."));
                    } else if (!Database.getInstance().getPreference(player.getName(), "pm", true)) {
                        player.sendMessage(TextComponent.fromLegacyText("§cVocê desativou o recebimento de tells."));
                    } else {
                        Bungee.tell.put(target, player);
                        Bungee.tell.put(player, target);
                        target.sendMessage(TextComponent.fromLegacyText("§8Mensagem de: " + Rank.getRank(player).getPrefix() + player.getName() + "§8: §b" + join));
                        player.sendMessage(TextComponent.fromLegacyText("§8Mensagem para: " + Rank.getRank(target).getPrefix() + target.getName() + "§8: §b" + join));
                    }
                } else {
                    player.sendMessage(TextComponent.fromLegacyText("§cUsuário não encontrado."));
                }
            }
        }
    }
}