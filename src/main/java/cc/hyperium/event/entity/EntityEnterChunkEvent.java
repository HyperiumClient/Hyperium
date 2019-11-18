package cc.hyperium.event.entity;

import cc.hyperium.event.Event;
import net.minecraft.entity.Entity;

public class EntityEnterChunkEvent extends Event {

    private final Entity entity;
    private int newChunkX, newChunkZ, oldChunkX, oldChunkZ;

    public EntityEnterChunkEvent(Entity entity, int newChunkX, int newChunkZ, int oldChunkX, int oldChunkZ) {
        this.entity = entity;
        this.newChunkX = newChunkX;
        this.newChunkZ = newChunkZ;
        this.oldChunkX = oldChunkX;
        this.oldChunkZ = oldChunkZ;
    }

    public Entity getEntity() {
        return entity;
    }

    public int getNewChunkX() {
        return newChunkX;
    }

    public void setNewChunkX(int newChunkX) {
        this.newChunkX = newChunkX;
    }

    public int getNewChunkZ() {
        return newChunkZ;
    }

    public void setNewChunkZ(int newChunkZ) {
        this.newChunkZ = newChunkZ;
    }

    public int getOldChunkX() {
        return oldChunkX;
    }

    public void setOldChunkX(int oldChunkX) {
        this.oldChunkX = oldChunkX;
    }

    public int getOldChunkZ() {
        return oldChunkZ;
    }

    public void setOldChunkZ(int oldChunkZ) {
        this.oldChunkZ = oldChunkZ;
    }
}
