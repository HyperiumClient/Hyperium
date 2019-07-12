/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.launch.transformers;

import cc.hyperium.Hyperium;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.io.CharSource;
import com.google.common.io.LineProcessor;
import com.google.common.io.Resources;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.io.*;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static org.objectweb.asm.Opcodes.*;

/**
 * This class allows for Access Transformers to exist inside of Hyperium
 * With this implemented, you're able to make anything private / protected inside of
 * a normal net.minecraft class public, allowing you to access it without the need for
 * an @Accessor annotation {@link org.spongepowered.asm.mixin.gen.Accessor}.
 * <p>
 * To use it, make a file in src/main/resources titled "modid_at.cfg", then head to
 * build.gradle and add to the jar manifest;
 * "AT": "modid_at.cfg"
 *
 * Obviously you'll replace modid with whatever the mod name is.
 *
 * If there's any issues, contact asbyth in the Sk1er Community Discord and I'll do my best
 * to help you with any problems you encounter.
 */

public class AccessTransformer implements IClassTransformer {

    private Multimap<String, Modifier> modifiers = ArrayListMultimap.create();

    public AccessTransformer() throws IOException {
        this("hyperium_at.cfg");
    }

    private AccessTransformer(String rulesFile) throws IOException {
        readMapFile(rulesFile);
    }

    AccessTransformer(Class<? extends AccessTransformer> dummyClass) {
    }

    private void readMapFile(String rulesFile) throws IOException {
        File file = new File(rulesFile);
        URL rulesResource;

        if (file.exists()) {
            rulesResource = file.toURI().toURL();
        } else {
            rulesResource = Resources.getResource(rulesFile);
        }

        processATFile(Resources.asCharSource(rulesResource, Charsets.UTF_8));
        Hyperium.LOGGER.info("Loaded {} rules from AccessTransformer config file {}", modifiers.size(), rulesFile);
    }

