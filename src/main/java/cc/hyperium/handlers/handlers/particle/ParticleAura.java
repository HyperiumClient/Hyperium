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

package cc.hyperium.handlers.handlers.particle;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

import java.util.List;

/**
 * Created by mitchellkatz on 6/23/18. Designed for production use on Sk1er.club
 */
public class ParticleAura {

    private IParticle type;
    private AbstractAnimation animation;
    private int particleMaxAge;
    private boolean chroma;
    private boolean rgb;
    private int red = 255;
    private int blue = 255;
    private int green = 255;

    public ParticleAura(IParticle type, AbstractAnimation animation, int particleMaxAge, boolean chroma, boolean rgb) {
        this.type = type;
        this.animation = animation;
        this.particleMaxAge = particleMaxAge;
        this.chroma = chroma;
        this.rgb = rgb;
    }

    public void setRgb(int red, int green, int blue) {
        this.red = red;
        this.blue = blue;
        this.green = green;
    }

    public int getRed() {
        return red;
    }

    public int getBlue() {
        return blue;
    }

    public int getGreen() {
        return green;
    }

    public int getParticleMaxAge() {
        return particleMaxAge;
    }

    public IParticle getType() {
        return type;
    }

    public AbstractAnimation getAnimation() {
        return animation;
    }

    public List<Vec3> render(EntityPlayer entityPlayer, double x, double y, double z) {
        return animation.render(entityPlayer, x, y, z);
    }

    public boolean isChroma() {
        return chroma;
    }

    public boolean isRgb() {
        return rgb;
    }
}
