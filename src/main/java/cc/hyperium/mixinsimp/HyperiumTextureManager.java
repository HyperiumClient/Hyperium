package cc.hyperium.mixinsimp;

import cc.hyperium.handlers.handlers.animation.cape.CapeHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HyperiumTextureManager {

    public static HyperiumTextureManager INSTANCE;
    private TextureManager parent;
    private ConcurrentHashMap<ResourceLocation, ITextureObject> textures = new ConcurrentHashMap<>(16, 0.9f, 1);

    public HyperiumTextureManager(TextureManager parent) {
        this.parent = parent;
    }

    public boolean loadTickableTexture(ResourceLocation textureLocation, ITickableTextureObject textureObj, List<ITickable> listTickables) {
        if (parent.loadTexture(textureLocation, textureObj)) {
            listTickables.add(textureObj);
            textures.put(textureLocation, textureObj);
            return true;
        } else {
            return false;
        }
    }

    public void onResourceManagerReload() {
        CapeHandler.LOCK.lock();
        try {
            for (Map.Entry<ResourceLocation, ITextureObject> entry : textures.entrySet()) {
                ResourceLocation key = entry.getKey();
                ResourceLocation location;

                if (key.getResourcePath().contains(":")) {
                    String[] split = key.getResourcePath().split(":");
                    location = new ResourceLocation(split[0], split[1]);
                } else location = new ResourceLocation(key.getResourcePath());

                parent.loadTexture(location, entry.getValue());
            }
        } finally {
            CapeHandler.LOCK.unlock();
        }
    }

    public boolean loadTexture(ResourceLocation textureLocation, ITextureObject textureObj, IResourceManager theResourceManager, Logger logger) {
        ITextureObject textureCopy = textures.get(textureLocation);
        if (textureCopy != null) {
            textureObj = textureCopy;
        }
        boolean flag = true;

        try {
            textureObj.loadTexture(theResourceManager);
        } catch (IOException ioexception) {
            logger.warn("Failed to load texture: " + textureLocation, ioexception);
            textureObj = TextureUtil.missingTexture;
            textures.put(textureLocation, textureObj);
            flag = false;
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Registering texture");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Resource location being registered");
            crashreportcategory.addCrashSection("Resource location", textureLocation);
            String name = textureObj.getClass().getName();
            crashreportcategory.addCrashSectionCallable("Texture object class", () -> name);
            throw new ReportedException(crashreport);
        }

        textures.put(textureLocation, textureObj);
        return flag;
    }

    public ResourceLocation getDynamicTextureLocation(String name, DynamicTexture texture, Map<String, Integer> mapTextureCounters) {
        Integer integer = mapTextureCounters.get(name);

        if (integer == null) {
            integer = 1;
        } else {
            integer = integer + 1;
        }

        mapTextureCounters.put(name, integer);
        String format = String.format("dynamic/%s_%d", name, integer);
        ResourceLocation resourcelocation = new ResourceLocation(format);
        textures.put(resourcelocation, texture);
        parent.loadTexture(resourcelocation, texture);
        return resourcelocation;
    }

    public void bindTexture(ResourceLocation resource) {
        ITextureObject itextureobject = textures.get(resource);

        if (itextureobject == null) {
            itextureobject = new SimpleTexture(resource);
            parent.loadTexture(resource, itextureobject);
        }

        GlStateManager.bindTexture(itextureobject.getGlTextureId());
    }


    public ITextureObject getTexture(ResourceLocation textureLocation) {
        if (textureLocation == null) {
            return null;
        }
        return textures.get(textureLocation);
    }

    //TODO make this a process them async
    public void tick(List<ITickable> listTickables) {
        for (ITickable itickable : listTickables) {
            itickable.tick();
        }
    }

    public void print() {
        for (ResourceLocation s : textures.keySet()) {
            System.out.println(s.getResourcePath() + " -> " + textures.get(s).getClass());
        }
    }

    public void deleteTexture(ResourceLocation textureLocation) {
        ITextureObject itextureobject = this.getTexture(textureLocation);
        if (itextureobject != null) {
            TextureUtil.deleteTexture(itextureobject.getGlTextureId());
        }
        if (textureLocation != null)
            textures.remove(textureLocation);
    }
}
