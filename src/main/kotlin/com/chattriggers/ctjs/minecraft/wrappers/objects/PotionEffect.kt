package com.chattriggers.ctjs.minecraft.wrappers.objects

import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCPotionEffect

@External
class PotionEffect(private val effect: MCPotionEffect) {
    fun getName(): String = effect.effectName

    fun getAmplifier(): Int = effect.amplifier

    fun getDuration(): Int = effect.duration

    fun getID(): Int = effect.potionID

    fun isAmbient(): Boolean = effect.isAmbient

    fun isDurationMax(): Boolean = effect.isPotionDurationMax

    fun showsParticles(): Boolean = effect.isShowParticles

    override fun toString() = effect.toString()
}
