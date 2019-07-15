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

package cc.hyperium.mixinsimp.client.resources;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

public class HyperiumResourcePackRepository {

    public void deleteOldServerResourcesPacks(CallbackInfo callbackInfo, File dirServerResourcepacks) {
        try {
            FileUtils.listFiles(dirServerResourcepacks, TrueFileFilter.TRUE, null);
        } catch (Exception e) {
            callbackInfo.cancel();
        }
    }
}
