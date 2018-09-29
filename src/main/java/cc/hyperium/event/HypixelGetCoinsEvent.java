package cc.hyperium.event;

public final class HypixelGetCoinsEvent extends Event {

    private final int coins;

    public HypixelGetCoinsEvent(int coins) {
        this.coins = coins;
    }

    public final int getCoins() {
        return this.coins;
    }
}
