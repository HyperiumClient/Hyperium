package cc.hyperium.event;

import java.util.List;

import com.google.common.base.Preconditions;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

public final class ItemTooltipEvent extends Event {

    @NotNull
    private final ItemStack item;
    @NotNull
    private final List toolTip;

    public ItemTooltipEvent(@NotNull ItemStack item, @NotNull List toolTip) {
        Preconditions.checkNotNull(item, "item");
        Preconditions.checkNotNull(toolTip, "toolTip");

        this.item = item;
        this.toolTip = toolTip;
    }

    @NotNull
    public final ItemStack getItem() {
        return this.item;
    }

    @NotNull
    public final List getToolTip() {
        return this.toolTip;
    }
}
