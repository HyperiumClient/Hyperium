package com.chattriggers.ctjs.minecraft.wrappers.objects

import com.chattriggers.ctjs.utils.kotlin.MCPotionEffect

class PotionEffect(private val effect: MCPotionEffect) {
    fun getName(): String = this.effect.effectName

    fun getAmplifier() = this.effect.amplifier

    fun getDuration() = this.effect.duration

    fun getID(): Int {
        return this.effect.potionID
    }

    fun isAmbient() = this.effect.isAmbient

    fun isDurationMax() = this.effect.isPotionDurationMax

    fun showsParticles(): Boolean {
        return this.effect.isShowParticles
    }

    override fun toString() = this.effect.toString()
}