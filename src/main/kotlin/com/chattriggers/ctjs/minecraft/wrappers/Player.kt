package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.minecraft.wrappers.objects.Entity
import com.chattriggers.ctjs.minecraft.wrappers.objects.PotionEffect
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.Block
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.Sign
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Inventory
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import com.chattriggers.ctjs.utils.kotlin.MathHelper
import com.chattriggers.ctjs.utils.kotlin.RayTraceType
import net.minecraft.block.BlockSign
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.scoreboard.ScorePlayerTeam
import java.util.stream.Collectors

object Player {
    /**
     * Gets Minecraft's EntityPlayerSP object representing the user
     *
     * @return The Minecraft EntityPlayerSP object representing the user
     */
    @JvmStatic
    fun getPlayer(): EntityPlayerSP? {
        return Client.getMinecraft().thePlayer
    }

    @JvmStatic
    fun getX() = getPlayer()?.posX ?: 0f

    @JvmStatic
    fun getY() = getPlayer()?.posY ?: 0f

    @JvmStatic
    fun getZ() = getPlayer()?.posZ ?: 0f

    /**
     * Gets the player's x motion.
     * This is the amount the player will move in the x direction next tick.
     *
     * @return the player's x motion
     */
    @JvmStatic
    fun getMotionX() = getPlayer()?.motionX ?: 0f

    /**
     * Gets the player's y motion.
     * This is the amount the player will move in the y direction next tick.
     *
     * @return the player's y motion
     */
    @JvmStatic
    fun getMotionY() = getPlayer()?.motionY ?: 0f

    /**
     * Gets the player's z motion.
     * This is the amount the player will move in the z direction next tick.
     *
     * @return the player's z motion
     */
    @JvmStatic
    fun getMotionZ() = getPlayer()?.motionZ ?: 0f

    /**
     * Gets the player's camera pitch.
     *
     * @return the player's camera pitch
     */
    @JvmStatic
    fun getPitch() = MathHelper.
            //#if MC<=10809
            wrapAngleTo180_float(getPlayer()?.rotationPitch ?: 0f)
            //#else
            //$$ wrapDegrees(getPlayer()?.rotationPitch ?: 0f);
            //#endif

    /**
     * Gets the player's camera yaw.
     *
     * @return the player's camera yaw
     */
    @JvmStatic
    fun getYaw() = MathHelper.
            //#if MC<=10809
            wrapAngleTo180_float(getPlayer()?.rotationYaw ?: 0f)
            //#else
            //$$ wrapDegrees(getPlayer()?.rotationYaw ?: 0f);
            //#endif

    /**
     * Gets the player's yaw rotation without wrapping.
     *
     * @return the yaw
     */
    @JvmStatic
    fun getRawYaw() = getPlayer()?.rotationYaw ?: 0

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
    fun getHP() = getPlayer()?.health ?: 0

    @JvmStatic
    fun getHunger() = getPlayer()?.foodStats?.foodLevel ?: 0

    @JvmStatic
    fun getSaturation() = getPlayer()?.foodStats?.foodLevel ?: 0

    @JvmStatic
    fun getArmorPoints() = getPlayer()?.totalArmorValue ?: 0

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
    fun getAirLevel() = getPlayer()?.air ?: 0

    @JvmStatic
    fun getXPLevel() = getPlayer()?.experienceLevel ?: 0

    @JvmStatic
    fun getXPProgress() = getPlayer()?.experience ?: 0

    @JvmStatic
    fun getBiome(): String {
        val player = getPlayer() ?: return ""
        val world = World.getWorld() ?: return ""

        val chunk = world.getChunkFromBlockCoords(player.position)

        //#if MC<=10809
        val biome = chunk.getBiome(player.position, world.worldChunkManager)
        //#else
        //$$ val biome = chunk.getBiome(player.position, world.biomeProvider)
        //#endif

        return biome.biomeName
    }

    /**
     * Gets the light level at the player's current position.
     *
     * @return the light level at the player's current position
     */
    @JvmStatic
    fun getLightLevel() = World.getWorld()?.getLight(getPlayer()?.position) ?: 0

    @JvmStatic
    fun isSneaking() = getPlayer()?.isSneaking ?: false

    @JvmStatic
    fun isSprinting() = getPlayer()?.isSprinting ?: false

    /**
     * Checks if player can be pushed by water.
     *
     * @return true if the player is flying, false otherwise
     */
    @JvmStatic
    fun isFlying() = !(getPlayer()?.isPushedByWater ?: true)

    @JvmStatic
    fun isSleeping() = getPlayer()?.isPlayerSleeping ?: false

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

        return when {
            mop.typeOfHit == RayTraceType.BLOCK -> {
                val pos = mop.blockPos
                val block = Block(world.getBlockState(pos).block).setBlockPos(pos)

                if (block.block is BlockSign) Sign(block) else block
            }
            mop.typeOfHit == RayTraceType.ENTITY -> Entity(mop.entityHit)
            else -> Block(0)
        }
    }

    @JvmStatic
    fun getHeldItem() = Item(getPlayer()?.inventory?.getCurrentItem())

    /**
     * Gets the inventory of the player, i.e. the inventory accessed by 'e'.
     *
     * @return the player's inventory
     */
    @JvmStatic
    fun getInventory() = Inventory(getPlayer()!!.inventory)

    /**
     * Gets the display name for the player,
     * i.e. the name shown in tab list and in the player's nametag.
     * @return the display name
     */
    @JvmStatic
    fun getDisplayName() = TextComponent(getPlayerName(getPlayerInfo()))

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

    private fun getPlayerInfo() = Client.getConnection().getPlayerInfo(getPlayer()!!.uniqueID)

    /**
     * Gets the inventory the user currently has open, i.e. a chest.
     *
     * @return the currently opened inventory
     */
    @JvmStatic
    fun getOpenedInventory() = Inventory(getPlayer()!!.openContainer)

    object armor {
        /**
         * @return the item in the player's helmet slot
         */
        @JvmStatic
        val helmet: Item
            get() = getInventory().getStackInSlot(39)

        /**
         * @return the item in the player's chestplate slot
         */
        @JvmStatic
        val chestplate: Item
            get() = getInventory().getStackInSlot(38)

        /**
         * @return the item in the player's leggings slot
         */
        @JvmStatic
        val leggings: Item
            get() = getInventory().getStackInSlot(37)

        /**
         * @return the item in the player's boots slot
         */
        @JvmStatic
        val boots: Item
            get() = getInventory().getStackInSlot(36)
    }
}
