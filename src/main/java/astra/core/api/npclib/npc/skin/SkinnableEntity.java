package astra.core.api.npclib.npc.skin;

import org.bukkit.entity.Player;
import astra.core.api.npclib.api.npc.NPC;

 
public interface SkinnableEntity {

  public NPC getNPC();

  public Player getEntity();

  public SkinPacketTracker getSkinTracker();

  public void setSkin(Skin skin);

  public Skin getNpcSkin();

  public void setSkinFlags(byte flags);
}
