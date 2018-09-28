package cc.hyperium.event;

import com.google.common.base.Preconditions;

import org.jetbrains.annotations.NotNull;

/**
 * Invoked when the player receives a party invite
 */
public final class HypixelPartyInviteEvent extends Event {

    @NotNull
    private final String from;

    public HypixelPartyInviteEvent(@NotNull String from) {
        Preconditions.checkNotNull(from, "from");

        this.from = from;
    }

    @NotNull
    public final String getFrom() {
        return this.from;
    }
}