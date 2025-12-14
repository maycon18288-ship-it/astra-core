package astra.core.mysql.data.container;

import astra.core.player.PlayerProfileEnums;
import org.json.simple.JSONObject;
import astra.core.mysql.data.DataContainer;
import astra.core.mysql.data.interfaces.AbstractContainer;

import java.util.Objects;

@SuppressWarnings("unchecked")
public class PreferencesContainer extends AbstractContainer {

  public PreferencesContainer(DataContainer dataContainer) {
    super(dataContainer);
  }

  public void changePlayerVisibility() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("pv", Objects.requireNonNull(PlayerProfileEnums.PlayerVisibility.getOriginal((long) preferences.get("pv"))).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }

  public void changePrivateMessages() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("pm", Objects.requireNonNull(PlayerProfileEnums.PrivateMessages.getOriginal((long) preferences.get("pm"))).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }

  public void changeProtectionLobby() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("pl", Objects.requireNonNull(PlayerProfileEnums.ProtectionLobby.getOriginal((long) preferences.get("pl"))).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }

  public void changeParty() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("party", Objects.requireNonNull(PlayerProfileEnums.PartyRequest.getOriginal((long) preferences.get("party"))).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }

  public void changeClan() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("clan", Objects.requireNonNull(PlayerProfileEnums.ClanRequest.getOriginal((long) preferences.get("clan"))).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }

  public void changeFriendRequest() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("amg", Objects.requireNonNull(PlayerProfileEnums.FriendRequest.getOriginal((long) preferences.get("amg"))).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }

  public void changeAutoGG() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("autogg", Objects.requireNonNull(PlayerProfileEnums.AutoGG.getOriginal((long) preferences.get("autogg"))).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }

  public void changeChat() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("chat", Objects.requireNonNull(PlayerProfileEnums.Chat.getOriginal((long) preferences.get("chat"))).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }

  public void changeNotificacao() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("notificacao", Objects.requireNonNull(PlayerProfileEnums.Notificacao.getOriginal((long) preferences.get("notificacao"))).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }

  public void changeMencao() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("mencao", Objects.requireNonNull(PlayerProfileEnums.Mencao.getOriginal((long) preferences.get("mencao"))).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }

  public void changeScoreboard() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("scoreboard", Objects.requireNonNull(PlayerProfileEnums.Scoreboard.getOriginal((long) preferences.get("scoreboard"))).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }

  public void changePartidaAutomatica() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("partidaautomatica", Objects.requireNonNull(PlayerProfileEnums.PartidaAutomatica.getOriginal((long) preferences.get("partidaautomatica"))).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }

  public void changeApelido() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("apelido", Objects.requireNonNull(PlayerProfileEnums.Apelido.getOriginal((long) preferences.get("apelido"))).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }

  public PlayerProfileEnums.PlayerVisibility getPlayerVisibility() {
    return PlayerProfileEnums.PlayerVisibility.getOriginal((long) this.dataContainer.getAsJsonObject().get("pv"));
  }

  public PlayerProfileEnums.PrivateMessages getPrivateMessages() {
    return PlayerProfileEnums.PrivateMessages.getOriginal((long) this.dataContainer.getAsJsonObject().get("pm"));
  }

  public PlayerProfileEnums.ProtectionLobby getProtectionLobby() {
    return PlayerProfileEnums.ProtectionLobby.getOriginal((long) this.dataContainer.getAsJsonObject().get("pl"));
  }

  public PlayerProfileEnums.PartyRequest getParty() {
    return PlayerProfileEnums.PartyRequest.getOriginal((long) this.dataContainer.getAsJsonObject().get("party"));
  }

  public PlayerProfileEnums.ClanRequest getClan() {
    return PlayerProfileEnums.ClanRequest.getOriginal((long) this.dataContainer.getAsJsonObject().get("clan"));
  }

  public PlayerProfileEnums.FriendRequest getFriend() {
    return PlayerProfileEnums.FriendRequest.getOriginal((long) this.dataContainer.getAsJsonObject().get("amg"));
  }

  public PlayerProfileEnums.AutoGG getAutoGG() {
    return PlayerProfileEnums.AutoGG.getOriginal((long) this.dataContainer.getAsJsonObject().get("autogg"));
  }

  public PlayerProfileEnums.Chat getChat() {
    return PlayerProfileEnums.Chat.getOriginal((long) this.dataContainer.getAsJsonObject().get("chat"));
  }

  public PlayerProfileEnums.Notificacao getNotificacao() {
    return PlayerProfileEnums.Notificacao.getOriginal((long) this.dataContainer.getAsJsonObject().get("notificacao"));
  }

  public PlayerProfileEnums.Mencao getMencao() {
    return PlayerProfileEnums.Mencao.getOriginal((long) this.dataContainer.getAsJsonObject().get("mencao"));
  }

  public PlayerProfileEnums.Scoreboard getScoreboard() {
    return PlayerProfileEnums.Scoreboard.getOriginal((long) this.dataContainer.getAsJsonObject().get("scoreboard"));
  }

  public PlayerProfileEnums.PartidaAutomatica getPartidaAutomatica() {
    return PlayerProfileEnums.PartidaAutomatica.getOriginal((long) this.dataContainer.getAsJsonObject().get("partidaautomatica"));
  }

  public PlayerProfileEnums.Apelido getApelido() {
    return PlayerProfileEnums.Apelido.getOriginal((long) this.dataContainer.getAsJsonObject().get("apelido"));
  }
}
