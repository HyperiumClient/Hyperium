package cc.hyperium.handlers.handlers.animation.cape;

import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class DynamicCape implements ICape {


    private List<ResourceLocation> locations;
    private double delay;
    private int totalFrames;
    private int frame;
    private int totalms;

    public DynamicCape(List<ResourceLocation> locations, int delay, int totalFrames) {
        this.locations = locations;
        this.delay = delay;
        this.totalFrames = totalFrames;
        //Gif delay is in 10's of ms
        totalms = delay * totalFrames;
    }

    public List<ResourceLocation> getLocations() {
        return locations;
    }


    @Override
    public ResourceLocation get() {
        if (totalms == 0)
            return null;
        double percent = (double) (System.currentTimeMillis() % totalms) / totalms;
        return locations.get((int) (percent * (double) totalFrames));
    }

    @Override
    public void delete(TextureManager manager) {
        for (ResourceLocation location : locations) {
            ITextureObject texture = manager.getTexture(location);
            if (texture instanceof CapeTexture) {
                //Unlink the buffered image so garbage collector can do its magic
                ((CapeTexture) texture).clearTextureData();
            }
            manager.deleteTexture(location);
        }

    }
}
