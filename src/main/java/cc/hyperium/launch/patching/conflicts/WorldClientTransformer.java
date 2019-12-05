package cc.hyperium.launch.patching.conflicts;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Iterator;

public class WorldClientTransformer implements ConflictTransformer {
    @Override
    public String getClassName() {
        return "net.minecraft.client.multiplayer.WorldClient";
    }

    @Override
    public ClassNode transform(ClassNode original) {
        for (MethodNode node : original.methods) {
            if (node.name.equals("doVoidFogParticles")) {
                Iterator<AbstractInsnNode> iterator = node.instructions.iterator();
                while (iterator.hasNext()) {
                    AbstractInsnNode insn = iterator.next();
                    if (insn instanceof IntInsnNode
                            && insn.getOpcode() == Opcodes.SIPUSH
                            && ((IntInsnNode) insn).operand == 1000) {
                        ((IntInsnNode) insn).operand = 500;
                    }
                }
            }
        }
        return original;
    }
}
