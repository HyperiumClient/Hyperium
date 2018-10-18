package cc.hyperium.mixinsimp.renderer;

import java.util.Objects;

public class StringHash {
    private int hash;

    public StringHash(String text, float red, float green, float blue, float alpha, boolean shadow) {
        this.hash = Objects.hash(text, red, green, blue, alpha, shadow);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof StringHash)) return false;

        return Objects.equals(this.hash, ((StringHash) other).hash);
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
