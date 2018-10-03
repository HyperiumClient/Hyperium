package com.chattriggers.ctjs.minecraft.libs

import com.chattriggers.ctjs.utils.kotlin.External

@External
object MathLib {
    /**
     * Maps a number from one range to another.
     *
     * @param number  the number to map
     * @param in_min  the original range min
     * @param in_max  the original range max
     * @param out_min the final range min
     * @param out_max the final range max
     * @return the re-mapped number
     */
    @JvmStatic
    fun map(number: Float, in_min: Float, in_max: Float, out_min: Float, out_max: Float): Float {
        return (number - in_min) * (out_max - out_min) / (in_max - in_min) + out_min
    }

    /**
     * Clamps a floating number between two values.
     *
     * @param number the number to clamp
     * @param min    the minimum
     * @param max    the maximum
     * @return the clamped number
     */
    @JvmStatic
    fun clampFloat(number: Float, min: Float, max: Float): Float {
        return if (number < min) min else if (number > max) max else number
    }


    /**
     * Clamps an integer number between two values.
     *
     * @param number the number to clamp
     * @param min    the minimum
     * @param max    the maximum
     * @return the clamped number
     */
    @JvmStatic
    fun clamp(number: Int, min: Int, max: Int): Int {
        return if (number < min) min else if (number > max) max else number
    }
}