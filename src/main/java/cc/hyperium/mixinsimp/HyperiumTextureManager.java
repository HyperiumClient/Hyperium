package cc.hyperium.mixinsimp;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.WorldChangeEvent;
import cc.hyperium.handlers.handlers.animation.cape.CapeHandler;
import cc.hyperium.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HyperiumTextureManager {


    private TextureManager parent;


    private HashMap<String, ITextureObject> textures = new HashMap<>();

    public HyperiumTextureManager(TextureManager parent) {
        this.parent = parent;
        EventBus.INSTANCE.register(this);
    }

    public boolean loadTickableTexture(ResourceLocation textureLocation, ITickableTextureObject textureObj, List<ITickable> listTickables) {
        if (parent.loadTexture(textureLocation, textureObj)) {
            listTickables.add(textureObj);
            textures.put(textureLocation.toString(), textureObj);
            return true;
        } else {
            return false;
        }
    }

    public void onResourceManagerReload(IResourceManager resourceManager) {
        CapeHandler.LOCK.lock();
        try {
            for (Map.Entry<String, ITextureObject> entry : textures.entrySet()) {
                String key = entry.getKey();
                ResourceLocation location;
                if (key.contains(":")) {
                    String[] split = key.split(":");
                    location = new ResourceLocation(split[0], split[1]);
                } else location = new ResourceLocation(key);
                parent.loadTexture(location, entry.getValue());
            }
            Utils.INSTANCE.setCursor(new ResourceLocation("textures/cursor.png"));
        } catch (Exception e) {

        } finally {
            CapeHandler.LOCK.unlock();
        }
    }

    public boolean loadTexture(ResourceLocation textureLocation, ITextureObject textureObj, IResourceManager theResourceManager, Logger logger) {
        ITextureObject textureCopy = textures.get(textureLocation.toString());
        if (textureCopy != null) {
            textureObj = textureCopy;
        }
        boolean flag = true;

        try {
            textureObj.loadTexture(theResourceManager);
        } catch (IOException ioexception) {
            logger.warn("Failed to load texture: " + textureLocation, ioexception);
            textureObj = TextureUtil.missingTexture;
            textures.put(textureLocation.toString(), textureObj);
            flag = false;
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Registering texture");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Resource location being registered");
            crashreportcategory.addCrashSection("Resource location", textureLocation);
            String name = textureObj.getClass().getName();
            crashreportcategory.addCrashSectionCallable("Texture object class", () -> name);
            throw new ReportedException(crashreport);
        }

        textures.put(textureLocation.toString(), textureObj);
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

    public void bindTexture(ResourceLocation resource) {
        ITextureObject itextureobject = (ITextureObject) textures.get(resource.toString());

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
        return (ITextureObject) textures.get(textureLocation.toString());
    }

    public void tick(List<ITickable> listTickables) {
        for (ITickable itickable : listTickables) {
            itickable.tick();
        }
    }

    @InvokeEvent
    public void worldSwitch(WorldChangeEvent event) {
        WorldClient theWorld = Minecraft.getMinecraft().theWorld;
        if (theWorld != null) {
            for (EntityPlayer playerEntity : theWorld.playerEntities) {
                ResourceLocation locationSkin = ((AbstractClientPlayer) playerEntity).getLocationSkin();
                if (locationSkin != null) {
                    System.out.println("Deleting skin for " + playerEntity.getName());
                    deleteTexture(locationSkin);
                }
                ResourceLocation locationCape = ((AbstractClientPlayer) playerEntity).getLocationCape();
                if (locationCape != null) {
                    CapeHandler capeHandler = Hyperium.INSTANCE.getHandlers().getCapeHandler();
                    ResourceLocation cape = capeHandler.getCape(((AbstractClientPlayer) playerEntity));
                    if (cape != null && cape.equals(locationCape))
                        return;
                    System.out.println("Deleting cape for " + playerEntity.getName());
                    deleteTexture(locationCape);
                }
            }
        }
    }

    public void deleteTexture(ResourceLocation textureLocation) {
        ITextureObject itextureobject = this.getTexture(textureLocation);

        if (itextureobject != null) {
            TextureUtil.deleteTexture(itextureobject.getGlTextureId());
            ITextureObject remove = textures.remove(textureLocation.toString());
            if (remove != null) {
                System.out.println("Successfully deleted: " + textureLocation.toString());
            } else System.out.println("Failed to delete: " + textureLocation.toString());
        }
    }

}
