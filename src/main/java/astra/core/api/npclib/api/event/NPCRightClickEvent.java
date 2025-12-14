package astra.core.api.npclib.api.event;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import astra.core.api.npclib.api.npc.NPC;

 
public class NPCRightClickEvent extends NPCEvent {

  private NPC npc;
  @Getter
  private Player player;

  public NPCRightClickEvent(NPC npc, Player clicked) {
    this.npc = npc;
    this.player = clicked;
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
