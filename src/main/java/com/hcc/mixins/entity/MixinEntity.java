package com.hcc.mixins.entity;


import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
abstract class MixinEntity {

    @Shadow
    public Vec3 getLook(float particalTicks) {
        return null;
    }
}