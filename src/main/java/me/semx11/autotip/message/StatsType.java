package me.semx11.autotip.message;

import java.util.function.BiConsumer;
import me.semx11.autotip.stats.StatsDaily;

public enum StatsType {
    NONE((stats, matcher) -> {
        // What did you expect?
    }),
    ONE_TIP_SENT((stats, matcher) -> {
        stats.addTipsSent(1);
    }),
    TIPS_SENT((stats, matcher) -> {
        stats.addTipsSent(matcher.getInt("tipsSent"));
    }),
    TIPS_RECEIVED((stats, matcher) -> {
        stats.addTipsReceived(matcher.getInt("tipsReceived"));
    }),
    XP_SENT((stats, matcher) -> {
        stats.addXpSent(matcher.getInt("xpSent"));
    }),
    XP_TIPS_SENT((stats, matcher) -> {
        stats.addXpTipsSent(matcher.getInt("xpSent"));
    }),
    XP_RECEIVED((stats, matcher) -> {
        stats.addXpReceived(matcher.getInt("xpReceived"));
    }),
    XP_TIPS_RECEIVED((stats, matcher) -> {
        stats.addXpTipsReceived(matcher.getInt("xpReceived"));
    }),
    COINS_SENT((stats, matcher) -> {
        stats.addCoinsSent(matcher.getString("game"), matcher.getInt("coinsSent"));
    }),
    COINS_RECEIVED((stats, matcher) -> {
        stats.addCoinsReceived(matcher.getString("game"), matcher.getInt("coinsReceived"));
    });

    private final BiConsumer<StatsDaily, MessageMatcher> consumer;

    StatsType(BiConsumer<StatsDaily, MessageMatcher> consumer) {
        this.consumer = consumer;
    }

    public BiConsumer<StatsDaily, MessageMatcher> getConsumer() {
        return consumer;
    }

}
