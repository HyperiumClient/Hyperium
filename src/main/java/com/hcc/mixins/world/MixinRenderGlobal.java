package com.hcc.mixins.world;

import com.hcc.utils.EntityLimiter;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.util.ClassInheritanceMultiMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(RenderGlobal.class)
public class MixinRenderGlobal {

    @ModifyVariable(method = "renderEntities", at = @At(value = "STORE"))
    private ClassInheritanceMultiMap<Entity> renderEntities(ClassInheritanceMultiMap<Entity> classinheritancemultimap) {
        if (!classinheritancemultimap.isEmpty()) {
            List<Entity> entities = new EntityLimiter(EntityLimiter.EntityLimiterMethods.RANGE, 15)
                    .getRenderingEntities(new ArrayList<>(classinheritancemultimap));
            ClassInheritanceMultiMap<Entity> map = new ClassInheritanceMultiMap<>(Entity.class);
            map.addAll(entities);
            return map;
        }
        return classinheritancemultimap;
    }
}