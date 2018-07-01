package cc.hyperium.handlers.handlers.particle.animations;

import cc.hyperium.handlers.handlers.particle.AbstractAnimation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;

public class TornadoAnimation extends AbstractAnimation {
    @Override
    public List<Vec3> render(EntityPlayer player, double x, double y, double z) {
        ArrayList<Vec3> vec3s = new ArrayList<>();


        Vec3 base = new Vec3(x, y + .2, z);
        double z1 = .2;
        for (int i = 0; i < 20; i++) {
            if (i % 2 ==0)
                continue;
            double v = Math.pow(10, z1 + (i / 10D) - 1) / 5 + .3;
            double period = 5000*v;
            double v2 = (System.currentTimeMillis() % period) / period;
            v2 *= Math.PI * 2;
            for (int j = 0; j < 15; j++) {
                double v1 = Math.PI * 2D * j / 15D+ v2;
                vec3s.add(base.addVector(v * Math.sin(v1), i / 10D, v * Math.cos(v1)));
            }
        }
        return vec3s;
    }
}
