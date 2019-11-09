package cc.hyperium.event.entity;

import cc.hyperium.event.Event;
import net.minecraft.entity.player.EntityPlayer;

public class FovUpdateEvent extends Event {

    private final EntityPlayer player;
    private final float currentFov;
    private float newFov;

    public FovUpdateEvent(EntityPlayer player, float fov) {
        this.player = player;
        this.currentFov = fov;
        this.newFov = fov;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public float getCurrentFov() {
        return currentFov;
    }

    public float getNewFov() {
        return newFov;
    }

    public void setNewFov(float newFov) {
        this.newFov = newFov;
    }
}
