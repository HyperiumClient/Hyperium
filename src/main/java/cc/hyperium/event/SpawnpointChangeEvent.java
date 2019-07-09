package cc.hyperium.event;

import net.minecraft.util.BlockPos;

/**
 * Invoked when the spawnpoint has changed
 * This is useful for detecting minigames
 */
public class SpawnpointChangeEvent extends Event {

    private final BlockPos blockPos;

    public SpawnpointChangeEvent(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }
}
