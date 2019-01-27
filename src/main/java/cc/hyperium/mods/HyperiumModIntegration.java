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

import cc.hyperium.mods.autofriend.AutofriendMod;
import cc.hyperium.mods.autogg.AutoGG;
import cc.hyperium.mods.blockoverlay.BlockOverlay;
import cc.hyperium.mods.browser.BrowserMod;
import cc.hyperium.mods.chromahud.ChromaHUD;
import cc.hyperium.mods.chunkanimator.ChunkAnimator;
import cc.hyperium.mods.fortnitecompass.FortniteCompassMod;
import cc.hyperium.mods.glintcolorizer.GlintColorizer;
import cc.hyperium.mods.hgames.HGames;
import cc.hyperium.mods.itemphysic.ItemPhysicMod;
import cc.hyperium.mods.keystrokes.KeystrokesMod;
import cc.hyperium.mods.levelhead.Levelhead;
import cc.hyperium.mods.motionblur.MotionBlurMod;
import cc.hyperium.mods.nickhider.NickHider;
import cc.hyperium.mods.oldanimations.OldAnimations;
import cc.hyperium.mods.playtime.PlayTime;
import cc.hyperium.mods.spotify.SpotifyControls;
import cc.hyperium.mods.tabtoggle.TabToggleMod;
import cc.hyperium.mods.timechanger.TimeChanger;
import cc.hyperium.mods.togglechat.ToggleChatMod;
import cc.hyperium.mods.victoryroyale.VictoryRoyale;
import me.semx11.autotip.Autotip;

/**
 * Basic inbuilt mod handler, including many community mods such as ChromaHUD, LevelHead and
 * ToggleChat
 */
public class HyperiumModIntegration {
    private final KeystrokesMod keystrokesMod;
    private final TimeChanger timeChanger;
    private final ToggleChatMod toggleChat;
    private final Levelhead levelhead;
    private final ChromaHUD chromaHUD;
    private final Autotip autotip;
    private final AutoGG autogg;
    private final HGames hgames;
    private final GlintColorizer glintcolorizer;
    private final BlockOverlay blockOverlay;
    private final SpotifyControls spotifyControls;
    private final MotionBlurMod motionBlur;
    private final OldAnimations oldanimations;
    private final AutofriendMod autofriend;
    private final PlayTime playTime;
    private final FortniteCompassMod fncompass;
    private final TabToggleMod tabToggle;
    private final ItemPhysicMod itemPhysicMod;
    private final BrowserMod browserMod;
    private final VictoryRoyale victoryRoyale;
    private final ChunkAnimator chunkAnimator;

    public HyperiumModIntegration() {
        // ChromaHUD implementation
        this.chromaHUD = ((ChromaHUD) new ChromaHUD().init());

        // LevelHead implementation
        this.levelhead = ((Levelhead) new Levelhead().init());

        // ToggleChat implementation
        this.toggleChat = ((ToggleChatMod) new ToggleChatMod().init());

        // TimeChanger implementation
        this.timeChanger = ((TimeChanger) new TimeChanger().init());

        // KeystrokesMod implementation
        this.keystrokesMod = ((KeystrokesMod) new KeystrokesMod().init());

        // Autotip implementation
        this.autotip = new Autotip();
        autotip.init();

        // AutoGG implementation
        this.autogg = ((AutoGG) new AutoGG().init());

        // HGames implementation
        this.hgames = ((HGames) new HGames().init());

        // Old Animations implementation

        this.oldanimations = ((OldAnimations) new OldAnimations().init());

        // Block Overlay implementation
        this.blockOverlay = ((BlockOverlay) new BlockOverlay().init());

        // Spotify Controls Implementation
        this.spotifyControls = ((SpotifyControls) new SpotifyControls().init());

        //Motion Blur
        this.motionBlur = ((MotionBlurMod) new MotionBlurMod().init());

        // Glint Colorizer implementation
        this.glintcolorizer = ((GlintColorizer) new GlintColorizer().init());
        NickHider nickHider = new NickHider();
        nickHider.init();

        //Autofriend implementation
        this.autofriend = ((AutofriendMod) new AutofriendMod().init());

        //Play Time addon port
        this.playTime = (PlayTime) new PlayTime().init();

        this.fncompass = (FortniteCompassMod) new FortniteCompassMod().init();

        this.tabToggle = (TabToggleMod) new TabToggleMod().init();

        this.itemPhysicMod = (ItemPhysicMod) new ItemPhysicMod().init();

        this.browserMod = (BrowserMod) new BrowserMod().init();

        this.victoryRoyale = (VictoryRoyale) new VictoryRoyale().init();

        this.chunkAnimator = (ChunkAnimator) new ChunkAnimator().init();
    }

    public KeystrokesMod getKeystrokesMod() {
        return keystrokesMod;
    }

    public TimeChanger getTimeChanger() {
        return timeChanger;
    }

    public ToggleChatMod getToggleChat() {
        return toggleChat;
    }

    public Levelhead getLevelhead() {
        return levelhead;
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

    public AutofriendMod getAutofriend() {
        return autofriend;
    }

    public PlayTime getPlayTime() {
        return playTime;
    }

    public FortniteCompassMod getFncompass() {
        return fncompass;
    }

    public TabToggleMod getTabToggle() {
        return tabToggle;
    }

    public ItemPhysicMod getItemPhysicMod() {
        return itemPhysicMod;
    }

    public BrowserMod getBrowserMod() {
        return browserMod;
    }

    public VictoryRoyale getVictoryRoyale() {
        return victoryRoyale;
    }

    public ChunkAnimator getChunkAnimator() {
        return chunkAnimator;
    }
}
