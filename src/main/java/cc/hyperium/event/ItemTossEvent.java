package cc.hyperium.event;

import com.google.common.base.Preconditions;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;

import org.jetbrains.annotations.NotNull;

public final class ItemTossEvent extends Event {

    @NotNull
    private final EntityPlayer player;

    @NotNull
    private final EntityItem item;

    public ItemTossEvent(@NotNull EntityPlayer player, @NotNull EntityItem item) {
        Preconditions.checkNotNull(player, "player");
        Preconditions.checkNotNull(item, "item");

        this.player = player;
        this.item = item;
    }

    @NotNull
    public final EntityPlayer getPlayer() {
        return this.player;
    }

    @NotNull
    public final EntityItem getItem() {
        return this.item;
    }
}
