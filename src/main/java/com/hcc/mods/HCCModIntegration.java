package com.hcc.mods;

import com.hcc.mods.chromahud.ChromaHUD;
import com.hcc.mods.levelhead.Levelhead;

/**
 * Created by mitchellkatz on 2/14/18. Designed for production use on Sk1er.club
 */
public class HCCModIntegration {

    private ChromaHUD chromaHUD;
    private Levelhead levelhead;

    public HCCModIntegration() {
        chromaHUD = new ChromaHUD();
        levelhead = new Levelhead();
    }

    public ChromaHUD getChromaHUD() {
        return chromaHUD;
    }

    public Levelhead getLevelhead() {
        return levelhead;
    }
}
