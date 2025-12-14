package astra.core.api.enums;

import astra.core.utils.StringUtils;
import lombok.Getter;

import java.util.concurrent.ThreadLocalRandom;

public enum EnumRarity {
    DIVINO("§b§lDIVINO", 10),
    EPICO("§6§lÉPICO", 25),
    RARO("§d§lRARO", 50),
    COMUM("§9§lCOMUM", 100);

    private static final EnumRarity[] VALUES = values();
    @Getter
    private final String name;
    private final int percentage;

    EnumRarity(String name, int percentage) {
        this.name = name;
        this.percentage = percentage;
    }

    public static EnumRarity getRandomRarity() {
        int random = ThreadLocalRandom.current().nextInt(100);
        for (EnumRarity rarity : VALUES) {
            if (random <= rarity.percentage) {
                return rarity;
            }
        }

        return COMUM;
    }

    public static EnumRarity fromName(String name) {
        for (EnumRarity rarity : VALUES) {
            if (rarity.name().equalsIgnoreCase(name)) {
                return rarity;
            }
        }

        return COMUM;
    }

    public String getColor() {
        return StringUtils.getFirstColor(this.getName());
    }

    public String getTagged() {
        return this.getColor() + "[" + StringUtils.stripColors(this.getName()) + "]";
    }
}
