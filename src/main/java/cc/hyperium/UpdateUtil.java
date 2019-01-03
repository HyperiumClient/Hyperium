/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium;

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
