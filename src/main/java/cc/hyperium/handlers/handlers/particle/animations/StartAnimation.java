package cc.hyperium.handlers.handlers.particle.animations;

import cc.hyperium.handlers.handlers.particle.AbstractAnimation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitchellkatz on 6/24/18. Designed for production use on Sk1er.club
 */
public class StartAnimation extends AbstractAnimation {
    @Override
    public List<Vec3> render(EnumParticleTypes type, EntityPlayer player, double x, double y, double z) {
        ArrayList<Vec3> vec3s = new ArrayList<>();
        Vec3 base = new Vec3(x, y + player.getEyeHeight(), z);
        
        return vec3s;
    }
}