    void processATFile(CharSource rulesResource) throws IOException {
        rulesResource.readLines(new LineProcessor<Void>() {
            @Override
            public Void getResult() {
                return null;
            }

            @Override
            public boolean processLine(String input) throws IOException {
                String line = Iterables.getFirst(Splitter.on('#').limit(2).split(input), "").trim();

                if (line.length() == 0) {
                    return true;
                }

                List<String> parts = Lists.newArrayList(Splitter.on(" ").trimResults().split(line));

                if (parts.size() > 3) {
                    throw new RuntimeException("Invalid config file line " + input);
                }

                Modifier modifier = new Modifier();
                modifier.setTargetAccess(parts.get(0));

                if (parts.size() == 2) {
                    modifier.modifyClassVisibility = true;
                } else {
                    String nameReference = parts.get(2);
                    int parenIdx = nameReference.indexOf('(');
                    if (parenIdx > 0) {
                        modifier.desc = nameReference.substring(parenIdx);
                        modifier.name = nameReference.substring(0, parenIdx);
                    } else {
                        modifier.name = nameReference;
                    }
                }

                String className = parts.get(1).replace('/', '.');
                modifiers.put(className, modifier);
                return true;
            }
        });
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (bytes == null) return null;
        if (!modifiers.containsKey(transformedName)) return bytes;

        ClassNode node = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(node, 0);

        Collection<Modifier> mods = modifiers.get(transformedName);

        for (Modifier modifier : mods) {
            if (modifier.modifyClassVisibility) {
                node.access = getFixedAccess(node.access, modifier);
                continue;
            }

            if (modifier.desc.isEmpty()) {
                for (FieldNode field : node.fields) {
                    if (field.name.equals(modifier.name) || field.name.equals("*")) {
                        field.access = getFixedAccess(field.access, modifier);
                        if (!modifier.name.equals("*")) {
                            break;
                        }
                    }
                }
            } else {
                List<MethodNode> nowOverridable = Lists.newArrayList();
                for (MethodNode method : node.methods) {
                    if ((method.name.equals(modifier.name) && method.desc.equals(modifier.desc)) || modifier.name.equals("*")) {
                        method.access = getFixedAccess(method.access, modifier);

                        if (!method.name.equals("<init>")) {
                            boolean wasPrivate = (modifier.oldAccess & ACC_PRIVATE) == ACC_PRIVATE;
                            boolean isNowPrivate = (modifier.newAccess & ACC_PRIVATE) == ACC_PRIVATE;

                            if (wasPrivate && !isNowPrivate) {
                                nowOverridable.add(method);
                            }
                        }

                        if (!modifier.name.equals("*")) {
                            break;
                        }
                    }
                }

                replaceInvokeSpecial(node, nowOverridable);
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        return writer.toByteArray();
    }

    private void replaceInvokeSpecial(ClassNode clazz, List<MethodNode> toReplace) {
        for (MethodNode method : clazz.methods) {
            for (Iterator<AbstractInsnNode> it = method.instructions.iterator(); it.hasNext(); ) {
                AbstractInsnNode insn = it.next();
                if (insn.getOpcode() == INVOKESPECIAL) {
                    MethodInsnNode mInsn = (MethodInsnNode) insn;
                    for (MethodNode n : toReplace) {
                        if (n.name.equals(mInsn.name) && n.desc.equals(mInsn.desc)) {
                            mInsn.setOpcode(INVOKEVIRTUAL);
                            break;
                        }
                    }
                }
            }
        }
    }

    private int getFixedAccess(int access, Modifier target) {
        target.oldAccess = access;
        int t = target.targetAccess;
        int ret = (access & ~7);

        switch (access & 7) {
            case ACC_PRIVATE:
                ret |= t;
                break;

            case 0:
                ret |= (t != ACC_PRIVATE ? t : 0);
                break;

            case ACC_PROTECTED:
                ret |= (t != ACC_PRIVATE && t != 0 ? t : ACC_PROTECTED);
                break;

            case ACC_PUBLIC:
                ret |= (t != ACC_PRIVATE && t != 0 && t != ACC_PROTECTED ? t : ACC_PUBLIC);
                break;

            default:
                throw new RuntimeException("Something went wrong fixing access point");
        }

        if (target.changeFinal) {
            if (target.markFinal) {
                ret |= ACC_FINAL;
            } else {
                ret &= ~ACC_FINAL;
            }
        }

        target.newAccess = ret;
        return ret;
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: AccessTransformer <JarPath> <MapFile> [MapFile2]... ");
            System.exit(1);
        }

        boolean hasTransformer = false;
        AccessTransformer[] trans = new AccessTransformer[args.length - 1];
        for (int x = 1; x < args.length; x++) {
            try {
                trans[x - 1] = new AccessTransformer(args[x]);
                hasTransformer = true;
            } catch (IOException e) {
                System.out.println("Could not read Transformer Map: " + args[x]);
                e.printStackTrace();
            }
        }

        if (!hasTransformer) {
            System.out.println("Could not find a valid transformer to perform");
            System.exit(1);
        }

        File orig = new File(args[0]);
        File temp = new File(args[0] + ".ATBack");
        if (!orig.exists() && !temp.exists()) {
            System.out.println("Could not find target jar: " + orig);
            System.exit(1);
        }

        if (!orig.renameTo(temp)) {
            System.out.println("Could not rename file: " + orig + " -> " + temp);
            System.exit(1);
        }

        try {
            processJar(temp, orig, trans);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        if (!temp.delete()) {
            System.out.println("Could not delete temp file: " + temp);
        }
    }

    private static void processJar(File inFile, File outFile, AccessTransformer[] transformers) throws IOException {
        ZipInputStream inJar = null;
        ZipOutputStream outJar = null;

        try {
            try {
                inJar = new ZipInputStream(new BufferedInputStream(new FileInputStream(inFile)));
            } catch (FileNotFoundException e) {
                throw new FileNotFoundException("Could not open input file: " + e.getMessage());
            }

            try {
                outJar = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outFile)));
            } catch (FileNotFoundException e) {
                throw new FileNotFoundException("Could not open output file: " + e.getMessage());
            }

            ZipEntry entry;
            while ((entry = inJar.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    outJar.putNextEntry(entry);
                    continue;
                }

                byte[] data = new byte[4096];
                ByteArrayOutputStream entryBuffer = new ByteArrayOutputStream();

                int len;
                do {
                    len = inJar.read(data);
                    if (len > 0) {
                        entryBuffer.write(data, 0, len);
                    }
                }
                while (len != -1);

                byte[] entryData = entryBuffer.toByteArray();

                String entryName = entry.getName();

                if (entryName.endsWith(".class") && !entryName.startsWith(".")) {
                    ClassNode cls = new ClassNode();
                    ClassReader rdr = new ClassReader(entryData);
                    rdr.accept(cls, 0);
                    String name = cls.name.replace('/', '.').replace('\\', '.');

                    for (AccessTransformer trans : transformers) {
                        entryData = trans.transform(name, name, entryData);
                    }
                }

                ZipEntry newEntry = new ZipEntry(entryName);
                outJar.putNextEntry(newEntry);
                outJar.write(entryData);
            }
        } finally {
            if (outJar != null) {
                try {
                    outJar.close();
                } catch (IOException ignored) {
                }
            }

            if (inJar != null) {
                try {
                    inJar.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    static class Modifier {
        String name = "";
        String desc = "";
        int oldAccess = 0;
        int newAccess = 0;
        int targetAccess = 0;
        boolean changeFinal = false;
        boolean markFinal = false;
        boolean modifyClassVisibility;

        private void setTargetAccess(String name) {
            if (name.startsWith("public")) targetAccess = ACC_PUBLIC;
            else if (name.startsWith("private")) targetAccess = ACC_PRIVATE;
            else if (name.startsWith("protected")) targetAccess = ACC_PROTECTED;

            if (name.endsWith("-f")) {
                changeFinal = true;
                markFinal = false;
            } else if (name.endsWith("+f")) {
                changeFinal = true;
                markFinal = true;
            }
        }
    }

    Multimap<String, Modifier> getModifiers() {
        return modifiers;
    }
}
