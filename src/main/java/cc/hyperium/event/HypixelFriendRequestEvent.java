package cc.hyperium.event;

import com.google.common.base.Preconditions;

import org.jetbrains.annotations.NotNull;

/**
 * Invoked when received a friend request is received
 */
public final class HypixelFriendRequestEvent extends Event {

    @NotNull
    private final String from;

    public HypixelFriendRequestEvent(@NotNull String from) {
        Preconditions.checkNotNull(from, "from");

        this.from = from;
    }

    @NotNull
    public final String getFrom() {
        return this.from;
    }
}
