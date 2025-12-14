package astra.core.nms.v1_8_R3.network;

import net.minecraft.server.v1_8_R3.PlayerConnection;
import astra.core.nms.v1_8_R3.entity.EntityNPCPlayer;

public class EmptyNetHandler extends PlayerConnection {

  public EmptyNetHandler(EntityNPCPlayer entityplayer) {
    super(entityplayer.server, new EmptyNetworkManager(), entityplayer);
  }

}
