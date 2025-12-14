package astra.core.bukkit.commands;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VoarCommand extends Commands {

    private static final List<String> FLY = new ArrayList<>();
    private static final String LOBBY_WORLD_NAME = "world";

    public VoarCommand() {
        super("voar");
    }

    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEste comando só pode ser executado por jogadores.");
            return;
        }

        Player player = (Player) sender;
        World world = player.getWorld();

        if (!world.getName().equalsIgnoreCase(LOBBY_WORLD_NAME)) {
            player.sendMessage("§cComando não encontrado.");
            return;
        }

        if (!player.hasPermission("aCore.fly")) {
            player.sendMessage("§cVocê não tem permissão.");
            return;
        }

        if (FlyPlayer(player)) {
            FLY.remove(player.getName());
            player.setAllowFlight(false);
            player.sendMessage("§bVoar: §eModo voar §cdesativado§e!");
        } else {
            FLY.add(player.getName());
            player.setAllowFlight(true);
            player.sendMessage("§bVoar: §eModo voar §aativado§e!");
        }
    }

    public static void remove(Player player) {
        FLY.remove(player.getName());
        player.setAllowFlight(false);
    }

    public static boolean FlyPlayer(Player player) {
        return FLY.contains(player.getName());
    }
}