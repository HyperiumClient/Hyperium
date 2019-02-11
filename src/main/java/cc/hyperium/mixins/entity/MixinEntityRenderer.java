/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mixins.entity;

import cc.hyperium.event.RenderWorldEvent;
import cc.hyperium.handlers.handlers.reach.ReachDisplay;
import cc.hyperium.mixinsimp.entity.HyperiumEntityRenderer;
import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {
    @Shadow
    private float thirdPersonDistance;
    @Shadow
    private float thirdPersonDistanceTemp;
    @Shadow
    private boolean cloudFog;
    @Shadow
    private Minecraft mc;
    @Shadow
    private Entity pointedEntity;
    private HyperiumEntityRenderer hyperiumEntityRenderer = new HyperiumEntityRenderer((EntityRenderer) (Object) this);
    @Shadow
    @Final
    public static int shaderCount;

    @Shadow
    private int shaderIndex;

    //endStartSection
    @Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V", shift = At.Shift.AFTER))
    private void updateCameraAndRender(float partialTicks, long nano, CallbackInfo ci) {
        hyperiumEntityRenderer.updateCameraAndRender();
    }

    @Inject(method = "activateNextShader", at = @At("INVOKE_ASSIGN"))
    public void activateNextShader(CallbackInfo callbackInfo) {
        HyperiumEntityRenderer.INSTANCE.isUsingShader = this.shaderIndex != shaderCount;
    }

    /**
     * @author CoalOres
     * @reason 360 Perspective
     */
    @Overwrite
    private void orientCamera(float partialTicks) {
        hyperiumEntityRenderer.orientCamera(partialTicks, this.thirdPersonDistanceTemp, this.thirdPersonDistance, this.cloudFog, this.mc);
    }

    @Inject(method = "renderWorldPass", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V", args = "ldc=hand"))
    private void onRenderWorld(int pass, float partialTicks, long nano, CallbackInfo info) {
        new RenderWorldEvent(partialTicks).post();
    }

    @Inject(method = "renderWorldPass", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V", args = "ldc=outline"), cancellable = true)
    public void drawOutline(int pass, float part, long nano, CallbackInfo info) {
        hyperiumEntityRenderer.drawOutline(part, this.mc);
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
        Entity entity = this.mc.getRenderViewEntity();
        if (entity != null) {
            if (this.mc.theWorld != null) {
                this.mc.mcProfiler.startSection("pick");
                this.mc.pointedEntity = null;
                double d0 = (double) this.mc.playerController.getBlockReachDistance();
                this.mc.objectMouseOver = entity.rayTrace(d0, partialTicks);
                double d1 = d0;
                Vec3 vec3 = entity.getPositionEyes(partialTicks);
                boolean flag = false;

                if (this.mc.playerController.extendedReach()) {
                    d0 = 6.0D;
                    d1 = 6.0D;
                } else {
                    if (d0 > 3.0D) {
                        flag = true;
                    }
                }

                if (this.mc.objectMouseOver != null) {
                    d1 = this.mc.objectMouseOver.hitVec.distanceTo(vec3);
                }

                Vec3 vec31 = entity.getLook(partialTicks);
                Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
                this.pointedEntity = null;
                Vec3 vec33 = null;
                float f = 1.0F;
                List<Entity> list = this.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand((double) f, (double) f, (double) f), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
                double d2 = d1;

                for (int j = 0; j < list.size(); ++j) {
                    Entity entity1 = list.get(j);
                    float f1 = entity1.getCollisionBorderSize();
                    AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double) f1, (double) f1, (double) f1);
                    MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

                    if (axisalignedbb.isVecInside(vec3)) {
                        if (d2 >= 0.0D) {
                            this.pointedEntity = entity1;
                            vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                            d2 = 0.0D;
                        }
                    } else if (movingobjectposition != null) {
                        double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                        if (d3 < d2 || d2 == 0.0D) {
                            if (entity1 == entity.ridingEntity) {
                                if (d2 == 0.0D) {
                                    this.pointedEntity = entity1;
                                    vec33 = movingobjectposition.hitVec;
                                }
                            } else {
                                this.pointedEntity = entity1;
                                vec33 = movingobjectposition.hitVec;
                                d2 = d3;
                            }
                        }
                    }
                }
                double v = 0;

                if (this.pointedEntity != null && flag && (v = vec3.distanceTo(vec33)) > 3.0D) {
                    this.pointedEntity = null;
                    this.mc.objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, null, new BlockPos(vec33));
                }
                if (v != 0 || this.pointedEntity != null)
                    ReachDisplay.dis = v;


                if (this.pointedEntity != null && (d2 < d1 || this.mc.objectMouseOver == null)) {
                    this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity, vec33);

                    if (this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof EntityItemFrame) {
                        this.mc.pointedEntity = this.pointedEntity;
                    }
                }

                this.mc.mcProfiler.endSection();
            }
        }
    }
}
