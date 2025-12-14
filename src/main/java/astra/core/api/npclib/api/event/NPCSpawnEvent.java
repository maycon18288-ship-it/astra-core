package astra.core.api.npclib.api.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import astra.core.api.npclib.api.npc.NPC;
 
public class NPCSpawnEvent extends NPCEvent implements Cancellable {

  private NPC npc;
  @Getter
  @Setter
  private boolean cancelled;

  public NPCSpawnEvent(NPC npc) {
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
