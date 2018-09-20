package com.chattriggers.ctjs.engine.module

import java.util.ArrayList

data class ModuleMetadata(
        val name: String? = null,
        val version: String? = null,
        val tags: ArrayList<String>? = null,
        val pictureLink: String? = null,
        val creator: String? = null,
        val description: String? = null,
        val requires: ArrayList<String>? = null,
        val ignored: ArrayList<String>? = null,
        var fileName: String? = null,
        var isRequired: Boolean = false
) {
    val isDefault: Boolean
        get() = name == null
}
