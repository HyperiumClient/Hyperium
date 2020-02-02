package cc.hyperium.launch.patching.conflicts

import cc.hyperium.config.Settings
import cc.hyperium.event.Event
import cc.hyperium.event.EventBus
import cc.hyperium.event.render.EntityRenderEvent
import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityArmorStand
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

class RendererLivingEntityTransformer : ConflictTransformer {
    override fun getClassName() = "bjl"

    // todo: f2 = f1 - f in doRender;
    override fun transform(original: ClassNode): ClassNode {
        for (method in original.methods) {
            if (method.name == "addLayer") {
                method.access = Opcodes.ACC_PUBLIC
            }

            if (method.name == "doRender") {
                val disableArmorstands = assembleBlock {
                    getstatic(Settings::class, "DISABLE_ARMORSTANDS", boolean)
                    ifeq(L["4"])
                    aload_1
                    instanceof(EntityArmorStand::class)
                    ifeq(L["4"])
                    _return
                    +L["4"]
                    new(EntityRenderEvent::class)
                    dup
                    aload_1
                    dload_2
                    d2f
                    dload(4)
                    d2f
                    dload(6)
                    d2f
                    aload_1
                    getfield(EntityLivingBase::class, "rotationPitch", float)
                    fload(8)
                    fconst_1
                    invokespecial(
                        EntityRenderEvent::class,
                        "<init>",
                        void,
                        Entity::class,
                        float,
                        float,
                        float,
                        float,
                        float,
                        float
                    )
                    astore(10)
                    getstatic(EventBus::class, "INSTANCE", EventBus::class)
                    aload(10)
                    invokevirtual(EventBus::class, "post", void, Event::class)
                    aload(10)
                    invokevirtual(EntityRenderEvent::class, "isCancelled", boolean)
                    ifeq(L["7"])
                    _return
                    +L["7"]
                }.first

                method.instructions.insertBefore(method.instructions.first, disableArmorstands)
            }
        }

        return original
    }
}