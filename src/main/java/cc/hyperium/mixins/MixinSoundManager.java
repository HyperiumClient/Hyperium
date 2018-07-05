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

package cc.hyperium.mixins;

import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.SoundPlayEvent;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundManager;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.locks.ReentrantLock;

@Mixin(SoundManager.class)
public class MixinSoundManager {

    private ReentrantLock lock = new ReentrantLock();

    /**
     * Sound will not play unless the window is active while the out of
     * focus sounds option is disabled
     *
     * @param sound the sound
     * @param ci    callback
     */
    @Inject(
            method = "playSound",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/audio/SoundHandler;getSound(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/client/audio/SoundEventAccessorComposite;"),
            cancellable = true
    )
    private void playSound(ISound sound, CallbackInfo ci) {
        if (Settings.SMART_SOUNDS && !Display.isActive()) {
            ci.cancel(); // does not stop music from being played but whatever
            return;
        }
        SoundPlayEvent e = new SoundPlayEvent(sound);
        EventBus.INSTANCE.post(e);

        if (e.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "updateAllSounds", at = @At("HEAD"))
    public void startUpdate(CallbackInfo info) {
        lock.lock();
    }

    @Inject(method = "updateAllSounds", at = @At("TAIL"))
    public void endUpdate(CallbackInfo info) {
        lock.unlock();
    }


    @Inject(method = "playSound", at = @At("HEAD"))
    public void startPlaySound(CallbackInfo info) {
        lock.lock();
    }

    @Inject(method = "playSound", at = @At("TAIL"))
    public void endPlaySound(CallbackInfo info) {
        lock.unlock();
    }
    @Inject(method = "stopAllSounds", at = @At("HEAD"))
    public void startStopAllSounds(CallbackInfo info) {
        lock.lock();
    }

    @Inject(method = "stopAllSounds", at = @At("TAIL"))
    public void endStopAllSounds(CallbackInfo info) {
        lock.unlock();
    }

}
