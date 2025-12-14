package astra.core.api.npclib.api;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import astra.core.api.npclib.api.npc.NPC;

 
public interface EntityController {
  
  void spawn(Location location, NPC npc);
  
  void remove();
  
  Entity getBukkitEntity();
}
