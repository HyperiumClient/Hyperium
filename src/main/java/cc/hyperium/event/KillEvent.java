package cc.hyperium.event;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

public final class KillEvent extends Event {

    @NotNull
    private final String user;

    public KillEvent(@NotNull String user) {
        Preconditions.checkNotNull(user, "user");

        this.user = user;
    }

    @NotNull
    public final String getUser() {
        return this.user;
    }
}
