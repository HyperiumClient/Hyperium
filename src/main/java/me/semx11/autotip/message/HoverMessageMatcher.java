package me.semx11.autotip.message;

import java.util.regex.Pattern;
import me.semx11.autotip.stats.StatsDaily;

public class HoverMessageMatcher extends MessageMatcher {

    private final StatsType statsType;

    public HoverMessageMatcher(Pattern pattern, String input, StatsType statsType) {
        super(pattern, input);
        this.statsType = statsType;
    }

    public void applyStats(StatsDaily stats) {
        statsType.getConsumer().accept(stats, this);
    }

}
