package astra.core.api.npclib.api.event;

import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import astra.core.api.npclib.api.npc.NPC;

 
public class NPCStopFollowingEvent extends NPCEvent {

  private NPC npc;
  @Getter
  private Entity target;

  public NPCStopFollowingEvent(NPC npc, Entity target) {
    this.npc = npc;
    this.target = target;
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
