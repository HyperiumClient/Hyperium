/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hcc.mixins.entity;

import com.hcc.event.EventBus;
import com.hcc.event.PlayerGetCapeEvent;

import com.hcc.event.PlayerGetSkinEvent;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(NetworkPlayerInfo.class)
public abstract class MixinNetworkPlayerInfo {
    
    @Shadow
    @Final
    private GameProfile gameProfile;
    
    @Shadow private ResourceLocation locationSkin;
    @Shadow private ResourceLocation locationCape;
    
    private EntityPlayer thePlayer;
    
    /**
     * Allow for better cape customization
     *
     * @author boomboompower
     */
    @Overwrite
    public ResourceLocation getLocationCape() {
        ResourceLocation cape = this.locationCape;
        
        if (cape == null) {
            this.loadPlayerTextures();
        }
        
        // Fix the NPE occurs inside the event
        if (getPlayer() == null) {
            return cape;
        }
    
        PlayerGetCapeEvent event = new PlayerGetCapeEvent(getPlayer(), cape);
        
        EventBus.INSTANCE.post(event);
        
        cape = event.getCape();
        
        return this.locationCape = cape;
    }
    
    /**
     * Allow for better skin customization
     *
     * @author boomboompower
     */
    @Overwrite
    public ResourceLocation getLocationSkin() {
        ResourceLocation skin = this.locationSkin;
    
        if (skin == null) {
            this.loadPlayerTextures();
        }
        
        if (getPlayer() == null) {
            return normalizeSkin(skin);
        }
    
        PlayerGetSkinEvent event = new PlayerGetSkinEvent(getPlayer(), skin);
    
        EventBus.INSTANCE.post(event);
    
        skin = event.getSkin();
    
        return this.locationSkin = normalizeSkin(skin);
    }
    
    private EntityPlayer getPlayer() {
        if (this.thePlayer == null) {
            try {
                this.thePlayer = Minecraft.getMinecraft().theWorld.getPlayerEntityByUUID(this.gameProfile.getId());
            } catch (NullPointerException ex) {
                ex.printStackTrace();
                // These things can happen, right??!?!?!
            }
        }
        
        return this.thePlayer;
    }
    
    private ResourceLocation normalizeSkin(ResourceLocation skin) {
        return (skin != null ? skin : DefaultPlayerSkin.getDefaultSkin(this.gameProfile.getId()));
    }
    
    @Shadow protected abstract void loadPlayerTextures();
}