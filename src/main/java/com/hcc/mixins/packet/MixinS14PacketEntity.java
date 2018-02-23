package com.hcc.mixins.packet;

import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(S14PacketEntity.class)
public class MixinS14PacketEntity {
    
    @Shadow protected int entityId;
    
    /**
     * Fixes npe in internal code because of null worlds
     *
     * @author boomboompower
     */
    @Overwrite
    public Entity getEntity(World worldIn) {
        return worldIn != null ? worldIn.getEntityByID(this.entityId) : null;
    }
}
