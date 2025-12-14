package astra.core.bukkit;

import astra.core.player.party.Party;
import astra.core.bukkit.BukkitPartyManager;

public class BukkitParty extends Party {

  public BukkitParty(String leader, int slots) {
    super(leader, slots);
  }

  @Override
  public void delete() {
    BukkitPartyManager.listParties().remove(this);
    BukkitPartyManager.removePartyFromRedis(this.getLeader());
    this.destroy();
  }

  @Override
  public void update() {
    super.update();
    BukkitPartyManager.savePartyToRedis(this);
  }
}
