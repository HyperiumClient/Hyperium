package cc.hyperium.launch.patching.conflicts

import cc.hyperium.config.Settings
import cc.hyperium.hooks.GuiSlotHook
import codes.som.anthony.koffee.insns.jvm.*
import codes.som.anthony.koffee.koffee
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.GuiSlot
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.renderer.texture.TextureManager
import net.minecraft.util.MathHelper
import net.minecraft.util.ResourceLocation
import org.objectweb.asm.tree.ClassNode
import java.awt.Color
import java.lang.StringBuilder

class GuiSlotTransformer : ConflictTransformer {
    override fun getClassName() = "awi"

    override fun transform(original: ClassNode): ClassNode {
        original.methods.forEach {
            when (it.name) {
                "drawScreen" -> {
                    it.instructions.clear()
                    it.localVariables.clear()
                    it.koffee {
                        aload_0
                        getfield(GuiSlot::class, "field_178041_q", boolean)
                        ifeq(L["1"])
                        aload_0
                        iload_1
                        putfield(GuiSlot::class, "mouseX", int)
                        aload_0
                        iload_2
                        putfield(GuiSlot::class, "mouseY", int)
                        aload_0
                        invokevirtual(GuiSlot::class, "getScrollBarX", int)
                        istore(4)
                        iload(4)
                        bipush(6)
                        iadd
                        istore(5)
                        aload_0
                        invokevirtual(GuiSlot::class, "bindAmountScrolled", void)
                        invokestatic(GlStateManager::class, "disableLighting", void)
                        invokestatic(GlStateManager::class, "disableFog", void)
                        invokestatic(Tessellator::class, "getInstance", Tessellator::class)
                        astore(6)
                        aload(6)
                        invokevirtual(Tessellator::class, "getWorldRenderer", WorldRenderer::class)
                        astore(7)
                        fconst_1
                        fconst_1
                        fconst_1
                        fconst_1
                        invokestatic(GlStateManager::class, "color", void, float, float, float, float)
                        new(ScaledResolution::class)
                        dup
                        aload_0
                        getfield(GuiSlot::class, "mc", Minecraft::class)
                        invokespecial(ScaledResolution::class, "<init>", void, Minecraft::class)
                        astore(8)
                        aload_0
                        getfield(GuiSlot::class, "mc", Minecraft::class)
                        invokevirtual(Minecraft::class, "getTextureManager", TextureManager::class)
                        new(ResourceLocation::class)
                        dup
                        ldc("hyperium")
                        new(StringBuilder::class)
                        dup
                        invokespecial(StringBuilder::class, "<init>", void)
                        ldc("textures/material/backgrounds/")
                        invokevirtual(StringBuilder::class, "append", StringBuilder::class, String::class)
                        getstatic(Settings::class, "BACKGROUND", String::class)
                        invokevirtual(StringBuilder::class, "append", StringBuilder::class, String::class)
                        ldc(".png")
                        invokevirtual(StringBuilder::class, "append", StringBuilder::class, String::class)
                        invokevirtual(StringBuilder::class, "toString", String::class)
                        invokespecial(ResourceLocation::class, "<init>", void, String::class, String::class)
                        invokevirtual(TextureManager::class, "bindTexture", void, ResourceLocation::class)
                        aload_0
                        getfield(GuiSlot::class, "left", int)
                        aload_0
                        getfield(GuiSlot::class, "top", int)
                        aload_0
                        getfield(GuiSlot::class, "left", int)
                        i2f
                        aload_0
                        getfield(GuiSlot::class, "top", int)
                        i2f
                        aload_0
                        getfield(GuiSlot::class, "right", int)
                        aload_0
                        getfield(GuiSlot::class, "left", int)
                        isub
                        aload_0
                        getfield(GuiSlot::class, "bottom", int)
                        aload_0
                        getfield(GuiSlot::class, "top", int)
                        isub
                        aload_0
                        getfield(GuiSlot::class, "right", int)
                        aload_0
                        getfield(GuiSlot::class, "left", int)
                        isub
                        aload_0
                        getfield(GuiSlot::class, "bottom", int)
                        aload_0
                        getfield(GuiSlot::class, "top", int)
                        isub
                        aload(8)
                        invokevirtual(ScaledResolution::class, "getScaledWidth", int)
                        i2f
                        aload(8)
                        invokevirtual(ScaledResolution::class, "getScaledHeight", int)
                        i2f
                        invokestatic(Gui::class, "drawScaledCustomSizeModalRect", void, int, int, float, float, int, int, int, int, float, float)
                        aload_0
                        getfield(GuiSlot::class, "left", int)
                        aload_0
                        getfield(GuiSlot::class, "width", int)
                        iconst_2
                        idiv
                        iadd
                        aload_0
                        invokevirtual(GuiSlot::class, "getListWidth", int)
                        iconst_2
                        idiv
                        isub
                        iconst_2
                        iadd
                        istore(9)
                        aload_0
                        getfield(GuiSlot::class, "top", int)
                        iconst_4
                        iadd
                        aload_0
                        getfield(GuiSlot::class, "amountScrolled", float)
                        f2i
                        isub
                        istore(10)
                        aload_0
                        getfield(GuiSlot::class, "hasListHeader", boolean)
                        ifeq(L["21"])
                        aload_0
                        iload(9)
                        iload(10)
                        aload(6)
                        invokevirtual(GuiSlot::class, "drawListHeader", void, int, int, Tessellator::class)
                        +L["21"]
                        aload_0
                        iload(9)
                        iload(10)
                        iload_1
                        iload_2
                        invokevirtual(GuiSlot::class, "drawSelectionBox", void, int, int, int, int)
                        invokestatic(GlStateManager::class, "disableDepth", void)
                        iconst_4
                        istore(11)
                        aload_0
                        iconst_0
                        aload_0
                        getfield(GuiSlot::class, "top", int)
                        sipush(255)
                        sipush(255)
                        invokevirtual(GuiSlot::class, "overlayBackground", void, int, int, int, int)
                        aload_0
                        aload_0
                        getfield(GuiSlot::class, "bottom", int)
                        aload_0
                        getfield(GuiSlot::class, "height", int)
                        sipush(255)
                        sipush(255)
                        invokevirtual(GuiSlot::class, "overlayBackground", void, int, int, int, int)
                        invokestatic(GlStateManager::class, "enableBlend", void)
                        sipush(770)
                        sipush(771)
                        iconst_0
                        iconst_1
                        invokestatic(GlStateManager::class, "tryBlendFuncSeparate", void, int, int, int, int)
                        invokestatic(GlStateManager::class, "disableAlpha", void)
                        sipush(7425)
                        invokestatic(GlStateManager::class, "shadeModel", void, int)
                        invokestatic(GlStateManager::class, "disableTexture2D", void)
                        aload_0
                        invokevirtual(GuiSlot::class, "func_148135_f", int)
                        istore(12)
                        iload(12)
                        ifle(L["34"])
                        aload_0
                        getfield(GuiSlot::class, "bottom", int)
                        aload_0
                        getfield(GuiSlot::class, "top", int)
                        isub
                        aload_0
                        getfield(GuiSlot::class, "bottom", int)
                        aload_0
                        getfield(GuiSlot::class, "top", int)
                        isub
                        imul
                        aload_0
                        invokevirtual(GuiSlot::class, "getContentHeight", int)
                        idiv
                        istore(13)
                        iload(13)
                        bipush(32)
                        aload_0
                        getfield(GuiSlot::class, "bottom", int)
                        aload_0
                        getfield(GuiSlot::class, "top", int)
                        isub
                        bipush(8)
                        isub
                        invokestatic(MathHelper::class, "clamp_int", int, int, int, int)
                        istore(13)
                        aload_0
                        getfield(GuiSlot::class, "amountScrolled", float)
                        f2i
                        aload_0
                        getfield(GuiSlot::class, "bottom", int)
                        aload_0
                        getfield(GuiSlot::class, "top", int)
                        isub
                        iload(13)
                        isub
                        imul
                        iload(12)
                        idiv
                        aload_0
                        getfield(GuiSlot::class, "top", int)
                        iadd
                        istore(14)
                        iload(14)
                        aload_0
                        getfield(GuiSlot::class, "top", int)
                        if_icmpge(L["39"])
                        aload_0
                        getfield(GuiSlot::class, "top", int)
                        istore(14)
                        +L["39"]
                        iload(4)
                        aload_0
                        getfield(GuiSlot::class, "top", int)
                        iload(5)
                        aload_0
                        getfield(GuiSlot::class, "bottom", int)
                        new(Color::class)
                        dup
                        iconst_0
                        iconst_0
                        iconst_0
                        bipush(100)
                        invokespecial(Color::class, "<init>", void, int, int, int, int)
                        invokevirtual(Color::class, "getRGB", int)
                        invokestatic(Gui::class, "drawRect", void, int, int, int, int, int)
                        iload(4)
                        iload(14)
                        iload(5)
                        iload(14)
                        iload(13)
                        iadd
                        new(Color::class)
                        dup
                        sipush(255)
                        sipush(255)
                        sipush(255)
                        sipush(200)
                        invokespecial(Color::class, "<init>", void, int, int, int, int)
                        invokevirtual(Color::class, "getRGB", int)
                        invokestatic(Gui::class, "drawRect", void, int, int, int, int, int)
                        +L["34"]
                        aload_0
                        iload_1
                        iload_2
                        invokevirtual(GuiSlot::class, "func_148142_b", void, int, int)
                        invokestatic(GlStateManager::class, "enableTexture2D", void)
                        sipush(7424)
                        invokestatic(GlStateManager::class, "shadeModel", void, int)
                        invokestatic(GlStateManager::class, "enableAlpha", void)
                        invokestatic(GlStateManager::class, "disableBlend", void)
                        +L["1"]
                        _return
                    }
                }
                "overlayBackground" -> {
                    it.instructions.clear()
                    it.localVariables.clear()
                    it.koffee {
                        iload_1
                        iload_2
                        invokestatic(GuiSlotHook::class, "overlayBackground", void, int, int)
                        _return
                    }
                }
            }
        }

        return original
    }
}