package cc.hyperium.handlers.handlers.keybinds.keybinds;

import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import cc.hyperium.integrations.spotify.Spotify;
import org.lwjgl.input.Keyboard;

public class ToggleSpotifyKeybind extends HyperiumBind {

    public ToggleSpotifyKeybind() {
        super("Toggle Spotify", Keyboard.KEY_PERIOD);
    }

    @Override
    public void onPress() {
        if (Spotify.instance == null)
            return;
        Spotify.instance.pause(!Spotify.instance.getCachedStatus().isPlaying());
    }
}
