package cc.hyperium.mixins.client.renderer.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.*;
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
import java.util.List;
import java.util.Map;

@Mixin(TextureManager.class)
public abstract class MixinTextureManager {


    @Shadow @Final private List<ITickable> listTickables;
    @Shadow @Final private static Logger logger;
    @Shadow @Final private Map<ResourceLocation, ITextureObject> mapTextureObjects;

    @Shadow
    @Final
    private Map<String, Integer> mapTextureCounters;
    private HashMap<String, ITextureObject> textures = new HashMap<>();

    /**
     * @author asbyth
     * @reason fix missing textures
     */
    @Overwrite
    public boolean loadTickableTexture(ResourceLocation resourceLocation, ITickableTextureObject textureObject) {
        if (loadTexture(resourceLocation, textureObject)) {
            listTickables.add(textureObject);
            textures.put(resourceLocation.toString(), textureObject);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @author asbyth
     * @reason fix missing textures
     */
    @Overwrite
    public boolean loadTexture(ResourceLocation textureLocation, ITextureObject textureObject) {
        boolean loaded = true;

        ITextureObject textureCopy = textures.get(textureLocation.toString());
        if (textureCopy != null) textureObject = textureCopy;

        try {
            textureObject.loadTexture(Minecraft.getMinecraft().getResourceManager());
        } catch (IOException e) {
            logger.warn("Failed to load texture {}", textureLocation, e);
            textureObject = TextureUtil.missingTexture;
            mapTextureObjects.put(textureLocation, textureObject);
            loaded = false;
        } catch (Throwable e) {
            ITextureObject object = textureObject;
            CrashReport report = CrashReport.makeCrashReport(e, "Registering texture");
            CrashReportCategory category = report.makeCategory("Resource location being registered");
            category.addCrashSection("Resource location", textureLocation);
            category.addCrashSectionCallable("Texture object class", () -> object.getClass().getName());
            throw new ReportedException(report);
        }

        mapTextureObjects.put(textureLocation, textureObject);
        return loaded;
    }

    /**
     * @author asbyth
     * @reason Fix dynamic missing textures
     */
    @Overwrite
    public ResourceLocation getDynamicTextureLocation(String name, DynamicTexture texture) {
        Integer integer = mapTextureCounters.get(name);

        if (integer == null) integer = 1;
        else integer = integer + 1;

        mapTextureCounters.put(name, integer);
        String format = String.format("dynamic/%s_%d", name, integer);
        ResourceLocation resourceLocation = new ResourceLocation(format);
        textures.put(resourceLocation.toString(), texture);
        loadTexture(resourceLocation, texture);
        return resourceLocation;
    }
}
