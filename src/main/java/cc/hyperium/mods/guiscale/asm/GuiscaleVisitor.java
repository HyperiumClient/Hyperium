package cc.hyperium.mods.guiscale.asm;

import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.lib.Opcodes;

public class GuiscaleVisitor extends MethodVisitor implements Opcodes {
    public GuiscaleVisitor(MethodVisitor mv) {
        super(262144, mv);
    }

    public void visitInsn(int opcode) {
        super.visitInsn(opcode);
        if (opcode == 135) {
            this.visitMethodInsn(184, "cc/hyperium/mods/guiscale/Guiscale", "getGuiScale", "()D", false);
            this.visitInsn(99);
        }
    }
}
