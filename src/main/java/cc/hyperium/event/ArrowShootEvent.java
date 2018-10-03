package cc.hyperium.event;

import com.google.common.base.Preconditions;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class ArrowShootEvent extends Event {

    @NotNull
    private final EntityArrow arrow;

    @NotNull
    private final ItemStack bow;

    private final int charge;

    public ArrowShootEvent(@NotNull EntityArrow arrow, int charge, @NotNull ItemStack bow) {
        Preconditions.checkNotNull(arrow, "arrow");
        Preconditions.checkNotNull(bow, "bow");

        this.arrow = arrow;
        this.charge = charge;
        this.bow = bow;
    }

    @NotNull
    public final EntityArrow getArrow() {
        return this.arrow;
    }

    @NotNull
    public final ItemStack getBow() {
        return this.bow;
    }

    public final int getCharge() {
        return this.charge;
    }
}
