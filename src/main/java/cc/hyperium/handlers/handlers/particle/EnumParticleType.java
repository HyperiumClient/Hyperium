package cc.hyperium.handlers.handlers.particle;

import cc.hyperium.handlers.handlers.particle.particle.CloudParticle;
import cc.hyperium.handlers.handlers.particle.particle.CritParticle;
import cc.hyperium.handlers.handlers.particle.particle.ExplosionParticle;
import cc.hyperium.handlers.handlers.particle.particle.FireworkSparkParticle;
import cc.hyperium.handlers.handlers.particle.particle.FlameParticle;
import cc.hyperium.handlers.handlers.particle.particle.FootstepParticle;
import cc.hyperium.handlers.handlers.particle.particle.HeartParticle;
import cc.hyperium.handlers.handlers.particle.particle.LavaDripParticle;
import cc.hyperium.handlers.handlers.particle.particle.LavaParticle;
import cc.hyperium.handlers.handlers.particle.particle.NoteParticle;
import cc.hyperium.handlers.handlers.particle.particle.PortalParticle;
import cc.hyperium.handlers.handlers.particle.particle.RedstoneParticle;
import cc.hyperium.handlers.handlers.particle.particle.SlimeParticle;
import cc.hyperium.handlers.handlers.particle.particle.SmokeLargeParticle;
import cc.hyperium.handlers.handlers.particle.particle.SmokeNormalParticle;
import cc.hyperium.handlers.handlers.particle.particle.SnowShovelParticle;
import cc.hyperium.handlers.handlers.particle.particle.SnowballParticle;
import cc.hyperium.handlers.handlers.particle.particle.SpellInstantParticle;
import cc.hyperium.handlers.handlers.particle.particle.SpellMobAmbientParticle;
import cc.hyperium.handlers.handlers.particle.particle.SpellMobParticle;
import cc.hyperium.handlers.handlers.particle.particle.SpellParticle;
import cc.hyperium.handlers.handlers.particle.particle.SpellWitchParticle;
import cc.hyperium.handlers.handlers.particle.particle.SuspendedDepthParticle;
import cc.hyperium.handlers.handlers.particle.particle.SuspendedParticle;
import cc.hyperium.handlers.handlers.particle.particle.TownAuraParticle;
import cc.hyperium.handlers.handlers.particle.particle.VillagerAngryParticle;
import cc.hyperium.handlers.handlers.particle.particle.VillagerHappyParticle;
import cc.hyperium.handlers.handlers.particle.particle.WaterBubbleParticle;
import cc.hyperium.handlers.handlers.particle.particle.WaterDripParticle;
import cc.hyperium.handlers.handlers.particle.particle.WaterDropParticle;

/**
 * Created by mitchellkatz on 6/25/18. Designed for production use on Sk1er.club
 */
public enum EnumParticleType {

    CLOUD("Cloud Particle", new CloudParticle()),
    CRIT("Crit Particle", new CritParticle()),
    EXPLOSION("Explosion Particle", new ExplosionParticle()),
    FIREWORK("Firework Particle", new FireworkSparkParticle()),
    FLAME("Flame Particle", new FlameParticle()),
    FOOTSTEP("Footstep Particle", new FootstepParticle()),
    HEART("Heart Particle", new HeartParticle()),
    LAVA_DRIP("Lava Drip Particle", new LavaDripParticle()),
    LAVA("Lava Particle", new LavaParticle()),
    NOTE("Note Block Particle", new NoteParticle()),
    PORTAL("Portal Particle", new PortalParticle()),
    REDSTONE("Redstone Particle", new RedstoneParticle()),
    SLIME("Slime Particle", new SlimeParticle()),
    SMOKE_LARGE("Large Smoke Particle", new SmokeLargeParticle()),
    SMOKE_NORMAL("Smoke Particle", new SmokeNormalParticle()),
    SNOWBALL("Snowball Particle", new SnowballParticle()),
    SNOW_SHOVEL("Snow Shovel Particle", new SnowShovelParticle()),
    SPELL_INSTANT("Instant Spell Particle", new SpellInstantParticle()),
    SPELL_MOB_AMBIENT("Ambient Mob Spell Particle", new SpellMobAmbientParticle()),
    SPELL_MOB("Mob Spell Particle", new SpellMobParticle()),
    SPELL("Spell Particle", new SpellParticle()),
    WITCH("Witch Particle", new SpellWitchParticle()),
    SUSPENDED_DEPTH("Suspended Depth Particle", new SuspendedDepthParticle()),
    SUSPENDED("Suspended Particle", new SuspendedParticle()),
    TOWN_AURA("Town Aura Particle", new TownAuraParticle()),
    VILLAGER_ANGRY("Angry Villager Particle", new VillagerAngryParticle()),
    VILLAGER_HAPPY("Happy Villager Particle", new VillagerHappyParticle()),
    WATER_BUBBLE("Water Bubble Particle", new WaterBubbleParticle()),
    WATER_DRIP("Water Drip Particle", new WaterDripParticle()),
    WATER_DROP("Water Drop", new WaterDropParticle());


    private String name;
    private IParticle particle;

    EnumParticleType(String s, IParticle particle) {
        this.name = s;
        this.particle = particle;


    }

    public static EnumParticleType parse(String key) {

        try {
            return valueOf(key);
        } catch (Exception e) {
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public IParticle getParticle() {
        return particle;
    }
}

