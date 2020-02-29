package cc.hyperium.launch.deobf;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.commons.RemappingClassAdapter;

public class DeobfAdapter extends RemappingClassAdapter {

  public DeobfAdapter(ClassVisitor cv) {
    super(cv, DeobfRemapper.INSTANCE);
  }

  @Override
  public void visit(int version, int access, String name, String signature, String superName,
      String[] interfaces) {
    if (interfaces == null) {
      interfaces = new String[0];
    }
    DeobfRemapper.INSTANCE.addSupertypeMappings(name, superName, interfaces);
    super.visit(version, access, name, signature, superName, interfaces);
  }

  @Override
  public void visitInnerClass(String name, String outerName,
      String innerName, int access) {
    name = remapper.mapType(name);
    super.visitInnerClass(name, outerName, name.substring(name.lastIndexOf('$') + 1), access);
  }
}
