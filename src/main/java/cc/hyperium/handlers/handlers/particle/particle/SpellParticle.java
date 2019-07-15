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

package cc.hyperium.handlers.handlers.particle.particle;

import cc.hyperium.handlers.handlers.particle.IParticle;
import cc.hyperium.mixinsimp.client.particle.IMixinEffectRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import java.util.Map;

/**
 * Created by mitchellkatz on 6/25/18. Designed for production use on Sk1er.club
 */
public class SpellParticle implements IParticle {
    @Override
    public EntityFX spawn(World world, double x, double y, double z) {
        Map<Integer, IParticleFactory> particleMap = ((IMixinEffectRenderer) Minecraft.getMinecraft().effectRenderer).getParticleMap();
        IParticleFactory iParticleFactory = particleMap.get(EnumParticleTypes.SPELL.getParticleID());
        return iParticleFactory.getEntityFX(EnumParticleTypes.SPELL.getParticleID(), world, x, y, z, 0.0F, -0.1F, 0.0F, 0);
    }
}
