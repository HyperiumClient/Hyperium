package cc.hyperium.launch.deobf;

import cc.hyperium.launch.deobf.mappings.Mappings;
import net.minecraft.launchwrapper.IClassNameTransformer;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class DeobfTransformer implements IClassTransformer, IClassNameTransformer {
  public static final DeobfuscatingRemapper REMAPPER = new DeobfuscatingRemapper(Mappings.INSTANCE.getStable22());
  private static final int READER_FLAGS = ClassReader.EXPAND_FRAMES;
  private static final int WRITER_FLAGS = ClassWriter.COMPUTE_MAXS;

  @Override
  public byte[] transform(String name, String transformedName, byte[] basicClass) {
    if (basicClass == null) {
      return null;
    }
    ClassReader reader = new ClassReader(basicClass);
    ClassWriter writer = new ClassWriter(WRITER_FLAGS);
    DeobfAdapter adapter = new DeobfAdapter(writer, REMAPPER);
    reader.accept(adapter, READER_FLAGS);
    return writer.toByteArray();
  }

  @Override
  public String unmapClassName(String name) {
    return unmap(name).replace('/', '.');
  }

  public static String unmap(String name) {
    String result = REMAPPER.getMappings().getReverseClassMap().get(name);
    if (result == null) {
      return name;
    }
    return result;
  }

  @Override
  public String remapClassName(String name) {
    return REMAPPER.map(name).replace('/', '.');
  }
}
