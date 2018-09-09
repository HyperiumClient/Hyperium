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

import cc.hyperium.mixinsimp.renderer.model.IMixinModelBiped
import cc.hyperium.purchases.HyperiumPurchase
import com.mojang.authlib.GameProfile
import net.minecraft.client.audio.ISound
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.item.ItemStack
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
 * Invoked once player swings
 */
class PlayerSwingEvent(val player: UUID, val posVec: Vec3, val lookVec: Vec3, val pos: BlockPos)

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
 * All the methods used by HypixelDetector to detect Hypixel or Badlion
 * This is used by the above two events
 */
enum class ServerVerificationMethod {
    IP, MOTD
}

class ServerSwitchEvent(val from: String, val to: String)

class KillEvent(val user: String)

class EntityRenderEvent(val entityIn: Entity,
                        val posX: Float,
                        val posY: Float, val posZ: Float,
                        val pitch: Float, val yaw: Float, val scale: Float) : CancellableEvent()

/**
 * Invoked when the scoreboard is rendered
 */
class RenderScoreboardEvent(val x: Double, val y: Double, val objective: ScoreObjective, val resolution: ScaledResolution) : CancellableEvent()

class DrawBlockHighlightEvent(val player: EntityPlayer, val target: MovingObjectPosition, val partialTicks: Float) : CancellableEvent()

/**
 * Get called before the angles of the upperleg gets copied into the lower leg etc
 * Edit the player rotation here, if the upperleg and the lowerleg need the same roations
 */
class PreCopyPlayerModelAnglesEvent(entity: AbstractClientPlayer, model: IMixinModelBiped) : CopyPlayerModelAnglesEvent(entity, model)

/**
 * Get called after the angles of the upperleg gets copied into the lower leg etc
 * Edit the player rotation here, if the upperleg and the lowerleg need other roations
 */
class PostCopyPlayerModelAnglesEvent(entity: AbstractClientPlayer, model: IMixinModelBiped) : CopyPlayerModelAnglesEvent(entity, model)

/**
 * Called when this player attacks an entity
 */
class PlayerAttackEntityEvent(val uuid: UUID, val entity: Entity)

class PurchaseLoadEvent(val uuid: UUID, val purchase: HyperiumPurchase, val self: Boolean)

class FriendRemoveEvent(val fullName: String, val name: String)

class ArrowShootEvent(val arrow: EntityArrow, val charge: Int, val bow: ItemStack)

class LivingDeathEvent(val entity: EntityLivingBase, val cause: DamageSource)

abstract class CopyPlayerModelAnglesEvent(val entity: AbstractClientPlayer, val model: IMixinModelBiped)

class SoundPlayEvent(val sound: ISound) : CancellableEvent()


class LevelupEvent(val level: Int)

class AchievementGetEvent(val achievement: String)

/**
 * Called when entities are about to be rendered in the world
 */
class RenderEntitiesEvent(val partialTicks: Float)

class ItemPickupEvent(val player: EntityPlayer, val item: EntityItem) : Event()

class ItemTossEvent(val player: EntityPlayer, val item: EntityItem) : Event()

class ItemTooltipEvent(val item: ItemStack, val toolTip: List<String>) : Event()

class RenderWorldEvent(val partialTicks: Float) : Event()

class WorldLoadEvent() : Event()

class WorldUnloadEvent() : Event()

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
