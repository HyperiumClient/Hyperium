package cc.hyperium.event;

import com.google.common.base.Preconditions;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import org.jetbrains.annotations.NotNull;

public final class DrawBlockHighlightEvent extends CancellableEvent {

    @NotNull
    private final EntityPlayer player;

    @NotNull
    private final MovingObjectPosition target;

    private final float partialTicks;

    public DrawBlockHighlightEvent(@NotNull EntityPlayer player, @NotNull MovingObjectPosition target, float partialTicks) {
        Preconditions.checkNotNull(player, "player");
        Preconditions.checkNotNull(target, "target");

        this.player = player;
        this.target = target;
        this.partialTicks = partialTicks;
    }

    @NotNull
    public final EntityPlayer getPlayer() {
        return this.player;
    }

    @NotNull
    public final MovingObjectPosition getTarget() {
        return this.target;
    }

    public final float getPartialTicks() {
        return this.partialTicks;
    }
}
