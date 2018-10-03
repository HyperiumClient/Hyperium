package com.chattriggers.ctjs.utils.console

import java.io.File
import java.io.OutputStream
import java.io.PrintStream
import javax.swing.JTextArea

class TextAreaOutputStream(private val textArea: JTextArea, lang: String) : OutputStream() {
    private val buffer = StringBuilder(128)

    init {
        val file = File("./logs/ctjs-$lang.log")
        file.delete()
        file.createNewFile()
    }

    private val logger = File("./logs/ctjs-$lang.log").bufferedWriter()

    val printStream = PrintStream(this)

    override fun write(b: Int) {
        val letter = b.toChar()
        buffer.append(letter)

        if (letter == '\n') {
            val line = buffer.toString()

            textArea.append(line)
            logger.append(line)

            buffer.delete(0, buffer.length)
        }
    }

    fun println(line: Any) {
        printStream.println(line)
    }

    fun clear() {
        textArea.text = ""
    }
}