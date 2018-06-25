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
public class DoubleTwirlAnimation extends AbstractAnimation {


    @Override
    public List<Vec3> render(EntityPlayer player, double x, double y, double z) {
        double l = (System.currentTimeMillis() % 1000) / 1000D;
        l *= Math.PI;

        List<Vec3> list = new ArrayList<>();
        y += 1.5;
        Vec3 vec3 = new Vec3(x, y, z);
        list.add(vec3.addVector(MathHelper.cos((float) l), 0, MathHelper.sin(((float) l))));
        l += Math.PI;
        list.add(vec3.addVector(MathHelper.cos((float) l), 0, MathHelper.sin(((float) l))));

        return list;
    }

}
