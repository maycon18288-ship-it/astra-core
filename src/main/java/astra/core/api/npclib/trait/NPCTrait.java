package astra.core.api.npclib.trait;

import astra.core.api.npclib.api.npc.NPC;
import astra.core.api.npclib.api.trait.Trait;

 
public abstract class NPCTrait implements Trait {

  private NPC npc;

  public NPCTrait(NPC npc) {
    this.npc = npc;
  }

  public NPC getNPC() {
    return npc;
  }

  @Override
  public void onAttach() {}

  @Override
  public void onSpawn() {}

  @Override
  public void onDespawn() {}

  @Override
  public void onRemove() {}
}
