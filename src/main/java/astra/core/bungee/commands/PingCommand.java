package astra.core.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PingCommand extends Commands {

    public PingCommand() {super("ping", "latencia", "ms");}

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText("§cComando exclusivo para jogadores."));
        } else {
            ProxiedPlayer player = (ProxiedPlayer)sender;
            int ping = player.getPing();

            if (ping > 200) {
                player.sendMessage(TextComponent.fromLegacyText("§dSeu ping é: §C" + ping + "ms§d!"));
            } else {
                player.sendMessage(TextComponent.fromLegacyText("§dSeu ping é: §a" + ping + "ms§d!"));
            }
        }
    }
}
