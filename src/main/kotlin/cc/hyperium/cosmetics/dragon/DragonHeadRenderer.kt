/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cc.hyperium.cosmetics.dragon

import cc.hyperium.cosmetics.CosmeticsUtil.interpolate
import cc.hyperium.cosmetics.CosmeticsUtil.shouldHide
import cc.hyperium.event.InvokeEvent
import cc.hyperium.event.render.RenderPlayerEvent
import cc.hyperium.purchases.EnumPurchaseType
import cc.hyperium.purchases.PurchaseApi
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.client.model.ModelBase
import net.minecraft.client.model.ModelRenderer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.BlockPos
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import kotlin.math.abs

class DragonHeadRenderer(private val dragonCosmetic: DragonCosmetic) : ModelBase() {
    private val mc = Minecraft.getMinecraft()
    private val jaw: ModelRenderer
    private val head: ModelRenderer
    private val selectedLoc = ResourceLocation("textures/entity/enderdragon/dragon.png")

    @InvokeEvent
    fun onRenderPlayer(event: RenderPlayerEvent) {
        if (shouldHide(EnumPurchaseType.DRAGON_HEAD)) return
        val entity: EntityPlayer = event.entity
        if (dragonCosmetic.isPurchasedBy(entity.uniqueID) && !entity.isInvisible) {
            val packageIfReady = PurchaseApi.getInstance().getPackageIfReady(event.entity.uniqueID)
            if (packageIfReady == null || packageIfReady.cachedSettings.isDragonHeadDisabled) return
            GlStateManager.pushMatrix()
            GlStateManager.translate(event.x, event.y, event.z)
            renderHead(event.entity, event.partialTicks)
            GlStateManager.popMatrix()
        }
    }

    private fun renderHead(player: EntityPlayer, partialTicks: Float) {
        val scale = 1.0
        val rotate = interpolate(player.prevRotationYawHead, player.rotationYawHead, partialTicks)
        val rotate1 = interpolate(player.prevRotationPitch, player.rotationPitch, partialTicks)
        GlStateManager.scale(-scale, -scale, scale)
        GlStateManager.rotate(180.0f + rotate, 0.0F, 1.0F, 0.0F)
        GlStateManager.translate(0.0, -(player.height - .4) / scale, 0.0)
        GlStateManager.translate(0.0, 0.0, .05 / scale)
        GlStateManager.rotate(rotate1, 1.0F, 0.0F, 0.0F)
        GlStateManager.translate(0.0, -0.3 / scale, .06)
        if (player.isSneaking) GlStateManager.translate(0.0, 0.125 / scale, 0.0)
        val colors = floatArrayOf(1.0f, 1.0f, 1.0f)

        if (player.onGround) {
            jaw.rotateAngleX = 0f
        } else {
            if (mc.theWorld != null) {
                var e = -1
                val theWorld = Minecraft.getMinecraft().theWorld
                val chunk =
                        theWorld.getChunkFromBlockCoords(BlockPos(player.posX, player.posY, player.posZ))
                for (i in 0..254) {
                    if (i > player.posY) break
                    val block = chunk.getBlock(BlockPos(player.posX, i.toDouble(), player.posZ))
                    if (block != null && block.material != Material.air) e = i
                }
                jaw.rotateAngleX = 0f
                if (e != -1) {
                    var dis = abs(e - player.posY)
                    dis /= 4.0
                    if (dis != 0.0) {
                        jaw.rotateAngleX = (dis / (1 + dis)).toFloat()
                    }
                }
            } else {
                jaw.rotateAngleX = 0f
            }
        }

        GlStateManager.color(colors[0], colors[1], colors[2])
        mc.textureManager.bindTexture(selectedLoc)
        GlStateManager.scale(0.5f, 0.5f, 0.5f)
        head.render(.1f)
        GlStateManager.cullFace(GL11.GL_BACK)
        GlStateManager.disableCull()
    }

    init {
        val f = -16.0f
        textureWidth = 256
        textureHeight = 256
        setTextureOffset("body.body", 0, 0)
        setTextureOffset("wing.skin", -56, 88)
        setTextureOffset("wingtip.skin", -56, 144)
        setTextureOffset("rearleg.main", 0, 0)
        setTextureOffset("rearfoot.main", 112, 0)
        setTextureOffset("rearlegtip.main", 196, 0)
        setTextureOffset("head.upperhead", 112, 30)
        setTextureOffset("wing.bone", 112, 88)
        setTextureOffset("head.upperlip", 176, 44)
        setTextureOffset("jaw.jaw", 176, 65)
        setTextureOffset("frontleg.main", 112, 104)
        setTextureOffset("wingtip.bone", 112, 136)
        setTextureOffset("frontfoot.main", 144, 104)
        setTextureOffset("neck.box", 192, 104)
        setTextureOffset("frontlegtip.main", 226, 138)
        setTextureOffset("body.scale", 220, 53)
        setTextureOffset("head.scale", 0, 0)
        setTextureOffset("neck.scale", 48, 0)
        setTextureOffset("head.nostril", 112, 0)
        head = ModelRenderer(this, "head")
        head.addBox("upperlip", -6.0f, -1.0f, -8.0f + f, 12, 5, 16)
        head.addBox("upperhead", -8.0f, -8.0f, 6.0f + f, 16, 16, 16)
        head.mirror = true
        head.addBox("scale", -5.0f, -12.0f, 12.0f + f, 2, 4, 6)
        head.addBox("nostril", -5.0f, -3.0f, -6.0f + f, 2, 2, 4)
        head.mirror = false
        head.addBox("scale", 3.0f, -12.0f, 12.0f + f, 2, 4, 6)
        head.addBox("nostril", 3.0f, -3.0f, -6.0f + f, 2, 2, 4)
        jaw = ModelRenderer(this, "jaw")
        jaw.setRotationPoint(0.0f, 4.0f, 8.0f + f)
        jaw.addBox("jaw", -6.0f, 0.0f, -16.0f, 12, 4, 16)
        head.addChild(jaw)
    }
}
