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

package cc.hyperium.event

import cc.hyperium.event.minigames.Minigame
import cc.hyperium.mixinsimp.renderer.model.IMixinModelBiped
import com.mojang.authlib.GameProfile
import net.minecraft.client.audio.ISound
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.renderer.RenderGlobal
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.network.INetHandler
import net.minecraft.network.Packet
import net.minecraft.scoreboard.ScoreObjective
import net.minecraft.util.*
import java.lang.reflect.Method
import java.util.*

/**
 * Used to store information about events and index so they can be easily accessed by ASM
 */
data class EventSubscriber(val instance: Any, val method: Method, val priority: Priority)

/**
 * Assign to a method to invoke an event
 * The first parameter of the method should be the event it is calling
 */
annotation class InvokeEvent(val priority: Priority = Priority.NORMAL)

/**
 * Invoked once the client has started
 */
class PreInitializationEvent

/**
 * Invoked once the client has started
 */
class InitializationEvent

/**
 * Invoked once the player has joined a server
 */
class ServerJoinEvent(val server: String, val port: Int)

/**
 * Invoked once the player has left a server
 */
class ServerLeaveEvent

/**
 * Invoked when the world is changed
 */
class WorldChangeEvent

/**
 * Invoked once left mouse is pressed
 */
class LeftMouseClickEvent

/**
 * Invoked once right mouse is pressed
 */
class RightMouseClickEvent

/**
 * Invoked once a mouse button is pressed
 */
class MouseButtonEvent(val value: Int)

/**
 * Invoked once a key is pressed
 */
class KeypressEvent(val key: Int, val isRepeat: Boolean)

/**
 * Invoked when the spawnpoint has changed
 * This is useful for detecting minigames
 */
class SpawnpointChangeEvent(val blockPos: BlockPos)

/**
 * Invoked every tick
 */
class TickEvent

/**
 * Invoked every frame; used to render in the ingame GUI
 */
class RenderGuiEvent

/**
 * Invoked every frame; used to render in the 3D space
 */
class RenderEvent

/**
 * Invoked when the hud of the client is rendered
 */
class RenderHUDEvent(partialTicks: Float)

/**
 * Invoked once a packet is received by the client, right before it is processed
 */
class PacketReceivedEvent(var packet: Packet<out INetHandler>)

/**
 * Invoked once a chat message is sent
 */
class ChatEvent(var chat: IChatComponent) : CancellableEvent()

/**
 * Invoked when a chat packet is received, will not detect messages directly printed to the chat
 */
class ServerChatEvent(val type: Byte, var chat: IChatComponent) : CancellableEvent()

/**
 * Invoked when the player presses the enter key in the GuiChat class
 *
 * @param message the message the player is sending
 */
class SendChatMessageEvent(val message: String) : CancellableEvent()

/**
 * Invoked when a gui screen is opened
 *
 * @param gui the gui that is being opened
 */
class GuiOpenEvent(var gui: GuiScreen?) : CancellableEvent()

/**
 * Invoked when a user clicks within a GuiScreen.
 *
 * @param mouseX x position of the mouse on click
 * @param mouseY y position of the mouse on click
 * @param button Mouse button clicked (0 = left, 1 = right, 2 = middle)
 * @param gui GUI that detected the click
 */
class GuiClickEvent(var mouseX: Int, var mouseY: Int, var button: Int, var gui: GuiScreen?) : CancellableEvent()

/**
 * Invoked when a players cape is grabbed from the game
 * code, this allows easy modification of that cape
 *
 * @param cape the cape that will be used (may be null)
 */
class PlayerGetCapeEvent(val profile: GameProfile, var cape: ResourceLocation?)

/**
 * Invoked when a players skin is grabbed from the game
 * code, this allows easy modification of the players skin
 *
 * @param skin the skin that will be used (may be null)
 */
class PlayerGetSkinEvent(val profile: GameProfile, var skin: ResourceLocation?)

/**
 * Invoked once the player has joined singleplayer
 */
class SingleplayerJoinEvent

/**
 * Invoked once player swings
 */
class PlayerSwingEvent(val player: UUID, val posVec: Vec3, val lookVec: Vec3, val pos: BlockPos)

/**
 * Invoked when player joins a minigame on Hypixel
 */
class JoinMinigameEvent(val minigame: Minigame)

/**
 * Invoked when a player model is rendered
 */
class RenderPlayerEvent(val entity: AbstractClientPlayer, val renderManager: RenderManager, val x: Double, val y: Double, val z: Double, val partialTicks: Float)

/**
 * Invoked when received a friend request is received
 */
class HypixelFriendRequestEvent(val from: String)

/**
 * Invoked when the player receives a party invite
 */
class HypixelPartyInviteEvent(val from: String)

/**
 * Invoked when player(s) win a game
 */
class HypixelWinEvent(val winners: List<String>)

/**
 * Invoked when the selected item is about to be rendered
 */
class RenderSelectedItemEvent(val scaledRes: ScaledResolution)

/**
 * Called when the player joins hypixel
 * @param method method used to verify the player is online Hypixel
 */
class JoinHypixelEvent(val method: ServerVerificationMethod)

/**
 * Called when the player joins badlion
 * @param method method used to verify the player is online Badlion
 */
class JoinBadlionEvent(val method: ServerVerificationMethod)

/**
 * All the methods used by HypixelDetector to detect Hypixel or Badlion
 * This is used by the above two events
 */
enum class ServerVerificationMethod {
    IP, MOTD
}

/**
 * Called when the game is shutting down, use this to save your configs
 */
class GameShutDownEvent

class ServerSwitchEvent(val from: String, val to: String)

class KillEvent(val user: String)

class EntityRenderEvent(val entityIn: Entity,
                        val model: ModelBiped, val p_78088_2_: Float,
                        val p_78088_3_: Float, val p_78088_4_: Float,
                        val p_78088_5_: Float, val p_78088_6_: Float, val scale: Float) : CancellableEvent()

/**
 * Invoked when the scoreboard is rendered
 */
class RenderScoreboardEvent(val x: Double, val y: Double, val objective: ScoreObjective, val resolution: ScaledResolution) : CancellableEvent()

class DrawBlockHighlightEvent(val context: RenderGlobal, val player: EntityPlayer, val target: MovingObjectPosition, val subID: Int, val currentItem: ItemStack?, val partialTicks: Float) : CancellableEvent()

/**
 * Get called before the angles of the upperleg gets copied into the lower leg etc
 * Edit the player rotation here, if the upperleg and the lowerleg need the same roations
 */
class PreCopyPlayerModelAnglesEvent(val entity: AbstractClientPlayer, val model: IMixinModelBiped)

/**
 * Get called after the angles of the upperleg gets copied into the lower leg etc
 * Edit the player rotation here, if the upperleg and the lowerleg need other roations
 */
class PostCopyPlayerModelAnglesEvent(val entity: AbstractClientPlayer, val model: IMixinModelBiped)

class SoundPlayEvent(val sound: ISound) : CancellableEvent()

enum class ElementType {
    ALL,
    HELMET,
    PORTAL,
    CROSSHAIR,
    BOSS_HEALTH,
    ARMOR,
    HEALTH,
    FOOD,
    AIR,
    HOTBAR,
    EXPERIENCE,
    TEXT,
    HEALTH_MOUNT,
    JUMP_BAR,
    CHAT,
    PLAYER_LIST,
    DEBUG
}