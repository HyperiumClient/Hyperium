package cc.hyperium.launch.patching.conflicts;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class GameSettingsTransformer implements ConflictTransformer {
    @Override
    public String getClassName() {
        return "avh";
    }

    @Override
    public ClassNode transform(ClassNode original) {
        original.visitField(Opcodes.ACC_PRIVATE, "needsResourceRefresh", "Z", null, null).visitEnd();
        MethodNode node = new MethodNode(Opcodes.ACC_PUBLIC, "onGuiClosed", "()V", null, null);
        node.instructions.add(createOnGuiClosedList());
        original.methods.add(node);
        for (MethodNode method : original.methods) {
            if (method.name.equals("a") && method.desc.equals("(Lavh$a;F)V")) {
                // needsResourceRefresh = true; return;
                InsnList l = new InsnList();
                l.add(new VarInsnNode(Opcodes.ALOAD, 0));
                l.add(new InsnNode(Opcodes.ICONST_1));
                l.add(new FieldInsnNode(Opcodes.PUTFIELD, "avh", "needsResourceRefresh", "Z"));
                l.add(new InsnNode(Opcodes.RETURN));
                // insert it after setBlurMipmapDirect
                method.instructions.insert(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "bmh", "a", "(ZZ)V", false), l);
            } else if (method.name.equals("a")) {
                System.out.println(method.desc);
            }
        }
        return original;
    }

    private InsnList createOnGuiClosedList() {
        InsnList l = new InsnList();
        LabelNode label = new LabelNode(new Label());
        // if (this.needsResourceRefresh)
        l.add(new VarInsnNode(Opcodes.ALOAD, 0));
        l.add(new FieldInsnNode(Opcodes.GETFIELD, "avh", "needsResourceRefresh", "Z"));
        l.add(new JumpInsnNode(Opcodes.IFEQ, label));
        // this.mc.scheduleResourcesRefresh()
        l.add(new VarInsnNode(Opcodes.ALOAD, 0));
        l.add(new FieldInsnNode(Opcodes.GETFIELD, "avh", "ay", "Lave;")); // this.mc
        l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "ave", "B", "()Lcom/google/common/util/concurrent/ListenableFuture;", false)); // scheduleResourcesRefresh()
        l.add(new InsnNode(Opcodes.POP));
        // this.needsResourceRefresh = false
        l.add(new VarInsnNode(Opcodes.ALOAD, 0));
        l.add(new InsnNode(Opcodes.ICONST_2));
        l.add(new FieldInsnNode(Opcodes.PUTFIELD, "avh", "needsResourceRefresh", "Z"));
        // return
        l.add(label);
        l.add(new InsnNode(Opcodes.RETURN));
        return l;
    }
}
