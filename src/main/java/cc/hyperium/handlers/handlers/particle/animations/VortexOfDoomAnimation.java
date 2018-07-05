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
        if (l <= 1) {
            l = Math.pow(l, 2) / 2;
        } else {
            l = -(Math.pow(l - 2, 2) - 2) / 2;
        }
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
