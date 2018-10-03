package com.chattriggers.ctjs.minecraft.wrappers.objects

import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCPotionEffect

@External
class PotionEffect(private val effect: MCPotionEffect) {
    fun getName(): String = this.effect.effectName

    fun getAmplifier(): Int = this.effect.amplifier

    fun getDuration(): Int = this.effect.duration

    fun getID(): Int {
        return this.effect.potionID
    }

    fun isAmbient(): Boolean = this.effect.isAmbient

    fun isDurationMax(): Boolean = this.effect.isPotionDurationMax

    fun showsParticles(): Boolean {
        return this.effect.isShowParticles
    }

    override fun toString() = this.effect.toString()
}