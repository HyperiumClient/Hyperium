/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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

        if (compound.hasKey("Name", 8)) s = compound.getString("Name");
        if (compound.hasKey("Id", 8)) s1 = compound.getString("Id");

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

                    int bound = nbttaglist.tagCount();
                    for (int i = 0; i < bound; i++) {
                        NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
                        String s3 = nbttagcompound1.getString("Value");
                        gameprofile.getProperties().put(s2, nbttagcompound1.hasKey("Signature", 8) ?
                            new Property(s2, s3, nbttagcompound1.getString("Signature")) : new Property(s2, s3));
                    }
                }
            }

            return gameprofile;
        }
    }
}
