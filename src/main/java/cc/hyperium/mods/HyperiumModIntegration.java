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

    private final AbstractMod keystrokesMod;
    private final AbstractMod timeChanger;
    private final AbstractMod skinChanger;
    private final AbstractMod toggleChat;
    private final AbstractMod utilities;
    private final AbstractMod levelhead;
    private final AbstractMod killScreenshot;
    private final AbstractMod motionBlurMod;

    private final AbstractMod chromaHUD;
    private final AbstractMod autotip;
    private final AbstractMod autogg;
    private final AbstractMod hgames;
    private final AbstractMod glintcolorizer;
    private AbstractMod blockOverlay;
    private AbstractMod spotifyControls;
    private AbstractMod motionBlur;
    private AbstractMod oldanimations;

    public HyperiumModIntegration() {
        // ChromaHud implementation
        this.chromaHUD = new ChromaHUD().init();

        // LevelHead implementation
        this.levelhead = new Levelhead().init();

        // Utilities implementation
        this.utilities = new UtilitiesMod().init();

        // ToggleChat implementation
        this.toggleChat = new ToggleChatMod().init();

        // SkinChanger implementation
        this.skinChanger = new SkinChangerMod().init();

        // TimeChanger implementation
        this.timeChanger = new TimeChanger().init();

        // KeystrokesMod implementation
        this.keystrokesMod = new KeystrokesMod().init();

        // Autotip implementation
        this.autotip = new Autotip().init();

        // AutoGG implementation
        this.autogg = new AutoGG().init();

        // HGames implementation
        this.hgames = new HGames().init();

        // Old Animations implementation

        this.oldanimations = new OldAnimations().init();

        // Block Overlay implementation
        this.blockOverlay = new BlockOverlay().init();

        // Kill Screenshot implementation
        this.killScreenshot = new KillScreenshot().init();

        // Spotify Controls Implementation
        this.spotifyControls = new SpotifyControls().init();

        //Motion Blur
        this.motionBlur = new MotionBlurMod().init();

        // Glint Colorizer implementation
        this.glintcolorizer = new GlintColorizer().init();

        this.motionBlurMod = new MotionBlurMod().init();
    }

    public AbstractMod getMotionBlurMod() {
        return motionBlurMod;
    }

    public AbstractMod getAutogg() {
        return autogg;
    }

    public AbstractMod getHgames() {
        return hgames;
    }

    public AbstractMod getGlintcolorizer() {
        return glintcolorizer;
    }

    public AbstractMod getBlockOverlay() {
        return blockOverlay;
    }

    public AbstractMod getSpotifyControls() {
        return spotifyControls;
    }

    public AbstractMod getOldanimations() {
        return oldanimations;
    }

    /**
     * A getter for the running Autotip instance
     *
     * @return the running Autotip instance
     */
    public AbstractMod getAutotip() {
        return this.autotip;
    }

    /**
     * A getter for the running AutoGG instance
     *
     * @return the running AutoGG instance
     */
    public AbstractMod getAutoGG() {
        return this.autogg;
    }

    public AbstractMod getMotionBlur() {
        return this.motionBlurMod;
    }

    /**
     * A getter for the running HGames instance
     *
     * @return the running HGames instance
     */
    public AbstractMod getHGames() {
        return this.hgames;
    }

    /**
     * A getter for the running ChromeHUD instance
     *
     * @return the running ChromeHUD instance
     */
    public AbstractMod getChromaHUD() {
        return this.chromaHUD;
    }

    /**
     * A getter for the running LevelHead instance
     *
     * @return the running LevelHead instance
     */
    public AbstractMod getLevelhead() {
        return this.levelhead;
    }

    /**
     * A getter for the running Utilities instance
     *
     * @return the running Utilities instance
     */
    public AbstractMod getUtilities() {
        return this.utilities;
    }

    /**
     * A getter for the running TimeChanger instance
     *
     * @return the running TimeChanger instance
     */
    public AbstractMod getTimeChanger() {
        return timeChanger;
    }

    /**
     * A getter for the running SkinChanger instance
     *
     * @return the running SkinChanger instance
     */
    public AbstractMod getSkinChanger() {
        return this.skinChanger;
    }

    /**
     * A getter for the running ToggleChat instance
     *
     * @return the running ToggleChat instance
     */
    public AbstractMod getToggleChat() {
        return this.toggleChat;
    }

    /**
     * A getter for the running KeystrokesMod instance
     *
     * @return the running KeystrokesMod instance
     */
    public AbstractMod getKeystrokesMod() {
        return this.keystrokesMod;
    }

    /**
     * A getter for the running KillScreenshot instance
     *
     * @return the running KillScreenshot instance
     */
    public AbstractMod getKillScreenshot() {
        return this.killScreenshot;
    }

    /**
     * A getter for the running GlintColorizer instance
     *
     * @return the running GlintColorizer instance
     */
    public AbstractMod getGlintColorizer() {
        return this.glintcolorizer;
    }
}
