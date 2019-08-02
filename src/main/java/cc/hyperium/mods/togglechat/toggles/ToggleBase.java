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

package cc.hyperium.mods.togglechat.toggles;

import cc.hyperium.utils.ChatColor;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A lite-wight version of ToggleBase
 *
 * @author boomboompower
 */
public abstract class ToggleBase {

    /**
     * Default constructor for ToggleBase
     */
    public ToggleBase() {
    }

    /**
     * Returns the name of the specified ToggleBase
     *
     * @return the name of the specified ToggleBase, cannot be null
     */
    public abstract String getName();

    /**
     * Checks the given text to see if it should be toggled
     *
     * @param message message to test
     * @return true if the message matches the toggle test
     */
    public abstract boolean shouldToggle(final String message);

    /**
     * Checks to see if the given chat is enabled
     *
     * @return true if the player wants to see the given chat
     */
    public abstract boolean isEnabled();

    /**
     * Sets the message to be toggled or not. Is used in
     * toggle loading
     *
     * @param enabled used in loading to set the toggled enabled/disabled
     */
    public abstract void setEnabled(boolean enabled);

    /**
     * Sets the toggle to be the opposite
     * of the current state
     *
     * @see #isEnabled()
     */
    public void toggle() {
        setEnabled(!isEnabled());
    }

    /**
     * Gets the description of the specified toggle,
     * this will show up in the main toggle Gui
     *
     * @return description of the toggle, can be null
     */
    public abstract LinkedList<String> getDescription();

    /**
     * Confirms if the toggle has a description
     * returns false if the description is null or empty
     *
     * @return true if the description is valid
     */
    public final boolean hasDescription() {
        return getDescription() != null && !getDescription().isEmpty();
    }

    /**
     * Gets the display format for the button.
     * Will be formatted when loaded
     *
     * @return The button display format
     */
    public String getDisplayName() {
        return getName() + ": %s";
    }

    /**
     * Should the shouldToggle method use the
     * formatted chat for the regular check?
     *
     * @return true if the formatted message should
     * be used
     */
    public boolean useFormattedMessage() {
        return false;
    }

    /**
     * Assistance in linked-list creation
     *
     * @param entry the array by which the list will be backed
     * @param <T>   the class of the objects in the list
     * @return a list view of the specified array
     */
    @SafeVarargs
    public final <T> LinkedList<T> asLinked(T... entry) {
        return new LinkedList<>(Arrays.asList(entry));
    }

    /**
     * Checks if the message contains something without
     * being case-sensitive
     *
     * @param message  The message to check
     * @param contains the contents
     * @return true if it contains it
     */
    public final boolean containsIgnoreCase(String message, String contains) {
        return Pattern.compile(Pattern.quote(contains), Pattern.CASE_INSENSITIVE).matcher(message).find();
    }

    public String getStatus(boolean in) {
        return in ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled";
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ToggleBase && getName().equals(((ToggleBase) other).getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
