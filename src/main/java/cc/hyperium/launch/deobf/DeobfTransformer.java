package cc.hyperium.launch.deobf;

import net.minecraft.launchwrapper.IClassNameTransformer;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class DeobfTransformer implements IClassTransformer, IClassNameTransformer {
    private static final int READER_FLAGS = ClassReader.EXPAND_FRAMES;
    private static final int WRITER_FLAGS = ClassWriter.COMPUTE_MAXS;

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null) return null;
        ClassReader reader = new ClassReader(basicClass);
        ClassWriter writer = new ClassWriter(WRITER_FLAGS);
        DeobfAdapter adapter = new DeobfAdapter(writer);
        reader.accept(adapter, READER_FLAGS);
        return writer.toByteArray();
    }

    @Override
    public String unmapClassName(String name) {
        String result = DeobfRemapper.INSTANCE.unmap(name);
        return result.replace('/', '.');
    }

    @Override
    public String remapClassName(String name) {
        String result = DeobfRemapper.INSTANCE.map(name);
        return result.replace('/', '.');
    }
}
