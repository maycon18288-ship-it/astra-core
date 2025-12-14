package astra.core.api.npclib.api.event;

import org.bukkit.event.HandlerList;
import astra.core.api.npclib.api.npc.NPC;

 
public class NPCNavigationEndEvent extends NPCEvent {

  private NPC npc;

  public NPCNavigationEndEvent(NPC npc) {
    this.npc = npc;
  }

  public NPC getNPC() {
    return npc;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }

  private static final HandlerList HANDLER_LIST = new HandlerList();

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }
}
