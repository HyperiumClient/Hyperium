/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hcc.mixins;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundManager;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundManager.class)
public class MixinSoundManager {

    /**
     * Sound will not play unless the window is active
     *
     * @param sound
     * @param ci
     */
    @Inject(method = "playSound", at = @At("HEAD"), cancellable = true)
    private void playSound(ISound sound, CallbackInfo ci) {
        if (!Display.isActive()) { // todo: settings to enable / disable
            ci.cancel(); // does not stop music from being played but whatever
        }
    }

}