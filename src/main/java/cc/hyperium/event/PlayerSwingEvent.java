package cc.hyperium.event;

import java.util.UUID;

import com.google.common.base.Preconditions;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.NotNull;

/**
 * Invoked once player swings
 */
public final class PlayerSwingEvent extends Event {

    @NotNull
    private final UUID player;

    @NotNull
    private final Vec3 posVec;

    @NotNull
    private final Vec3 lookVec;

    @NotNull
    private final BlockPos pos;

    public PlayerSwingEvent(@NotNull UUID player, @NotNull Vec3 posVec, @NotNull Vec3 lookVec, @NotNull BlockPos pos) {
        Preconditions.checkNotNull(player, "player");
        Preconditions.checkNotNull(posVec, "posVec");
        Preconditions.checkNotNull(lookVec, "lookVec");
        Preconditions.checkNotNull(pos, "pos");

        this.player = player;
        this.posVec = posVec;
        this.lookVec = lookVec;
        this.pos = pos;
    }

    @NotNull
    public final UUID getPlayer() {
        return this.player;
    }

    @NotNull
    public final Vec3 getPosVec() {
        return this.posVec;
    }

    @NotNull
    public final Vec3 getLookVec() {
        return this.lookVec;
    }

    @NotNull
    public final BlockPos getPos() {
        return this.pos;
    }
}
