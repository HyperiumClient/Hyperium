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

package cc.hyperium.mixins.client.audio;

import cc.hyperium.mixinsimp.client.audio.HyperiumSoundManager;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundManager.class)
public class MixinSoundManager {

    private HyperiumSoundManager hyperiumSoundManager = new HyperiumSoundManager();

    /**
     * Sound will not play unless the window is active while the out of
     * focus sounds option is disabled
     *
     * @param sound the sound
     * @param ci    callback
     */
    @Inject(method = "playSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/audio/SoundHandler;getSound(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/client/audio/SoundEventAccessorComposite;"), cancellable = true)
    private void playSound(ISound sound, CallbackInfo ci) {
        hyperiumSoundManager.playSound(sound, ci);
    }

    @Inject(method = "updateAllSounds", at = @At("HEAD"))
    private void startUpdate(CallbackInfo info) {
        hyperiumSoundManager.startUpdate();
    }

    @Inject(method = "updateAllSounds", at = @At("TAIL"))
    private void endUpdate(CallbackInfo info) {
        hyperiumSoundManager.endUpdate();
    }

    @Inject(method = "stopAllSounds", at = @At("HEAD"))
    private void startStopAllSounds(CallbackInfo info) {
        hyperiumSoundManager.startStopAllSounds();
    }

    @Inject(method = "stopAllSounds", at = @At("TAIL"))
    private void endStopAllSounds(CallbackInfo info) {
        hyperiumSoundManager.endStopAllSounds();
    }
}
