package astra.core.api.npclib;

import astra.core.Core;
import astra.core.api.holograms.HologramLibrary;
import astra.core.api.holograms.api.Hologram;
import astra.core.api.npclib.api.npc.NPC;
import astra.core.api.npclib.trait.NPCTrait;
import astra.core.plugin.AuroraConfig;
import astra.core.utils.BukkitUtils;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ShopNPC {
    private static final AuroraConfig CONFIG = Core.getInstance().getConfig("npcs");
    private static final List<ShopNPC> NPCS = new ArrayList<>();

    @Getter
    private String id;
    @Getter
    private Location location;
    private NPC npc;
    private Hologram hologram;

    public ShopNPC(Location location, String id) {
        this.location = location;
        this.id = id;
        if (!this.location.getChunk().isLoaded()) {
            this.location.getChunk().load(true);
        }

        this.spawn();
    }

    public static void setupNPCs() {
        if (!CONFIG.contains("shopnpc")) {
            CONFIG.set("shopnpc", new ArrayList<>());
        }

        for (String serialized : CONFIG.getStringList("shopnpc")) {
            if (serialized.split("; ").length > 6) {
                String id = serialized.split("; ")[6];

                NPCS.add(new ShopNPC(BukkitUtils.deserializeLocation(serialized), id));
            }
        }
    }

    public static void add(String id, Location location) {
        NPCS.add(new ShopNPC(location, id));
        List<String> list = CONFIG.getStringList("shopnpc");
        list.add(BukkitUtils.serializeLocation(location) + "; " + id);
        CONFIG.set("shopnpc", list);
    }

    public static void remove(ShopNPC npc) {
        NPCS.remove(npc);
        List<String> list = CONFIG.getStringList("shopnpc");
        list.remove(BukkitUtils.serializeLocation(npc.getLocation()) + "; " + npc.getId());
        CONFIG.set("shopnpc", list);

        npc.destroy();
    }

    public static ShopNPC getById(String id) {
        return NPCS.stream().filter(npc -> npc.getId().equals(id)).findFirst().orElse(null);
    }

    public static Collection<ShopNPC> listNPCs() {
        return NPCS;
    }

    public void spawn() {
        if (this.npc != null) {
            this.npc.destroy();
            this.npc = null;
        }

        if (this.hologram != null) {
            HologramLibrary.removeHologram(this.hologram);
            this.hologram = null;
        }

        this.hologram = HologramLibrary.createHologram(this.location.clone().add(0, 0.5, 0));
        for (int index = Arrays.asList("§6Novas promoções!", "§5§lASTRA SHOP", "§eClique para abrir!").size(); index > 0; index--) {
            this.hologram.withLine(Arrays.asList("§6Novas promoções!", "§5§lASTRA SHOP", "§eClique para abrir!").get(index - 1));
        }

        this.npc = NPCLibrary.createNPC(EntityType.PLAYER, "§8[NPC] ");
        this.npc.data().set("shop-npc", true);
        this.npc.data().set(NPC.HIDE_BY_TEAMS_KEY, true);
        this.npc.addTrait(new NPCSkinTrait(this.npc, "ewogICJ0aW1lc3RhbXAiIDogMTc0ODQ1ODAzOTI3NCwKICAicHJvZmlsZUlkIiA6ICJiNTQ1ZDcxNDJkZmM0MWVlYjBjMDcwOTkwOTI4NTE0MyIsCiAgInByb2ZpbGVOYW1lIiA6ICJraXdpNDgxMiIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS81MmFhYzcwMGNjZjVlZDhhNjNlMmI0YzYxNGMwNmFiY2Q4MmY3ZWQwNjBmZTBlYWUzOWNkZjc5M2E3MmNmZmI4IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=", "LsUCTdIx2d5ej/irgc03xpAHcynOU6ktTKI4/7dWyDwCGTztgvD1zvfrj3/wFsyvwYV/itlTd/cWqH9CHi8Ik9nfWPWQ88jINp6THanLB6tZSXVkDsXvbvPxcVEXI25oRo6wbtdWZYTSodO1xbq1ZiX6O7wRUKVq8OUVrDd4NbHyd8rwR52xayqTXxjxGNxT1LxDzVEKVhDMRodd44bDW4D9oq81gmXtuzLsxpwKnEMQUyBBqIKzQBi81gSXwJ6nD0o22q7byvL4XrCtTwPxZbU+cPnWxqLTkQ+QCiNn+kCaJtthNxU8/I9UEWaBh10Kf2lxVQK0LaTtmy8CrbynNYJG68/Mpo0lBxwjL9wcOot2mcLCCCkAvrsi+0G2AjaLRBfwJCgwHwo70C579vSsw8HSU4fSESXWaQUwLwtD/AmknVElC0ZZOyd0MZCx27+KhyCsO4rIkp3yDi7qGxcfiism1DJ1aYlRVlzevl4y0o6DP0VrWnHP6qCj7pCANRKmjbJoIoPpjlKW3YqTXvtr7Dlh3MJVXXN0/QeXbiLkLHUWDAUqT7xjgTCgtXL9mWWhAWIPyXQ/vY3OP2M38Gl5JGHWhs4NRzVfGY5S8dF9qMQOCIw7ODcVJ6YK6USnmPxqHCVnYYXZoVt/nGr1JW5WbFF2WAOco4MAOh8SjB4yZEQ="));
        this.npc.addTrait(new NPCTrait(this.npc) {
            @Override
            public void onSpawn() {
                ((Player) getNPC().getEntity()).setItemInHand(new ItemStack(Material.EXP_BOTTLE));
            }
        });
        this.npc.spawn(this.location);
    }

    public void destroy() {
        this.id = null;
        this.location = null;

        this.npc.destroy();
        this.npc = null;
        HologramLibrary.removeHologram(this.hologram);
        this.hologram = null;
    }
}