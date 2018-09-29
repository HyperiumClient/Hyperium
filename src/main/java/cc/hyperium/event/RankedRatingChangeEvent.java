package cc.hyperium.event;

public final class RankedRatingChangeEvent extends Event {

    private final int rating;
    private final int delta;

    public RankedRatingChangeEvent(int rating, int delta) {
        this.rating = rating;
        this.delta = delta;
    }

    public final int getRating() {
        return this.rating;
    }

    public final int getDelta() {
        return this.delta;
    }
}
