package com.hcc.mods;

import com.hcc.mods.chromahud.ChromaHUD;
import com.hcc.mods.levelhead.Levelhead;
import com.hcc.mods.togglechat.ToggleChatMod;

/**
 * Basic inbuilt mod handler, including many community mods such as
 *      ChromaHUD, LevelHead and ToggleChat
 */
public class HCCModIntegration {

    private ChromaHUD chromaHUD;
    private Levelhead levelhead;
    private ToggleChatMod toggleChat;

    public HCCModIntegration() {
        this.chromaHUD = new ChromaHUD();
        this.levelhead = new Levelhead();

        // Basically just a simple constructor for togglechat.
        this.toggleChat = new ToggleChatMod().init();
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
}
