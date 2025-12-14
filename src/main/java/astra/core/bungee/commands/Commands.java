package astra.core.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import astra.core.bungee.Bungee;

public abstract class Commands extends Command {

  public Commands(String name, String... aliases) {
    super(name, null, aliases);
    ProxyServer.getInstance().getPluginManager().registerCommand(Bungee.getInstance(), this);
  }

  public abstract void perform(CommandSender sender, String[] args);

  @Override
  public void execute(CommandSender sender, String[] args) {
    this.perform(sender, args);
  }

  public static void setupCommands() {
    ProxyServer proxy = Bungee.getInstance().getProxy();
    
    proxy.getPluginManager().registerCommand(Bungee.getInstance(), new LobbyCommand());
    new FakeCommand();
    new FakeResetCommand();
    new FakeListCommand();
    new PartyCommand();
    new TellCommand();
    new ReplyCommand();
    new StaffChatCommand();
    new PartyChatCommand();
    new PingCommand();
  }
}