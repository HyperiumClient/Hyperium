package cc.hyperium.mixinsimp.entity;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.PlayerGetCapeEvent;
import cc.hyperium.event.PlayerGetSkinEvent;
import cc.hyperium.mixins.entity.IMixinNetworkPlayerInfo;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;

public class HyperiumNetworkPlayerInfo {
    private NetworkPlayerInfo parent;

    public HyperiumNetworkPlayerInfo(NetworkPlayerInfo parent) {
        this.parent = parent;
    }

    public ResourceLocation getLocationCape(GameProfile gameProfile, ResourceLocation locationCape) {
        ResourceLocation cape = locationCape;

        if (cape == null) {
            ((IMixinNetworkPlayerInfo) parent).callLoadPlayerTextures();
        }

        PlayerGetCapeEvent event = new PlayerGetCapeEvent(gameProfile, cape);

        EventBus.INSTANCE.post(event);

        cape = event.getCape();

        ((IMixinNetworkPlayerInfo) parent).setLocationCape(cape);
        return cape;
    }

    public ResourceLocation getLocationSkin(GameProfile gameProfile, ResourceLocation locationSkin) {
        ResourceLocation skin = locationSkin;

        if (skin == null) {
            ((IMixinNetworkPlayerInfo) parent).callLoadPlayerTextures();
        }

        PlayerGetSkinEvent event = new PlayerGetSkinEvent(gameProfile, skin);

        EventBus.INSTANCE.post(event);

        skin = event.getSkin();

        ResourceLocation normalizedSkin = normalizeSkin(skin, gameProfile);
        ((IMixinNetworkPlayerInfo) parent).setLocationSkin(normalizedSkin);
        return normalizedSkin;
    }

    private ResourceLocation normalizeSkin(ResourceLocation skin, GameProfile gameProfile) {
        return (skin != null ? skin : DefaultPlayerSkin.getDefaultSkin(gameProfile.getId()));
    }
}
