package cc.hyperium.launch.patching.conflicts

import cc.hyperium.Hyperium
import cc.hyperium.gui.GuiAddonError
import cc.hyperium.gui.GuiHyperiumScreenMainMenu
import cc.hyperium.gui.GuiHyperiumScreenTos
import cc.hyperium.handlers.HyperiumHandlers
import cc.hyperium.handlers.handlers.GuiDisplayHandler
import cc.hyperium.internal.addons.AddonMinecraftBootstrap
import codes.som.anthony.koffee.insns.jvm.*
import codes.som.anthony.koffee.koffee
import net.minecraft.client.gui.GuiScreen
import org.apache.logging.log4j.Logger
import org.objectweb.asm.tree.ClassNode
import java.util.concurrent.ConcurrentHashMap

class GuiMainMenuTransformer : ConflictTransformer {
    override fun getClassName() = "aya"

    override fun transform(original: ClassNode): ClassNode {
        original.methods.forEach {
            when (it.name) {
                "initGui" -> {
                    it.instructions.clear()
                    it.localVariables.clear()
                    it.tryCatchBlocks.clear()
                    it.instructions.koffee {
                        getstatic(Hyperium::class, "INSTANCE", Hyperium::class)
                        invokevirtual(Hyperium::class, "isAcceptedTos", boolean)
                        ifeq(L["1"])
                        aload_0
                        invokevirtual("net/minecraft/client/gui/GuiMainMenu", "drawDefaultBackground", void)
                        +L["1"]
                        _return
                    }
                }

                "drawScreen" -> {
                    it.instructions.clear()
                    it.localVariables.clear()
                    it.tryCatchBlocks.clear()
                    it.instructions.koffee {
                        getstatic(Hyperium::class, "INSTANCE", Hyperium::class)
                        invokevirtual(Hyperium::class, "getHandlers", HyperiumHandlers::class)
                        invokevirtual(HyperiumHandlers::class, "getGuiDisplayHandler", GuiDisplayHandler::class)
                        astore(4)
                        getstatic(Hyperium::class, "INSTANCE", Hyperium::class)
                        invokevirtual(Hyperium::class, "isAcceptedTos", boolean)
                        ifne(L["2"])
                        getstatic(Hyperium::class, "INSTANCE", Hyperium::class)
                        invokevirtual(Hyperium::class, "isDevEnv", boolean)
                        ifne(L["2"])
                        getstatic(Hyperium::class, "LOGGER", Logger::class)
                        ldc("Hasn't accepted! Redirecting them!")
                        invokeinterface(Logger::class, "info", void, String::class)
                        aload(4)
                        new(GuiHyperiumScreenTos::class)
                        dup
                        invokespecial(GuiHyperiumScreenTos::class, "<init>", void)
                        invokevirtual(GuiDisplayHandler::class, "setDisplayNextTick", void, GuiScreen::class)
                        goto(L["5"])
                        +L["2"]
                        invokestatic(AddonMinecraftBootstrap::class, "getDependenciesLoopMap", ConcurrentHashMap::class)
                        invokevirtual(ConcurrentHashMap::class, "isEmpty", boolean)
                        ifeq(L["6"])
                        invokestatic(AddonMinecraftBootstrap::class, "getMissingDependenciesMap", ConcurrentHashMap::class)
                        invokevirtual(ConcurrentHashMap::class, "isEmpty", boolean)
                        ifne(L["7"])
                        +L["6"]
                        aload(4)
                        new(GuiAddonError::class)
                        dup
                        invokespecial(GuiAddonError::class, "<init>", void)
                        invokevirtual(GuiDisplayHandler::class, "setDisplayNextTick", void, GuiScreen::class)
                        goto(L["5"])
                        aload(4)
                        new(GuiHyperiumScreenMainMenu::class)
                        dup
                        invokespecial(GuiHyperiumScreenMainMenu::class, "<init>", void)
                        invokevirtual(GuiDisplayHandler::class, "setDisplayNextTick", void, GuiScreen::class)
                        +L["5"]
                        _return
                    }
                }
            }
        }
        return original
    }
}
