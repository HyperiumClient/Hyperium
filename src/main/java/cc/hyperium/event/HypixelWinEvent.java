package cc.hyperium.event;

import java.util.List;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

/**
 * Invoked when player(s) win a game
 */
public final class HypixelWinEvent extends Event {

    @NotNull
    private final List winners;

    public HypixelWinEvent(@NotNull List winners) {
        Preconditions.checkNotNull(winners, "winners");

        this.winners = winners;
    }

    @NotNull
    public final List getWinners() {
        return this.winners;
    }
}
