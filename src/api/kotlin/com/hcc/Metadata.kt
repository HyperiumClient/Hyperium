/**
 * Contains String Contants of the mod
 */
@file:JvmName("Metadata")
package com.hcc

val MODID
    @JvmName("getModid")
    get() = "HCC"

val VERSION
    @JvmName("getVersion")
    get() = "1.0"

val AUTHORS
    @JvmName("getAuthors")
    get() = arrayOf("Kevin", "CoalOres", "Sk1er", "VRCube")

var FORGE = false
    @JvmName("isUsingForge") get

var OPTIFINE = false
    @JvmName("isUsingOptifine") get