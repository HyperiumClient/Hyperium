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

package cc.hyperium.mods.togglechat;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.mods.togglechat.commands.CommandToggleChat;
import cc.hyperium.mods.togglechat.config.ToggleChatConfig;
import cc.hyperium.mods.togglechat.toggles.ToggleBaseHandler;
import cc.hyperium.utils.ChatColor;

/**
 * Basically just a lightweight version of ToggleChat
 *
 * @author boomboompower
 */
public final class ToggleChatMod extends AbstractMod {

    /**
     * The metadata of ToggleChat
     */
    private final Metadata meta;

    /**
     * A basic CONFIG loader
     */
    private ToggleChatConfig configLoader;

    /**
     * A different implementation to the normal ToggleChat, just manages all toggles
     */
    private ToggleBaseHandler toggleHandler;

    public ToggleChatMod() {
        Metadata metadata = new Metadata(this, "ToggleChatLite", "1.0", "boomboompower");

        metadata.setDisplayName(ChatColor.AQUA + "ToggleChatLite");

        meta = metadata;
    }

    public AbstractMod init() {
        configLoader = new ToggleChatConfig(this, Hyperium.folder);

        toggleHandler = new ToggleBaseHandler();
        toggleHandler.remake();

        EventBus.INSTANCE.register(new ToggleChatEvents(this));

        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandToggleChat(this));

        configLoader.loadToggles();

        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return meta;
    }

    /**
     * Getter for ToggleChat's configuration
     *
     * @return the configuration
     */
    public ToggleChatConfig getConfigLoader() {
        return configLoader;
    }

    /**
     * Getter for ToggleChat's ToggleHandler
     *
     * @return the handlers instance
     */
    public ToggleBaseHandler getToggleHandler() {
        return toggleHandler;
    }
}
