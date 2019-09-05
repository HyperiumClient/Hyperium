package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import cc.hyperium.mixins.client.gui.IMixinGuiChat
import com.chattriggers.ctjs.minecraft.objects.KeyBind
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.GuiNewChat
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.network.NetHandlerPlayClient
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.Display

@External
object Client {
    /**
     * Gets Minecraft's Minecraft object
     *
     * @return The Minecraft object
     */
    @JvmStatic
    fun getMinecraft(): Minecraft = Minecraft.getMinecraft()!!

    /**
     * Gets Minecraft's NetHandlerPlayClient object
     *
     * @return The NetHandlerPlayClient object
     */
    @JvmStatic
    fun getConnection(): NetHandlerPlayClient =
        getMinecraft().netHandler

    /**
     * Gets the Minecraft GuiNewChat object for the chat Gui
     *
     * @return The GuiNewChat object for the chat Gui
     */
    @JvmStatic
    fun getChatGUI(): GuiNewChat? = getMinecraft().ingameGUI?.chatGUI

    @JvmStatic
    fun isInChat(): Boolean = getMinecraft().currentScreen is GuiChat

    @JvmStatic
    fun isInTab(): Boolean = getMinecraft().gameSettings.keyBindPlayerList.isKeyDown

    /**
     * Gets whether or not the Minecraft window is active
     * and in the foreground of the user's Screen.
     *
     * @return true if the game is active, false otherwise
     */
    @JvmStatic
    fun isTabbedIn(): Boolean = Display.isActive()

    /**
     * Get the [KeyBind] from an already existing
     * Minecraft KeyBinding, otherwise, returns null.
     *
     * @param keyCode the keycode to search for, see Keyboard below. Ex. Keyboard.KEY_A
     * @return the [KeyBind] from a Minecraft KeyBinding, or null if one doesn't exist
     * @see [Keyboard](http://legacy.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html)
     */
    @JvmStatic
    fun getKeyBindFromKey(keyCode: Int): KeyBind? {
        return getMinecraft().gameSettings.keyBindings
            .firstOrNull { it.keyCode == keyCode }
            ?.let { KeyBind(it) }
    }

    /**
     * Get the [KeyBind] from an already existing
     * Minecraft KeyBinding, else, return a new one.
     *
     * @param keyCode the keycode to search for, see Keyboard below. Ex. Keyboard.KEY_A
     * @return the [KeyBind] from a Minecraft KeyBinding, or null if one doesn't exist
     * @see [Keyboard](http://legacy.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html)
     */
    @JvmStatic
    fun getKeyBindFromKey(keyCode: Int, description: String): KeyBind {
        return getMinecraft().gameSettings.keyBindings
            .firstOrNull { it.keyCode == keyCode }
            ?.let { KeyBind(it) }
            ?: KeyBind(description, keyCode)
    }

    /**
     * Get the [KeyBind] from an already existing
     * Minecraft KeyBinding, else, null.
     *
     * @param description the key binding's original description
     * @return the key bind, or null if one doesn't exist
     */
    @JvmStatic
    fun getKeyBindFromDescription(description: String): KeyBind? {
        return getMinecraft().gameSettings.keyBindings
            .firstOrNull { it.keyDescription == description }
            ?.let { KeyBind(it) }
    }

    @JvmStatic
    fun getFPS(): Int = Minecraft.getDebugFPS()

    @JvmStatic
    fun getVersion(): String = getMinecraft().version

    @JvmStatic
    fun getMaxMemory(): Long = Runtime.getRuntime().maxMemory()

    @JvmStatic
    fun getTotalMemory(): Long = Runtime.getRuntime().totalMemory()

    @JvmStatic
    fun getFreeMemory(): Long = Runtime.getRuntime().freeMemory()

    @JvmStatic
    fun getMemoryUsage(): Int = Math.round(
        (getTotalMemory() - getFreeMemory()) * 100 / getMaxMemory().toFloat()
    )

    @JvmStatic
    fun getSystemTime(): Long = Minecraft.getSystemTime()

    @JvmStatic
    fun getMouseX(): Float {
        val mx = Mouse.getX().toFloat()
        val rw = Renderer.Screen.getWidth().toFloat()
        val dw = getMinecraft().displayWidth.toFloat()
        return mx * rw / dw
    }

    @JvmStatic
    fun getMouseY(): Float {
        val my = Mouse.getY().toFloat()
        val rh = Renderer.Screen.getHeight().toFloat()
        val dh = getMinecraft().displayHeight.toFloat()
        return rh - my * rh / dh - 1f
    }

    @JvmStatic
    fun isInGui(): Boolean = Gui.get() != null

    /**
     * Gets the chat message currently typed into the chat Gui.
     *
     * @return A blank string if the Gui isn't open, otherwise, the message
     */
    @JvmStatic
    fun getCurrentChatMessage(): String {
        return if (isInChat()) {
            val chatGui = getMinecraft().currentScreen as IMixinGuiChat
            chatGui.inputField.text
        } else ""
    }

    /**
     * Sets the current chat message, if the chat Gui is not open, one will be opened.
     *
     * @param message the message to put in the chat text box.
     */
    @JvmStatic
    fun setCurrentChatMessage(message: String) {
        if (isInChat()) {
            val chatGui = getMinecraft().currentScreen as IMixinGuiChat
            chatGui.inputField.text = message
        } else getMinecraft().displayGuiScreen(GuiChat(message))
    }

    object Gui {
        /**
         * Gets the Java class name of the currently open Gui, for example, "GuiChest"
         *
         * @return the class name of the current Gui
         */
        @JvmStatic
        fun getClassName(): String = get()?.javaClass?.simpleName ?: "null"

        /**
         * Gets the Minecraft Gui class that is currently open
         *
         * @return the Minecraft Gui
         */
        @JvmStatic
        fun get(): GuiScreen? = getMinecraft().currentScreen

        /**
         * Closes the currently open Gui
         */
        @JvmStatic
        fun close() {
            getMinecraft().displayGuiScreen(null)
        }
    }

    object camera {
        @JvmStatic
        fun getX(): Double = Client.getMinecraft().renderManager.viewerPosX

        @JvmStatic
        fun getY(): Double = Client.getMinecraft().renderManager.viewerPosY

        @JvmStatic
        fun getZ(): Double = Client.getMinecraft().renderManager.viewerPosZ
    }
}
