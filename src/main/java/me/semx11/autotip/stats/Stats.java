package me.semx11.autotip.stats;

import me.semx11.autotip.Autotip;
import me.semx11.autotip.chat.MessageUtil;
import me.semx11.autotip.config.GlobalSettings;
import me.semx11.autotip.config.GlobalSettings.GameAlias;
import me.semx11.autotip.config.GlobalSettings.GameGroup;
import me.semx11.autotip.gson.exclusion.Exclude;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Stats {

    static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DecimalFormat FORMAT = (DecimalFormat) NumberFormat.getInstance(Locale.US);

    @Exclude
    protected final Autotip autotip;
    @Exclude
    protected final GlobalSettings settings;

    protected int tipsSent;
    protected int tipsReceived;
    protected int xpSent;
    protected int xpReceived;

    Map<String, Coins> gameStatistics = new ConcurrentHashMap<>();

    public Stats(Autotip autotip) {
        this.autotip = autotip;
        settings = autotip.getGlobalSettings();
    }

    public String getTipsTotal() {
        return FORMAT.format(getTipsTotalInt());
    }

    public int getTipsTotalInt() {
        return tipsSent + tipsReceived;
    }

    public String getTipsSent() {
        return FORMAT.format(tipsSent);
    }

    public int getTipsSentInt() {
        return tipsSent;
    }

    public void addTipsSent(int tips) {
        tipsSent += tips;
    }

    public void addXpTipsSent(int xp) {
        addXpSent(xp);
        addTipsSent(xp / settings.getXpPerTipSent());
    }

    public String getTipsReceived() {
        return FORMAT.format(tipsReceived);
    }

    public int getTipsReceivedInt() {
        return tipsReceived;
    }

    public void addTipsReceived(int tips) {
        tipsReceived += tips;
    }

    public void addXpTipsReceived(int xp) {
        addXpReceived(xp);
        addTipsReceived(xp / settings.getXpPerTipReceived());
    }

    public String getXpTotal() {
        return FORMAT.format(getXpTotalInt());
    }

    public int getXpTotalInt() {
        return xpSent + xpReceived;
    }

    public String getXpSent() {
        return FORMAT.format(xpSent);
    }

    public int getXpSentInt() {
        return xpSent;
    }

    public void addXpSent(int xp) {
        xpSent += xp;
    }

    public String getXpReceived() {
        return FORMAT.format(xpReceived);
    }

    public int getXpReceivedInt() {
        return xpReceived;
    }

    public void addXpReceived(int xp) {
        xpReceived += xp;
    }

    public Map<String, Coins> getGameStatistics() {
        return gameStatistics;
    }

    public void addCoinsSent(String game, int coins) {
        addCoins(game, coins, 0);
    }

    public void addCoinsReceived(String game, int coins) {
        addCoins(game, 0, coins);
    }

    public void addCoins(String game, int coinsSent, int coinsReceived) {
        addCoins(game, new Coins(coinsSent, coinsReceived));
    }

    protected void addCoins(String game, Coins coins) {
        coins = new Coins(coins);
        for (GameGroup group : settings.getGameGroups()) {
            if (game.equals(group.getName())) {
                for (String groupGame : group.getGames()) {
                    addCoins(groupGame, coins);
                }

                return;
            }
        }

        for (GameAlias alias : settings.getGameAliases()) {
            for (String aliasAlias : alias.getAliases()) {
                if (game.equals(aliasAlias)) {
                    for (String aliasGame : alias.getGames()) {
                        addCoins(aliasGame, coins);
                    }

                    return;
                }
            }
        }

        gameStatistics.merge(game, coins, Coins::merge);
    }

    public Stats merge(final Stats that) {
        tipsSent += that.tipsSent;
        tipsReceived += that.tipsReceived;
        xpSent += that.xpSent;
        xpReceived += that.xpReceived;
        that.gameStatistics.forEach(this::addCoins);
        return this;
    }

    public void print() {
        MessageUtil messageUtil = autotip.getMessageUtil();
        messageUtil.separator();
        gameStatistics.entrySet().stream()
            .sorted(Map.Entry.<String, Coins>comparingByValue().reversed())
            .forEach(entry -> {
                String game = entry.getKey();
                Coins coins = entry.getValue();
                messageUtil.getKeyHelper("command.stats").withKey("coins", context -> context.getBuilder(game, coins.getTotal())
                    .setHover(context.getKey("coinsHover"), game, coins.getSent(),
                        coins.getReceived())
                    .send());
            });
        messageUtil.getKeyHelper("command.stats").withKey("tips", context -> context.getBuilder(getTipsTotal())
            .setHover(context.getKey("tipsHover"), getTipsSent(),
                getTipsReceived())
            .send()).withKey("xp", context -> context.getBuilder(getXpTotal())
            .setHover(context.getKey("xpHover"), getXpSent(), getXpReceived())
            .send());
        if (this instanceof StatsDaily) {
            messageUtil.sendKey("command.stats.date", ((StatsDaily) this).getDateString());
        } else if (this instanceof StatsRange) {
            StatsRange range = (StatsRange) this;
            messageUtil.sendKey("command.stats.dateRange", range.getStartString(), range.getEndString());
        }

        messageUtil.separator();
    }
}
