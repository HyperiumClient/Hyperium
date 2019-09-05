package com.chattriggers.ctjs.utils.kotlin

import net.minecraft.client.renderer.Tessellator

fun ITextComponent.getStyling(): TextStyle =
    this.chatStyle!!

fun TextStyle.getClick(): TextClickEvent? =
    chatClickEvent

fun TextStyle.getHover(): TextHoverEvent? =
    chatHoverEvent

fun Tessellator.getRenderer(): WorldRenderer =
    worldRenderer

operator fun String.times(times: Number): String {
    val stringBuilder = StringBuilder()

    for (i in 0..times.toInt()) {
        stringBuilder.append(this)
    }

    return stringBuilder.toString()
}
