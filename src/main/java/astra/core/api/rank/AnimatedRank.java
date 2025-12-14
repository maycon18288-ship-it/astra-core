package astra.core.api.rank;

import lombok.Getter;

import java.util.List;

@Getter
public class AnimatedRank extends Rank {
    private final List<String> animatedprefix;
    private final long interval;

    public AnimatedRank(String name, List<String> prefix, String permission, boolean alwaysVisible, long interval) {
        super(name, prefix.get(0), permission, alwaysVisible);

        this.animatedprefix = prefix;
        this.interval = interval;
    }
}
