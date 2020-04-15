package cc.hyperium.hooks;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.render.DrawBlockHighlightEvent;
import cc.hyperium.integrations.perspective.PerspectiveModifierHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;
import org.lwjgl.opengl.Display;

public class EntityRendererHook {

  public static void updateRendererHook(Minecraft mc) {
    if (Hyperium.INSTANCE.getHandlers() == null || Hyperium.INSTANCE.getHandlers().getPerspectiveHandler() == null) {
      return;
    }

    PerspectiveModifierHandler perspectiveHandler = Hyperium.INSTANCE.getHandlers().getPerspectiveHandler();
    boolean activeDisplay = Display.isActive();

    if (mc.inGameHasFocus && activeDisplay) {
      if (perspectiveHandler.enabled && mc.gameSettings.thirdPersonView != 1) {
        perspectiveHandler.onDisable();
      }

      if (perspectiveHandler.enabled) {
        mc.mouseHelper.mouseXYChange();

        float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        float f1 = f * f * f * 8.0F;
        float f2 = (float) mc.mouseHelper.deltaX * f1;
        float f3 = (float) mc.mouseHelper.deltaY * f1;

        // Modifying pitch and yaw values.
        perspectiveHandler.modifiedYaw += f2 / 8.0F;
        perspectiveHandler.modifiedPitch += f3 / 8.0F;

        // Checking if pitch exceeds maximum range.
        if (Math.abs(perspectiveHandler.modifiedPitch) > 90.0F) {
          if (perspectiveHandler.modifiedPitch > 0.0F) {
            perspectiveHandler.modifiedPitch = 90.0F;
          } else {
            perspectiveHandler.modifiedPitch = -90.0F;
          }
        }
      }
    }
  }

