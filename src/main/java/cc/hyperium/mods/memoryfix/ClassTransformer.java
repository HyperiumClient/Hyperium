package cc.hyperium.mods.memoryfix;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.RemappingClassAdapter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Iterator;
import java.util.function.BiConsumer;

/**
 * Optifine capes memory leak
 *
 * @author prplz
 */
public class ClassTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        switch (name) {
            case "CapeUtils":
                // Use our CapeImageBuffer instead of OptiFine's
                return transformCapeUtils(bytes);
            case "io.prplz.memoryfix.CapeImageBuffer":
                // Redirect our stub calls to optifine
                return transformMethods(bytes, this::transformCapeImageBuffer);
            default:
                return bytes;
        }
    }

    private byte[] transformMethods(byte[] bytes, BiConsumer<ClassNode, MethodNode> transformer) {
        ClassReader classReader = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        classNode.methods.forEach(m -> transformer.accept(classNode, m));

        ClassWriter classWriter = new ClassWriter(0);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

    private byte[] transformCapeUtils(byte[] bytes) {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        RemappingClassAdapter adapter = new RemappingClassAdapter(classWriter, new Remapper() {
            @Override
            public String map(String typeName) {
                if (typeName.equals("CapeUtils$1")) {
                    return "io.prplz.memoryfix.CapeImageBuffer".replace('.', '/');
                }
                return typeName;
            }
        });

        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(adapter, ClassReader.EXPAND_FRAMES);
        return classWriter.toByteArray();
    }

    private void transformCapeImageBuffer(ClassNode clazz, MethodNode method) {
        Iterator<AbstractInsnNode> iter = method.instructions.iterator();
        while (iter.hasNext()) {
            AbstractInsnNode insn = iter.next();
            if (insn instanceof MethodInsnNode) {
                MethodInsnNode methodInsn = (MethodInsnNode) insn;
                if (methodInsn.name.equals("parseCape")) {
                    methodInsn.owner = "CapeUtils";
                } else if (methodInsn.name.equals("setLocationOfCape")) {
                    methodInsn.setOpcode(Opcodes.INVOKEVIRTUAL);
                    methodInsn.owner = "net/minecraft/client/entity/AbstractClientPlayer";
                    methodInsn.desc = "(Lnet/minecraft/util/ResourceLocation;)V";
                }
            }
        }
    }
}