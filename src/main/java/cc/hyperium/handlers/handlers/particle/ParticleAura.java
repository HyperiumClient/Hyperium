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

    public int
    getParticleMaxAge() {
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
