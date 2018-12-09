package me.semx11.autotip.stats;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.chat.MessageUtil;
import me.semx11.autotip.config.GlobalSettings;
import me.semx11.autotip.config.GlobalSettings.GameAlias;
import me.semx11.autotip.config.GlobalSettings.GameGroup;
import me.semx11.autotip.gson.exclusion.Exclude;

public abstract class Stats {

    static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DecimalFormat FORMAT = (DecimalFormat) NumberFormat.getInstance(Locale.US);

    @Exclude
    protected final Autotip autotip;
    @Exclude
    protected final GlobalSettings settings;

    protected int tipsSent = 0;
    protected int tipsReceived = 0;
    protected int xpSent = 0;
    protected int xpReceived = 0;

    Map<String, Coins> gameStatistics = new ConcurrentHashMap<>();

    public Stats(Autotip autotip) {
        this.autotip = autotip;
        this.settings = autotip.getGlobalSettings();
    }

    public String getTipsTotal() {
        return FORMAT.format(this.getTipsTotalInt());
    }

    public int getTipsTotalInt() {
        return tipsSent + tipsReceived;
    }

    public String getTipsSent() {
        return FORMAT.format(this.getTipsSentInt());
    }

    public int getTipsSentInt() {
        return tipsSent;
    }

    public void addTipsSent(int tips) {
        this.tipsSent += tips;
    }

    public void addXpTipsSent(int xp) {
        this.addXpSent(xp);
        this.addTipsSent(xp / settings.getXpPerTipSent());
    }

    public String getTipsReceived() {
        return FORMAT.format(this.getTipsReceivedInt());
    }

    public int getTipsReceivedInt() {
        return tipsReceived;
    }

    public void addTipsReceived(int tips) {
        this.tipsReceived += tips;
    }

    public void addXpTipsReceived(int xp) {
        this.addXpReceived(xp);
        this.addTipsReceived(xp / settings.getXpPerTipReceived());
    }

    public String getXpTotal() {
        return FORMAT.format(this.getXpTotalInt());
    }

    public int getXpTotalInt() {
        return xpSent + xpReceived;
    }

    public String getXpSent() {
        return FORMAT.format(this.getXpSentInt());
    }

    public int getXpSentInt() {
        return xpSent;
    }

    public void addXpSent(int xp) {
        this.xpSent += xp;
    }

    public String getXpReceived() {
        return FORMAT.format(this.getXpReceivedInt());
    }

    public int getXpReceivedInt() {
        return xpReceived;
    }

    public void addXpReceived(int xp) {
        this.xpReceived += xp;
    }

    public Map<String, Coins> getGameStatistics() {
        return gameStatistics;
    }

    public void addCoinsSent(String game, int coins) {
        this.addCoins(game, coins, 0);
    }

    public void addCoinsReceived(String game, int coins) {
        this.addCoins(game, 0, coins);
    }

    public void addCoins(String game, int coinsSent, int coinsReceived) {
        this.addCoins(game, new Coins(coinsSent, coinsReceived));
    }

    protected void addCoins(String game, Coins coins) {
        coins = new Coins(coins);
        for (GameGroup group : settings.getGameGroups()) {
            if (game.equals(group.getName())) {
                for (String groupGame : group.getGames()) {
                    this.addCoins(groupGame, coins);
                }
                return;
            }
        }
        for (GameAlias alias : settings.getGameAliases()) {
            for (String aliasAlias : alias.getAliases()) {
                if (game.equals(aliasAlias)) {
                    for (String aliasGame : alias.getGames()) {
                        this.addCoins(aliasGame, coins);
                    }
                    return;
                }
            }
        }
        this.gameStatistics.merge(game, coins, Coins::merge);
    }

    public Stats merge(final Stats that) {
        this.tipsSent += that.tipsSent;
        this.tipsReceived += that.tipsReceived;
        this.xpSent += that.xpSent;
        this.xpReceived += that.xpReceived;
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
                    messageUtil.getKeyHelper("command.stats").withKey("coins", context -> {
                        context.getBuilder(game, coins.getTotal())
                                .setHover(context.getKey("coinsHover"), game, coins.getSent(),
                                        coins.getReceived())
                                .send();
                    });
                });
        messageUtil.getKeyHelper("command.stats").withKey("tips", context -> {
            context.getBuilder(this.getTipsTotal())
                    .setHover(context.getKey("tipsHover"), this.getTipsSent(),
                            this.getTipsReceived())
                    .send();
        }).withKey("xp", context -> {
            context.getBuilder(this.getXpTotal())
                    .setHover(context.getKey("xpHover"), this.getXpSent(), this.getXpReceived())
                    .send();
        });
        if (this instanceof StatsDaily) {
            messageUtil.sendKey("command.stats.date", ((StatsDaily) this).getDateString());
        } else if (this instanceof StatsRange) {
            StatsRange range = (StatsRange) this;
            messageUtil.sendKey("command.stats.dateRange", range.getStartString(),
                    range.getEndString());
        }
        messageUtil.separator();
    }

}
