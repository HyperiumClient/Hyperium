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

package cc.hyperium.mixinsimp.client.audio;

import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.world.audio.SoundPlayEvent;
import net.minecraft.client.audio.ISound;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.locks.ReentrantLock;

public class HyperiumSoundManager {

    private ReentrantLock lock = new ReentrantLock();

    public void playSound(ISound sound, CallbackInfo ci) {
        if (Settings.SMART_SOUNDS && !Display.isActive()) {
            ci.cancel(); // does not stop music from being played but whatever
            return;
        }

        SoundPlayEvent e = new SoundPlayEvent(sound);
        EventBus.INSTANCE.post(e);

        if (e.isCancelled()) ci.cancel();
    }

    public void startUpdate() {
        lock.lock();
    }
    public void endUpdate() {
        lock.unlock();
    }
    public void startStopAllSounds() {
        lock.lock();
    }
    public void endStopAllSounds() {
        lock.unlock();
    }
}
