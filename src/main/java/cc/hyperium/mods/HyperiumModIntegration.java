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

package cc.hyperium.mods;

import cc.hyperium.mods.autogg.AutoGG;
import cc.hyperium.mods.blockoverlay.BlockOverlay;
import cc.hyperium.mods.chromahud.ChromaHUD;
import cc.hyperium.mods.glintcolorizer.GlintColorizer;
import cc.hyperium.mods.hgames.HGames;
import cc.hyperium.mods.keystrokes.KeystrokesMod;
import cc.hyperium.mods.killscreenshot.KillScreenshot;
import cc.hyperium.mods.levelhead.Levelhead;
import cc.hyperium.mods.motionblur.MotionBlurMod;
import cc.hyperium.mods.oldanimations.OldAnimations;
import cc.hyperium.mods.skinchanger.SkinChangerMod;
import cc.hyperium.mods.spotify.SpotifyControls;
import cc.hyperium.mods.timechanger.TimeChanger;
import cc.hyperium.mods.togglechat.ToggleChatMod;
import cc.hyperium.mods.utilities.UtilitiesMod;
import me.semx11.autotip.Autotip;

/**
 * Basic inbuilt mod handler, including many community mods such as
 * ChromaHUD, LevelHead and ToggleChat
 */
public class HyperiumModIntegration {

    private final KeystrokesMod keystrokesMod;
    private final TimeChanger timeChanger;
    private final SkinChangerMod skinChanger;
    private final ToggleChatMod toggleChat;
    private final UtilitiesMod utilities;
    private final Levelhead levelhead;
    private final KillScreenshot killScreenshot;
    private final ChromaHUD chromaHUD;
    private final Autotip autotip;
    private final AutoGG autogg;
    private final HGames hgames;
    private final GlintColorizer glintcolorizer;
    private final BlockOverlay blockOverlay;
    private final SpotifyControls spotifyControls;
    private final MotionBlurMod motionBlur;
    private final OldAnimations oldanimations;

    public HyperiumModIntegration() {
        // ChromaHud implementation
        this.chromaHUD = ((ChromaHUD) new ChromaHUD().init());

        // LevelHead implementation
        this.levelhead = ((Levelhead) new Levelhead().init());

        // Utilities implementation
        this.utilities = ((UtilitiesMod) new UtilitiesMod().init());

        // ToggleChat implementation
        this.toggleChat = ((ToggleChatMod) new ToggleChatMod().init());

        // SkinChanger implementation
        this.skinChanger = ((SkinChangerMod) new SkinChangerMod().init());

        // TimeChanger implementation
        this.timeChanger = ((TimeChanger) new TimeChanger().init());

        // KeystrokesMod implementation
        this.keystrokesMod = ((KeystrokesMod) new KeystrokesMod().init());

        // Autotip implementation
        this.autotip = new Autotip().init();

        // AutoGG implementation
        this.autogg = ((AutoGG) new AutoGG().init());

        // HGames implementation
        this.hgames = ((HGames) new HGames().init());

        // Old Animations implementation

        this.oldanimations = ((OldAnimations) new OldAnimations().init());

        // Block Overlay implementation
        this.blockOverlay = ((BlockOverlay) new BlockOverlay().init());

        // Kill Screenshot implementation
        this.killScreenshot = ((KillScreenshot) new KillScreenshot().init());

        // Spotify Controls Implementation
        this.spotifyControls = ((SpotifyControls) new SpotifyControls().init());

        //Motion Blur
        this.motionBlur = ((MotionBlurMod) new MotionBlurMod().init());

        // Glint Colorizer implementation
        this.glintcolorizer = ((GlintColorizer) new GlintColorizer().init());


    }

    public KeystrokesMod getKeystrokesMod() {
        return keystrokesMod;
    }

    public TimeChanger getTimeChanger() {
        return timeChanger;
    }

    public SkinChangerMod getSkinChanger() {
        return skinChanger;
    }

    public ToggleChatMod getToggleChat() {
        return toggleChat;
    }

    public UtilitiesMod getUtilities() {
        return utilities;
    }

    public Levelhead getLevelhead() {
        return levelhead;
    }

    public KillScreenshot getKillScreenshot() {
        return killScreenshot;
    }

    public ChromaHUD getChromaHUD() {
        return chromaHUD;
    }

    public Autotip getAutotip() {
        return autotip;
    }

    public AutoGG getAutoGG() {
        return autogg;
    }

    public HGames getHGames() {
        return hgames;
    }

    public GlintColorizer getGlintcolorizer() {
        return glintcolorizer;
    }

    public BlockOverlay getBlockOverlay() {
        return blockOverlay;
    }

    public SpotifyControls getSpotifyControls() {
        return spotifyControls;
    }

    public MotionBlurMod getMotionBlur() {
        return motionBlur;
    }

    public OldAnimations getOldanimations() {
        return oldanimations;
    }


}