  public static void orientCameraHook(EntityRenderer renderer, float partialTicks) {
    PerspectiveModifierHandler perspectiveHandler = Hyperium.INSTANCE.getHandlers().getPerspectiveHandler();
    Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
    float f = entity.getEyeHeight();
    double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)partialTicks;
    double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)partialTicks + (double)f;
    double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)partialTicks;

    if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPlayerSleeping()) {
      f = (float)((double)f + 1.0D);
      GlStateManager.translate(0.0F, 0.3F, 0.0F);

      if (!Minecraft.getMinecraft().gameSettings.debugCamEnable) {
        BlockPos blockpos = new BlockPos(entity);
        IBlockState iblockstate = Minecraft.getMinecraft().theWorld.getBlockState(blockpos);
        Block block = iblockstate.getBlock();

        if (block == Blocks.bed) {
          int j = iblockstate.getValue(BlockBed.FACING).getHorizontalIndex();
          GlStateManager.rotate((float)(j * 90), 0.0F, 1.0F, 0.0F);
        }

        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0F, 0.0F, -1.0F, 0.0F);
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, -1.0F, 0.0F, 0.0F);
      }
    } else if (Minecraft.getMinecraft().gameSettings.thirdPersonView > 0) {
      double d3 = renderer.thirdPersonDistanceTemp + (renderer.thirdPersonDistance - renderer.thirdPersonDistanceTemp) * partialTicks;

      if (Minecraft.getMinecraft().gameSettings.debugCamEnable) {
        GlStateManager.translate(0.0F, 0.0F, (float)(-d3));
      } else {
        float f1 = entity.rotationYaw;
        float f2 = entity.rotationPitch;

        if (perspectiveHandler.enabled) {
          f1 = perspectiveHandler.modifiedYaw;
          f2 = perspectiveHandler.modifiedPitch;
        }
        if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) {
          f2 += 180.0F;
        }

        double d4 = (double)(-MathHelper.sin(f1 / 180.0F * (float)Math.PI) * MathHelper.cos(f2 / 180.0F * (float)Math.PI)) * d3;
        double d5 = (double)(MathHelper.cos(f1 / 180.0F * (float)Math.PI) * MathHelper.cos(f2 / 180.0F * (float)Math.PI)) * d3;
        double d6 = (double)(-MathHelper.sin(f2 / 180.0F * (float)Math.PI)) * d3;

        for (int i = 0; i < 8; ++i) {
          float f3 = (float)((i & 1) * 2 - 1);
          float f4 = (float)((i >> 1 & 1) * 2 - 1);
          float f5 = (float)((i >> 2 & 1) * 2 - 1);
          f3 = f3 * 0.1F;
          f4 = f4 * 0.1F;
          f5 = f5 * 0.1F;
          MovingObjectPosition movingobjectposition = Minecraft.getMinecraft().theWorld.rayTraceBlocks(new Vec3(d0 + (double)f3, d1 + (double)f4, d2 + (double)f5), new Vec3(d0 - d4 + (double)f3 + (double)f5, d1 - d6 + (double)f4, d2 - d5 + (double)f5));

          if (movingobjectposition != null) {
            double d7 = movingobjectposition.hitVec.distanceTo(new Vec3(d0, d1, d2));

            if (d7 < d3) {
              d3 = d7;
            }
          }
        }

        if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) {
          GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        }

        if (perspectiveHandler.enabled) {
          GlStateManager.rotate(perspectiveHandler.modifiedPitch - f2, 1.0f, 0.0f, 0.0f);
          GlStateManager.rotate(perspectiveHandler.modifiedYaw - f1, 0.0f, 1.0f, 0.0f);
          GlStateManager.translate(0.0f, 0.0f, (float) (-d3));
          GlStateManager.rotate(f1 - perspectiveHandler.modifiedYaw, 0.0f, 1.0f, 0.0f);
          GlStateManager.rotate(f2 - perspectiveHandler.modifiedPitch, 1.0f, 0.0f, 0.0f);
        } else {
          GlStateManager.rotate(entity.rotationPitch - f2, 1.0F, 0.0F, 0.0F);
          GlStateManager.rotate(entity.rotationYaw - f1, 0.0F, 1.0F, 0.0F);
          GlStateManager.translate(0.0F, 0.0F, (float)(-d3));
          GlStateManager.rotate(f1 - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
          GlStateManager.rotate(f2 - entity.rotationPitch, 1.0F, 0.0F, 0.0F);
        }
      }
    } else {
      GlStateManager.translate(0.0F, 0.0F, -0.1F);
    }

    if (!Minecraft.getMinecraft().gameSettings.debugCamEnable) {
      float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180.0f;
      final float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
      final float roll = 0.0f;

      if (entity instanceof EntityAnimal) {
        EntityAnimal entityanimal = (EntityAnimal)entity;
        yaw = entityanimal.prevRotationYawHead + (entityanimal.rotationYawHead - entityanimal.prevRotationYawHead) * partialTicks + 180.0f;
      }

      if (perspectiveHandler.enabled) {
        GlStateManager.rotate(roll, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(perspectiveHandler.modifiedPitch, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(perspectiveHandler.modifiedYaw + 180.0f, 0.0f, 1.0f, 0.0f);
      } else {
        GlStateManager.rotate(roll, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(pitch, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(yaw, 0.0f, 1.0f, 0.0f);
      }
    }

    GlStateManager.translate(0.0F, -f, 0.0F);
    d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)partialTicks;
    d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)partialTicks + (double)f;
    d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)partialTicks;
    renderer.cloudFog = Minecraft.getMinecraft().renderGlobal.hasCloudFog(d0, d1, d2, partialTicks);
  }

  public static void postDrawBlock(Minecraft mc, float partialTicks) {
    DrawBlockHighlightEvent drawBlockHighlightEvent = new DrawBlockHighlightEvent(((EntityPlayer) mc.getRenderViewEntity()), mc.objectMouseOver, partialTicks);
    EventBus.INSTANCE.post(drawBlockHighlightEvent);
    if (drawBlockHighlightEvent.isCancelled()) {
      Hyperium.INSTANCE.getHandlers().getConfigOptions().isCancelBox = true;
    }
  }
}
