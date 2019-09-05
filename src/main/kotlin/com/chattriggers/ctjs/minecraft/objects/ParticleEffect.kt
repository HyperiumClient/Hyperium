package com.chattriggers.ctjs.minecraft.objects

import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCParticle

@External
class ParticleEffect constructor(
    x: Double,
    y: Double,
    z: Double,
    xSpeed: Double = 0.0,
    ySpeed: Double = 0.0,
    zSpeed: Double = 0.0
) : MCParticle(World.getWorld(), x, y, z, xSpeed, ySpeed, zSpeed) {
    fun scale(scale: Float) = apply { super.multipleParticleScaleBy(scale) }

    override fun multiplyVelocity(multiplier: Float) = apply { super.multiplyVelocity(multiplier) }

    fun setColor(r: Float, g: Float, b: Float, a: Float? = null) = apply {
        super.setRBGColorF(r, g, b)
        if (a != null) alpha = a
    }

    fun setColor(color: Int) = apply {
        setColor(
            (color shr 16 and 255).toFloat() / 255.0f,
            (color shr 8 and 255).toFloat() / 255.0f,
            (color and 255).toFloat() / 255.0f,
            (color shr 24 and 255).toFloat() / 255.0f
        )
    }

    fun setAlpha(a: Float) = apply { super.setAlphaF(a) }

    fun remove() = apply {
        super.setDead()
    }
}
