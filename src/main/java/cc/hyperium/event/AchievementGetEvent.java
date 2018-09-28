package cc.hyperium.event;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

public final class AchievementGetEvent extends Event {

    @NotNull
    private final String achievement;

    public AchievementGetEvent(@NotNull String achievement) {
        Preconditions.checkNotNull(achievement, "achievement");

        this.achievement = achievement;
    }

    @NotNull
    public final String getAchievement() {
        return this.achievement;
    }
}
