package cc.hyperium.handlers.handlers.particle.animations;

import cc.hyperium.handlers.handlers.particle.AbstractAnimation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitchellkatz on 6/25/18. Designed for production use on Sk1er.club
 */
public class DoubleHelix extends AbstractAnimation {
    @Override
    public List<Vec3> render(EntityPlayer player, double x, double y, double z) {
        double l = (System.currentTimeMillis() % 1000) / 1000D;
        l *= Math.PI * 2;

        List<Vec3> list = new ArrayList<>();
        y += 1.8;
        Vec3 vec3 = new Vec3(x, y, z);
        for (int i = 0; i < 50; i++) {
            l += Math.PI / 500 * i;
            list.add(vec3.addVector(MathHelper.cos((float) l), -.03D * i, MathHelper.sin(((float) l))));
        }

        return list;
    }
}
