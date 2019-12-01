package cc.hyperium.launch.patching.conflicts;

import org.objectweb.asm.tree.ClassNode;

public interface ConflictTransformer {
    String getClassName();

    ClassNode transform(ClassNode original);
}
