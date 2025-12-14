package astra.core.api.titles;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Title {

  private final String id;
  private final String title;
  private final String permission;
  private final String name;
  private final String displayMaterial;
  private final String lore;
  private final TitleCategory titleCategory;
  private final TitleAnimation animation;


  public Title(String displayMaterial, String lore, String name, String id, String title, String permission, TitleCategory category, TitleAnimation animation) {
    this.displayMaterial = displayMaterial;
    this.id = id;
    this.title = title;
    this.permission = permission;
    this.name = name;
    this.lore = lore;
    this.titleCategory = category;
    this.animation = animation;
  }

    private static final List<Title> TITLES = new ArrayList<>();

  public static Title getById(String id) {
    return TITLES.stream().filter(title -> title.getId().equals(id)).findFirst().orElse(null);
  }

  public static List<Title> listTitles() {
    return TITLES;
  }

  public static List<Title> getTitleByCategory(TitleCategory category) {
    return TITLES.stream().
             filter(title -> title.getTitleCategory() == category).
             collect(Collectors.toList());
  }

  public static Title getTitle(String name) {
    if (name == null || name.isEmpty()) return null;
    return TITLES.stream().filter(title -> title.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
  }

  public boolean isExclusive() {
    return this.permission != null && !this.permission.isEmpty();
  }

  public enum TitleAnimation {
    NONE,
    RAINBOW,
    FADE,
    ASTRA,
  }

  public enum TitleCategory {
    GERAL,
    BEDWARS,
    SKYWARS,
    THEBRIDGE,
    DUELS,
    EGGWARS,
    ESPECIAIS,
    PREMIACAO
  }
}
