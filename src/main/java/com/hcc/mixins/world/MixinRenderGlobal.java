package com.hcc.mixins.world;

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