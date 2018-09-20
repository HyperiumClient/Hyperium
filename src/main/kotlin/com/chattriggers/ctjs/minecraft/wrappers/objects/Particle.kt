package com.chattriggers.ctjs.minecraft.wrappers.objects

import com.chattriggers.ctjs.minecraft.mixins.MixinEntityFX
import com.chattriggers.ctjs.utils.kotlin.MCParticle

class Particle(val underlyingEntity: MCParticle) {
    fun scale(scale: Float) {
        this.underlyingEntity.multipleParticleScaleBy(scale)
    }

    fun multiplyVelocity(multiplier: Float) {
        this.underlyingEntity.multiplyVelocity(multiplier)
    }

    fun setColor(r: Float, g: Float, b: Float) {
        this.underlyingEntity.setRBGColorF(r, g, b)
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
        this.underlyingEntity.setAlphaF(a)
    }

    /**
     * Sets the amount of ticks this particle will live for
     *
     * @param maxAge the particles max age (in ticks)
     */
    fun setMaxAge(maxAge: Int) {
        (this.underlyingEntity as MixinEntityFX).particleMaxAge = maxAge
    }

    fun remove() {
        //#if MC<=10809
        this.underlyingEntity.setDead()
        //#else
        //$$ this.underlyingEntity.setExpired();
        //#endif
    }
}