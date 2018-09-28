package cc.hyperium.event;

import com.google.common.base.Preconditions;
import net.minecraft.client.gui.ScaledResolution;
import org.jetbrains.annotations.NotNull;

/**
 * Invoked when the selected item is about to be rendered
 */
public final class RenderSelectedItemEvent extends Event {

    @NotNull
    private final ScaledResolution scaledRes;

    public RenderSelectedItemEvent(@NotNull ScaledResolution scaledRes) {
        Preconditions.checkNotNull(scaledRes, "scaledRes");

        this.scaledRes = scaledRes;
    }

    @NotNull
    public final ScaledResolution getScaledRes() {
        return this.scaledRes;
    }
}
