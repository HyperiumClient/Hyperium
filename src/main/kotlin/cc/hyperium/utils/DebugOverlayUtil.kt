package cc.hyperium.utils

import cc.hyperium.VERSION
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.chunk.RenderChunk
import net.minecraft.util.BlockPos
import net.minecraft.util.MathHelper

// todo: change format strings to kotlin string interp
object DebugOverlayUtil {

    private val mc = Minecraft.getMinecraft()

    fun renderOldDebugInfoLeft() {
        val fontRendererObj: FontRenderer = mc.fontRendererObj
        fontRendererObj.drawStringWithShadow(
            "Minecraft 1.8.9 (${Minecraft.getDebugFPS()} fps, ${RenderChunk.renderChunksUpdated} chunk updates)",
            2f,
            2f,
            -1
        )
        fontRendererObj.drawStringWithShadow(mc.renderGlobal.debugInfoRenders, 2f, 12f, -1)
        fontRendererObj.drawStringWithShadow(mc.renderGlobal.debugInfoEntities, 2f, 22f, -1)
        fontRendererObj.drawStringWithShadow(
            "P: ${mc.effectRenderer.statistics}. T: ${mc.theWorld.debugLoadedEntities}", 2f, 32f, -1
        )
        fontRendererObj.drawStringWithShadow(mc.theWorld.providerName, 2f, 42f, -1)
        val posX = MathHelper.floor_double(mc.thePlayer.posX)
        val posY = MathHelper.floor_double(mc.thePlayer.posY)
        val posZ = MathHelper.floor_double(mc.thePlayer.posZ)
        fontRendererObj.drawStringWithShadow(
            String.format("x: %.5f (%d) // c: %d (%d)", mc.thePlayer.posX, posX, posX shr 4, posX and 15),
            2f,
            64f,
            -1
        )
        fontRendererObj.drawStringWithShadow(
            String.format(
                "y: %.3f (feet pos, %.3f eyes pos)",
                mc.thePlayer.entityBoundingBox.minY, mc.thePlayer.posY
            ), 2f, 72f, -1
        )
        val entity = mc.renderViewEntity
        val enumfacing = entity.horizontalFacing
        fontRendererObj.drawStringWithShadow(
            String.format("z: %.5f (%d) // c: %d (%d)", mc.thePlayer.posZ, posZ, posZ shr 4, posZ and 15),
            2f,
            80f,
            -1
        )
        val yaw = MathHelper.floor_double((mc.thePlayer.rotationYaw * 4.0f / 360.0f).toDouble() + 0.5) and 3
        fontRendererObj.drawStringWithShadow(
            "f: $yaw ($enumfacing) / ${MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw)}", 2f, 88f, -1
        )
        if (mc.theWorld != null && !mc.theWorld.isAirBlock(BlockPos(posX, posY, posZ))) {
            val chunk = mc.theWorld.getChunkFromBlockCoords(BlockPos(posX, posY, posZ))

            fontRendererObj.drawStringWithShadow(
                "lc: " + (chunk.topFilledSegment + 15) + " b: " +
                        chunk.getBiome(BlockPos(posX and 15, 64, posZ and 15), mc.theWorld.worldChunkManager)
                            .biomeName + " bl: "
                        + chunk.getBlockLightOpacity(
                    BlockPos(
                        posX and 15,
                        posY,
                        posZ and 15
                    )
                ) + " sl: " + chunk.getBlockLightOpacity(
                    BlockPos(posX and 15, posY, posZ and 15)
                ) + " rl: " + chunk.getBlockLightOpacity(BlockPos(posX and 15, posY, posZ and 15)), 2f, 96f, -1
            )
        }

        fontRendererObj.drawStringWithShadow(
            String.format(
                "ws: %.3f, fs: %.3f, g: %b, fl: %d", mc.thePlayer.capabilities.walkSpeed,
                mc.thePlayer.capabilities.flySpeed, mc.thePlayer.onGround, mc.theWorld.height
            ), 2f, 104f, -1
        )

        if (mc.entityRenderer != null && mc.entityRenderer.isShaderActive) {
            fontRendererObj.drawStringWithShadow(
                "shader: ${mc.entityRenderer.shaderGroup.shaderGroupName}",
                2f,
                112f,
                -1
            )
        }
    }

    fun renderOldDebugInfoRight(scaledResolution: ScaledResolution) {
        val scaledWidth = scaledResolution.scaledWidth
        val fontRendererObj = mc.fontRendererObj
        val maxMemory = Runtime.getRuntime().maxMemory()
        val totalMemory = Runtime.getRuntime().totalMemory()
        val freeMemory = Runtime.getRuntime().freeMemory()
        val usedMemory = totalMemory - freeMemory
        var memoryStr =
            "Used memory: " + usedMemory * 100L / maxMemory + "% (" + usedMemory / 1024L / 1024L + "MB) of " + maxMemory / 1024L / 1024L + "MB"
        fontRendererObj.drawStringWithShadow(
            memoryStr,
            scaledWidth - fontRendererObj.getStringWidth(memoryStr) - 2.toFloat(),
            2f,
            -1
        )
        memoryStr =
            "Allocated memory: " + totalMemory * 100L / maxMemory + "% (" + totalMemory / 1024L / 1024L + "MB)"
        fontRendererObj.drawStringWithShadow(
            memoryStr,
            scaledWidth - fontRendererObj.getStringWidth(memoryStr) - 2.toFloat(),
            12f,
            -1
        )
        val versionString = "Hyperium $VERSION"
        fontRendererObj.drawStringWithShadow(
            versionString,
            scaledWidth - fontRendererObj.getStringWidth(versionString) - 2.toFloat(),
            22f,
            -1
        )
    }
}