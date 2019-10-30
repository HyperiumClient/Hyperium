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

package cc.hyperium.handlers.handlers.particle.animations;

import cc.hyperium.handlers.handlers.particle.AbstractAnimation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitchellkatz on 6/23/18. Designed for production use on Sk1er.club
 */
public class TripleTwirlAnimation extends AbstractAnimation {


    @Override
    public List<Vec3> render(EntityPlayer player, double x, double y, double z) {
        double l = (System.currentTimeMillis() % 1000) / 1000D;
        l *= Math.PI * 2 / 3;
        List<Vec3> list = new ArrayList<>();
        y += 1.8;
        Vec3 vec3 = new Vec3(x, y, z);
        list.add(vec3.addVector(MathHelper.cos((float) l), 0, MathHelper.sin(((float) l))));
        l += Math.PI * 2 / 3;
        list.add(vec3.addVector(MathHelper.cos((float) l), 0, MathHelper.sin(((float) l))));
        l += Math.PI * 2 / 3;
        list.add(vec3.addVector(MathHelper.cos((float) l), 0, MathHelper.sin(((float) l))));
        return list;
    }

}
