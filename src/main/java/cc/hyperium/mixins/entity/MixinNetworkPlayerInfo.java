/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mixins.entity;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.PlayerGetCapeEvent;
import cc.hyperium.event.PlayerGetSkinEvent;

import com.mojang.authlib.GameProfile;

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

    @Shadow
    private ResourceLocation locationSkin;
    @Shadow
    private ResourceLocation locationCape;

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

        PlayerGetCapeEvent event = new PlayerGetCapeEvent(this.gameProfile, cape);

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

        PlayerGetSkinEvent event = new PlayerGetSkinEvent(this.gameProfile, skin);

        EventBus.INSTANCE.post(event);

        skin = event.getSkin();

        return this.locationSkin = normalizeSkin(skin);
    }

    private ResourceLocation normalizeSkin(ResourceLocation skin) {
        return (skin != null ? skin : DefaultPlayerSkin.getDefaultSkin(this.gameProfile.getId()));
    }

    @Shadow
    protected abstract void loadPlayerTextures();
}