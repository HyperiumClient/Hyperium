package cc.hyperium.event;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

public final class FriendRemoveEvent extends Event {

    @NotNull
    private final String fullName;
    @NotNull
    private final String name;

    public FriendRemoveEvent(@NotNull String fullName, @NotNull String name) {
        Preconditions.checkNotNull(fullName, "fullName");
        Preconditions.checkNotNull(name, "name");

        this.fullName = fullName;
        this.name = name;
    }

    @NotNull
    public final String getFullName() {
        return this.fullName;
    }

    @NotNull
    public final String getName() {
        return this.name;
    }
}