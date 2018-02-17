/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
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

package com.hcc.mods;

import com.hcc.mods.chromahud.ChromaHUD;
import com.hcc.mods.levelhead.Levelhead;
import com.hcc.mods.togglechat.ToggleChatMod;
import me.semx11.autotip.Autotip;

/**
 * Basic inbuilt mod handler, including many community mods such as
 * ChromaHUD, LevelHead and ToggleChat
 */
public class HCCModIntegration {

    private ChromaHUD chromaHUD;
    private Levelhead levelhead;
    private ToggleChatMod toggleChat;
    private Autotip autotip;

    public HCCModIntegration() {
        this.chromaHUD = new ChromaHUD();
        this.levelhead = new Levelhead();

        // Basically just a simple constructor for togglechat.
        this.toggleChat = new ToggleChatMod().init();
        this.autotip = new Autotip();
        autotip.init();
    }

    public ChromaHUD getChromaHUD() {
        return chromaHUD;
    }

    public Levelhead getLevelhead() {
        return levelhead;
    }

    public ToggleChatMod getToggleChat() {
        return this.toggleChat;
    }

    public Autotip getAutotip() {
        return autotip;
    }
}
