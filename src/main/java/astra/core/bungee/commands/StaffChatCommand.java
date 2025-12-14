package astra.core.bungee.commands;


import astra.core.api.actionbar.ActionBarAPI;
import astra.core.mysql.Database;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import astra.core.api.rank.Rank;
import astra.core.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class StaffChatCommand extends Commands {
    public static List<String> IGNORE = new ArrayList();

    public StaffChatCommand() {
        super("sc", "s", "staffchat");
    }

    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText("§cApenas jogadores podem utilizar este comando."));
        } else {
            ProxiedPlayer player = (ProxiedPlayer)sender;
            if (!player.hasPermission("sc.view")) {
                player.sendMessage(TextComponent.fromLegacyText("§cVocê não tem permissão."));
            } else if (args.length == 0) {
                player.sendMessage(TextComponent.fromLegacyText("§cUtilize /sc [mensagem] ou /sc [ativar/desativar]."));
            } else {
                String message = args[0];
                if (message.equalsIgnoreCase("ativar")) {
                    player.sendMessage(TextComponent.fromLegacyText("§aO chat da equipe foi ativado!"));
                    IGNORE.remove(player.getName());
                } else if (message.equalsIgnoreCase("desativar")) {
                    player.sendMessage(TextComponent.fromLegacyText("§cO chat da equipe foi desativado."));
                    IGNORE.add(player.getName());
                } else {
                    String format = StringUtils.formatColors(StringUtils.join((Object[])args, " "));
                    BungeeCord.getInstance().getPlayers().stream().filter((pplayer) -> pplayer.hasPermission("sc.view") && !IGNORE.contains(pplayer.getName())).forEach((pplayer) -> {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("STAFF_BAR");
                        out.writeUTF(pplayer.getName());
                        String string = Database.getInstance().getTagAndName(player.getName());
                        Rank tagRole = Rank.getRoleByName(string.split(" : ")[0]);
                        pplayer.getServer().sendData("Core", out.toByteArray());
                        pplayer.sendMessage(TextComponent.fromLegacyText("§d[Staff Chat] §7[" + StringUtils.capitalise(player.getServer().getInfo().getName().toLowerCase()) + "] §7" + tagRole.getPrefix() + player.getName() + "§8: " + "§f" + format));
                    });
                }
            }
        }

    }
}
