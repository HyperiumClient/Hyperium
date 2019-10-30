package me.semx11.autotip.util;

public class Version implements Comparable<Version> {

    private String version;

    public Version(String version) {
        assert version != null : "Version can not be null";
        assert version.matches("[0-9]+(\\.[0-9]+)*") : "Invalid version format";
        this.version = version;
    }

    public final String get() {
        return version;
    }

    @Override
    public int compareTo(Version that) {
        if (that == null) return 1;
        String[] thisParts = get().split("\\.");
        String[] thatParts = that.get().split("\\.");
        int length = Math.max(thisParts.length, thatParts.length);
        for (int i = 0; i < length; i++) {
            int thisPart = i < thisParts.length ? Integer.parseInt(thisParts[i]) : 0;
            int thatPart = i < thatParts.length ? Integer.parseInt(thatParts[i]) : 0;
            if (thisPart < thatPart) return -1;
            if (thisPart > thatPart) return 1;
        }

        return 0;
    }

    @Override
    public boolean equals(Object that) {
        return this == that || that != null && getClass() == that.getClass() && compareTo((Version) that) == 0;
    }

    @Override
    public String toString() {
        return get();
    }
}
