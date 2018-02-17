package me.semx11.autotip.util;

import java.util.regex.Pattern;

public enum MinecraftVersion {
    V1_8, V1_8_8, V1_8_9, V1_9, V1_9_4, V1_10, V1_10_2, V1_11, V1_11_2;

    public static MinecraftVersion fromString(String version) throws IllegalArgumentException {
        Pattern p = Pattern
                .compile("^(1\\.8(\\.[8-9])?|1\\.9(\\.4)?|1\\.10(\\.2)?|1\\.11(\\.2)?)$");
        if (p.matcher(version).matches()) {
            return valueOf("V" + version.replaceAll("\\.", "_"));
        }
        return null;
    }

    public String toString() {
        return this.name().substring(1).replaceAll("_", ".");
    }

}
