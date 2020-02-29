package cc.hyperium.launch.patching;

import cc.hyperium.launch.deobf.DeobfRemapper;
import net.minecraft.launchwrapper.Launch;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class PatchDeobfClassWriter extends ClassWriter {

  public PatchDeobfClassWriter(int flags) {
    super(flags);
  }

  public PatchDeobfClassWriter(ClassReader classReader, int flags) {
    super(classReader, flags);
  }

  @Override
  protected String getCommonSuperClass(String type1, String type2) {
    Class<?> c, d;
    try {
      c = Class
          .forName(DeobfRemapper.INSTANCE.map(type1).replace('/', '.'), false, Launch.classLoader);
      d = Class
          .forName(DeobfRemapper.INSTANCE.map(type2).replace('/', '.'), false, Launch.classLoader);
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
