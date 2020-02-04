package cc.hyperium.launch.patching.conflicts

import cc.hyperium.config.Settings
import cc.hyperium.utils.ItemUtil
import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import net.minecraft.item.Item
import net.minecraft.item.ItemArmor
import net.minecraft.item.ItemStack
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodInsnNode

class LayerArmorBaseTransformer : ConflictTransformer {
    override fun getClassName() = "bkn"

    override fun transform(original: ClassNode): ClassNode {
        for (method in original.methods) {
            if (method.name == "renderLayer") {
                val disableArmor = assembleBlock {
                    aload(10)
                    ifnull(L["2"])
                    aload(10)
                    invokevirtual(ItemStack::class, "getItem", Item::class)
                    instanceof(ItemArmor::class)
                    ifeq(L["2"])
                    getstatic(Settings::class, "HIDE_LEATHER_ARMOR", boolean)
                    ifeq(L["2"])
                    getstatic(ItemUtil::class, "INSTANCE", ItemUtil::class)
                    invokevirtual(ItemUtil::class, "getLeatherArmorCollection", ArrayList::class)
                    aload(10)
                    invokevirtual(ItemStack::class, "getItem", Item::class)
                    invokevirtual(ArrayList::class, "contains", boolean, Object::class)
                    ifeq(L["2"])
                    _return
                    +L["2"]
                }.first

                for (insn in method.instructions.iterator()) {
                    if (insn is MethodInsnNode && insn.name == "getCurrentArmor") {
                        method.instructions.insertBefore(insn.next?.next, disableArmor)
                    }
                }
            }

            if (method.name == "renderGlint") {
                val disableEnchantmentGlint = assembleBlock {
                    getstatic(Settings::class, "DISABLE_ENCHANT_GLINT", boolean)
                    ifeq(L["1"])
                    _return
                    +L["1"]
                }.first

                method.instructions.insertBefore(method.instructions.first, disableEnchantmentGlint)
            }
        }

        return original
    }
}