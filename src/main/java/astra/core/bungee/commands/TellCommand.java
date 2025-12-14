package astra.core.bungee.commands;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import astra.core.bungee.Bungee;
import astra.core.mysql.Database;
import astra.core.api.rank.Rank;
import astra.core.utils.StringUtils;

public class TellCommand extends Commands {

    public TellCommand() {
        super("tell", "mensagem");
    }

    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText("§cApenas jogadores podem utilizar este comando."));
        } else {
            ProxiedPlayer player = (ProxiedPlayer)sender;

            if (args.length < 2) {
                player.sendMessage(TextComponent.fromLegacyText("§cUtilize: /tell [jogador] [mensagem]."));
            } else {
                ProxiedPlayer tKL = BungeeCord.getInstance().getPlayer(args[0]);
                String string = Database.getInstance().getTagAndName(player.getName());
                Rank tagRole = Rank.getRoleByName(string.split(" : ")[0]);
                String prefix = tagRole.getPrefix();

                String string2 = Database.getInstance().getTagAndName(tKL.getName());
                Rank tagRole2 = Rank.getRoleByName(string2.split(" : ")[0]);
                String prefix2 = tagRole2.getPrefix();
                if (player == tKL) {
                    player.sendMessage(TextComponent.fromLegacyText("§cVocê não pode mandar mensagens provadas para sí mesmo."));
                } else {
                    String msg = StringUtils.join(args, 1, " ");
                    if (player.hasPermission("aCore.tell.color")) {
                        msg = StringUtils.formatColors(msg);
                    }

                    if (tKL.isConnected()) {
                        if (!Database.getInstance().getPreference(tKL.getName(), "pm", true)) {
                            player.sendMessage(TextComponent.fromLegacyText("§cEste jogador desativou as mensagens privadas."));
                        } else if (!Database.getInstance().getPreference(player.getName(), "pm", true)) {
                            player.sendMessage(TextComponent.fromLegacyText("§cVocê desativou o tell."));
                        } else {
                            Bungee.tell.put(tKL, player);
                            Bungee.tell.put(player, tKL);
                            tKL.sendMessage(TextComponent.fromLegacyText(prefix + player.getName() + " §d⥤ §eVocê§f: §b" + msg));

                            player.sendMessage(TextComponent.fromLegacyText("§eVocê §d⥤ " + prefix2 + tKL.getName() + "§f: §b" + msg));
                        }
                    } else {
                        player.sendMessage(TextComponent.fromLegacyText("§cO usuário não foi encontrado."));
                    }
                }
            }
        }
    }
}
