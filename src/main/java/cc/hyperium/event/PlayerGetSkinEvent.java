package cc.hyperium.event;

import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;

import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Invoked when a players skin is grabbed from the game
 * code, this allows easy modification of the players skin
 */
public final class PlayerGetSkinEvent extends Event {

    @NotNull
    private final GameProfile profile;
    @Nullable
    private ResourceLocation skin;

    public PlayerGetSkinEvent(@NotNull GameProfile profile, @Nullable ResourceLocation skin) {
        Preconditions.checkNotNull(profile, "profile");

        this.profile = profile;
        this.skin = skin;
    }

    @NotNull
    public final GameProfile getProfile() {
        return this.profile;
    }

    @Nullable
    public final ResourceLocation getSkin() {
        return this.skin;
    }

    public final void setSkin(@Nullable ResourceLocation var1) {
        this.skin = var1;
    }
}
