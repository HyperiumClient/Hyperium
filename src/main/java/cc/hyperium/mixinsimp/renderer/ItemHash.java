package cc.hyperium.mixinsimp.renderer;

import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

public class ItemHash {
    private int hash;

    public ItemHash(IBakedModel model, int color, String unlocalized, int damage, int meta, NBTTagCompound compound) {
        this.hash = Objects.hash(model, color, unlocalized, damage, meta, compound);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ItemHash)) return false;

        return Objects.equals(this.hash, ((ItemHash) other).hash);
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
