/*
 *      Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published
 *      by the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mixins.utils;

import cc.hyperium.mixinsimp.HyperiumResourcePackRepository;
import net.minecraft.client.resources.ResourcePackRepository;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(ResourcePackRepository.class)
public class MixinResourcePackRepository {


    @Final
    @Shadow
    private final File dirServerResourcepacks = null;
    private HyperiumResourcePackRepository hyperiumResourcePackRepository = new HyperiumResourcePackRepository();
    /**
     * @author
     */
    @Inject(method = "func_183028_i", at = @At("HEAD"), cancellable = true)
    private void func_183028_i(CallbackInfo callbackInfo) {
        hyperiumResourcePackRepository.func_183028_i(callbackInfo,dirServerResourcepacks);
    }
}
