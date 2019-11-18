package cc.hyperium.event.world.chunk;

import cc.hyperium.event.Event;
import net.minecraft.world.chunk.Chunk;

public class ChunkLoadEvent extends Event {

    private Chunk chunk;

    public ChunkLoadEvent(Chunk chunk) {
        this.chunk = chunk;
    }

    public Chunk getChunk() {
        return chunk;
    }
}
