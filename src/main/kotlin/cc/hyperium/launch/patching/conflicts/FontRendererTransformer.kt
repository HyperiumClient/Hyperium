package cc.hyperium.launch.patching.conflicts

import cc.hyperium.config.Settings
import cc.hyperium.mods.nickhider.NickHider
import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import org.objectweb.asm.tree.ClassNode

class FontRendererTransformer : ConflictTransformer {
    override fun getClassName() = "net.minecraft.client.gui.FontRenderer"

    override fun transform(original: ClassNode): ClassNode {
        for (method in original.methods) {
            if (method.name == "renderString" && method.desc == "(Ljava/lang/String;FFIZ)I") {
                val list = assembleBlock {
                    getstatic(NickHider::class, "instance", NickHider::class)
                    ifnonnull(L["1"])
                    aload_1
                    goto(L["2"])
                    +L["1"]
                    getstatic(NickHider::class, "instance", NickHider::class)
                    aload_1
                    invokevirtual(NickHider::class, "apply", String::class, String::class)
                    +L["2"]
                    astore_1

                    iload(5)
                    ifeq(L["4"])
                    getstatic(Settings::class, "DISABLE_SHADOW_TEXT", boolean)
                    ifeq(L["4"])
                    iconst_0
                    ireturn
                    +L["4"]
                }.first
                list.add(method.instructions)
                method.instructions = list
            } else if (method.name == "getStringWidth" && method.desc == "(Ljava/lang/String;)I") {
                val list = assembleBlock {
                    getstatic(NickHider::class, "instance", NickHider::class)
                    ifnonnull(L["1"])
                    aload_1
                    goto(L["2"])
                    +L["1"]
                    getstatic(NickHider::class, "instance", NickHider::class)
                    aload_1
                    invokevirtual(NickHider::class, "apply", String::class, String::class)
                    +L["2"]
                    astore_1
                }.first

                list.add(method.instructions)
                method.instructions = list
            }
        }
        return original
    }
}
