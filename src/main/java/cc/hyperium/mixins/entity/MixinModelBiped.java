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

import cc.hyperium.event.EntityRenderEvent;
import cc.hyperium.event.EventBus;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelBiped.class)
public class MixinModelBiped {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void render(Entity entityIn, float limbSwing, float limbSwingAmount, float p_78088_4_, float ageInTicks, float headPitch, float scale, CallbackInfo ci) {
        ModelBiped model = (ModelBiped) (Object) this;
        EntityRenderEvent event = new EntityRenderEvent(entityIn, model, limbSwing, limbSwingAmount, p_78088_4_, ageInTicks, headPitch, scale);
        EventBus.INSTANCE.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}
