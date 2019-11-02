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

package cc.hyperium.addons.customcrosshair;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.AbstractAddon;
import cc.hyperium.addons.customcrosshair.command.CommandCustomCrosshair;
import cc.hyperium.addons.customcrosshair.crosshair.CustomCrosshair;
import cc.hyperium.addons.customcrosshair.utils.CustomCrosshairConfig;
import cc.hyperium.event.EventBus;
import cc.hyperium.utils.ChatColor;

import java.awt.*;

public class CustomCrosshairAddon extends AbstractAddon {

    public static final Color PRIMARY = new Color(23, 107, 192, 255);
    public static final Color PRIMARY_T = new Color(23, 107, 192, 128);
    public static final Color SECONDARY = new Color(255, 255, 255, 255);

    private static CustomCrosshairAddon instance;

    private CustomCrosshair crosshair;
    private CustomCrosshairConfig config;

    public static String VERSION = "0.5.3-hyperium";

    @Override
    public AbstractAddon init() {
        EventBus.INSTANCE.register(this);
        instance = this;
        crosshair = new CustomCrosshair();
        config = new CustomCrosshairConfig(this);

        if (!config.readSaveFile()) {
            config.writeSaveFileDefault();
        }

        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandCustomCrosshair(this));

        EventBus.INSTANCE.register(crosshair);

        return this;
    }

    @Override
    public Metadata getAddonMetadata() {
        Metadata metadata = new Metadata(this, "Custom Crosshair Addon", "0.5.3", "Amplifiable");
        metadata.setDisplayName(ChatColor.GREEN + "Custom Crosshair Addon");
        metadata.setDescription("CustomCrosshair");
        return metadata;
    }

    public void resetCrosshair() {
        crosshair = new CustomCrosshair();
    }

    public CustomCrosshair getCrosshair() {
        if (crosshair == null) {
            resetCrosshair();
        }

        return crosshair;
    }

    public CustomCrosshairConfig getConfig() {
        return config;
    }

    public static CustomCrosshairAddon getCrosshairMod() {
        return instance;
    }
}
