package cc.hyperium.handlers.handlers.particle.animations;

import cc.hyperium.handlers.handlers.particle.AbstractAnimation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitchellkatz on 6/24/18. Designed for production use on Sk1er.club
 */
public class ExplodeAnimation extends AbstractAnimation {
    @Override
    public List<Vec3> render(EntityPlayer player, double x, double y, double z) {
        ArrayList<Vec3> vec3s = new ArrayList<>();
        Vec3 base = new Vec3(x, y + 1.8F, z);
        double l = 1.0 - System.currentTimeMillis() % 1000 / 500D;

        double magnitude = Math.pow(l, 2);
        float baseAngle = player.getRotationYawHead();
        for (int i = 0; i < 3; i++) {
            vec3s.add(base.addVector(magnitude * Math.sin(Math.toRadians(baseAngle)), 0, magnitude * Math.cos(Math.toRadians(baseAngle))));
            baseAngle += 120;
        }


        return vec3s;
    }
}
