package cc.hyperium.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

/*
 * Created by Cubxity on 01/06/2018
 */
public enum Icons {

    HOME(new ResourceLocation("textures/material/home.png")),
    SETTINGS(new ResourceLocation("textures/material/settings.png")),
    EXTENSION(new ResourceLocation("textures/material/extension.png")),
    INFO(new ResourceLocation("textures/material/info.png")),
    ERROR(new ResourceLocation("textures/material/error.png")),
    DOWNLOAD(new ResourceLocation("textures/material/download.png")),
    TOOL(new ResourceLocation("textures/material/tools.png")),
    COSMETIC(new ResourceLocation("textures/material/cosmetic.png")),
    SPOTIFY(new ResourceLocation("textures/material/spotify.png"));



    private ResourceLocation res;

    Icons(ResourceLocation res) {
        this.res = res;
    }

    public void bind() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(res);
    }

    public ResourceLocation getResource() {
        return res;
    }

}
