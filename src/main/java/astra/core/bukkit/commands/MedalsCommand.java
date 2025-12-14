package astra.core.bukkit.commands;

import astra.core.api.enums.Medal;
import astra.core.menus.account.aparencia.TagsMenu;
import astra.core.menus.command.MedalsCommandMenu;
import astra.core.menus.command.TagsCommandMenu;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import astra.core.mysql.data.DataContainer;
import astra.core.game.GameState;
import astra.core.player.Account;
import astra.core.player.fake.FakeManager;
import astra.core.utils.StringUtils;
import astra.core.utils.TagUtils;

import java.util.ArrayList;
import java.util.List;

public class MedalsCommand extends Commands {

    public MedalsCommand() {
        super("medals", "medalha", "medal");
    }

    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEste comando só pode ser executado por jogadores.");
            return;
        }

        Player player = (Player) sender;

        if (FakeManager.isFake(player.getName())) {
            player.sendMessage("§cNão é possível executar este comando com o /nick ativado.");
            player.playSound(player.getLocation(), Sound.NOTE_PIANO, 0.5f, 2.0f);
            return;
        }

        List<String> medalhasDisponiveis = new ArrayList<>();
        for (Medal medal : Medal.values()) {
            String permission = medal.getPermission();
            if (player.hasPermission(permission)) {
                medalhasDisponiveis.add(medal.getName());
            }
        }

        if (args.length == 0) {
            new MedalsCommandMenu(Account.getAccount(player.getName()));
        }
    }
}
