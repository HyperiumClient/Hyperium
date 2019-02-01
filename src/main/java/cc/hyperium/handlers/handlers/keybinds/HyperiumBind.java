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

package cc.hyperium.handlers.handlers.keybinds;

import cc.hyperium.Hyperium;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.apache.commons.text.WordUtils;

/**
 * Our implementation of the normal Minecraft KeyBinding, uses anonymous
 * classes instead of using an onTick event, this is debatably better
 *
 * @author CoalOres
 * @author boomboompower
 */
public class HyperiumBind {

    /**
     * The default key code
     */
    private final int defaultKeyCode;

    private final String description;
    private int key;

    private boolean wasPressed;

    private boolean conflicted = false;
    protected boolean conflictExempt = false;

    public HyperiumBind(String description, int key) {
        this(description, key, "Hyperium");
    }

    public HyperiumBind(String description, int key, String category) {
        this.defaultKeyCode = key;

        this.description = description;
        this.key = key;
    }

    /**
     * Returns the current code of the key
     *
     * @return the key code
     */
    public int getKeyCode() {
        return this.key;
    }

    /**
     * Setter for the key code, sets it here and calls the super
     * method to update it as well
     *
     * @param key the key
     */
    public void setKeyCode(int key) {
        this.key = key;
    }

    /**
     * Returns the default key code for the key
     *
     * @return the default key code
     */
    public int getDefaultKeyCode() {
        return this.defaultKeyCode;
    }

    /**
     * Returns the key description, this will be capitalized if the capitalize
     * method returns true (see {@link #capitalizeDescription()})
     *
     * @return the keys description
     */
    public String getKeyDescription() {
        String message = this.description;

        if (this.capitalizeDescription()) {
            message = WordUtils.capitalizeFully(message);
        }

        return message;
    }

    /**
     * Gets the description without the formatting, this is used for saving
     *
     * @return the real description of the KeyBind
     */
    protected String getRealDescription() {
        return this.description;
    }

    /**
     * Setter for the WasPressed variable, if true it means the buttons
     * last event was to be pressed
     *
     * @param wasPressed if the button was pressed
     */
    public void setWasPressed(boolean wasPressed) {
        this.wasPressed = wasPressed;
    }

    /**
     * Setter for the conflicted variable, if true it means the
     * bind is conflicting with another.
     */
    public void setConflicted(boolean conflicted) {
        this.conflicted = conflicted;
    }

    public boolean isConflicted() {
        return this.conflicted;
    }

    /**
     * Was the the last event on the key a key press?
     *
     * @return true if the key was last pressed
     */
    public boolean wasPressed() {
        return this.wasPressed;
    }

    /**
     * Should the Key Description be capitalized, this looks neater in the
     * controls menu, instead of leaving it lowercase
     *
     * @return true if the description should be capitalized
     */
    public boolean capitalizeDescription() {
        return true;
    }

    /**
     * Called when the button is pressed, override this ton
     */
    public void onPress() {
        // We want these to be changed
    }

    public void onRelease() {
        // We want these to be changed
    }

    public void detectConflicts() {
        setConflicted(false);

        int currentKeyCode = this.getKeyCode();

        if (currentKeyCode == 0 || conflictExempt) {
            // Allow multiple binds to be set to NONE.
            return;
        }

        List<HyperiumBind> otherBinds = new ArrayList<>(Hyperium.INSTANCE.getHandlers().getKeybindHandler().getKeybinds().values());
        otherBinds.remove(this);

        // Check for conflicts with Minecraft binds.
        for (KeyBinding keyBinding : Minecraft.getMinecraft().gameSettings.keyBindings) {
            int keyCode = keyBinding.getKeyCode();

            if (currentKeyCode == keyCode) {
                // There is a conflict!
                setConflicted(true);
            }
        }

        // Check for conflicts with other Hyperium binds.
        for (HyperiumBind hyperiumBind : otherBinds) {
            if (hyperiumBind.conflictExempt) {
                continue;
            }
            int keyCode = hyperiumBind.getKeyCode();

            if (currentKeyCode == keyCode) {
                // There is a conflict!
                setConflicted(true);
            }
        }
    }
}
