package astra.core.api.npclib.npc.skin;

import astra.core.api.profile.Mojang;
import astra.core.api.profile.InvalidMojangException;
import astra.core.nms.NMS;
 
public class Skin {

  private String value, signature;

  private Skin(String value, String signature) {
    this.value = value;
    this.signature = signature;
  }

  private Skin fetch(String name) {
    try {
      String id = Mojang.getUUID(name);
      if (id != null) {
        String property = Mojang.getSkinProperty(id);
        if (property != null) {
          this.value = property.split(" : ")[1];
          this.signature = property.split(" : ")[2];
        }
      }
    } catch (InvalidMojangException e) {
      System.out.println("Cannot fetch skin from name " + name + ": " + e.getMessage());
    }

    return this;
  }

  public void apply(SkinnableEntity entity) {
    NMS.setValueAndSignature(entity.getEntity(), value, signature);
  }

  public static Skin fromName(String name) {
    return new Skin(null, null).fetch(name);
  }

  public static Skin fromData(String value, String signature) {
    return new Skin(value, signature);
  }
}
