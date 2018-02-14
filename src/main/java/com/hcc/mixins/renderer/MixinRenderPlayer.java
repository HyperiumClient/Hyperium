/*
 * Hypixel Community Client, Client optimized for Hypixel Network
 * Copyright (C) 2018  HCC Dev Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hcc.mixins.renderer;

import com.hcc.event.EventBus;
import com.hcc.event.RenderPlayerEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public class MixinRenderPlayer {
    @Inject(method="doRender", at=@At("HEAD"))
    public void doRender(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci){
        EventBus.INSTANCE.post(new RenderPlayerEvent(entity));
    }
}

