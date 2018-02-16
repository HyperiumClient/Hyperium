/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hcc.event

import com.esotericsoftware.reflectasm.MethodAccess
import com.hcc.event.minigames.Minigame
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.util.BlockPos
import net.minecraft.util.IChatComponent
import java.util.*

/**
 * Used to store information about events and index so they can be easily accessed by ASM
 */
data class EventSubscriber(val instance: Any, val methodAccess: MethodAccess, val mIndex: Int, val priority: Priority)

/**
 * Assign to a method to invoke an event
 * The first parameter of the method should be the event it is calling
 */
annotation class InvokeEvent(val priority: Priority = Priority.NORMAL)

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
 * Invoked once left mouse is pressed
 */
class LeftMouseClickEvent

/**
 * Invoked once right mouse is pressed
 */
class RightMouseClickEvent

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
 * Invoked every tick; used to render
 */
class RenderEvent

class RenderHUDEvent(partialTicks: Float)

/**
 * Invoked once a chat message is sent
 */
class ChatEvent(var chat: IChatComponent): CancellableEvent()

/**
 * Invoked when the player presses the enter key in the GuiChat class
 */
class SendChatMessageEvent(val message: String): CancellableEvent()

/**
 * Invoked once the player has joined singleplayer
 */
class SingleplayerJoinEvent

/**
 * Invoked once player swings
 */
class PlayerSwingEvent(val player: UUID)

/**
 * Invoked when player joins a minigame on Hypixel
 */
class JoinMinigameEvent(val minigame: Minigame)

/**
 * Invoked when a player model is rendered
 */
class RenderPlayerEvent(val entity: AbstractClientPlayer, val renderManager: RenderManager, val x: Double, val y: Double, val z: Double)

/**
 * Invoked when a keybind is enabled.
 */
class KeyBindEnableEvent(val key: Int)

/**
 * Invoked when a keybind is disabled.
 */
class KeyBindDisableEvent(val key: Int)

