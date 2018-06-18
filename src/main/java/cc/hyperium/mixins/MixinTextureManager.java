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

import cc.hyperium.Hyperium;
import cc.hyperium.utils.Utils;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(TextureManager.class)
public class MixinTextureManager {

    @Inject(method = "<init>", at = @At("RETURN"))
    public void mod(IResourceManager resourceManager, CallbackInfo info) {
        try {
            System.out.println(Hyperium.INSTANCE.isDevEnv());
            Field mapTextureObjects = TextureManager.class.getDeclaredField(/*Hyperium.INSTANCE.isDevEnv() ?*/ "mapTextureObjects"/*:  "b"*/);
            mapTextureObjects.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(mapTextureObjects, mapTextureObjects.getModifiers() & ~Modifier.FINAL);
            mapTextureObjects.set((TextureManager) (Object) this, new ConcurrentHashMap<>());

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Inject(method = "onResourceManagerReload", at = @At("HEAD"))
    private void onResourceManagerReload(IResourceManager resourceManager, CallbackInfo ci) {
        Utils.INSTANCE.setCursor(new ResourceLocation("textures/cursor.png"));
    }
}
