package com.hcc.mixins.client.resources;

import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.IResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(AbstractResourcePack.class)
public abstract class MixinAbstractResourcePack implements IResourcePack {

    /**
    * @author prplz
     */


    private static final int IconSize = 64;

    @Overwrite
    protected abstract InputStream getInputStreamByName(String name) throws IOException;

    @Overwrite
    public BufferedImage getPackImage() throws IOException
    {
        BufferedImage originalIcon = TextureUtil.readBufferedImage(this.getInputStreamByName("pack.png"));
        if (originalIcon == null) {
            return null;
        }
        BufferedImage resizedIcon = new BufferedImage(IconSize, IconSize, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = resizedIcon.getGraphics();
        graphics.drawImage(originalIcon, 0, 0, IconSize, IconSize, null);
        graphics.dispose();
        return resizedIcon;
    }
}
