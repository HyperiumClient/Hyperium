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

public class VortexOfDoomAnimation extends AbstractAnimation {
    @Override
    public List<Vec3> render(EntityPlayer player, double x, double y, double z) {
        ArrayList<Vec3> vec3s = new ArrayList<>();
        Vec3 base = new Vec3(x, y + 1.7, z);
        double l = Math.abs(.5 - ((System.currentTimeMillis() % 10000D) / 10000D)) * 4;
        l = l <= 1 ? Math.pow(l, 2) / 2 : -(Math.pow(l - 2, 2) - 2) / 2;
        l *= 2;

        for (int i = 0; i < 40; i++) {
            double v = Math.PI / 40 * i * 2;

            for (int j = 0; j < 6; j++) {
                vec3s.add(base.addVector(MathHelper.sin((float) (v + l * j / 2.5D)), -.2 * j, MathHelper.cos((float) (v + l * j / 2.5D))));
            }
        }

        return vec3s;
    }
}
