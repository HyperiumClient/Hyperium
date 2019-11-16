package tk.amplifiable.mcgradle.tasks.mc;

import com.google.common.collect.Sets;
import com.google.common.io.ByteStreams;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InnerClassNode;
import org.objectweb.asm.tree.MethodNode;
import tk.amplifiable.mcgradle.MCGradleConstants;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class TaskMerge extends DefaultTask {
    private static final Class<SideOnly> SIDE_CLASS = SideOnly.class;
    private static final Class<Side> SIDE_ENUM_CLASS = Side.class;

    @Input
    public String version = MCGradleConstants.EXTENSION.version;

    @InputFile
    private File clientJar = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/client.jar");

    @InputFile
    private File serverJar = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/split_server.jar");

    @OutputFile
    private File mergedJar = new File(MCGradleConstants.CACHE_DIRECTORY, "jars/" + version + "/merged.jar");

    @TaskAction
    public void merge() throws IOException {
        try (ZipFile cJar = new ZipFile(clientJar); ZipFile sJar = new ZipFile(serverJar); ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(mergedJar)))) {
            Set<String> resources = new HashSet<>();
            Map<String, ZipEntry> clientClasses = getClasses(cJar, out, resources);
            Map<String, ZipEntry> serverClasses = getClasses(sJar, out, resources);
            Set<String> added = new HashSet<>();
            for (Map.Entry<String, ZipEntry> it : clientClasses.entrySet()) {
                String name = it.getKey();
                ZipEntry entry1 = it.getValue();
                ZipEntry entry2 = serverClasses.get(name);
                if (entry2 == null) {
                    copyClass(cJar, entry1, out, true);
                    added.add(name);
                    continue;
                }
                serverClasses.remove(name);
                byte[] data1 = read(cJar, it.getValue());
                byte[] data2 = read(sJar, entry2);
                byte[] data = process(data1, data2);
                ZipEntry entry3 = new ZipEntry(entry1.getName());
                out.putNextEntry(entry3);
                out.write(data);
                added.add(name);
            }
            for (Map.Entry<String, ZipEntry> entry : serverClasses.entrySet()) {
                copyClass(sJar, entry.getValue(), out, false);
            }

            for (String name : new String[]{SIDE_CLASS.getName(), SIDE_ENUM_CLASS.getName()}) {
                String name1 = name.replace('.', '/');
                String path = name1 + ".class";
                ZipEntry entry1 = new ZipEntry(path);
                if (!added.contains(name1)) {
                    out.putNextEntry(entry1);
                    out.write(getBytes(name1));
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void copyClass(ZipFile jar, ZipEntry entry, ZipOutputStream out, boolean clientOnly) throws IOException {
        ClassReader reader = new ClassReader(read(jar, entry));
        ClassNode node = new ClassNode();
        reader.accept(node, 0);
        if (node.visibleAnnotations == null) node.visibleAnnotations = new ArrayList<>();
        node.visibleAnnotations.add(getAnnotation(clientOnly));
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        byte[] data = writer.toByteArray();
        ZipEntry entry1 = new ZipEntry(entry.getName());
        if (out != null) {
            out.putNextEntry(entry1);
            out.write(data);
        }
    }

    @SuppressWarnings("unchecked")
    private AnnotationNode getAnnotation(boolean client) {
        AnnotationNode node = new AnnotationNode(Type.getDescriptor(SIDE_CLASS));
        node.values = new ArrayList<>();
        node.values.add("value");
        node.values.add(new String[]{Type.getDescriptor(SIDE_ENUM_CLASS), client ? "CLIENT" : "SERVER"});
        return node;
    }

    private Map<String, ZipEntry> getClasses(ZipFile file, ZipOutputStream ignoredOut, Set<String> resources) throws IOException {
        Map<String, ZipEntry> value = new HashMap<>();

        for (ZipEntry entry : Collections.list(file.entries())) {
            String name = entry.getName();
            if (name.equals("META-INF/MANIFEST.MF")) continue;
            if (entry.isDirectory()) continue;
            if (!name.endsWith(".class") || name.startsWith(".")) {
                if (!resources.contains(name)) {
                    ZipEntry entry1 = new ZipEntry(name);
                    ignoredOut.putNextEntry(entry1);
                    ignoredOut.write(read(file, entry));
                    resources.add(name);
                }
            } else {
                value.put(name.replace(".class", ""), entry);
            }
        }

        return value;
    }

    private byte[] read(ZipFile file, ZipEntry entry) throws IOException {
        return ByteStreams.toByteArray(file.getInputStream(entry));
    }

    private ClassNode getNode(byte[] b) {
        ClassReader reader = new ClassReader(b);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);
        return node;
    }

    private byte[] getBytes(String s) throws IOException {
        InputStream stream = TaskMerge.class.getResourceAsStream("/" + s.replace('.', '/') + ".class");
        return ByteStreams.toByteArray(stream);
    }

    private byte[] process(byte[] b1, byte[] b2) {
        ClassNode node1 = getNode(b1);
        ClassNode node2 = getNode(b2);

        processF(node1, node2);
        processM(node1, node2);
        processI(node1, node2);

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node1.accept(writer);
        return writer.toByteArray();
    }

    private boolean matchesInner(InnerClassNode o, InnerClassNode o2) {
        if (o.innerName == null && o2.innerName != null) return false;
        if (o.innerName != null && !o.innerName.equals(o2.innerName)) return false;
        if (o.name == null && o2.name != null) return false;
        if (o.name != null && !o.name.equals(o2.name)) return false;
        if (o.outerName == null && o2.outerName != null) return false;
        return o.outerName == null || !o.outerName.equals(o2.outerName);
    }

    private boolean doesNotContainInner(List<InnerClassNode> nodes, InnerClassNode node) {
        for (InnerClassNode n : nodes) {
            if (matchesInner(n, node)) return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private void processI(ClassNode c1, ClassNode c2) {
        List<InnerClassNode> i1 = c1.innerClasses;
        List<InnerClassNode> i2 = c2.innerClasses;
        for (InnerClassNode i : i1) {
            if (doesNotContainInner(i2, i)) i2.add(i);
        }
        for (InnerClassNode i : i2) {
            if (doesNotContainInner(i1, i)) i1.add(i);
        }
    }

    @SuppressWarnings("unchecked")
    private void processM(ClassNode c1, ClassNode c2) {
        List<MethodNode> m1 = c1.methods;
        List<MethodNode> m2 = c2.methods;
        LinkedHashSet<Method> all = Sets.newLinkedHashSet();

        int pos1 = 0;
        int pos2 = 0;
        int len1 = m1.size();
        int len2 = m2.size();
        String name1 = "";
        String lastName = name1;
        String name2;
        while (pos1 < len1 || pos2 < len2) {
            do {
                if (pos2 >= len2) {
                    break;
                }
                MethodNode sm = m2.get(pos2);
                name2 = sm.name;
                if (!name2.equals(lastName) && pos1 != len1) {
                    // System.out.println("sskip " + name2 + " " + lastName + " " + c2.name);
                    break;
                }
                Method m = new Method(sm);
                m.server = true;
                all.add(m);
                pos2++;
            } while (pos2 < len2);
            do {
                if (pos1 >= len1) {
                    break;
                }
                MethodNode cm = m1.get(pos1);
                lastName = name1;
                name1 = cm.name;
                if (!name1.equals(lastName) && pos2 != len2) {
                    break;
                }
                Method m = new Method(cm);
                m.client = true;
                all.add(m);
                pos1++;
            } while (pos1 < len1);
        }
        m1.clear();
        m2.clear();

        for (Method m : all) {
            m1.add(m.node);
            m2.add(m.node);
            if (!(m.server && m.client)) {
                if (m.node.visibleAnnotations == null) m.node.visibleAnnotations = new ArrayList();
                // System.out.println("(1) Adding annotation to " + m);
                m.node.visibleAnnotations.add(getAnnotation(m.client));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void processF(ClassNode c1, ClassNode c2) {
        List<FieldNode> f1 = c1.fields;
        List<FieldNode> f2 = c2.fields;

        int serverFieldID = 0;

        for (int clientFieldID = 0; clientFieldID < f1.size(); clientFieldID++) {
            FieldNode cf = f1.get(clientFieldID);
            if (serverFieldID < f2.size()) {
                FieldNode sf = f2.get(serverFieldID);
                if (!cf.name.equals(sf.name)) {
                    boolean found = false;
                    for (int i = serverFieldID + 1; i < f2.size(); i++) {
                        if (cf.name.equals(f2.get(i).name)) {
                            found = true;
                            break;
                        }
                    }

                    if (found) {
                        found = false;
                        for (int i = clientFieldID + 1; i < f1.size(); i++) {
                            if (sf.name.equals(f1.get(i).name)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            if (sf.visibleAnnotations == null) sf.visibleAnnotations = new ArrayList();
                            sf.visibleAnnotations.add(getAnnotation(false));
                            f1.add(clientFieldID, sf);
                        }
                    } else {
                        if (cf.visibleAnnotations == null) cf.visibleAnnotations = new ArrayList();
                        cf.visibleAnnotations.add(getAnnotation(true));
                        f2.add(serverFieldID, cf);
                    }
                }
            } else {
                if (cf.visibleAnnotations == null) cf.visibleAnnotations = new ArrayList();
                cf.visibleAnnotations.add(getAnnotation(true));
                f2.add(serverFieldID, cf);
            }
            serverFieldID++;
        }
        if (f2.size() != f1.size()) {
            for (int x = f1.size(); x < f2.size(); x++) {
                FieldNode sf = f2.get(x);
                if (sf.visibleAnnotations == null) sf.visibleAnnotations = new ArrayList<>();
                sf.visibleAnnotations.add(getAnnotation(true));
                f1.add(x, sf);
            }
        }
    }

    @Input
    public String getVersion() {
        return version;
    }

    @InputFile
    public File getClientJar() {
        return clientJar;
    }

    @InputFile
    public File getServerJar() {
        return serverJar;
    }

    @OutputFile
    public File getMergedJar() {
        return mergedJar;
    }

    private static class Method {
        private MethodNode node;
        private boolean client;
        private boolean server;

        Method(MethodNode node) {
            this.node = node;
        }

        @Override
        public String toString() {
            return "Method{" +
                    "node=" + node +
                    ", client=" + client +
                    ", server=" + server +
                    '}';
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Method)) {
                return false;
            }
            Method m = (Method) obj;
            boolean eq = Objects.equals(node.name, m.node.name) && Objects.equals(node.desc, m.node.desc);
            if (eq) {
                m.client = client | m.client;
                m.server = server | m.server;
                client = client | m.client;
                server = server | m.server;
            }
            return eq;
        }

        @Override
        public int hashCode() {
            return Objects.hash(node.name, node.desc);
        }
    }
}
