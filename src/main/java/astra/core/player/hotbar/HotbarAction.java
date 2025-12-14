package astra.core.player.hotbar;

import astra.core.player.Account;
import lombok.Getter;

@Getter
public class HotbarAction {

  private final String value;
  private final HotbarActionType actionType;

  public HotbarAction(String action) {
    String[] splitter = action.split(">");
    this.value = splitter.length > 1 ? splitter[1] : "";
    this.actionType = HotbarActionType.fromName(splitter[0]);
  }

  public void execute(Account profile) {
    if (this.actionType != null) {
      if (this.value.isEmpty()) {
        return;
      }
      
      this.actionType.execute(profile, this.value);
    }
  }

}
