package com.chattriggers.ctjs.minecraft.wrappers.objects

import cc.hyperium.mixinsimp.entity.player.IMixinEntityPlayer
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.scoreboard.ScorePlayerTeam

@External
class PlayerMP(val player: EntityPlayer) : Entity(player) {
    fun isSpectator() = player.isSpectator

    fun getActivePotionEffects(): List<PotionEffect> {
        return player.activePotionEffects.map {
            PotionEffect(it)
        }
    }

    fun getPing(): Int = getPlayerInfo().responseTime

    /**
     * Gets the item currently in the player's specified inventory slot.
     * 0 for main hand, 1-4 for armor
     * (2 for offhand in 1.12.2, and everything else shifted over).
     *
     * @param slot the slot to access
     * @return the item in said slot
     */
    fun getItemInSlot(slot: Int): Item = Item(player.getEquipmentInSlot(slot))

    /**
     * Gets the display name for this player,
     * i.e. the name shown in tab list and in the player's nametag.
     * @return the display name
     */
    fun getDisplayName(): TextComponent = TextComponent(getPlayerName(getPlayerInfo()))

    fun setTabDisplayName(textComponent: TextComponent) {
        getPlayerInfo().displayName = textComponent.chatComponentText
    }

    /**
     * Sets the name for this player shown above their head,
     * in their name tag
     *
     * @param textComponent the new name to display
     */
    fun setNametagName(textComponent: TextComponent) {
        (player as IMixinEntityPlayer).setDisplayName(textComponent.chatComponentText.formattedText)
    }

    private fun getPlayerName(networkPlayerInfoIn: NetworkPlayerInfo): String {
        return networkPlayerInfoIn.displayName?.formattedText
            ?: ScorePlayerTeam.formatPlayerName(
                networkPlayerInfoIn.playerTeam,
                networkPlayerInfoIn.gameProfile.name
            )
    }

    private fun getPlayerInfo(): NetworkPlayerInfo = Client.getConnection().getPlayerInfo(player.uniqueID)

    override fun toString(): String {
        return "PlayerMP{name:" + getName() +
                ",ping:" + getPing() +
                ",entity:" + super.toString() +
                "}"
    }

    override fun getName(): String = player.name
}
