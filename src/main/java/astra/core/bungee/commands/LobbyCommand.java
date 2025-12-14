package astra.core.bungee.commands;

import astra.core.bungee.Bungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class LobbyCommand extends Command {

    public LobbyCommand() {
        super("l", "lobbyprincipal", "hub");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("§cEste comando só pode ser usado por jogadores!");
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (player.getServer().getInfo().getName().equals("lobby") || player.getServer().getInfo().getName().equals("lobby2")) {
            player.sendMessage("§cVocê já está no lobby!");
        } else {
            player.sendMessage("§aConectando você ao lobby...");
            Bungee.getInstance().getServerManager().connectToServer(player, "lobby");
        }
    }
}
