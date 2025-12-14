package astra.core.player.hotbar;

import lombok.Getter;

public class HotbarButton {
  
  private final int slot;
  @Getter private final HotbarAction action;
  @Getter private final String icon;
  
  public HotbarButton(int slot, HotbarAction action, String icon) {
    this.slot = slot;
    this.action = action;
    this.icon = icon;
  }
  
  public int getSlot() {
    return this.slot - 1;
  }

}
