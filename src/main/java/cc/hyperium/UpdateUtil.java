package cc.hyperium;

import cc.hyperium.handlers.handlers.particle.EnumParticleType;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class UpdateUtil {
    private static final char[] hexCodes;

    static {
        hexCodes = "0123456789ABCDEF".toCharArray();
    }

    public static void main(String[] args) {
        for (EnumParticleType type : EnumParticleType.values()) {
            System.out.println("PARTICLE_" + type.name()+"(300, \"" +type.getName()+" Particle\", \"Add option for " + type.getName()+" particle in particle auras\", \"\", true),");
        }
        String fileName = "Hyperium";
        String ver = JOptionPane.showInputDialog(null, "Enter version", "");
        final File file = new File("build\\libs\\" + fileName + "-" + ver + ".jar");
        System.out.println(file.getAbsolutePath());
        final long size = file.length();
        final String sha1 = toHex(checksum(file, "SHA1")).toLowerCase();
        final String sha2 = toHex(checksum(file, "SHA-256")).toLowerCase();
        JTextArea textarea = new JTextArea("Size: " + size + "\n" +
                "Sha1: " + sha1 + " \n" +
                "Sha256: " + sha2);
        textarea.setEditable(true);
        JOptionPane.showMessageDialog(null, textarea, "Done", JOptionPane.ERROR_MESSAGE);

    }

    private static String toHex(final byte[] bytes) {
        final StringBuilder r = new StringBuilder(bytes.length * 2);
        for (final byte b : bytes) {
            r.append(hexCodes[b >> 4 & 0xF]);
            r.append(hexCodes[b & 0xF]);
        }
        return r.toString();
    }

    private static byte[] checksum(final File input, final String name) {
        try (final InputStream in = new FileInputStream(input)) {
            final MessageDigest digest = MessageDigest.getInstance(name);
            final byte[] block = new byte[4096];
            int length;
            while ((length = in.read(block)) > 0) {
                digest.update(block, 0, length);
            }
            return digest.digest();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
