package cc.hyperium.launch.patching.conflicts

import codes.som.anthony.koffee.insns.jvm.*
import codes.som.anthony.koffee.koffee
import org.objectweb.asm.tree.ClassNode
import java.awt.Graphics
import java.awt.Image
import java.awt.image.BufferedImage
import java.awt.image.ImageObserver
import java.io.InputStream

class AbstractResourcePackTransformer : ConflictTransformer {
    override fun getClassName() = "bmx"

    override fun transform(original: ClassNode): ClassNode {
        for (method in original.methods) {
            if (method.name == "a" && method.desc == "()Ljava/awt/image/BufferedImage;") {
                method.instructions.koffee {
                    instructions.clear()
                    aload_0
                    ldc("pack.png")
                    invokevirtual("bmx", "a", InputStream::class, String::class)
                    invokestatic("bml", "a", BufferedImage::class, InputStream::class)
                    astore_1
                    aload_1
                    ifnonnull(L["l2"])
                    aconst_null
                    areturn
                    +L["l2"]
                    new(BufferedImage::class)
                    dup
                    bipush(64)
                    bipush(64)
                    iconst_2
                    invokespecial(BufferedImage::class, "<init>", void, int, int, int)
                    astore_2
                    aload_2
                    invokevirtual(BufferedImage::class, "getGraphics", Graphics::class)
                    astore_3
                    aload_3
                    aload_1
                    iconst_0
                    iconst_0
                    bipush(64)
                    bipush(64)
                    aconst_null
                    invokevirtual(Graphics::class, "drawImage", boolean, Image::class, int, int, int, int, ImageObserver::class)
                    pop
                    aload_3
                    invokevirtual(Graphics::class, "dispose", void)
                    aload_2
                    areturn
                }
            }
        }
        return original
    }
}
