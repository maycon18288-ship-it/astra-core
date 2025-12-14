package astra.core.api.npclib.api.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import astra.core.api.npclib.api.npc.NPC;

 
public class NPCDeathEvent extends NPCEvent implements Cancellable {

  private NPC npc;
  private Player killer;
  @Getter
  @Setter
  private boolean cancelled;

  public NPCDeathEvent(NPC npc, Player killer) {
    this.npc = npc;
    this.killer = killer;
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
