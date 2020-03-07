package cc.hyperium.launch.patching.conflicts

import cc.hyperium.Hyperium
import cc.hyperium.gui.GuiAddonError
import cc.hyperium.gui.GuiHyperiumScreenMainMenu
import cc.hyperium.gui.GuiHyperiumScreenTos
import cc.hyperium.handlers.HyperiumHandlers
import cc.hyperium.handlers.handlers.GuiDisplayHandler
import cc.hyperium.internal.addons.AddonMinecraftBootstrap
import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
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
                    it.instructions = assembleBlock {
                        getstatic(Hyperium::class, "INSTANCE", Hyperium::class)
                        invokevirtual(Hyperium::class, "isAcceptedTos", boolean)
                        ifeq(L["label"])
                        aload_0
                        invokevirtual("net/minecraft/client/gui/GuiMainMenu", "drawDefaultBackground", void)
                        +L["label"]
                        _return
                    }.first
                }

                "drawScreen" -> {
                    it.instructions = assembleBlock {
                        getstatic(Hyperium::class, "INSTANCE", Hyperium::class)
                        invokevirtual(Hyperium::class, "isAcceptedTos", boolean)
                        ifne(L["l1"])
                        getstatic(Hyperium::class, "LOGGER", Logger::class)
                        ldc("Hasn't accepted! Redirecting them!")
                        invokeinterface(Logger::class, "info", void, String::class)
                        getstatic(Hyperium::class, "INSTANCE", Hyperium::class)
                        invokevirtual(Hyperium::class, "getHandlers", HyperiumHandlers::class)
                        invokevirtual(HyperiumHandlers::class, "getGuiDisplayHandler", GuiDisplayHandler::class)
                        new(GuiHyperiumScreenTos::class)
                        dup
                        invokespecial(GuiHyperiumScreenTos::class, "<init>", void)
                        invokevirtual(GuiDisplayHandler::class, "setDisplayNextTick", void, GuiScreen::class)
                        goto(L["l4"])
                        +L["l1"]
                        getstatic(Hyperium::class, "INSTANCE", Hyperium::class)
                        invokevirtual(Hyperium::class, "getHandlers", HyperiumHandlers::class)
                        invokevirtual(HyperiumHandlers::class, "getGuiDisplayHandler", GuiDisplayHandler::class)
                        invokestatic(AddonMinecraftBootstrap::class, "getDependenciesLoopMap", ConcurrentHashMap::class)
                        invokevirtual(ConcurrentHashMap::class, "isEmpty", boolean)
                        ifeq(L["l5"])
                        invokestatic(
                            AddonMinecraftBootstrap::class,
                            "getMissingDependenciesMap",
                            ConcurrentHashMap::class
                        )
                        invokevirtual(ConcurrentHashMap::class, "isEmpty", boolean)
                        ifne(L["l7"])
                        +L["l5"]
                        new(GuiAddonError::class)
                        dup
                        invokespecial(GuiAddonError::class, "<init>", void)
                        goto(L["l8"])
                        +L["l7"]
                        new(GuiHyperiumScreenMainMenu::class)
                        dup
                        invokespecial(GuiHyperiumScreenMainMenu::class, "<init>", void)
                        +L["l8"]
                        invokevirtual(GuiDisplayHandler::class, "setDisplayNextTick", void, GuiScreen::class)
                        +L["l4"]
                        _return
                    }.first
                }
            }
        }
        return original
    }
}
