/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods;

import cc.hyperium.mods.autogg.AutoGG;
import cc.hyperium.mods.blockoverlay.BlockOverlay;
import cc.hyperium.mods.chromahud.ChromaHUD;
import cc.hyperium.mods.chunkanimator.ChunkAnimator;
import cc.hyperium.mods.common.SoundHandler;
import cc.hyperium.mods.entityradius.EntityRadius;
import cc.hyperium.mods.glintcolorizer.GlintColorizer;
import cc.hyperium.mods.itemphysic.ItemPhysicMod;
import cc.hyperium.mods.keystrokes.KeystrokesMod;
import cc.hyperium.mods.levelhead.Levelhead;
import cc.hyperium.mods.motionblur.MotionBlurMod;
import cc.hyperium.mods.myposition.MyPosition;
import cc.hyperium.mods.nickhider.NickHider;
import cc.hyperium.mods.oldanimations.OldAnimations;
import cc.hyperium.mods.timechanger.TimeChanger;
import cc.hyperium.mods.togglechat.ToggleChatMod;
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
    private final GlintColorizer glintColorizer;
    private final BlockOverlay blockOverlay;
    private final MotionBlurMod motionBlur;
    private final OldAnimations oldAnimations;
    private final ItemPhysicMod itemPhysicMod;
    private final ChunkAnimator chunkAnimator;
    private final SoundHandler soundHandler;
    private final MyPosition myPosition;
    private final EntityRadius entityRadius;

    public HyperiumModIntegration() {
        // ChromaHUD implementation
        chromaHUD = ((ChromaHUD) new ChromaHUD().init());

        // LevelHead implementation
        levelhead = ((Levelhead) new Levelhead().init());

        // ToggleChat implementation
        toggleChat = ((ToggleChatMod) new ToggleChatMod().init());

        // TimeChanger implementation
        timeChanger = ((TimeChanger) new TimeChanger().init());

        // KeystrokesMod implementation
        keystrokesMod = ((KeystrokesMod) new KeystrokesMod().init());

        // Autotip implementation
        autotip = new Autotip();
        autotip.init();

        // AutoGG implementation
        autogg = ((AutoGG) new AutoGG().init());

        // Old Animations implementation
        oldAnimations = ((OldAnimations) new OldAnimations().init());

        // Block Overlay implementation
        blockOverlay = ((BlockOverlay) new BlockOverlay().init());

        //Motion Blur
        motionBlur = ((MotionBlurMod) new MotionBlurMod().init());

        // Glint Colorizer implementation
        glintColorizer = ((GlintColorizer) new GlintColorizer().init());

        NickHider nickHider = new NickHider();
        nickHider.init();

        itemPhysicMod = (ItemPhysicMod) new ItemPhysicMod().init();
        chunkAnimator = (ChunkAnimator) new ChunkAnimator().init();
        soundHandler = (SoundHandler) new SoundHandler().init();
        myPosition = (MyPosition) new MyPosition().init();
        entityRadius = (EntityRadius) new EntityRadius().init();
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

    public GlintColorizer getGlintColorizer() {
        return glintColorizer;
    }

    public BlockOverlay getBlockOverlay() {
        return blockOverlay;
    }

    public MotionBlurMod getMotionBlur() {
        return motionBlur;
    }

    public OldAnimations getOldAnimations() {
        return oldAnimations;
    }

    public ItemPhysicMod getItemPhysicMod() {
        return itemPhysicMod;
    }

    public ChunkAnimator getChunkAnimator() {
        return chunkAnimator;
    }

    public SoundHandler getSoundHandler() {
        return soundHandler;
    }

    public MyPosition getMyPosition() {
        return myPosition;
    }

    public EntityRadius getEntityRadius() {
        return entityRadius;
    }
}
