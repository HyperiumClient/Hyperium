package cc.hyperium.launch.patching.conflicts

import cc.hyperium.config.Settings
import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import codes.som.anthony.koffee.koffee
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.ItemRenderer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import org.objectweb.asm.tree.ClassNode

class ItemRendererTransformer : ConflictTransformer {
    override fun getClassName() = "bfn"

    // todo: renderItemInFirstPerson
    override fun transform(original: ClassNode): ClassNode {
        for (method in original.methods) {
            if (method.name == "transformFirstPersonItem") {
                val transformItemPosition = assembleBlock {
                    getstatic(Settings::class, "OLD_BOW", boolean)
                    ifeq(L["1"])
                    aload_0
                    getfield(ItemRenderer::class, "mc", Minecraft::class)
                    ifnull(L["1"])
                    aload_0
                    getfield(ItemRenderer::class, "mc", Minecraft::class)
                    getfield(Minecraft::class, "thePlayer", EntityPlayerSP::class)
                    ifnull(L["1"])
                    aload_0
                    getfield(ItemRenderer::class, "mc", Minecraft::class)
                    getfield(Minecraft::class, "thePlayer", EntityPlayerSP::class)
                    invokevirtual(EntityPlayerSP::class, "getItemInUse", ItemStack::class)
                    ifnull(L["1"])
                    aload_0
                    getfield(ItemRenderer::class, "mc", Minecraft::class)
                    getfield(Minecraft::class, "thePlayer", EntityPlayerSP::class)
                    invokevirtual(EntityPlayerSP::class, "getItemInUse", ItemStack::class)
                    invokevirtual(ItemStack::class, "getItem", Item::class)
                    ifnull(L["1"])
                    aload_0
                    getfield(ItemRenderer::class, "mc", Minecraft::class)
                    getfield(Minecraft::class, "thePlayer", EntityPlayerSP::class)
                    invokevirtual(EntityPlayerSP::class, "getItemInUse", ItemStack::class)
                    invokevirtual(ItemStack::class, "getItem", Item::class)
                    invokestatic(Item::class, "getIdFromItem", int, Item::class)
                    sipush(261)
                    if_icmpne(L["1"])
                    fconst_0
                    ldc(0.05f)
                    ldc(0.04f)
                    invokestatic(GlStateManager::class, "translate", void, float, float, float)
                    +L["1"]

                    getstatic(Settings::class, "OLD_ROD", boolean)
                    ifeq(L["5"])
                    aload_0
                    getfield(ItemRenderer::class, "mc", Minecraft::class)
                    ifnull(L["5"])
                    aload_0
                    getfield(ItemRenderer::class, "mc", Minecraft::class)
                    getfield(Minecraft::class, "thePlayer", EntityPlayerSP::class)
                    ifnull(L["5"])
                    aload_0
                    getfield(ItemRenderer::class, "mc", Minecraft::class)
                    getfield(Minecraft::class, "thePlayer", EntityPlayerSP::class)
                    invokevirtual(EntityPlayerSP::class, "getCurrentEquippedItem", ItemStack::class)
                    ifnull(L["5"])
                    aload_0
                    getfield(ItemRenderer::class, "mc", Minecraft::class)
                    getfield(Minecraft::class, "thePlayer", EntityPlayerSP::class)
                    invokevirtual(EntityPlayerSP::class, "getCurrentEquippedItem", ItemStack::class)
                    invokevirtual(ItemStack::class, "getItem", Item::class)
                    ifnull(L["5"])
                    aload_0
                    getfield(ItemRenderer::class, "mc", Minecraft::class)
                    getfield(Minecraft::class, "thePlayer", EntityPlayerSP::class)
                    invokevirtual(EntityPlayerSP::class, "getCurrentEquippedItem", ItemStack::class)
                    invokevirtual(ItemStack::class, "getItem", Item::class)
                    invokestatic(Item::class, "getIdFromItem", int, Item::class)
                    sipush(346)
                    if_icmpne(L["5"])
                    ldc(0.08f)
                    ldc(-0.027f)
                    ldc(-0.33f)
                    invokestatic(GlStateManager::class, "translate", void, float, float, float)
                    ldc(0.93)
                    fconst_1
                    fconst_1
                    invokestatic(GlStateManager::class, "scale", void, float, float, float)
                    +L["5"]

                    getstatic(Settings::class, "OLD_BLOCKHIT", boolean)
                    ifeq(L["9"])
                    aload_0
                    getfield(ItemRenderer::class, "mc", Minecraft::class)
                    ifnull(L["9"])
                    aload_0
                    getfield(ItemRenderer::class, "mc", Minecraft::class)
                    getfield(Minecraft::class, "thePlayer", EntityPlayerSP::class)
                    ifnull(L["9"])
                    aload_0
                    getfield(ItemRenderer::class, "mc", Minecraft::class)
                    getfield(Minecraft::class, "thePlayer", EntityPlayerSP::class)
                    getfield(EntityPlayerSP::class, "isSwingInProgress", boolean)
                    ifeq(L["9"])
                    aload_0
                    getfield(ItemRenderer::class, "mc", Minecraft::class)
                    getfield(Minecraft::class, "thePlayer", EntityPlayerSP::class)
                    invokevirtual(EntityPlayerSP::class, "getCurrentEquippedItem", ItemStack::class)
                    ifnull(L["9"])
                    aload_0
                    getfield(ItemRenderer::class, "mc", Minecraft::class)
                    getfield(Minecraft::class, "thePlayer", EntityPlayerSP::class)
                    invokevirtual(EntityPlayerSP::class, "isEating", boolean)
                    ifne(L["9"])
                    aload_0
                    getfield(ItemRenderer::class, "mc", Minecraft::class)
                    getfield(Minecraft::class, "thePlayer", EntityPlayerSP::class)
                    invokevirtual(EntityPlayerSP::class, "isBlocking", boolean)
                    ifne(L["9"])
                    ldc(0.85f)
                    ldc(0.85f)
                    ldc(0.85f)
                    invokestatic(GlStateManager::class, "scale", void, float, float, float)
                    ldc(-0.078f)
                    ldc(0.003f)
                    ldc(0.05f)
                    +L["9"]
                }.first

                method.instructions.insertBefore(method.instructions.first, transformItemPosition)
            }

            if (method.name == "renderFireInFirstPerson") {
                val changeFireHeightPush = assembleBlock {
                    invokestatic(GlStateManager::class, "pushMatrix", void)
                    dconst_0
                    getstatic(Settings::class, "FIRE_HEIGHT", double)
                    dconst_0
                    invokestatic(GlStateManager::class, "translate", void, double, double, double)
                }.first

                val changeFireHeightPop = assembleBlock {
                    invokestatic(GlStateManager::class, "popMatrix", void)
                }.first

                method.instructions.insertBefore(method.instructions.first, changeFireHeightPush)
                method.instructions.insertBefore(method.instructions.last?.previous, changeFireHeightPop)
            }
        }

        return original
    }
}