package com.chattriggers.ctjs.engine.langs.js

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.engine.IRegister
import com.chattriggers.ctjs.minecraft.libs.XMLHttpRequest
import com.chattriggers.ctjs.minecraft.objects.display.Display
import com.chattriggers.ctjs.minecraft.objects.display.DisplayLine
import com.chattriggers.ctjs.minecraft.objects.gui.Gui
import jdk.nashorn.api.scripting.ScriptObjectMirror

/*
This file holds the "glue" for this language.

Certain classes have triggers inside of them that need to know what loader to use,
and that's where these implementations come in.
 */

object JSRegister : IRegister {
    override fun getImplementationLoader(): ILoader = JSLoader
}

class JSGui : Gui() {
    override fun getLoader(): ILoader = JSLoader
}

class JSXMLHttpRequest : XMLHttpRequest() {
    override fun getLoader(): ILoader = JSLoader
}

class JSDisplayLine : DisplayLine {
    constructor(text: String) : super(text)
    constructor(text: String, config: ScriptObjectMirror) : super(text, config)

    override fun getLoader(): ILoader = JSLoader
}

class JSDisplay : Display {
    constructor() : super()
    constructor(config: ScriptObjectMirror?) : super(config)

    override fun createDisplayLine(text: String): DisplayLine {
        return JSDisplayLine(text)
    }
}