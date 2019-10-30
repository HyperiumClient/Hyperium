package com.chattriggers.ctjs.minecraft.wrappers.objects

import cc.hyperium.mixins.client.particle.IMixinEntityFX
import com.chattriggers.ctjs.utils.kotlin.MCParticle

class Particle(val underlyingEntity: MCParticle) {
    fun scale(scale: Float) {
        underlyingEntity.multipleParticleScaleBy(scale)
    }

    fun multiplyVelocity(multiplier: Float) {
        underlyingEntity.multiplyVelocity(multiplier)
    }

    fun setColor(r: Float, g: Float, b: Float) {
        underlyingEntity.setRBGColorF(r, g, b)
    }

    fun setColor(r: Float, g: Float, b: Float, a: Float) {
        setColor(r, g, b)
        setAlpha(a)
    }

    fun setColor(color: Long) {
        val red = (color shr 16 and 255).toFloat() / 255.0f
        val blue = (color shr 8 and 255).toFloat() / 255.0f
        val green = (color and 255).toFloat() / 255.0f
        val alpha = (color shr 24 and 255).toFloat() / 255.0f

        setColor(red, green, blue, alpha)
    }

    fun setAlpha(a: Float) {
        underlyingEntity.setAlphaF(a)
    }

    /**
     * Sets the amount of ticks this particle will live for
     *
     * @param maxAge the particles max age (in ticks)
     */
    fun setMaxAge(maxAge: Int) {
        (underlyingEntity as IMixinEntityFX).setParticleMaxAge(maxAge)
    }

    fun remove() {
        underlyingEntity.setDead()
    }
}
