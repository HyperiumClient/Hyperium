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
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Mixin(TextureManager.class)
public abstract class MixinTextureManager {


    @Shadow
    @Final
    private static Logger logger;
    @Shadow
    @Final
    private Map<ResourceLocation, ITextureObject> mapTextureObjects;
    @Shadow
    private IResourceManager theResourceManager;

    @Shadow
    @Final
    private Map<String, Integer> mapTextureCounters;
    //MIGHT BE MAJOR MEMORY LEAK
    //TODO test ^^^
    private HashMap<String, DynamicTexture> textures = new HashMap<>();

    /**
     * @author Sk1er
     * @reason Broken dynamic textures
     */
    @Overwrite
    public boolean loadTexture(ResourceLocation textureLocation, ITextureObject textureObj) {
        DynamicTexture dynamicTexture = textures.get(textureLocation.toString());
        if (dynamicTexture != null) {
            textureObj = dynamicTexture;
        }
        boolean flag = true;

        try {
            textureObj.loadTexture(this.theResourceManager);
        } catch (IOException ioexception) {
            logger.warn("Failed to load texture: " + textureLocation, ioexception);
            textureObj = TextureUtil.missingTexture;
            this.mapTextureObjects.put(textureLocation, textureObj);
            flag = false;
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Registering texture");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Resource location being registered");
            crashreportcategory.addCrashSection("Resource location", textureLocation);
            String name = textureObj.getClass().getName();
            crashreportcategory.addCrashSectionCallable("Texture object class", () -> name);
            throw new ReportedException(crashreport);
        }

        this.mapTextureObjects.put(textureLocation, textureObj);
        return flag;
    }

    /**
     * @author Sk1er
     * @reason Broken dynamic textures
     */
    @Overwrite
    public ResourceLocation getDynamicTextureLocation(String name, DynamicTexture texture) {
        Integer integer = (Integer) this.mapTextureCounters.get(name);

        if (integer == null) {
            integer = 1;
        } else {
            integer = integer + 1;
        }

        this.mapTextureCounters.put(name, integer);
        String format = String.format("dynamic/%s_%d", name, integer);
        ResourceLocation resourcelocation = new ResourceLocation(format);
        textures.put(resourcelocation.toString(), texture);
        this.loadTexture(resourcelocation, texture);
        return resourcelocation;
    }

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
