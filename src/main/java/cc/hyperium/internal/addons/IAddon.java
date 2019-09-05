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

package cc.hyperium.internal.addons;

/**
 * Interface of which the main class
 * of an Addon must implement
 *
 * @author Kevin Brewster
 * @since 1.0
 */
public interface IAddon {

    /**
     * Invoked once the plugin has successfully loaded
     * {@see cc.hyperium.internal.addons.AddonMinecraftBootstrap#init}
     */
    void onLoad();

    /**
     * Invoked once the game has been closed
     * this is executed at the start of {@link net.minecraft.client.Minecraft#shutdown}
     */
    void onClose();


    /**
     * Invoked on debug call. Can be used to add things into crash reports
     * <p>
     * This does not need to be overriden if it's not needed
     */
    default void sendDebugInfo() {
    }
}
