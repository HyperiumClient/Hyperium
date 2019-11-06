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

package cc.hyperium.mixins.client.renderer;

import cc.hyperium.event.render.RenderWorldEvent;
import cc.hyperium.handlers.handlers.reach.ReachDisplay;
import cc.hyperium.mixinsimp.client.renderer.HyperiumEntityRenderer;
import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {

    @Shadow private float thirdPersonDistance;
    @Shadow private float thirdPersonDistanceTemp;
    @Shadow private Minecraft mc;
    @Shadow private Entity pointedEntity;

    private HyperiumEntityRenderer hyperiumEntityRenderer = new HyperiumEntityRenderer((EntityRenderer) (Object) this);

    @Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V", shift = At.Shift.AFTER))
    private void updateCameraAndRender(float partialTicks, long nano, CallbackInfo ci) {
        hyperiumEntityRenderer.updateCameraAndRender();
    }

    @Inject(method = "activateNextShader", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/renderer/EntityRenderer;loadShader(Lnet/minecraft/util/ResourceLocation;)V"))
    private void activateNextShader(CallbackInfo callbackInfo) {
        HyperiumEntityRenderer.INSTANCE.isUsingShader = true;
    }

    /**
     * @author CoalOres
     * @reason 360 Perspective
     */
    @Overwrite
    private void orientCamera(float partialTicks) {
        hyperiumEntityRenderer.orientCamera(partialTicks, thirdPersonDistanceTemp, thirdPersonDistance, mc);
    }

    @Inject(method = "renderWorldPass", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V", args = "ldc=hand", shift = At.Shift.BEFORE))
    private void onRenderWorld(int pass, float partialTicks, long nano, CallbackInfo info) {
        mc.mcProfiler.startSection("hyperium_render_last");
        new RenderWorldEvent(mc.renderGlobal, partialTicks).post();
        mc.mcProfiler.endSection();
    }

    @Inject(method = "renderWorldPass", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V", args = "ldc=outline"), cancellable = true)
    private void drawOutline(int pass, float part, long nano, CallbackInfo info) {
        hyperiumEntityRenderer.drawOutline(part, mc);
    }

    @Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/profiler/Profiler;startSection(Ljava/lang/String;)V", args = "ldc=mouse"))
    private void updateCameraAndRender2(float partialTicks, long nanoTime, CallbackInfo ci) {
        hyperiumEntityRenderer.updatePerspectiveCamera();
    }

    /**
     * @author - Sk1er (added forward for distance)
     * @reason - ReachDisplay
     */
    @Overwrite
    public void getMouseOver(float partialTicks) {
        Entity entity = mc.getRenderViewEntity();
        if (entity != null && mc.theWorld != null) {
            mc.mcProfiler.startSection("pick");
            mc.pointedEntity = null;
            double d0 = mc.playerController.getBlockReachDistance();
            mc.objectMouseOver = entity.rayTrace(d0, partialTicks);
            double d1 = d0;
            Vec3 vec3 = entity.getPositionEyes(partialTicks);
            boolean flag = false;

            if (mc.playerController.extendedReach()) {
                d0 = 6.0D;
                d1 = 6.0D;
            } else if (d0 > 3.0D) {
                flag = true;
            }

            if (mc.objectMouseOver != null) d1 = mc.objectMouseOver.hitVec.distanceTo(vec3);

            Vec3 vec31 = entity.getLook(partialTicks);
            Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
            pointedEntity = null;
            Vec3 vec33 = null;
            float f = 1.0F;
            List<Entity> list = mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0,
                vec31.yCoord * d0, vec31.zCoord * d0).expand(f, f, f), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
            double d2 = d1;

            for (Entity entity1 : list) {
                float f1 = entity1.getCollisionBorderSize();
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f1, f1, f1);
                MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

                if (axisalignedbb.isVecInside(vec3)) {
                    if (d2 >= 0.0D) {
                        pointedEntity = entity1;
                        vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                        d2 = 0.0D;
                    }
                } else if (movingobjectposition != null) {
                    double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                    if (d3 < d2 || d2 == 0.0D) {
                        if (entity1 == entity.ridingEntity) {
                            if (d2 == 0.0D) {
                                pointedEntity = entity1;
                                vec33 = movingobjectposition.hitVec;
                            }
                        } else {
                            pointedEntity = entity1;
                            vec33 = movingobjectposition.hitVec;
                            d2 = d3;
                        }
                    }
                }
            }
            double v = 0;

            if (pointedEntity != null && flag && (v = vec3.distanceTo(vec33)) > 3.0D) {
                pointedEntity = null;
                mc.objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, null, new BlockPos(vec33));
            }

            if (v != 0 || pointedEntity != null) ReachDisplay.dis = v;

            if (pointedEntity != null && (d2 < d1 || mc.objectMouseOver == null)) {
                mc.objectMouseOver = new MovingObjectPosition(pointedEntity, vec33);

                if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                    mc.pointedEntity = pointedEntity;
                }
            }

            mc.mcProfiler.endSection();
        }
    }
}
