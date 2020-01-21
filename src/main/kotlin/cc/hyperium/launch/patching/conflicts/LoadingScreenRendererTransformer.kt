package cc.hyperium.launch.patching.conflicts

import cc.hyperium.config.Settings
import cc.hyperium.gui.loading.HyperiumLoadingScreen
import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import net.minecraft.client.LoadingScreenRenderer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.shader.Framebuffer
import org.objectweb.asm.tree.ClassNode

class LoadingScreenRendererTransformer : ConflictTransformer {
    override fun getClassName() = "avi"

    override fun transform(original: ClassNode): ClassNode {
        for (method in original.methods) {
            if (method.name == "setLoadingProgress") {
                val renderHyperiumScreen = assembleBlock {
                    getstatic(Settings::class, "HYPERIUM_LOADING_SCREEN", boolean)
                    ifeq(L["4"])
                    aload_0
                    getfield(LoadingScreenRenderer::class, "systemTime", long)
                    aload_0
                    getfield(LoadingScreenRenderer::class, "framebuffer", Framebuffer::class)
                    aload_0
                    getfield(LoadingScreenRenderer::class, "mc", Minecraft::class)
                    aload_0
                    getfield(LoadingScreenRenderer::class, "scaledResolution", ScaledResolution::class)
                    aload_0
                    getfield(LoadingScreenRenderer::class, "currentlyDisplayedText", String::class)
                    aload_0
                    getfield(LoadingScreenRenderer::class, "message", String::class)
                    iload_1
                    invokestatic(
                        HyperiumLoadingScreen::class,
                        "renderHyperiumLoadingScreen",
                        void,
                        long,
                        Framebuffer::class,
                        Minecraft::class,
                        ScaledResolution::class,
                        String::class,
                        String::class,
                        int
                    )
                    _return
                    +L["4"]
                }.first
            }
        }

        return original
    }
}
