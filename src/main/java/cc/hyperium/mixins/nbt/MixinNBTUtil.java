package cc.hyperium.mixins.nbt;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.UUID;

@Mixin(NBTUtil.class)
public class MixinNBTUtil {

    /**
     * @author Sk1er
     * @reason Not proper null checks
     */
    @Overwrite
    public static GameProfile readGameProfileFromNBT(NBTTagCompound compound) {
        String s = null;
        String s1 = null;

        if (compound.hasKey("Name", 8)) {
            s = compound.getString("Name");
        }

        if (compound.hasKey("Id", 8)) {
            s1 = compound.getString("Id");
        }

        if (StringUtils.isNullOrEmpty(s) && StringUtils.isNullOrEmpty(s1)) {
            return null;
        } else {
            UUID uuid = null;
            if (s1 != null)
                try {
                    uuid = UUID.fromString(s1);
                } catch (Throwable ignored) {
                }

            GameProfile gameprofile = new GameProfile(uuid, s);

            if (compound.hasKey("Properties", 10)) {
                NBTTagCompound nbttagcompound = compound.getCompoundTag("Properties");

                for (String s2 : nbttagcompound.getKeySet()) {
                    NBTTagList nbttaglist = nbttagcompound.getTagList(s2, 10);

                    for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                        NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
                        String s3 = nbttagcompound1.getString("Value");

                        if (nbttagcompound1.hasKey("Signature", 8)) {
                            gameprofile.getProperties().put(s2, new Property(s2, s3, nbttagcompound1.getString("Signature")));
                        } else {
                            gameprofile.getProperties().put(s2, new Property(s2, s3));
                        }
                    }
                }
            }

            return gameprofile;
        }
    }
}
