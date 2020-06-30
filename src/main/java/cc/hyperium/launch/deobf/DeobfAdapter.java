package cc.hyperium.launch.deobf;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.commons.RemappingClassAdapter;

public class DeobfAdapter extends RemappingClassAdapter {

  public DeobfAdapter(ClassVisitor cv, DeobfuscatingRemapper remapper) {
    super(cv, remapper);
  }
  @Override
  public void visitInnerClass(String name, String outerName,
      String innerName, int access) {
    name = remapper.mapType(name);
    super.visitInnerClass(name, outerName, name.substring(name.lastIndexOf('$') + 1), access);
  }
}
