package cc.hyperium.mixinsimp.renderer;

import java.util.Objects;

public class StringHash implements Comparable<StringHash> {

    private String text;
    private float red, green, blue, alpha;
    private boolean shadow;
    private int hash;
    public final long time = System.currentTimeMillis();

    public StringHash(String text, float red, float green, float blue, float alpha, boolean shadow) {
        this.text = text;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        this.shadow = shadow;
        this.hash = Objects.hash(text, red, green, blue, alpha, shadow);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringHash hash = (StringHash) o;
        return Float.compare(hash.red, red) == 0 &&
                Float.compare(hash.green, green) == 0 &&
                Float.compare(hash.blue, blue) == 0 &&
                Float.compare(hash.alpha, alpha) == 0 &&
                shadow == hash.shadow &&
                Objects.equals(text, hash.text);
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public int compareTo(StringHash o) {
        int t = Boolean.compare(o.shadow, this.shadow);
        if (t != 0)
            return t;
        t = Float.compare(o.red, this.red);
        if (t != 0)
            return t;
        t = Float.compare(o.green, this.green);
        if (t != 0)
            return t;
        t = Float.compare(o.blue, this.blue);
        if (t != 0)
            return t;
        t = Float.compare(o.alpha, this.alpha);
        if (t != 0)
            return t;

        return text.compareTo(o.text);
    }
}
