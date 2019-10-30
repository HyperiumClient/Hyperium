package me.semx11.autotip.message;

import me.semx11.autotip.chat.MessageOption;
import me.semx11.autotip.gson.exclusion.Exclude;
import me.semx11.autotip.stats.StatsDaily;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StatsMessage extends Message {

    @Exclude
    private final Map<String, StatsMessageMatcher> statsMessageCache = new ConcurrentHashMap<>();

    private StatsType statsType;
    private List<HoverMessage> hoverMessages;

    public StatsMessage() {
        super();
    }

    public StatsMessage(Pattern pattern, MessageOption hideFor, StatsType statsType) {
        super(pattern, hideFor);
        this.statsType = statsType;
    }

    public StatsMessageMatcher getMatcherFor(String input) {
        if (statsMessageCache.containsKey(input)) return statsMessageCache.get(input);
        StatsMessageMatcher matcher = new StatsMessageMatcher(pattern, input, statsType);
        statsMessageCache.put(input, matcher);
        return matcher;
    }

    public void applyHoverStats(String input, StatsDaily stats) {
        if (input == null || hoverMessages == null) return;
        List<String> lines = Arrays.stream(input.split("\n"))
            .map(String::trim)
            .collect(Collectors.toList());
        lines.forEach(line -> hoverMessages.stream()
            .map(message -> message.getMatcherFor(line))
            .filter(MessageMatcher::matches)
            .forEach(matcher -> matcher.applyStats(stats)));
    }
}
