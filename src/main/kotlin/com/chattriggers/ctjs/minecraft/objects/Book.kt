package com.chattriggers.ctjs.minecraft.objects

import cc.hyperium.mixins.client.gui.IMixinGuiScreenBook
import com.chattriggers.ctjs.minecraft.objects.gui.GuiHandler
import com.chattriggers.ctjs.minecraft.objects.message.Message
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.TextComponentSerializer
import net.minecraft.client.gui.GuiScreenBook
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagString

@External
class Book(bookName: String) {
    private var bookScreen: GuiScreenBook? = null
    private val book: ItemStack = ItemStack(Items.written_book)
    private val bookData: NBTTagCompound = NBTTagCompound()

    init {
        bookData["author"] = NBTTagString(Player.getName())
        bookData["title"] = NBTTagString("CT-$bookName")
        bookData["pages"] = NBTTagList()

        book.tagCompound = bookData
    }

    /**
     * Add a page to the book.
     *
     * @param message the entire message for what the page should be
     * @return the current book to allow method chaining
     */
    fun addPage(message: Message) = apply {
        val pages = bookData["pages"] as NBTTagList

        pages.appendTag(
            NBTTagString(
                TextComponentSerializer.componentToJson(
                    message.getChatMessage()
                )
            )
        )

        updateBookScreen(pages)
    }

    /**
     * Overloaded method for adding a simple page to the book.
     *
     * @param message a simple string to make the page
     * @return the current book to allow method chaining
     */
    fun addPage(message: String) = apply {
        addPage(Message(message))
    }

    /**
     * Sets a page of the book to the specified message.
     *
     * @param pageNumber the number of the page to set
     * @param message    the message to set the page to
     * @return the current book to allow method chaining
     */
    fun setPage(pageNumber: Int, message: Message) = apply {
        val pages = bookData.getTag("pages") as NBTTagList

        pages.set(
            pageNumber, NBTTagString(
                TextComponentSerializer.componentToJson(
                    message.getChatMessage()
                )
            )
        )

        updateBookScreen(pages)
    }

    fun updateBookScreen(pages: NBTTagList) {
        bookData.removeTag("pages")
        bookData["pages"] = pages
        book.tagCompound = bookData

        (bookScreen as? IMixinGuiScreenBook)?.bookPages = pages
    }

    @JvmOverloads
    fun display(page: Int = 0) {
        if (bookScreen == null) {
            bookScreen = GuiScreenBook(Player.getPlayer(), book, false)
        }

        (bookScreen as? IMixinGuiScreenBook)?.currPage = page

        GuiHandler.openGui(bookScreen ?: return)
    }

    fun isOpen(): Boolean = Client.getMinecraft().currentScreen === bookScreen

    fun getCurrentPage(): Int = if (!isOpen()) -1 else (bookScreen as IMixinGuiScreenBook).currPage

    operator fun NBTTagCompound.set(tag: String, value: NBTBase) {
        setTag(tag, value)
    }

    operator fun NBTTagCompound.get(tag: String): NBTBase = getTag(tag)
}
