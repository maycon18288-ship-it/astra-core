package astra.core.bukkit.commands;

import astra.core.menus.account.aparencia.TagsMenu;
import astra.core.menus.command.TagsCommandMenu;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import astra.core.mysql.data.DataContainer;
import astra.core.game.GameState;
import astra.core.player.Account;
import astra.core.player.fake.FakeManager;
import astra.core.api.rank.Rank;
import astra.core.utils.StringUtils;
import astra.core.utils.TagUtils;

import java.util.List;
import java.util.stream.Collectors;

public class TagCommand extends Commands {

    public TagCommand() {
        super("tag");
    }

    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEste comando só pode ser executado por jogadores.");
            return;
        }

        Player player = (Player) sender;
        Account profile = Account.getAccount(player.getName());

        if (FakeManager.isFake(player.getName())) {
            player.sendMessage("§cNão é possível executar este comando com o /nick ativado.");
            player.playSound(player.getLocation(), Sound.NOTE_PIANO, 0.5f, 2.0f);
            return;
        }

        List<String> tags = Rank.listRoles().stream()
                .filter(role -> player.hasPermission(role.getPermission()) || role.isDefault())
                .map(Rank::getName)
                .collect(Collectors.toList());

        if (args.length == 0) {
            sendAvailableTagsMessage(player, tags);
            return;
        }

        String action = args[0];
        if (tags.stream().noneMatch(tag -> StringUtils.stripColors(tag).equalsIgnoreCase(action))) {
            player.sendMessage("§cTag não encontrada.");
            return;
        }

        String selectedTag = tags.stream()
                .filter(tag -> StringUtils.stripColors(tag).equalsIgnoreCase(action))
                .findFirst().get();

        Rank role = Rank.getRoleByName(StringUtils.stripColors(selectedTag));

        if (profile.getDataContainer("Account", "tag").getAsString().equalsIgnoreCase(StringUtils.stripColors(role.getName()))) {
            player.sendMessage("§aVocê já está utilizando a tag " + role.getName() + "§c.");
        }

        if (profile.playingGame() && (profile.getGame().getState() == GameState.EMJOGO || profile.getGame().getState() == GameState.ENCERRADO)) {
            player.sendMessage("§aVocê selecionou a tag " + role.getName() + "§a.");
            TagUtils.setTag(player, role);
            DataContainer container = profile.getDataContainer("Account", "tag");
            container.set(StringUtils.stripColors(role.getName()));
            profile.save();
        } else {
            player.sendMessage("§aVocê selecionou a tag " + role.getName() + "§a.");
            TagUtils.setTag(player, role);
            DataContainer container = profile.getDataContainer("Account", "tag");
            container.set(StringUtils.stripColors(role.getName()));
            profile.save();
        }

        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1.0f, 1.0f);
    }

    private void sendAvailableTagsMessage(Player player, List<String> tags) {
        new TagsCommandMenu(Account.getAccount(player.getName()));
    }
}