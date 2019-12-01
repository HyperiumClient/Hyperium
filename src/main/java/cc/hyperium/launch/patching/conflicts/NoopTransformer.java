package cc.hyperium.launch.patching.conflicts;

import org.objectweb.asm.tree.ClassNode;

public class NoopTransformer implements ConflictTransformer {
    private final String className;

    public NoopTransformer(String className) {
        this.className = className;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public ClassNode transform(ClassNode original) {
        return original;
    }
}
