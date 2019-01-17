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

import cc.hyperium.mixinsimp.HyperiumTextureManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Map;

@Mixin(TextureManager.class)
public abstract class MixinTextureManager {


    @Shadow
    @Final
    private static Logger logger;

    private HyperiumTextureManager hyperiumTextureManager = new HyperiumTextureManager((TextureManager) (Object) this);

    @Shadow
    private IResourceManager theResourceManager;

    @Shadow
    @Final
    private Map<String, Integer> mapTextureCounters;


    @Shadow
    @Final
    private List<ITickable> listTickables;

    /**
     * @author Sk1er
     * @reason Broken dynamic textures
     */
    @Overwrite
    public boolean loadTexture(ResourceLocation textureLocation, ITextureObject textureObj) {
        return hyperiumTextureManager.loadTexture(textureLocation, textureObj, theResourceManager, logger);
    }

    /**
     * @author Sk1er
     * @reason Fix purple screen
     */
    @Overwrite
    public boolean loadTickableTexture(ResourceLocation textureLocation, ITickableTextureObject textureObj) {
        return hyperiumTextureManager.loadTickableTexture(textureLocation, textureObj, listTickables);
    }

    /**
     * @author Sk1er
     * @reason Broken dynamic textures
     */
    @Overwrite
    public ResourceLocation getDynamicTextureLocation(String name, DynamicTexture texture) {
        return hyperiumTextureManager.getDynamicTextureLocation(name, texture, mapTextureCounters);
    }

    /**
     * @author Sk1er and Mojang
     * @reason Add lock on cape loading to prevent concurrent modification exception in texture manager
     */
    @Overwrite
    public void onResourceManagerReload(IResourceManager resourceManager) {
        hyperiumTextureManager.onResourceManagerReload(resourceManager);
    }

    /**
     * @author Sk1er
     * @reason Conciliate texture hashmap to 1 object.
     */
    @Overwrite
    public void bindTexture(ResourceLocation resource) {
        hyperiumTextureManager.bindTexture(resource);
    }

    /**
     * @author Sk1er
     * @reason Conciliate texture hashmap to 1 object.
     */
    @Overwrite
    public ITextureObject getTexture(ResourceLocation textureLocation) {
        return hyperiumTextureManager.getTexture(textureLocation);
    }

    /**
     * @author Sk1er
     * @reason Conciliate texture hashmap to 1 object.
     */
    @Overwrite
    public void tick() {
        hyperiumTextureManager.tick(listTickables);
    }

    /**
     * @author Sk1er
     * @reason Conciliate texture hashmap to 1 object.
     */
    @Overwrite
    public void deleteTexture(ResourceLocation textureLocation) {
        hyperiumTextureManager.deleteTexture(textureLocation);
    }
}
