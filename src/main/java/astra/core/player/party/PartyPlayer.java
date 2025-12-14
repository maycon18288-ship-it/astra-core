package astra.core.player.party;

import astra.core.Manager;
import lombok.Getter;
import lombok.Setter;

@Getter
public class PartyPlayer {

  private final String name;
  @Setter
  private PartyRole role;

  public PartyPlayer(String name, PartyRole role) {
    this.name = name;
    this.role = role;
  }

  public void sendMessage(String message) {
    Object player = Manager.getPlayer(name);
    if (player != null) {
      Manager.sendMessage(player, message);
    }
  }

    public boolean isOnline() {
    return Manager.getPlayer(this.name) != null;
  }
}
