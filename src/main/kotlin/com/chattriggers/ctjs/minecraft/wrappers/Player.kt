package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.minecraft.wrappers.objects.Entity
import com.chattriggers.ctjs.minecraft.wrappers.objects.PotionEffect
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.Block
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.Sign
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Inventory
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MathHelper
import com.chattriggers.ctjs.utils.kotlin.RayTraceType
import net.minecraft.block.BlockSign
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.scoreboard.ScorePlayerTeam
import java.util.stream.Collectors

@External
object Player {
    /**
     * Gets Minecraft's EntityPlayerSP object representing the user
     *
     * @return The Minecraft EntityPlayerSP object representing the user
     */
    @JvmStatic
    fun getPlayer(): EntityPlayerSP? = Client.getMinecraft().thePlayer

    @JvmStatic
    fun getX(): Double = getPlayer()?.posX ?: 0.0

    @JvmStatic
    fun getY(): Double = getPlayer()?.posY ?: 0.0

    @JvmStatic
    fun getZ(): Double = getPlayer()?.posZ ?: 0.0

    /**
     * Gets the player's x motion.
     * This is the amount the player will move in the x direction next tick.
     *
     * @return the player's x motion
     */
    @JvmStatic
    fun getMotionX(): Double = getPlayer()?.motionX ?: 0.0

    /**
     * Gets the player's y motion.
     * This is the amount the player will move in the y direction next tick.
     *
     * @return the player's y motion
     */
    @JvmStatic
    fun getMotionY(): Double = getPlayer()?.motionY ?: 0.0

    /**
     * Gets the player's z motion.
     * This is the amount the player will move in the z direction next tick.
     *
     * @return the player's z motion
     */
    @JvmStatic
    fun getMotionZ(): Double = getPlayer()?.motionZ ?: 0.0

    /**
     * Gets the player's camera pitch.
     *
     * @return the player's camera pitch
     */
    @JvmStatic
    fun getPitch(): Float = MathHelper.wrapAngleTo180_float(getPlayer()?.rotationPitch ?: 0f)

    /**
     * Gets the player's camera yaw.
     *
     * @return the player's camera yaw
     */
    @JvmStatic
    fun getYaw(): Float = MathHelper.wrapAngleTo180_float(getPlayer()?.rotationYaw ?: 0f)

    /**
     * Gets the player's yaw rotation without wrapping.
     *
     * @return the yaw
     */
    @JvmStatic
    fun getRawYaw(): Float = getPlayer()?.rotationYaw ?: 0f

    /**
     * Gets the player's username.
     *
     * @return the player's username
     */
    @JvmStatic
    fun getName(): String = Client.getMinecraft().session.username

    @JvmStatic
    fun getUUID(): String = Client.getMinecraft().session.profile.id.toString()

    @JvmStatic
    fun getHP(): Float = getPlayer()?.health ?: 0f

    @JvmStatic
    fun getHunger(): Int = getPlayer()?.foodStats?.foodLevel ?: 0

    @JvmStatic
    fun getSaturation(): Int = getPlayer()?.foodStats?.foodLevel ?: 0

    @JvmStatic
    fun getArmorPoints(): Int = getPlayer()?.totalArmorValue ?: 0

    /**
     * Gets the player's air level.<br></br>
     *
     * The returned value will be an integer. If the player is not taking damage, it
     * will be between 300 (not in water) and 0. If the player is taking damage, it
     * will be between -20 and 0, getting reset to 0 every time the player takes damage.
     *
     * @return the player's air level
     */
    @JvmStatic
    fun getAirLevel(): Int = getPlayer()?.air ?: 0

    @JvmStatic
    fun getXPLevel(): Int = getPlayer()?.experienceLevel ?: 0

    @JvmStatic
    fun getXPProgress(): Float = getPlayer()?.experience ?: 0f

    @JvmStatic
    fun getBiome(): String {
        val player = getPlayer() ?: return ""
        val world = World.getWorld() ?: return ""

        val chunk = world.getChunkFromBlockCoords(player.position)

        val biome = chunk.getBiome(player.position, world.worldChunkManager)

        return biome.biomeName
    }

    /**
     * Gets the light level at the player's current position.
     *
     * @return the light level at the player's current position
     */
    @JvmStatic
    fun getLightLevel(): Int = World.getWorld()?.getLight(getPlayer()?.position) ?: 0

    @JvmStatic
    fun isSneaking(): Boolean = getPlayer()?.isSneaking ?: false

