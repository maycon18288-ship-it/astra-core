package astra.core.player.party;

import lombok.Getter;

@Getter
public enum PartyRole {
  MEMBER("Membro"),
  LEADER("Líder");

  private final String name;

  PartyRole(String name) {
    this.name = name;
  }

}
