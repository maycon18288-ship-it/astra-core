package astra.core.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import astra.core.Manager;
import astra.core.bungee.party.BungeeParty;
import astra.core.bungee.party.BungeePartyManager;
import astra.core.mysql.Database;
import astra.core.api.rank.Rank;
import astra.core.utils.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static astra.core.player.party.PartyRole.LEADER;

public class PartyCommand extends Commands {

  public PartyCommand() {
    super("party");
  }

  @Override
  public void perform(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText("§cApenas jogadores podem utilizar este comando."));
      return;
    }

    ProxiedPlayer player = (ProxiedPlayer) sender;
    if (args.length == 0) {
      player.sendMessage(TextComponent.fromLegacyText(
              " \n§d/p [mensagem] §f- §7Comunicar-se com os membros." +
                      "\n§d/party abrir §f- §7Tornar a party pública." +
                      "\n§d/party puxar §f- §7Puxa os membros para o servidor que está." +
                      "\n§d/party fechar §f- §7Tornar a party privada." +
                      "\n§d/party entrar [jogador] §f- §7Entrar em uma party pública." +
                      "\n§d/party aceitar [jogador] §f- §7Aceitar uma solicitação." +
                      "\n§d/party ajuda §f- §7Mostrar essa mensagem de ajuda." +
                      "\n§d/party convidar [jogador] §f- §7Convidar um jogador." +
                      "\n§d/party deletar §f- §7Deletar a party." +
                      "\n§d/party expulsar [jogador] §f- §7Expulsar um membro." +
                      "\n§d/party info §f- §7Informações da sua Party." +
                      "\n§d/party negar [jogador] §f- §7Negar uma solicitação." +
                      "\n§d/party sair §f- §7Sair da Party." +
                      "\n§d/party transferir [jogador] §f- §7Transferir a Party para outro membro." +
                      "\n "));
      return;
    }

    String action = args[0];
    if (action.equalsIgnoreCase("abrir")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cVocê não pertence a uma party."));
        return;
      }

      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cVocê não é o líder da party."));
        return;
      }

      if (party.isOpen()) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cSua party já é pública."));
        return;
      }

      party.setOpen(true);
      player.sendMessage(TextComponent.fromLegacyText("§d§lPARTY §7» §eVocê definiu a party como pública."));
    } else if (action.equalsIgnoreCase("fechar")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cVocê não pertence a uma Party."));
        return;
      }

      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cVocê não é o líder da party."));
        return;
      }

      if (!party.isOpen()) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cSua party já é privada."));
        return;
      }

      party.setOpen(false);
      player.sendMessage(TextComponent.fromLegacyText("§d§lPARTY §7» §eVocê fechou a party para apenas convidados."));
    } else if (action.equalsIgnoreCase("entrar")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cUtilize /party entrar [jogador]."));
        return;
      }

      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cVocê não pode entrar na party de você mesmo."));
        return;
      }

      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party != null) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cVocê já pertence a uma party."));
        return;
      }

      party = BungeePartyManager.getLeaderParty(target);
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " não é um Líder de Party."));
        return;
      }

      target = party.getName(target);
      if (!party.isOpen()) {
        player.sendMessage(TextComponent.fromLegacyText("§cA Party de " + Manager.getCurrent(target) + " está fechada apenas para convidados."));
        return;
      }

      if (party.canJoin()) {
        player.sendMessage(TextComponent.fromLegacyText("§cA Party de " + Manager.getCurrent(target) + " está lotada."));
        return;
      }

      party.join(player.getName());
      player.sendMessage(TextComponent.fromLegacyText(" \n§aVocê entrou na Party de " + Rank.getPrefixed(target) + "§a!\n "));
    } else if (action.equalsIgnoreCase("aceitar")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cUtilize /party aceitar [jogador]."));
        return;
      }

      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cVocê não pode aceitar convites de você mesmo."));
        return;
      }

      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party != null) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cVocê já pertence a uma Party."));
        return;
      }

      party = BungeePartyManager.getLeaderParty(target);
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " não é um Líder de Party."));
        return;
      }

      target = party.getName(target);
      if (!party.isInvited(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " não convidou você para Party."));
        return;
      }

      if (party.canJoin()) {
        player.sendMessage(TextComponent.fromLegacyText("§cA Party de " + Manager.getCurrent(target) + " está lotada."));
        return;
      }

      party.join(player.getName());
      player.sendMessage(TextComponent.fromLegacyText(" \n§aVocê entrou na Party de " + Rank.getPrefixed(target) + "§a!\n "));
    } else if (action.equalsIgnoreCase("ajuda")) {
      player.sendMessage(TextComponent.fromLegacyText(
              " \n§d/p [mensagem] §f- §7Comunicar-se com os membros." +
                      "\n§d/party abrir §f- §7Tornar a party pública." +
                      "\n§d/party puxar §f- §7Puxa os membros para o servidor que está." +
                      "\n§d/party fechar §f- §7Tornar a party privada." +
                      "\n§d/party entrar [jogador] §f- §7Entrar em uma party pública." +
                      "\n§d/party aceitar [jogador] §f- §7Aceitar uma solicitação." +
                      "\n§d/party ajuda §f- §7Mostrar essa mensagem de ajuda." +
                      "\n§d/party convidar [jogador] §f- §7Convidar um jogador." +
                      "\n§d/party deletar §f- §7Deletar a party." +
                      "\n§d/party expulsar [jogador] §f- §7Expulsar um membro." +
                      "\n§d/party info §f- §7Informações da sua Party." +
                      "\n§d/party negar [jogador] §f- §7Negar uma solicitação." +
                      "\n§d/party sair §f- §7Sair da Party." +
                      "\n§d/party transferir [jogador] §f- §7Transferir a Party para outro membro." +
                      "\n "));
    } else if (action.equalsIgnoreCase("deletar")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cVocê não pertence a uma party."));
        return;
      }

      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cVocê não é o líder da party."));
        return;
      }

      party.broadcast(" \n" + Rank.getPrefixed(player.getName()) + " §adeletou a Party!\n ", true);
      party.delete();
      player.sendMessage(TextComponent.fromLegacyText("§d§lPARTY §7» §cVocê deletou a party."));
    } else if (action.equalsIgnoreCase("expulsar")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cUtilize /party expulsar [jogador]."));
        return;
      }

      BungeeParty party = BungeePartyManager.getLeaderParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cVocê não é o líder da party."));
        return;
      }

      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cVocê não pode se expulsar."));
        return;
      }

      if (!party.isMember(target)) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cEsse jogador não pertence a sua party."));
        return;
      }

      target = party.getName(target);
      party.kick(target);
      party.broadcast(" \n" + Rank.getPrefixed(player.getName()) + " §aexpulsou " + Rank.getPrefixed(target) + " §ada Party!\n ");
    } else if (action.equalsIgnoreCase("info")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cVocê não pertence a uma Party."));
        return;
      }

      List<String> members = party.listMembers().stream().filter(pp -> pp.getRole() != LEADER).map(pp -> (pp.isOnline() ? "§a" : "§c") + pp.getName()).collect(Collectors.toList());
      player.sendMessage(TextComponent.fromLegacyText(
              " \n§6Líder: " + Rank.getPrefixed(party.getLeader()) + "\n§6Pública: " + (party.isOpen() ? "§aSim" : "§cNão") + "\n§6Limite de Membros: §f" + party.listMembers()
                      .size() + "/" + party.getSlots() + "\n§6Membros: " + StringUtils.join(members, "§7, ") + "\n "));
    } else if (action.equalsIgnoreCase("negar")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cUtilize /party negar [jogador]."));
        return;
      }

      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cVocê não pode negar convites de você mesmo."));
        return;
      }

      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party != null) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cVocê já pertence a uma party."));
        return;
      }

      party = BungeePartyManager.getLeaderParty(target);
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " não é um Líder de Party."));
        return;
      }

      target = party.getName(target);
      if (!party.isInvited(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " não convidou você para Party."));
        return;
      }

      party.reject(player.getName());
      player.sendMessage(TextComponent.fromLegacyText(" \n§aVocê negou o convite de Party de " + Rank.getPrefixed(target) + "§a!\n "));
    } else if (action.equalsIgnoreCase("sair")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cVocê não pertence a uma party."));
        return;
      }

      party.leave(player.getName());
      player.sendMessage(TextComponent.fromLegacyText("§d§lPARTY §7» §cVocê saiu da party."));
    } else if (action.equalsIgnoreCase("transferir")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cUtilize /party transferir [jogador]."));
        return;
      }

      BungeeParty party = BungeePartyManager.getLeaderParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cVocê não é o líder da party."));
        return;
      }

      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cVocê não pode transferir a Party para você mesmo."));
        return;
      }

      if (!party.isMember(target)) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cEsse jogador não pertence a sua Party."));
        return;
      }

      target = party.getName(target);
      party.transfer(target);
      party.broadcast(" \n" + Rank.getPrefixed(player.getName()) + " §atransferiu a liderança da Party para " + Rank.getPrefixed(target) + "§a!\n ");
    }

    else if (action.equalsIgnoreCase("puxar")) {
      if (!(sender instanceof ProxiedPlayer)) {
        sender.sendMessage(TextComponent.fromLegacyText("§cApenas jogadores podem utilizar este comando."));
        return;
      }

      if (args.length == 0) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cUtilize /party puxar para enviar os membros da Party."));
        return;
      }

      BungeeParty party = BungeePartyManager.getLeaderParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cVocê precisa ser o Líder de uma Party para utilizar este comando."));
        return;
      }

      String leaderServer = player.getServer().getInfo().getName();

      party.listMembers().forEach(member -> {
        if (!member.getName().equalsIgnoreCase(player.getName())) {
          ProxiedPlayer playerb = ProxyServer.getInstance().getPlayer(member.getName());
          if (playerb != null && playerb.isConnected()) {
            String currentServer = playerb.getServer().getInfo().getName();

            if (!currentServer.equals(leaderServer)) {
              playerb.sendMessage(TextComponent.fromLegacyText("§d§lPARTY §7» §eConectando..."));
              playerb.connect(ProxyServer.getInstance().getServerInfo(leaderServer));
              playerb.sendMessage(TextComponent.fromLegacyText("§d§lPARTY §7» §aVocê foi puxado para o mesmo servidor do líder."));
            } else {
              playerb.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cVocê já está no mesmo servidor que o líder."));
            }

          } else {
            player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cNão foi possível puxar o jogador " + Rank.getPrefixed(member.getName()) + "§c."));
          }
        }
      });

      player.sendMessage(TextComponent.fromLegacyText("§d§lPARTY §7» §cTodos os membros da party foram transferidos."));

    } else {
      if (action.equalsIgnoreCase("convidar")) {
        if (args.length == 1) {
          player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cUtilize /party convidar [jogador]."));
          return;
        }

        action = args[1];
      }

      ProxiedPlayer target = ProxyServer.getInstance().getPlayer(action);
      if (target == null) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cUsuário não encontrado."));
        return;
      }

      action = target.getName();
      if (action.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cVocê não pode enviar convites para você mesmo."));
        return;
      }

      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        party = BungeePartyManager.createParty(player);
      }

      if (!Database.getInstance().getPreference(target.getName(), "party", true)) {
        player.sendMessage(TextComponent.fromLegacyText("§c§c§lERRO! §7» §cEste jogador desativou o pedido de party."));
        return;
      }

      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cApenas o Líder pode enviar convites."));
        return;
      }

      if (party.canJoin()) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cA sua Party está cheia."));
        return;
      }

      if (party.isInvited(action)) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §cVocê já enviou um convite para " + Manager.getCurrent(action) + "."));
        return;
      }

      if (BungeePartyManager.getMemberParty(action) != null) {
        player.sendMessage(TextComponent.fromLegacyText("§c§lERRO! §7» §c" + Manager.getCurrent(action) + " já pertence a uma Party."));
        return;
      }

      party.invite(target);
      player.sendMessage(
              TextComponent.fromLegacyText(" \n" + Rank.getPrefixed(action) + " §afoi convidado para a Party. Ele tem 60 segundos para aceitar ou negar esta solicitação.\n "));
    }
  }
}
