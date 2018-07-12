package cc.hyperium.mixinsimp.renderer;

import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

public class CachedItem {

    private IBakedModel model;
    private int color;
    private String unlocalized;
    private int damage;
    private int meta;
    private NBTTagCompound compound;

    public CachedItem(IBakedModel model, int color, String unlocalized, int damage, int meta, NBTTagCompound compound) {
        this.model = model;
        this.color = color;
        this.unlocalized = unlocalized;
        this.damage = damage;
        this.meta = meta;
        this.compound = compound;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CachedItem that = (CachedItem) o;
        return color == that.color &&
                damage == that.damage &&
                meta == that.meta &&
                Objects.equals(model, that.model) &&
                Objects.equals(unlocalized, that.unlocalized) &&
                Objects.equals(compound, that.compound);
    }

    @Override
    public int hashCode() {

        return Objects.hash(model, color, unlocalized, damage, meta, compound);
    }
}
