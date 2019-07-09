package cc.hyperium.event;

import com.google.common.base.Preconditions;
import net.minecraft.client.audio.ISound;
import org.jetbrains.annotations.NotNull;

public final class SoundPlayEvent extends CancellableEvent {

    @NotNull
    private final ISound sound;

    public SoundPlayEvent(@NotNull ISound sound) {
        Preconditions.checkNotNull(sound, "sound");

        this.sound = sound;
    }

    @NotNull
    public final ISound getSound() {
        return this.sound;
    }
}
