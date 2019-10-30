package com.chattriggers.ctjs.utils.console

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.triggers.OnTrigger
import com.chattriggers.ctjs.utils.config.Config
import net.minecraft.network.ThreadQuickExitException
import java.awt.*
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.WindowEvent
import java.io.PrintStream
import javax.swing.*

class Console(val loader: ILoader?) {
    private val frame: JFrame = JFrame("ct.js ${loader?.getLanguageName() ?: "Default"} Console")
    private val taos: TextAreaOutputStream
    private val components = mutableListOf<Component>()
    private val history = mutableListOf<String>()
    private var historyOffset = 0

    val out: PrintStream

    init {
        frame.defaultCloseOperation = JFrame.HIDE_ON_CLOSE

        val textArea = JTextArea()
        taos = TextAreaOutputStream(textArea, loader?.getLanguageName() ?: "default")
        textArea.isEditable = false
        textArea.font = Font("DejaVu Sans Mono", Font.PLAIN, 15)
        val inputField = JTextField(1)
        inputField.isFocusable = true

        inputField.margin = Insets(5, 5, 5, 5)
        textArea.margin = Insets(5, 5, 5, 5)

        components.add(textArea)
        components.add(inputField)

        out = taos.printStream

        inputField.addKeyListener(object : KeyListener {
            override fun keyTyped(e: KeyEvent) {}

            override fun keyPressed(e: KeyEvent) {}

            override fun keyReleased(e: KeyEvent) {
                when (e.keyCode) {
                    KeyEvent.VK_ENTER -> {
                        var toPrint: Any? = null

                        val command = inputField.text
                        inputField.text = ""
                        history.add(command)
                        historyOffset = 0

                        try {
                            toPrint = loader?.eval(command)
                        } catch (error: ThreadQuickExitException) {
                        } catch (e: Exception) {
                            printStackTrace(e)
                            toPrint = "> $command"
                        }

                        taos.println(toPrint ?: command)
                    }

                    KeyEvent.VK_UP -> {
                        historyOffset++

                        try {
                            val message = history[history.size - historyOffset]
                            inputField.text = message
                        } catch (exception: Exception) {
                            historyOffset--
                        }
                    }

                    KeyEvent.VK_DOWN -> {
                        historyOffset--

                        if (historyOffset < 0) historyOffset = 0

                        try {
                            val message = history[history.size - historyOffset]
                            inputField.text = message
                        } catch (exception: Exception) {
                            historyOffset = 0
                            inputField.text = ""
                        }
                    }
                }
            }
        })

        frame.add(JScrollPane(textArea))
        frame.add(inputField, BorderLayout.SOUTH)
        frame.pack()
        frame.isVisible = false
        frame.setSize(800, 600)
    }

    fun clearConsole() {
        taos.clear()
    }

    fun printStackTrace(error: Throwable) {
        if (Config.openConsoleOnError) {
            showConsole()
        }

        error.printStackTrace(out)
    }

    fun printStackTrace(error: Throwable, trigger: OnTrigger) {
        printStackTrace(error)
    }

    fun printDeprecatedWarning(method: String) {
        out.println("WARNING: Use of deprecated method $method")
    }

    fun showConsole() {
        frame.isVisible = true

        val bg: Color
        val fg: Color

        if (Config.customTheme) {
            bg = Config.consoleBackgroundColor
            fg = Config.consoleForegroundColor
        } else {
            when (Config.consoleTheme) {
                "ashes.dark" -> {
                    bg = Color(28, 32, 35)
                    fg = Color(199, 204, 209)
                }
                "atelierforest.dark" -> {
                    bg = Color(28, 32, 35)
                    fg = Color(199, 204, 209)
                }
                "isotope.dark" -> {
                    bg = Color(0, 0, 0)
                    fg = Color(208, 208, 208)
                }
                "codeschool.dark" -> {
                    bg = Color(22, 27, 29)
                    fg = Color(126, 162, 180)
                }
                "gotham" -> {
                    bg = Color(10, 15, 20)
                    fg = Color(152, 209, 206)
                }
                "hybrid" -> {
                    bg = Color(29, 31, 33)
                    fg = Color(197, 200, 198)
                }
                "3024.light" -> {
                    bg = Color(247, 247, 247)
                    fg = Color(74, 69, 67)
                }
                "chalk.light" -> {
                    bg = Color(245, 245, 245)
                    fg = Color(48, 48, 48)
                }
                "blue" -> {
                    bg = Color(15, 18, 32)
                    fg = Color(221, 223, 235)
                }
                "slate" -> {
                    bg = Color(33, 36, 41)
                    fg = Color(193, 199, 208)
                }
                "red" -> {
                    bg = Color(26, 9, 11)
                    fg = Color(231, 210, 212)
                }
                "green" -> {
                    bg = Color(6, 10, 10)
                    fg = Color(47, 227, 149)
                }
                "aids" -> {
                    bg = Color(251, 251, 28)
                    fg = Color(192, 20, 214)
                }
                "default.dark" -> {
                    bg = Color(21, 21, 21)
                    fg = Color(208, 208, 208)
                }
                else -> {
                    bg = Color(21, 21, 21)
                    fg = Color(208, 208, 208)
                }
            }
        }

        for (comp in components) {
            comp.background = bg
            comp.foreground = fg
        }

        frame.toFront()
        frame.repaint()
    }

    protected fun finalize() {
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.dispatchEvent(WindowEvent(frame, WindowEvent.WINDOW_CLOSING))
    }
}
