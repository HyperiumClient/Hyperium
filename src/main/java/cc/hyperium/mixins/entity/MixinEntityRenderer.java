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

import cc.hyperium.mixinsimp.entity.HyperiumEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.MouseFilter;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {
    @Shadow
    private float thirdPersonDistance;
    @Shadow
    private float thirdPersonDistanceTemp;
    @Shadow
    private boolean cloudFog;
    @Shadow
    private boolean debugView;
    @Shadow
    private Minecraft mc;
    @Shadow
    private float fovModifierHandPrev;
    @Shadow
    private float fovModifierHand;
    @Shadow
    private MouseFilter mouseFilterYAxis;
    @Shadow
    private MouseFilter mouseFilterXAxis;
    @Shadow
    private boolean drawBlockOutline;

    @Shadow protected abstract void loadShader(ResourceLocation resourceLocationIn);

    @Shadow private ShaderGroup theShaderGroup;

    @Shadow public abstract void stopUseShader();

    private HyperiumEntityRenderer hyperiumEntityRenderer = new HyperiumEntityRenderer((EntityRenderer) (Object) this);


    //endStartSection
    @Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V", shift = At.Shift.AFTER))
    private void updateCameraAndRender(float partialTicks, long nano, CallbackInfo ci) {
        hyperiumEntityRenderer.updateCameraAndRender();
    }

    /**
     * @author CoalOres
     * @reason 360 Perspective
     */
    @Overwrite
    private void orientCamera(float partialTicks) {
        hyperiumEntityRenderer.orientCamera(partialTicks,this.thirdPersonDistanceTemp,this.thirdPersonDistance,this.cloudFog,this.mc);
    }

    /**
     * @author CoalOres
     * @reason 360 Perspective
     */

    @Inject(method = "renderWorldPass", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V", args = "ldc=outline"), cancellable = true)
    public void drawOutline(int pass, float part, long nano, CallbackInfo info) {
        hyperiumEntityRenderer.drawOutline(part,this.mc);
    }

    @Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/profiler/Profiler;startSection(Ljava/lang/String;)V", args = "ldc=mouse"))
    private void updateCameraAndRender2(float partialTicks, long nanoTime, CallbackInfo ci) {
        hyperiumEntityRenderer.updateCameraAndRender2();
    }

    // Motion Blur Methods
    public void motionBlurApplyShader(ResourceLocation resourceLocation) {
        if (OpenGlHelper.shadersSupported) {
            this.loadShader(resourceLocation);
        }
    }

    public void clearShaders(){
        this.stopUseShader();
    }
}