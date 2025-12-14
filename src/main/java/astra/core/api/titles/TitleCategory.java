package astra.core.api.titles;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum TitleCategory {
    GERAL(10, "PAPER", "Geral", Arrays.asList(
            new Title("NAME_TAG", "§7Exibição:\n§f▪ §cNão pertube", "Não pertube", "§cNão pertube!", "§cNão pertube!","", Title.TitleCategory.GERAL, Title.TitleAnimation.NONE),
            new Title("NAME_TAG", "§7Exibição:\n§f▪ §eProcurando party!", "Procurando party", "§eProcurando party!", "§eProcurando party!","", Title.TitleCategory.GERAL, Title.TitleAnimation.NONE),
            new Title("NAME_TAG", "§7Exibição:\n§f▪ §3Tirando um cochilo...", "Tirando um cochilo", "§3Tirando um cochilo...", "§3Tirando um cochilo...","", Title.TitleCategory.GERAL, Title.TitleAnimation.NONE))),

    ESPECIAIS(11, "GOLD_INGOT", "Especiais", Arrays.asList(
            new Title("GOLD_INGOT", "§7Exibição:\n§f▪ §6§lCAMPEÃO §cBW CUP §b(2025)", "CAMPEÃO BW CUP 2025", "CAMPEÃO BW CUP 2025", "CAMPEÃO BW CUP 2025","", Title.TitleCategory.ESPECIAIS, Title.TitleAnimation.FADE),
            new Title("DIAMOND", "§7Exibição:\n§f▪ §e§l✯ §d§lASTRA NO TOPO! §e§l✯", "Astra no topo!", "§d§lASTRA NO TOPO!", "§d§lASTRA NO TOPO!","", Title.TitleCategory.ESPECIAIS, Title.TitleAnimation.ASTRA))),

    BEDWARS(12, "BED", "Bed Wars", Arrays.asList(
            new Title("BED", "§7Exibição:\n§f▪ §eNível no Bed Wars: {levelbw}", "Nível no Bed Wars", "§eNível no Bed Wars: {levelbw}", "§eNível no Bed Wars: {levelbw}","", Title.TitleCategory.BEDWARS, Title.TitleAnimation.NONE))),

    SKYWARS(13, "ENDER_PEARL", "Sky Wars", Arrays.asList(
            new Title("ENDER_PEARL", "§7Exibição:\n§f▪ §cNão pertube", "Não pertube", "§cNão pertube!", "§cNão pertube!","", Title.TitleCategory.GERAL, Title.TitleAnimation.NONE))),

    EGGWARS(14, "DRAGON_EGG", "Egg Wars", Arrays.asList(
            new Title("DRAGON_EGG", "§7Exibição:\n§f▪ §cNão pertube", "Não pertube", "§cNão pertube!", "§cNão pertube!","", Title.TitleCategory.GERAL, Title.TitleAnimation.NONE))),

    DUELS(15, "BLAZE_ROD", "Duels", Arrays.asList(
            new Title("BLAZE_ROD", "§7Exibição:\n§f▪ §cNão pertube", "Não pertube", "§cNão pertube!", "§cNão pertube!","", Title.TitleCategory.GERAL, Title.TitleAnimation.NONE))),

    THEBRIDGE(16, "STAINED_CLAY:11", "The Bridge", Arrays.asList(
            new Title("STAINED_CLAY:11", "§7Exibição:\n§f▪ §cNão pertube", "Não pertube", "§cNão pertube!", "§cNão pertube!","", Title.TitleCategory.GERAL, Title.TitleAnimation.NONE)));


    private final String item;
    private final List<Title> titles;
    private final String displayName;
    private final int slotdomenu;

    TitleCategory(int slotdomenu, String item, String displayName, List<Title> titles) {
        this.titles = titles;
        this.displayName = displayName;
        this.item = item;
        this.slotdomenu = slotdomenu;
    }
}
