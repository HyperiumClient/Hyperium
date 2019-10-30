package com.chattriggers.ctjs.minecraft.objects

import cc.hyperium.Hyperium
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.client.settings.KeyBinding
import org.apache.commons.lang3.ArrayUtils
import java.util.*

@External
class KeyBind {
    private var keyBinding: HyperiumBind
    private var isCustom: Boolean = false
    private var isFirstDown: Boolean = false
    private var canResetDown: Boolean = false

    /**
     * Creates a new key bind, editable in the user's controls.
     *
     * @param description what the key bind does
     * @param keyCode     the keycode which the key bind will respond to, see Keyboard below. Ex. Keyboard.KEY_A
     * @see [Keyboard](http://legacy.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html)
     */
    constructor(description: String, keyCode: Int) {
        for (key in Client.getMinecraft().gameSettings.keyBindings) {
            if (key.keyCategory == "ChatTriggers" && key.keyDescription == description) {
                Client.getMinecraft().gameSettings.keyBindings =
                    ArrayUtils.removeElement(Client.getMinecraft().gameSettings.keyBindings, key)
                break
            }
        }

        keyBinding = object : HyperiumBind(description, keyCode, "ChatTriggers") {
            override fun onPress() {
                super.onPress()

                if (isFirstDown) {
                    isFirstDown = false
                } else if (canResetDown) {
                    isFirstDown = true
                }
            }

            override fun onRelease() {
                super.onRelease()

                canResetDown = true
            }
        }
        Hyperium.INSTANCE.handlers.keybindHandler.registerKeyBinding(keyBinding)

        keyBinds.add(this)
        isCustom = true
    }

    constructor(keyBinding: KeyBinding) {
        this.keyBinding = HyperiumBind(keyBinding.keyDescription, keyBinding.keyCode, keyBinding.keyCategory)
        isCustom = false
    }

    /**
     * Returns true if the key is pressed (used for continuous querying).
     *
     * @return whether or not the key is pressed
     */
    fun isKeyDown(): Boolean = keyBinding.wasPressed()

    /**
     * Returns true on the initial key press. For continuous querying use [.isKeyDown].
     *
     * @return whether or not the key has just been pressed
     */
    fun isPressed(): Boolean = isFirstDown

    /**
     * Gets the key code of the key.
     *
     * @return the integer key code
     */
    fun getKeyCode(): Int = keyBinding.keyCode

    /**
     * Sets the state of the key.
     *
     * @param pressed True to press, False to release
     */
    fun setState(pressed: Boolean) = KeyBinding.setKeyBindState(keyBinding.keyCode, pressed)


    companion object {
        @JvmStatic
        private val keyBinds = ArrayList<KeyBind>()

        @JvmStatic
        fun clearKeyBinds() {
            keyBinds.forEach {
                if (!it.isCustom) return
                Client.getMinecraft().gameSettings.keyBindings =
                    ArrayUtils.removeElement(Client.getMinecraft().gameSettings.keyBindings, it.keyBinding)
            }
        }
    }
}
