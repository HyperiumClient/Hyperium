package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.mixins.MixinGuiChat
import com.chattriggers.ctjs.minecraft.objects.KeyBind
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.GuiNewChat
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.network.NetHandlerPlayClient
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.Display

object Client {
    /**
     * Gets Minecraft's Minecraft object
     *
     * @return The Minecraft object
     */
    @JvmStatic
    fun getMinecraft() = Minecraft.getMinecraft()!!

    /**
     * Gets Minecraft's NetHandlerPlayClient object
     *
     * @return The NetHandlerPlayClient object
     */
    @JvmStatic
    fun getConnection(): NetHandlerPlayClient =
        getMinecraft().netHandler

    /**
     * Gets the Minecraft GuiNewChat object for the chat gui
     *
     * @return The GuiNewChat object for the chat gui
     */
    @JvmStatic
    fun getChatGUI(): GuiNewChat? = getMinecraft().ingameGUI?.chatGUI

    @JvmStatic
    fun isInChat() = getMinecraft().currentScreen is GuiChat

    @JvmStatic
    fun isInTab() =  getMinecraft().gameSettings.keyBindPlayerList.isKeyDown

    /**
     * Gets whether or not the Minecraft window is active
     * and in the foreground of the user's screen.
     *
     * @return true if the game is active, false otherwise
     */
    @JvmStatic
    fun isTabbedIn() = Display.isActive()

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
    fun getFPS() = Minecraft.getDebugFPS()

    @JvmStatic
    fun getVersion() = getMinecraft().version

    @JvmStatic
    fun getMaxMemory() = Runtime.getRuntime().maxMemory()

    @JvmStatic
    fun getTotalMemory() = Runtime.getRuntime().totalMemory()

    @JvmStatic
    fun getFreeMemory() = Runtime.getRuntime().freeMemory()

    @JvmStatic
    fun getMemoryUsage() = Math.round(
            (getTotalMemory() - getFreeMemory()) * 100 / getMaxMemory().toFloat()
    )

    @JvmStatic
    fun getSystemTime() = Minecraft.getSystemTime()

    @JvmStatic
    fun getMouseX(): Float {
        val mx = Mouse.getX().toFloat()
        val rw = Renderer.screen.getWidth().toFloat()
        val dw = getMinecraft().displayWidth.toFloat()
        return mx * rw / dw
    }

    @JvmStatic
    fun getMouseY(): Float {
        val my = Mouse.getY().toFloat()
        val rh = Renderer.screen.getHeight().toFloat()
        val dh = getMinecraft().displayHeight.toFloat()
        return rh - my * rh / dh - 1f
    }

    @JvmStatic
    fun isInGui() = gui.get() != null

    /**
     * Gets the chat message currently typed into the chat gui.
     *
     * @return A blank string if the gui isn't open, otherwise, the message
     */
    @JvmStatic
    fun getCurrentChatMessage(): String {
        return if (isInChat()) {
            val chatGui = getMinecraft().currentScreen as MixinGuiChat
            chatGui.inputField.text
        } else ""
    }

    /**
     * Sets the current chat message, if the chat gui is not open, one will be opened.
     *
     * @param message the message to put in the chat text box.
     */
    @JvmStatic
    fun setCurrentChatMessage(message: String) {
        if (isInChat()) {
            val chatGui = getMinecraft().currentScreen as MixinGuiChat
            chatGui.inputField.text = message
        } else Client.getMinecraft().displayGuiScreen(GuiChat(message))
    }

    object gui {
        /**
         * Gets the Java class name of the currently open gui, for example, "GuiChest"
         *
         * @return the class name of the current gui
         */
        @JvmStatic
        fun getClassName() = get()?.javaClass?.simpleName ?: "null"

        /**
         * Gets the Minecraft gui class that is currently open
         *
         * @return the Minecraft gui
         */
        @JvmStatic
        fun get(): GuiScreen? = getMinecraft().currentScreen

        /**
         * Closes the currently open gui
         */
        @JvmStatic
        fun close() {
            getMinecraft().displayGuiScreen(null)
        }
    }

    object camera {
        @JvmStatic
        val x: Double
            get() = Client.getMinecraft().renderManager.viewerPosX

        @JvmStatic
        val y: Double
            get() = Client.getMinecraft().renderManager.viewerPosY

        @JvmStatic
        val z: Double
            get() = Client.getMinecraft().renderManager.viewerPosZ
    }
}