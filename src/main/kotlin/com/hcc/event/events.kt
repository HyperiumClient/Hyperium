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
import net.minecraft.util.BlockPos
import net.minecraft.util.IChatComponent

/**
 * Used to store information about events and index so they can be easily accessed by ASM
 */
data class EventSubscriber(val instance: Any, val methodAccess: MethodAccess, val mIndex: Int)

/**
 * Assign to a method to invoke an event
 * The first parameter of the method should be the event it is calling
 */
annotation class InvokeEvent

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

/**
 * Invoked once a chat message is sent
 */
class ChatEvent(val chat: IChatComponent)

/**
 * Invoked once the player has joined singleplayer
 */
class SingleplayerJoinEvent