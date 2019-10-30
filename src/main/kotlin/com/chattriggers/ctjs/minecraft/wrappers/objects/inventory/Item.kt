package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory

import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.minecraft.wrappers.objects.Entity
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.Block
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import org.lwjgl.opengl.GL11
import net.minecraft.item.Item as MCItem

@External
class Item {
    val item: MCItem
    var itemStack: ItemStack

    /* Constructors */
    constructor(itemStack: ItemStack?) {
        if (itemStack == null) {
            item = ItemBlock(Block(0).block)
            this.itemStack = ItemStack(item)
        } else {
            item = itemStack.item
            this.itemStack = itemStack
        }
    }

    constructor(itemName: String) {
        item = MCItem.getByNameOrId(itemName)!!
        itemStack = ItemStack(item)
    }

    constructor(itemID: Int) {
        item = MCItem.getItemById(itemID)
        itemStack = ItemStack(item)
    }

    constructor(block: Block) {
        item = MCItem.getItemFromBlock(block.block)
        itemStack = ItemStack(item)
    }

    constructor(entityItem: EntityItem) {
        item = entityItem.entityItem.item
        itemStack = entityItem.entityItem
    }

    /**
     * Created an Item object from an Entity.
     * Has to be wrapping an EntityItem.
     *
     * @param entity the Entity
     */
    constructor(entity: Entity) {
        if (entity.entity is EntityItem) {
            item = entity.entity.entityItem.item
            itemStack = entity.entity.entityItem
        } else {
            throw IllegalArgumentException("Entity is not of type EntityItem")
        }
    }
    /* End of constructors */

    fun getID(): Int = MCItem.getIdFromItem(item)

    fun setStackSize(stackSize: Int) = apply {
        itemStack = ItemStack(item, stackSize)
    }

    fun getStackSize(): Int = itemStack.stackSize

    /**
     * Gets the item's unlocalized name.<br>
     * Example: <code>tile.wood</code>
     *
     * @return the item's unlocalized name
     */
    fun getUnlocalizedName(): String = item.unlocalizedName

    /**
     * Gets the item's registry name.<br>
     * Example: <code>minecraft:planks</code>
     *
     * @return the item's registry name
     */
    fun getRegistryName(): String = MCItem.itemRegistry.getNameForObject(item).toString()

    /**
     * Gets the item's stack display name.<br>
     * Example: <code>Oak Wood Planks</code>
     *
     * @return the item's stack display name
     */
    fun getName(): String = if (getID() == 0) "air" else itemStack.displayName

    fun getEnchantments(): Map<String, Int> {
        return EnchantmentHelper.getEnchantments(itemStack).mapKeys {
            Enchantment.getEnchantmentById(
                it.key
            ).name.replace("enchantment.", "")
        }
    }

    fun isEnchantable(): Boolean = itemStack.isItemEnchantable

    fun isEnchanted(): Boolean = itemStack.isItemEnchanted

    fun getItemNBT(): String = itemStack.writeToNBT(NBTTagCompound()).toString()

    fun getMetadata(): Int = itemStack.metadata

    fun canPlaceOn(block: Block): Boolean = itemStack.canPlaceOn(block.block)

    fun canHarvest(block: Block): Boolean = itemStack.canHarvestBlock(block.block)

    fun canDestroy(block: Block): Boolean = itemStack.canDestroy(block.block)

    /**
     * Gets the items durability, i.e. the number of uses left
     *
     * @return the items durability
     */
    fun getDurability(): Int = getMaxDamage() - getDamage()

    fun getDamage(): Int = itemStack.itemDamage

    fun setDamage(damage: Int) = apply {
        itemStack.itemDamage = damage
    }

    fun getMaxDamage(): Int = itemStack.maxDamage

    fun isDamagable(): Boolean = itemStack.isItemStackDamageable

    fun getLore(): List<String> =
        itemStack.getTooltip(Player.getPlayer(), Client.getMinecraft().gameSettings.advancedItemTooltips)

    /**
     * Renders the item icon to the client's overlay.
     *
     * @param x the x location
     * @param y the y location
     * @param scale the scale
     */
    @JvmOverloads
    fun draw(x: Float, y: Float, scale: Float = 1f) {
        val itemRenderer = Client.getMinecraft().renderItem

        GlStateManager.pushMatrix()

        GlStateManager.scale(scale, scale, 1f)
        GlStateManager.translate(x / scale, y / scale, 0f)
        GL11.glColor4f(1f, 1f, 1f, 1f)

        RenderHelper.enableStandardItemLighting()
        RenderHelper.enableGUIStandardItemLighting()

        itemRenderer.zLevel = 200f
        itemRenderer.renderItemIntoGUI(itemStack, 0, 0)

        GlStateManager.popMatrix()
    }

    /**
     * Checks whether another Item is the same as this one.
     * It compares id, stack size, and durability.
     *
     * @param other the object to compare to
     * @return whether the objects are equal
     */
    override fun equals(other: Any?): Boolean {
        return other is Item &&
                getID() == other.getID() &&
                getStackSize() == other.getStackSize() &&
                getDamage() == other.getDamage()
    }

    override fun hashCode(): Int {
        var result = item.hashCode()
        result = 31 * result + itemStack.hashCode()
        return result
    }

    override fun toString() = itemStack.toString()
}
