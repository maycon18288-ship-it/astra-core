package astra.core.bukkit.commands;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ClearChatCommand extends Commands {

    public ClearChatCommand() {
        super("cc", "clearchat", "chatclear");
    }

    public void perform(CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission("aCore.cmd.clear")) {
            sender.sendMessage("§cComando não encontrado.");
        } else {
            sender.sendMessage("§aClearing messages...");

            for(int i = 0; i < 999; ++i) {
                Bukkit.getOnlinePlayers().forEach((player) -> player.sendMessage(""));
            }

            TextComponent component = new TextComponent("");
            BaseComponent[] var5 = TextComponent.fromLegacyText("§aO chat foi limpo!");

            for (BaseComponent components : var5) {
                component.addExtra(components);
            }
            Bukkit.getOnlinePlayers().forEach((player) -> player.spigot().sendMessage(component));
        }

    }
}