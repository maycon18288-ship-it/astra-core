package astra.core.utils;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FakeTeam {
  private static int ID = 0;
  private final List<String> members = new ArrayList<>();
  private final String name;
  private String prefix = "";
  private String suffix = "";

  public FakeTeam(String prefix, String suffix, String name) {
    this.name = name == null ? "[TEAM:" + ++ID + "]" : name + ++ID;
    this.prefix = prefix;
    this.suffix = suffix;
  }

  public void addMember(String player) {
    if (!this.members.contains(player)) {
      this.members.add(player);
    }

  }

    public boolean isSimilar(String prefix, String suffix) {
    return this.prefix.equals(prefix) && this.suffix.equals(suffix);
  }
}