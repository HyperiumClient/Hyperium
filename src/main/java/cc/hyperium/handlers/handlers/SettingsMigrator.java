package cc.hyperium.handlers.handlers;

import cc.hyperium.Hyperium;
import cc.hyperium.config.DefaultConfig;
import cc.hyperium.mods.chromahud.ChromaHUD;
import cc.hyperium.mods.levelhead.Levelhead;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by mitchellkatz on 6/22/18. Designed for production use on Sk1er.club
 */
public class SettingsMigrator {


    public void migrate() {
        File folder = Hyperium.folder;
        File config = new File(folder.getParentFile(), "config");
        if (!config.exists())
            return;

        File chromahud = new File(config, "ChromaHUD.cfg");
        if (chromahud.exists()) {
            try {
                Files.copy(chromahud.toPath(), new File(config, "displayconfig.json").toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Hyperium.INSTANCE.getModIntegration().getChromaHUD().setup();
        }

        File level_head = new File(config, "LEVEL_HEAD.cfg");
        if (level_head.exists()) {
            DefaultConfig defaultConfig = new DefaultConfig(level_head);
            defaultConfig.register(Hyperium.INSTANCE.getModIntegration().getLevelhead().getConfig());
        }


    }
}
