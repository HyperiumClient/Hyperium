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

package cc.hyperium.mods.keystrokes;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.mods.keystrokes.config.KeystrokesSettings;
import cc.hyperium.mods.keystrokes.render.KeystrokesRenderer;
import cc.hyperium.mods.sk1ercommon.Sk1erMod;
import cc.hyperium.utils.ChatColor;

public class KeystrokesMod extends AbstractMod {

    /**
     * The mods metadata
     */
    private final Metadata metaData;

    private KeystrokesSettings config;
    private KeystrokesRenderer renderer;

    /**
     * Default constructor, this will load the mods metadata
     */
    public KeystrokesMod() {
        Metadata data = new Metadata(this, "KeystrokesMod", "5.0.1", "Fyu, boomboompower and Sk1er");

        data.setDisplayName(ChatColor.AQUA + "Keystrokes");

        this.metaData = data;
    }

    /**
     * Init method, loads configs and events for the KeystrokesMod
     *
     * @return this mods instance
     */
    @Override
    public AbstractMod init() {
        this.config = new KeystrokesSettings(this, Hyperium.folder);
        this.config.load();

        new Sk1erMod("keystrokesmod", "5.0.1").checkStatus();

        this.renderer = new KeystrokesRenderer(this);

        EventBus.INSTANCE.register(this.renderer);

        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandKeystrokes(this));

        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return this.metaData;
    }

    /**
     * Getter for the Keystrokes settings
     *
     * @return the keystrokes settings
     */
    public KeystrokesSettings getSettings() {
        return this.config;
    }

    /**
     * Getter for the Keystrokes renderer
     *
     * @return the keystrokes renderer
     */
    public KeystrokesRenderer getRenderer() {
        return this.renderer;
    }
}
