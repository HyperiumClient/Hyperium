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

import cc.hyperium.handlers.handlers.animation.CapeHandler;
import cc.hyperium.utils.Utils;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(TextureManager.class)
public abstract class MixinTextureManager {


    @Shadow
    @Final
    private Map<ResourceLocation, ITextureObject> mapTextureObjects;

    @Shadow
    public abstract boolean loadTexture(ResourceLocation textureLocation, ITextureObject textureObj);

    /**
     * @author Sk1er and Mojang
     */
    @Overwrite
    public void onResourceManagerReload(IResourceManager resourceManager) {
        CapeHandler.LOCK.lock();
        try {
            for (Map.Entry<ResourceLocation, ITextureObject> entry : this.mapTextureObjects.entrySet()) {
                this.loadTexture(entry.getKey(), entry.getValue());
            }
            Utils.INSTANCE.setCursor(new ResourceLocation("textures/cursor.png"));
        } catch (Exception e) {

        } finally {
            CapeHandler.LOCK.unlock();
        }
    }
}
