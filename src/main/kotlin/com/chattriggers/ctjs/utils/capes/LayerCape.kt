package com.chattriggers.ctjs.utils.capes

import com.chattriggers.ctjs.utils.kotlin.MathHelper
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.entity.RenderPlayer
import net.minecraft.client.renderer.entity.layers.LayerRenderer
import net.minecraft.entity.player.EnumPlayerModelParts

class LayerCape(private val playerRenderer: RenderPlayer) : LayerRenderer<AbstractClientPlayer> {
    override fun shouldCombineTextures() = false

    override fun doRenderLayer(player: AbstractClientPlayer, limbSwing: Float, limbSwingAmount: Float, partialTicks: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, scale: Float) {
        val rl = CapeHandler.getCapeResource(player)

        if (!player.hasPlayerInfo() || player.isInvisible || !player.isWearing(EnumPlayerModelParts.CAPE) || rl == null) return

        var f9 = 0.14f
        var f10 = 0.0f
        if (player.isSneaking) {
            f9 = 0.1f
            f10 = 0.09f
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        this.playerRenderer.bindTexture(rl)
        GlStateManager.pushMatrix()
        GlStateManager.translate(0.0f, f10, f9)
        val d0 = player.prevChasingPosX + (player.chasingPosX - player.prevChasingPosX) * partialTicks.toDouble() - (player.prevPosX + (player.posX - player.prevPosX) * partialTicks.toDouble())
        val d1 = player.prevChasingPosY + (player.chasingPosY - player.prevChasingPosY) * partialTicks.toDouble() - (player.prevPosY + (player.posY - player.prevPosY) * partialTicks.toDouble())
        val d2 = player.prevChasingPosZ + (player.chasingPosZ - player.prevChasingPosZ) * partialTicks.toDouble() - (player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks.toDouble())
        val f = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks
        val d3 = MathHelper.sin(f * 0.01745329f).toDouble()
        val d4 = (-MathHelper.cos(f * 0.01745329f)).toDouble()
        var f1 = d1.toFloat() * 10.0f
        //#if MC<=10809
        f1 = MathHelper.clamp_float(f1, 3.0f, 32.0f)
        //#else
        //$$ f1 = MathHelper.clamp(f1, 3.0F, 32.0F);
        //#endif
        var f2 = (d0 * d3 + d2 * d4).toFloat() * 100.0f
        val f3 = (d0 * d4 - d2 * d3).toFloat() * 100.0f
        if (f2 < 0.0f) {
            f2 = 0.0f
        }
        val f4 = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * partialTicks
        f1 += MathHelper.sin((player.prevDistanceWalkedModified + (player.distanceWalkedModified - player.prevDistanceWalkedModified) * partialTicks) * 6.0f) * 32.0f * f4
        if (player.isSneaking) {
            f1 += 20.0f
        }
        GlStateManager.rotate(5.0f + f2 / 2.0f + f1, 1.0f, 0.0f, 0.0f)
        GlStateManager.rotate(f3 / 2.0f, 0.0f, 0.0f, 1.0f)
        GlStateManager.rotate(-f3 / 2.0f, 0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f)
        this.playerRenderer.mainModel.renderCape(0.0625f)
        GlStateManager.popMatrix()
    }
}