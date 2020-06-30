package cc.hyperium.launch.patching;

import cc.hyperium.launch.deobf.DeobfTransformer;
import net.minecraft.launchwrapper.Launch;
import org.objectweb.asm.ClassWriter;

public class PatchDeobfClassWriter extends ClassWriter {

  public PatchDeobfClassWriter(int flags) {
    super(flags);
  }

  @Override
  protected String getCommonSuperClass(String type1, String type2) {
    Class<?> c, d;
    try {
      c = Class
          .forName(DeobfTransformer.REMAPPER.map(type1).replace('/', '.'), false, Launch.classLoader);
      d = Class
          .forName(DeobfTransformer.REMAPPER.map(type2).replace('/', '.'), false, Launch.classLoader);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    if (c.isAssignableFrom(d)) {
      return type1;
    }
    if (d.isAssignableFrom(c)) {
      return type2;
    }
    if (c.isInterface() || d.isInterface()) {
      return "java/lang/Object";
    } else {
      do {
        c = c.getSuperclass();
      } while (!c.isAssignableFrom(d));
      return c.getName().replace('.', '/');
    }
  }
}
