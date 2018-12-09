package me.semx11.autotip.stats;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Coins implements Comparable<Coins> {

    private static final DecimalFormat FORMAT = (DecimalFormat) NumberFormat.getInstance(Locale.US);

    private int coinsSent;
    private int coinsReceived;

    public Coins(int coinsSent, int coinsReceived) {
        this.coinsSent = coinsSent;
        this.coinsReceived = coinsReceived;
    }

    public Coins(Coins that) {
        this.coinsSent = that.coinsSent;
        this.coinsReceived = that.coinsReceived;
    }

    public String getTotal() {
        return FORMAT.format(this.getTotalInt());
    }

    public int getTotalInt() {
        return coinsSent + coinsReceived;
    }

    public String getSent() {
        return FORMAT.format(coinsSent);
    }

    public int getSentInt() {
        return coinsSent;
    }

    public void addSent(int coins) {
        coinsSent += coins;
    }

    public String getReceived() {
        return FORMAT.format(coinsReceived);
    }

    public int getReceivedInt() {
        return coinsReceived;
    }

    public void addReceived(int coins) {
        coinsReceived += coins;
    }

    public Coins merge(final Coins that) {
        this.coinsSent += that.coinsSent;
        this.coinsReceived += that.coinsReceived;
        return this;
    }

    @Override
    public int compareTo(Coins that) {
        return Integer.compare(this.getTotalInt(), that.getTotalInt());
    }

}
