package cc.hyperium.event;

import com.mojang.authlib.GameProfile;

import com.google.common.base.Preconditions;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Invoked when a players cape is grabbed from the game
 * code, this allows easy modification of that cape
 */
public final class PlayerGetCapeEvent extends Event {

    @NotNull
    private final GameProfile profile;

    @Nullable
    private ResourceLocation cape;

    public PlayerGetCapeEvent(@NotNull GameProfile profile, @Nullable ResourceLocation cape) {
        Preconditions.checkNotNull(profile, "profile");

        this.profile = profile;
        this.cape = cape;
    }

    @NotNull
    public final GameProfile getProfile() {
        return this.profile;
    }

    @Nullable
    public final ResourceLocation getCape() {
        return this.cape;
    }

    public final void setCape(@Nullable ResourceLocation capeIn) {
        this.cape = capeIn;
    }
}