    @JvmStatic
    fun isSprinting(): Boolean = getPlayer()?.isSprinting ?: false

    /**
     * Checks if player can be pushed by water.
     *
     * @return true if the player is flying, false otherwise
     */
    @JvmStatic
    fun isFlying(): Boolean = !(getPlayer()?.isPushedByWater ?: true)

    @JvmStatic
    fun isSleeping(): Boolean = getPlayer()?.isPlayerSleeping ?: false

    /**
     * Gets the direction the player is facing.
     * Example: "South West"
     *
     * @return The direction the player is facing, one of the four cardinal directions
     */
    @JvmStatic
    fun facing(): String {
        if (getPlayer() == null) return ""

        val yaw = getYaw()

        return when {
            yaw in -22.5..22.5 -> "South"
            yaw in 22.5..67.5 -> "South West"
            yaw in 67.5..112.5 -> "West"
            yaw in 112.5..157.5 -> "North West"
            yaw < -157.5 || yaw > 157.5 -> "North"
            yaw in -112.5..-157.5 -> "North East"
            yaw in -67.5..-112.5 -> "East"
            yaw in -22.5..-67.5 -> "South East"
            else -> ""
        }
    }

    @JvmStatic
    fun getActivePotionEffects(): List<PotionEffect> {
        return getPlayer()?.activePotionEffects
            ?.stream()
            ?.map { PotionEffect(it) }
            ?.collect(Collectors.toList())
            ?: listOf()
    }

    /**
     * Gets the current object that the player is looking at,
     * whether that be a block or an entity. Returns an air block when not looking
     * at anything.
     *
     * @return the [Block], [Sign], or [Entity] being looked at
     */
    @JvmStatic
    fun lookingAt(): Any {
        val mop = Client.getMinecraft().objectMouseOver ?: return Block(0)
        val world = World.getWorld() ?: return Block(0)

        return when (mop.typeOfHit) {
            RayTraceType.BLOCK -> {
                val pos = mop.blockPos
                val block = Block(world.getBlockState(pos).block).setBlockPos(pos)

                if (block.block is BlockSign) Sign(block) else block
            }
            RayTraceType.ENTITY -> Entity(mop.entityHit)
            else -> Block(0)
        }
    }

    @JvmStatic
    fun getHeldItem(): Item = Item(getPlayer()?.inventory?.getCurrentItem())

    /**
     * Gets the inventory of the player, i.e. the inventory accessed by 'e'.
     *
     * @return the player's inventory
     */
    @JvmStatic
    fun getInventory(): Inventory = Inventory(getPlayer()!!.inventory)

    /**
     * Gets the display name for the player,
     * i.e. the name shown in tab list and in the player's nametag.
     * @return the display name
     */
    @JvmStatic
    fun getDisplayName(): TextComponent = TextComponent(getPlayerName(getPlayerInfo()))

    /**
     * Sets the name for this player shown in tab list
     *
     * @param textComponent the new name to display
     */
    @JvmStatic
    fun setTabDisplayName(textComponent: TextComponent) {
        getPlayerInfo().displayName = textComponent.chatComponentText
    }

    @JvmStatic
    private fun getPlayerName(networkPlayerInfoIn: NetworkPlayerInfo): String {
        return if (networkPlayerInfoIn.displayName != null)
            networkPlayerInfoIn.displayName?.formattedText.toString()
        else
            ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.playerTeam, networkPlayerInfoIn.gameProfile.name)
    }

    private fun getPlayerInfo(): NetworkPlayerInfo = Client.getConnection().getPlayerInfo(getPlayer()!!.uniqueID)

    /**
     * Gets the inventory the user currently has open, i.e. a chest.
     *
     * @return the currently opened inventory
     */
    @JvmStatic
    fun getOpenedInventory(): Inventory = Inventory(getPlayer()!!.openContainer)

    object armor {
        /**
         * @return the item in the player's helmet slot
         */
        @JvmStatic
        fun getHelmet(): Item = getInventory().getStackInSlot(39)

        /**
         * @return the item in the player's chestplate slot
         */
        @JvmStatic
        fun getChestplate(): Item = getInventory().getStackInSlot(38)

        /**
         * @return the item in the player's leggings slot
         */
        @JvmStatic
        fun getLeggings(): Item = getInventory().getStackInSlot(37)

        /**
         * @return the item in the player's boots slot
         */
        @JvmStatic
        fun getBoots(): Item = getInventory().getStackInSlot(36)
    }
}
