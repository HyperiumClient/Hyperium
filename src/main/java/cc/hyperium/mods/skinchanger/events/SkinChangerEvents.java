/*
 *     Copyright (C) 2017 boomboompower
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods.skinchanger.events;

import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.PlayerGetCapeEvent;
import cc.hyperium.event.PlayerGetSkinEvent;
import cc.hyperium.mods.skinchanger.SkinChangerMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.UUID;

public class SkinChangerEvents {
    
    private final SkinChangerMod mod;
    
    public SkinChangerEvents(SkinChangerMod theMod) {
        this.mod = theMod;
    }
    
    @InvokeEvent
    public void onGetSkin(PlayerGetSkinEvent event) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            String name = this.mod.getConfig().getSkinName();
            
            if (name != null && !name.isEmpty()) {
                ResourceLocation loc = getSkin(name);
                
                if (loc != null) {
                    event.setSkin(loc);
                }
            }
        });
    }
    
    @InvokeEvent
    public void onGetCape(PlayerGetCapeEvent event) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            if (!this.mod.getConfig().isUsingCape()) return;
    
            String name = this.mod.getConfig().getOfCapeName();
    
            if (name != null && !name.isEmpty()) {
                ResourceLocation loc = getOfCape(name);
        
                if (loc != null) {
                    event.setCape(loc);
                }
            }
        });
    }
    
    private ResourceLocation getSkin(String name) {
        if (name != null && !name.isEmpty()) {
            final ResourceLocation location = new ResourceLocation("skins/" + name);
        
            File file1 = new File(new File(Hyperium.folder, "skins"), UUID.nameUUIDFromBytes(name.getBytes()).toString().substring(0, 2));
            File file2 = new File(file1, UUID.nameUUIDFromBytes(name.getBytes()).toString() + ".png");
            
            // Rely on the skin cache to work its magic
            if (!file2.exists()) {
                return null;
            }
        
            final IImageBuffer imageBuffer = new ImageBufferDownload();
            ThreadDownloadImageData imageData = new ThreadDownloadImageData(file2, String.format("https://minotar.net/skin/%s", name), DefaultPlayerSkin.getDefaultSkinLegacy(), new IImageBuffer() {
                @Override
                public BufferedImage parseUserSkin(BufferedImage image) {
                    return imageBuffer.parseUserSkin(image);
                }
            
                @Override
                public void skinAvailable() {
                    imageBuffer.skinAvailable();
                }
            });
            Minecraft.getMinecraft().getTextureManager().loadTexture(location, imageData);
            return location;
        } else {
            return null;
        }
    }
    
    protected ResourceLocation getOfCape(String name) {
        if (name != null && !name.isEmpty()) {
            final String url = "http://s.optifine.net/capes/" + name + ".png";
        
            final String id = UUID.nameUUIDFromBytes(name.getBytes()).toString();
        
            final ResourceLocation rl = new ResourceLocation("ofcape/" + id);
        
            File file1 = new File(new File(Hyperium.folder, "ofcape"), id);
            File file2 = new File(file1, id + ".png");
        
            // Rely on the cache
            if (!file2.exists()) {
                return null;
            }
        
            TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        
            IImageBuffer imageBuffer = new IImageBuffer() {
                @Override
                public BufferedImage parseUserSkin(BufferedImage img) {
                    int imageWidth = 64;
                    int imageHeight = 32;
                    int srcWidth = img.getWidth();
                
                    for (int srcHeight = img.getHeight(); imageWidth < srcWidth || imageHeight < srcHeight; imageHeight *= 2) {
                        imageWidth *= 2;
                    }
                
                    BufferedImage imgNew = new BufferedImage(imageWidth, imageHeight, 2);
                    Graphics g = imgNew.getGraphics();
                    g.drawImage(img, 0, 0, null);
                    g.dispose();
                    return imgNew;
                }
            
                @Override
                public void skinAvailable() {
                }
            };
            textureManager.loadTexture(rl, new ThreadDownloadImageData(file2, url, null, imageBuffer));
        
            return rl;
        } else {
            return null;
        }
    }
}
