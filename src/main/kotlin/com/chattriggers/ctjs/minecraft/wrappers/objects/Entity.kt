package com.chattriggers.ctjs.minecraft.wrappers.objects

import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCEntity
import com.chattriggers.ctjs.utils.kotlin.MathHelper
import net.minecraft.entity.EntityLivingBase
import java.util.*

@External
open class Entity(val entity: MCEntity) {
    fun getX() = entity.posX

    fun getY() = entity.posY

    fun getZ() = entity.posZ

    /**
     * Gets the pitch, the horizontal direction the entity is facing towards.
     * This has a range of -180 to 180.
     *
     * @return the entity's pitch
     */
    fun getPitch(): Double =
        MathHelper.wrapAngleTo180_float(entity.rotationPitch).toDouble()

    /**
     * Gets the yaw, the vertical direction the entity is facing towards.
     * This has a range of -180 to 180.
     *
     * @return the entity's yaw
     */
    fun getYaw(): Double =
        MathHelper.wrapAngleTo180_float(entity.rotationYaw).toDouble()

    /**
     * Gets the entity's x motion.
     * This is the amount the entity will move in the x direction next tick.
     *
     * @return the player's x motion
     */
    fun getMotionX(): Double = entity.motionX

    /**
     * Gets the entity's y motion.
     * This is the amount the entity will move in the y direction next tick.
     *
     * @return the player's y motion
     */
    fun getMotionY(): Double = entity.motionY

    /**
     * Gets the entity's z motion.
     * This is the amount the entity will move in the z direction next tick.
     *
     * @return the player's z motion
     */
    fun getMotionZ(): Double = entity.motionZ

    /**
     * Gets the entity's health, -1 if not a living entity
     *
     * @return the entity's health
     */
    fun getHP(): Float {
        return if (entity is EntityLivingBase)
            entity.health
        else -1f
    }

    fun getRiding(): Entity? {
        val riding = entity.ridingEntity

        return if (riding == null)
            null
        else
            Entity(riding)
    }

    fun getRider(): Entity? {
        return if (entity.riddenByEntity == null)
            null
        else
            Entity(entity.riddenByEntity)
    }

    fun getRiders(): List<Entity> = listOf()

    /**
     * Checks whether or not the entity is dead.
     * This is a fairly loose term, dead for a particle could mean it has faded,
     * while dead for an entity means it has no health.
     *
     * @return whether or not an entity is dead
     */
    fun isDead(): Boolean = entity.isDead

    /**
     * Gets the entire width of the entity's hitbox
     *
     * @return the entity's width
     */
    fun getWidth(): Float = entity.width

    /**
     * Gets the entire height of the entity's hitbox
     *
     * @return the entity's height
     */
    fun getHeight(): Float = entity.height

    /**
     * Gets the height of the eyes on the entity,
     * can be added to its Y coordinate to get the actual Y location of the eyes.
     * This value defaults to 85% of an entity's height, however is different for some entities.
     *
     * @return the height of the entity's eyes
     */
    fun getEyeHeight(): Float = entity.eyeHeight

    /**
     * Gets the name of the entity, could be "Villager",
     * or, if the entity has a custom name, it returns that.
     *
     * @return the (custom) name of the entity
     */
    open fun getName(): String = entity.name

    /**
     * Gets the Java class name of the entity, for example "EntityVillager"
     *
     * @return the entity's class name
     */
    fun getClassName(): String = entity.javaClass.simpleName

    /**
     * Gets the Java UUID object of this entity.
     * Use of [UUID.toString] in conjunction is recommended.
     *
     * @return the entity's uuid
     */
    fun getUUID(): UUID = entity.uniqueID

    override fun toString(): String {
        return ("Entity{"
                + getName()
                + ",x:" + getX()
                + ",y:" + getY()
                + ",z:" + getZ()
                + "}")
    }
}
