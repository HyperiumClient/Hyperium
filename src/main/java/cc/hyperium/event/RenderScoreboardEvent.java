package cc.hyperium.event;

import com.google.common.base.Preconditions;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.ScoreObjective;
import org.jetbrains.annotations.NotNull;

/**
 * Invoked when the scoreboard is rendered
 */
public final class RenderScoreboardEvent extends CancellableEvent {

    private final double x;
    private final double y;

    @NotNull
    private final ScoreObjective objective;

    @NotNull
    private final ScaledResolution resolution;

    public RenderScoreboardEvent(double x, double y, @NotNull ScoreObjective objective, @NotNull ScaledResolution resolution) {
        Preconditions.checkNotNull(objective, "objective");
        Preconditions.checkNotNull(resolution, "resolution");

        this.x = x;
        this.y = y;

        this.objective = objective;
        this.resolution = resolution;
    }

    @NotNull
    public final ScoreObjective getObjective() {
        return this.objective;
    }

    @NotNull
    public final ScaledResolution getResolution() {
        return this.resolution;
    }

    public final double getX() {
        return this.x;
    }

    public final double getY() {
        return this.y;
    }
}
