package cc.hyperium.handlers.handlers.animation.cape;

import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class DynamicCape implements ICape {


    private List<ResourceLocation> locations;
    private double totalDuration;
    private int totalFrames;
    private int frame;

    public DynamicCape(List<ResourceLocation> locations, double totalDuration, int totalFrames) {
        System.out.println("Created");
        this.locations = locations;
        this.totalDuration = totalDuration;
        this.totalFrames = totalFrames;
    }

    public List<ResourceLocation> getLocations() {
        return locations;
    }

    public double getTotalDuration() {
        return totalDuration;
    }

    public int getTotalFrames() {
        return totalFrames;
    }

    @Override
    public ResourceLocation get() {
        frame++;
        if (frame >= locations.size()-1)
            frame = 0;
        return locations.get(frame);
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
