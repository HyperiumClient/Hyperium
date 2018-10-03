package cc.hyperium.event;

import com.google.common.base.Preconditions;

import org.jetbrains.annotations.NotNull;

public final class ServerSwitchEvent extends Event {

    @NotNull
    private final String from;

    @NotNull
    private final String to;

    public ServerSwitchEvent(@NotNull String from, @NotNull String to) {
        Preconditions.checkNotNull(from, "from");
        Preconditions.checkNotNull(to, "to");

        this.from = from;
        this.to = to;
    }

    @NotNull
    public final String getFrom() {
        return this.from;
    }

    @NotNull
    public final String getTo() {
        return this.to;
    }
}
