package cc.hyperium.mods.motionblur.resource;

import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class MotionBlurResourceManager implements IResourceManager {
    public MotionBlurResourceManager() {
    }

    public Set<String> getResourceDomains() {
        return null;
    }

    public IResource getResource(ResourceLocation location) throws IOException {
        return new MotionBlurResource();
    }

    public List<IResource> getAllResources(ResourceLocation location) throws IOException {
        return null;
    }
}
