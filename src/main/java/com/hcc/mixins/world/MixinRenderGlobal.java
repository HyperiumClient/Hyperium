/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hcc.mixins.world;

import com.hcc.utils.EntityLimiter;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.util.ClassInheritanceMultiMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(RenderGlobal.class)
public class MixinRenderGlobal {

    @ModifyVariable(method = "renderEntities", at = @At(value = "STORE"))
    private ClassInheritanceMultiMap<Entity> renderEntities(ClassInheritanceMultiMap<Entity> classinheritancemultimap) {
        if (!classinheritancemultimap.isEmpty()) {
            List<Entity> entities = new EntityLimiter(EntityLimiter.EntityLimiterMethods.RANGE, 15)
                    .getRenderingEntities(new ArrayList<>(classinheritancemultimap));
            ClassInheritanceMultiMap<Entity> map = new ClassInheritanceMultiMap<>(Entity.class);
            map.addAll(entities);
            return map;
        }
        return classinheritancemultimap;
    }
}