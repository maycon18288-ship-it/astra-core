package astra.core.api.npclib;

import astra.core.api.npclib.api.npc.NPC;
import astra.core.api.npclib.npc.skin.Skin;
import astra.core.api.npclib.npc.skin.SkinnableEntity;
import astra.core.api.npclib.trait.NPCTrait;

public class NPCSkinTrait extends NPCTrait {

    private Skin skin;

    public NPCSkinTrait(NPC npc, String value, String signature) {
        super(npc);
        this.skin = Skin.fromData(value, signature);
    }

    @Override
    public void onSpawn() {
        this.skin.apply((SkinnableEntity) this.getNPC().getEntity());
    }
}
