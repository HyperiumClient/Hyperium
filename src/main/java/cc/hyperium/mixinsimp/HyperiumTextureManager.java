package cc.hyperium.mixinsimp;

import cc.hyperium.handlers.handlers.animation.cape.CapeHandler;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HyperiumTextureManager {


    private TextureManager parent;


    private HashMap<String, DynamicTexture> textures = new HashMap<>();

    public HyperiumTextureManager(TextureManager parent) {
        this.parent = parent;
    }

    public void onResourceManagerReload(IResourceManager resourceManager, Map<ResourceLocation, ITextureObject> mapTextureObjects) {
        CapeHandler.LOCK.lock();
        try {
            for (Map.Entry<ResourceLocation, ITextureObject> entry : mapTextureObjects.entrySet()) {
                parent.loadTexture(entry.getKey(), entry.getValue());
            }
            Utils.INSTANCE.setCursor(new ResourceLocation("textures/cursor.png"));
        } catch (Exception e) {

        } finally {
            CapeHandler.LOCK.unlock();
        }
    }

    public boolean loadTexture(ResourceLocation textureLocation, ITextureObject textureObj, IResourceManager theResourceManager, Map<ResourceLocation, ITextureObject> mapTextureObjects, Logger logger) {
        DynamicTexture dynamicTexture = textures.get(textureLocation.toString());
        if (dynamicTexture != null) {
            textureObj = dynamicTexture;
        }
        boolean flag = true;

        try {
            textureObj.loadTexture(theResourceManager);
        } catch (IOException ioexception) {
            logger.warn("Failed to load texture: " + textureLocation, ioexception);
            textureObj = TextureUtil.missingTexture;
            mapTextureObjects.put(textureLocation, textureObj);
            flag = false;
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Registering texture");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Resource location being registered");
            crashreportcategory.addCrashSection("Resource location", textureLocation);
            String name = textureObj.getClass().getName();
            crashreportcategory.addCrashSectionCallable("Texture object class", () -> name);
            throw new ReportedException(crashreport);
        }

        mapTextureObjects.put(textureLocation, textureObj);
        return flag;
    }

    public ResourceLocation getDynamicTextureLocation(String name, DynamicTexture texture, Map<String, Integer> mapTextureCounters) {
        Integer integer = (Integer) mapTextureCounters.get(name);

        if (integer == null) {
            integer = 1;
        } else {
            integer = integer + 1;
        }

        mapTextureCounters.put(name, integer);
        String format = String.format("dynamic/%s_%d", name, integer);
        ResourceLocation resourcelocation = new ResourceLocation(format);
        textures.put(resourcelocation.toString(), texture);
        parent.loadTexture(resourcelocation, texture);
        return resourcelocation;
    }


}
