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

package cc.hyperium.addons.sidebar;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.AbstractAddon;
import cc.hyperium.addons.sidebar.commands.CommandSidebar;
import cc.hyperium.addons.sidebar.config.Configuration;
import cc.hyperium.addons.sidebar.gui.GuiSidebar;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderScoreboardEvent;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class SidebarAddon extends AbstractAddon {
    private static final Logger logger = LogManager.getLogger("SidebarAddon");
    private File saveFile;
    private GuiSidebar guiSidebar;

    public static final String VERSION = "1.0.0";

    @Override
    public AbstractAddon init() {
        EventBus.INSTANCE.register(this);
        Minecraft mc = Minecraft.getMinecraft();
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandSidebar(this));
        this.saveFile = new File(mc.mcDataDir, "hyperium/sidebaraddon.json");
        this.guiSidebar = new GuiSidebar();
        this.loadConfig();
        logger.info("Successfully loaded SidebarAddon!");

        return this;
    }

    @Override
    public Metadata getAddonMetadata() {
        AbstractAddon.Metadata metadata = new AbstractAddon.Metadata(this, "SidebarAddon", "1.0.1", "Amplifiable");
        metadata.setDescription("Allows for full scoreboard customization");

        return metadata;
    }

    public GuiSidebar getSidebarGui() {
        return this.guiSidebar;
    }

    public void saveConfig() {
        try {
            final Configuration config = Configuration.load(saveFile);
            this.updateConfig(config, false);
            config.save(saveFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadConfig() {
        try {
            final Configuration config = Configuration.load(saveFile);
            this.updateConfig(config, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @InvokeEvent
    public void renderScoreboard(RenderScoreboardEvent e) {
        e.setCancelled(true);
        this.guiSidebar.drawSidebar(e.getObjective(), e.getResolution());
    }

    private void updateConfig(final Configuration config, final boolean load) {
        if (load) {
            this.guiSidebar.enabled = config.enabled;
        } else {
            config.enabled = this.guiSidebar.enabled;
        }
        if (load) {
            this.guiSidebar.offsetX = config.offsetX;
        } else {
            config.offsetX = this.guiSidebar.offsetX;
        }
        if (load) {
            this.guiSidebar.offsetY = config.offsetY;
        } else {
            config.offsetY = this.guiSidebar.offsetY;
        }
        if (load) {
            this.guiSidebar.scale = config.scale;
        } else {
            config.scale = this.guiSidebar.scale;
        }
        if (load) {
            this.guiSidebar.redNumbers = config.redNumbers;
        } else {
            config.redNumbers = this.guiSidebar.redNumbers;
        }
        if (load) {
            this.guiSidebar.shadow = config.shadow;
        } else {
            config.shadow = this.guiSidebar.shadow;
        }
        if (load) {
            this.guiSidebar.color = config.rgb;
        } else {
            config.rgb = this.guiSidebar.color;
        }
        if (load) {
            this.guiSidebar.alpha = config.alpha;
        } else {
            config.alpha = this.guiSidebar.alpha;
        }
        if (load) {
            this.guiSidebar.chromaEnabled = config.chromaEnabled;
        } else {
            config.chromaEnabled = this.guiSidebar.chromaEnabled;
        }
        if (load) {
            this.guiSidebar.chromaSpeed = config.chromaSpeed;
        } else {
            config.chromaSpeed = this.guiSidebar.chromaSpeed;
        }
        if (load) {
            this.guiSidebar.chromaType = config.chromaType;
        } else {
            config.chromaType = this.guiSidebar.chromaType;
        }
    }
}
