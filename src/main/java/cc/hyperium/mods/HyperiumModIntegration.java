/*
 * Hyperium Client, Free client with huds and popular mod
 *     Copyright (C) 2018  Hyperium Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods;

import cc.hyperium.mods.skinchanger.SkinChangerMod;
import cc.hyperium.mods.chromahud.ChromaHUD;
import cc.hyperium.mods.levelhead.Levelhead;
import cc.hyperium.mods.togglechat.ToggleChatMod;
import me.semx11.autotip.Autotip;

/**
 * Basic inbuilt mod handler, including many community mods such as
 * ChromaHUD, LevelHead and ToggleChat
 */
public class HyperiumModIntegration {
    
    private IBaseMod skinChanger;
    private IBaseMod toggleChat;

    private ChromaHUD chromaHUD;
    private Levelhead levelhead;
    private Autotip autotip;

    public HyperiumModIntegration() {
        this.chromaHUD = new ChromaHUD();
        this.levelhead = new Levelhead();

        // ToggleChat implementation
        this.toggleChat = new ToggleChatMod().init();
        
        // SkinChanger implementation
        this.skinChanger = new SkinChangerMod().init();

        // Autotip implementation
        this.autotip = new Autotip().init();
    }

    /**
     * A getter for the running Autotip instance
     *
     * @return the running Autotip instance
     */
    public Autotip getAutotip() {
        return this.autotip;
    }

    /**
     * A getter for the running ChromeHUD instance
     *
     * @return the running ChromeHUD instance
     */
    public ChromaHUD getChromaHUD() {
        return this.chromaHUD;
    }

    /**
     * A getter for the running LevelHead instance
     *
     * @return the running LevelHead instance
     */
    public Levelhead getLevelhead() {
        return this.levelhead;
    }
    
    /**
     * A getter for the running SkinChanger instance
     *
     * @return the running SkinChanger instance
     */
    public IBaseMod getSkinChanger() {
        return this.skinChanger;
    }
    
    /**
     * A getter for the running ToggleChat instance
     *
     * @return the running ToggleChat instance
     */
    public IBaseMod getToggleChat() {
        return this.toggleChat;
    }
}
