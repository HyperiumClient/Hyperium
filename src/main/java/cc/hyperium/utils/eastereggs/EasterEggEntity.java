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

package cc.hyperium.utils.eastereggs;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;

/**
 * EasterEgg user thing
 *
 * @author SHARDcoder
 */
public class EasterEggEntity {
    public static Entity getEntity(EntityPlayer player, String classPath) {
        EntityLivingBase disguise = null;
        try {
            disguise = (EntityLivingBase) Class.forName(classPath).getConstructor(World.class).newInstance(player.worldObj);
            disguise.ticksExisted = 1;
            disguise.posX = player.posX;
            disguise.lastTickPosX = player.lastTickPosX;
            disguise.posY = player.posY;
            disguise.lastTickPosY = player.lastTickPosY;
            disguise.posZ = player.posZ;
            disguise.lastTickPosZ = player.lastTickPosZ;
            disguise.rotationPitch = player.rotationPitch;
            disguise.prevRotationPitch = player.prevRotationPitch;
            disguise.rotationYaw = player.rotationYaw;
            disguise.prevRotationYaw = player.prevRotationYaw;
            disguise.renderYawOffset = player.renderYawOffset;
            disguise.prevRenderYawOffset = player.prevRenderYawOffset;
            disguise.rotationYawHead = player.rotationYawHead;
            disguise.prevRotationYawHead = player.prevRotationYawHead;
            disguise.limbSwing = player.limbSwing;
            disguise.limbSwingAmount = player.limbSwingAmount;
            disguise.prevLimbSwingAmount = player.prevLimbSwingAmount;
            disguise.setInvisible(false);
            for (PotionEffect effect : player.getActivePotionEffects()) {
                if (effect.getPotionID() == 14)
                    disguise.setInvisible(true);
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return disguise;
    }
}
