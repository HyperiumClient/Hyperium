package cc.hyperium.mixins.gui;

import cc.hyperium.config.Settings;
import cc.hyperium.mixins.MixinMinecraft;
import me.semx11.autotip.util.ReflectionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.chunk.Chunk;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Mixin(GuiOverlayDebug.class)
public abstract class MixinGuiOverlayDebug {
    @Shadow @Final private Minecraft mc;

    @Shadow protected abstract void renderDebugInfoLeft();

    @Shadow protected abstract void renderDebugInfoRight(ScaledResolution p_175239_1_);

    @Shadow protected abstract void renderLagometer();

    /**
     * @reason Add 1.7 debug
     * @author SiroQ
     */
    @Overwrite
    public void renderDebugInfo(ScaledResolution scaledResolutionIn){
        this.mc.mcProfiler.startSection("debug");
        GlStateManager.pushMatrix();
        if(Settings.OLD_DEBUG){
            ScaledResolution var5 = new ScaledResolution(this.mc);
            int var6 = var5.getScaledWidth();
            int var7 = var5.getScaledHeight();
            FontRenderer fontRendererObj = this.mc.fontRendererObj;
            int var21;
            int var22;
            int var23;
            Field debugFPSField = ReflectionUtil.findField(Minecraft.class,new String[]{"debugFPS","field_71470_ab", "ao"});
            debugFPSField.setAccessible(true);
            try {
                fontRendererObj.drawStringWithShadow("Minecraft 1.8.9 (" + debugFPSField.get(null) + " fps, " + RenderChunk.renderChunksUpdated + " chunk updates)", 2, 2, 16777215);
            }catch (IllegalAccessException e){
                e.printStackTrace();
            }
            fontRendererObj.drawStringWithShadow(this.mc.renderGlobal.getDebugInfoRenders(), 2, 12, 16777215);
            fontRendererObj.drawStringWithShadow(this.mc.renderGlobal.getDebugInfoEntities(), 2, 22, 16777215);
            fontRendererObj.drawStringWithShadow("P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.theWorld.getDebugLoadedEntities(), 2, 32, 16777215);
            fontRendererObj.drawStringWithShadow(this.mc.theWorld.getProviderName(), 2, 42, 16777215);
            long var36 = Runtime.getRuntime().maxMemory();
            long var41 = Runtime.getRuntime().totalMemory();
            long var44 = Runtime.getRuntime().freeMemory();
            long var45 = var41 - var44;
            String var20 = "Used memory: " + var45 * 100L / var36 + "% (" + var45 / 1024L / 1024L + "MB) of " + var36 / 1024L / 1024L + "MB";
            var21 = 14737632;
            fontRendererObj.drawStringWithShadow(var20, var6 - fontRendererObj.getStringWidth(var20) - 2, 2, 14737632);
            var20 = "Allocated memory: " + var41 * 100L / var36 + "% (" + var41 / 1024L / 1024L + "MB)";
            fontRendererObj.drawStringWithShadow(var20, var6 - fontRendererObj.getStringWidth(var20) - 2, 12, 14737632);
            var22 = MathHelper.floor_double(this.mc.thePlayer.posX);
            var23 = MathHelper.floor_double(this.mc.thePlayer.posY);
            int var24 = MathHelper.floor_double(this.mc.thePlayer.posZ);
            fontRendererObj.drawStringWithShadow(String.format("x: %.5f (%d) // c: %d (%d)", new Object[] {Double.valueOf(this.mc.thePlayer.posX), Integer.valueOf(var22), Integer.valueOf(var22 >> 4), Integer.valueOf(var22 & 15)}), 2, 64, 14737632);
            fontRendererObj.drawStringWithShadow(String.format("y: %.3f (feet pos, %.3f eyes pos)", new Object[] {Double.valueOf(this.mc.thePlayer.getEntityBoundingBox().minY), Double.valueOf(this.mc.thePlayer.posY)}), 2, 72, 14737632);
            Entity entity = this.mc.getRenderViewEntity();
            EnumFacing enumfacing = entity.getHorizontalFacing();
            fontRendererObj.drawStringWithShadow(String.format("z: %.5f (%d) // c: %d (%d)", new Object[] {Double.valueOf(this.mc.thePlayer.posZ), Integer.valueOf(var24), Integer.valueOf(var24 >> 4), Integer.valueOf(var24 & 15)}), 2, 80, 14737632);
            int var25 = MathHelper.floor_double((double)(this.mc.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            fontRendererObj.drawStringWithShadow("f: " + var25 + " (" + enumfacing + ") / " + MathHelper.wrapAngleTo180_float(this.mc.thePlayer.rotationYaw), 2, 88, 14737632);

            if (this.mc.theWorld != null && !this.mc.theWorld.isAirBlock(new BlockPos(var22, var23, var24)))
            {
                Chunk var26 = this.mc.theWorld.getChunkFromBlockCoords(new BlockPos(var22, var23,var24));
                fontRendererObj.drawStringWithShadow("lc: " + (var26.getTopFilledSegment() + 15) + " b: " + var26.getBiome(new BlockPos(var22 & 15, 64,var24 & 15), this.mc.theWorld.getWorldChunkManager()).biomeName + " bl: " + var26.getBlockLightOpacity(new BlockPos(var22 & 15, var23, var24 & 15)) + " sl: " + var26.getBlockLightOpacity(new BlockPos(var22 & 15, var23, var24 & 15)) + " rl: " + var26.getBlockLightOpacity(new BlockPos(var22 & 15, var23, var24 & 15)), 2, 96, 14737632);
            }

            fontRendererObj.drawStringWithShadow(String.format("ws: %.3f, fs: %.3f, g: %b, fl: %d", new Object[] {Float.valueOf(this.mc.thePlayer.capabilities.getWalkSpeed()), Float.valueOf(this.mc.thePlayer.capabilities.getFlySpeed()), Boolean.valueOf(this.mc.thePlayer.onGround), Integer.valueOf(this.mc.theWorld.getHeight())}), 2, 104, 14737632);

            if (this.mc.entityRenderer != null && this.mc.entityRenderer.isShaderActive())
            {
                fontRendererObj.drawStringWithShadow(String.format("shader: %s", new Object[] {this.mc.entityRenderer.getShaderGroup().getShaderGroupName()}), 2, 112, 14737632);
            }

            GL11.glPopMatrix();
            this.mc.mcProfiler.endSection();
            return;
        }
        this.renderDebugInfoLeft();
        this.renderDebugInfoRight(scaledResolutionIn);
        GlStateManager.popMatrix();
        if (this.mc.gameSettings.showLagometer) {
            this.renderLagometer();
        }
        this.mc.mcProfiler.endSection();
    }
}
