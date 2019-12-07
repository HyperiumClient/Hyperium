package cc.hyperium.launch.patching.conflicts;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Iterator;

public class GameSettingsTransformer implements ConflictTransformer {
    @Override
    public String getClassName() {
        return "net.minecraft.client.settings.GameSettings";
    }

    @Override
    public ClassNode transform(ClassNode original) {
        original.visitField(Opcodes.ACC_PRIVATE, "needsResourceRefresh", "Z", null, null).visitEnd();
        MethodNode node = new MethodNode(Opcodes.ACC_PUBLIC, "onGuiClosed", "()V", null, null);
        node.maxLocals = 1;
        node.maxStack = 2;
        node.instructions.add(createOnGuiClosedList());
        original.methods.add(node);
        for (MethodNode method : original.methods) {
            if (method.name.equals("setOptionFloatValue")) {
                // needsResourceRefresh = true; return;
                InsnList l = new InsnList();
                l.add(new VarInsnNode(Opcodes.ALOAD, 0));
                l.add(new InsnNode(Opcodes.ICONST_1));
                l.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/client/settings/GameSettings", "needsResourceRefresh", "Z"));
                l.add(new InsnNode(Opcodes.RETURN));
                // insert it after setBlurMipmapDirect
                Iterator<AbstractInsnNode> it = method.instructions.iterator();
                while (it.hasNext()) {
                    AbstractInsnNode insn = it.next();
                    if (insn instanceof MethodInsnNode && insn.getOpcode() == Opcodes.INVOKEVIRTUAL
                            && ((MethodInsnNode) insn).owner.equals("net/minecraft/client/Minecraft")
                            && ((MethodInsnNode) insn).name.equals("scheduleResourcesRefresh")
                            && ((MethodInsnNode) insn).desc.equals("()Lcom/google/common/util/concurrent/ListenableFuture;")) {
                        method.instructions.insertBefore(insn.getPrevious().getPrevious(), l);
                        break;
                    }
                }
                method.instructions.insert(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                        "net/minecraft/client/renderer/texture/TextureMap",
                        "setBlurMipmapDirect",
                        "(ZZ)V",
                        false), l);
            }
        }
        return original;
    }

    private InsnList createOnGuiClosedList() {
        InsnList l = new InsnList();
        LabelNode label = new LabelNode(new Label());
        // if (this.needsResourceRefresh)
        l.add(new VarInsnNode(Opcodes.ALOAD, 0));
        l.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/settings/GameSettings", "needsResourceRefresh", "Z"));
        l.add(new JumpInsnNode(Opcodes.IFEQ, label));
        // this.mc.scheduleResourcesRefresh()
        l.add(new VarInsnNode(Opcodes.ALOAD, 0));
        l.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/settings/GameSettings", "mc", "Lnet/minecraft/client/Minecraft;")); // this.mc
        l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "scheduleResourcesRefresh",
                "()Lcom/google/common/util/concurrent/ListenableFuture;", false)); // scheduleResourcesRefresh()
        l.add(new InsnNode(Opcodes.POP));
        // this.needsResourceRefresh = false
        l.add(new VarInsnNode(Opcodes.ALOAD, 0));
        l.add(new InsnNode(Opcodes.ICONST_2));
        l.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/client/settings/GameSettings", "needsResourceRefresh", "Z"));
        // return
        l.add(label);
        l.add(new InsnNode(Opcodes.RETURN));
        return l;
    }
}
