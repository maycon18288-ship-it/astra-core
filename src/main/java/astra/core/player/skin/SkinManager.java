package astra.core.player.skin;

import lombok.Getter;

@Getter
public class SkinManager {
    private final String name;
    private final String value;
    private final String signature;

    public SkinManager(String name, String value, String signature) {
        this.name = name;
        this.value = value;
        this.signature = signature;
    }
}