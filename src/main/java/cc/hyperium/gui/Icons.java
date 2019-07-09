package cc.hyperium.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

/*
 * Created by Cubxity on 01/06/2018
 */
public enum Icons {

    SETTINGS(new ResourceLocation("textures/material/settings.png")),
    EXTENSION(new ResourceLocation("textures/material/extension.png")),
    ERROR(new ResourceLocation("textures/material/error.png")),
    DOWNLOAD(new ResourceLocation("textures/material/download.png")),
    EXIT(new ResourceLocation("textures/material/exit.png")),
    CLOSE(new ResourceLocation("textures/material/close.png")),
    ARROW_DOWN(new ResourceLocation("textures/material/arrow_down.png")),
    ARROW_DOWN_ALT(new ResourceLocation("textures/material/arrow_down_alt.png")),
    ARROW_UP_ALT(new ResourceLocation("textures/material/arrow_up_alt.png")),
    ARROW_LEFT(new ResourceLocation("textures/material/arrow_left.png")),
    ARROW_RIGHT(new ResourceLocation("textures/material/arrow_right.png")),
    LIGHTBULB(new ResourceLocation("textures/material/lightbulb.png")),
    LIGHTBULB_SOLID(new ResourceLocation("textures/material/lightbulb-solid.png"));

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
