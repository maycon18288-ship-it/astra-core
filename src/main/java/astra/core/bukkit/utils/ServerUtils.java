package astra.core.bukkit.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ServerUtils {
    
    private static Plugin plugin;
    
    public static void init(Plugin plugin) {
        ServerUtils.plugin = plugin;
    }
    
    public static void connectToServer(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        
        player.sendMessage("§aConectando você ao servidor " + server + "...");
        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }
    
    public static void connectToLobby(Player player) {
        connectToServer(player, "lobby");
    }
    
    public static void connectToBedWars(Player player) {
        connectToServer(player, "bedwars");
    }
    
    public static void connectToSkyWars(Player player) {
        connectToServer(player, "skywars");
    }
    
    public static void connectToEggWars(Player player) {
        connectToServer(player, "eggwars");
    }
    
    public static void connectToPvP(Player player) {
        connectToServer(player, "pvp");
    }
} 