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

package cc.hyperium.handlers.handlers;

import cc.hyperium.Hyperium;
import cc.hyperium.config.DefaultConfig;

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
        if (!config.exists()) return;

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
            defaultConfig.register(Hyperium.INSTANCE.getModIntegration().getLevelhead().getDisplayManager().getMasterConfig());
        }
    }
}
