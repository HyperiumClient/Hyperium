package cc.hyperium.handlers.handlers.fov;

import cc.hyperium.config.Settings;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.entity.FovUpdateEvent;
import net.minecraft.init.Items;
import net.minecraft.potion.PotionEffect;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FovModifier {

    private static final float MODIFIER_SPEED = 0.1F;
    private static final float MODIFIER_SLOWNESS = -0.075F;
    private static final float MAX_BOW_TICKS = 20.0F;
    private static final Map<Integer, Float> MODIFIER_BY_TICK = new HashMap<>();

    @InvokeEvent
    public void fovChange(FovUpdateEvent event) {
        float base = 1.0F;

        if (event.getPlayer().isSprinting()) {
            base += (float) (0.15000000596046448 * Settings.SPRINTING_FOV_MODIFIER);
        }

        if (event.getPlayer().getItemInUse() != null && event.getPlayer().getItemInUse().getItem().equals(Items.bow)) {
            int duration = (int) Math.min(event.getPlayer().getItemInUseDuration(), MAX_BOW_TICKS);
            float modifier = MODIFIER_BY_TICK.get(duration);
            base -= modifier * Settings.BOW_FOV_MODIFIER;
        }

        Collection<PotionEffect> effects = event.getPlayer().getActivePotionEffects();
        for (PotionEffect effect : effects) {
            if (effect.getPotionID() == 1) {
                base += (MODIFIER_SPEED * (effect.getAmplifier() + 1) * Settings.SPEED_FOV_MODIFIER);
            }

            if (effect.getPotionID() == 2) {
                base += (MODIFIER_SLOWNESS * (effect.getAmplifier() + 1) * Settings.SLOWNESS_FOV_MODIFIER);
            }
        }

        event.setNewFov(base);
    }

    static {
        MODIFIER_BY_TICK.put(0, 0.0F);
        MODIFIER_BY_TICK.put(1, 3.7497282E-4f);
        MODIFIER_BY_TICK.put(2, 0.0015000105f);
        MODIFIER_BY_TICK.put(3, 0.0033749938f);
        MODIFIER_BY_TICK.put(4, 0.0059999824f);
        MODIFIER_BY_TICK.put(5, 0.009374976f);
        MODIFIER_BY_TICK.put(6, 0.013499975f);
        MODIFIER_BY_TICK.put(7, 0.01837498f);
        MODIFIER_BY_TICK.put(8, 0.023999989f);
        MODIFIER_BY_TICK.put(9, 0.030375004f);
        MODIFIER_BY_TICK.put(10, 0.037500024f);
        MODIFIER_BY_TICK.put(11, 0.04537499f);
        MODIFIER_BY_TICK.put(12, 0.05400002f);
        MODIFIER_BY_TICK.put(13, 0.063374996f);
        MODIFIER_BY_TICK.put(14, 0.07349998f);
        MODIFIER_BY_TICK.put(15, 0.084375024f);
        MODIFIER_BY_TICK.put(16, 0.096000016f);
        MODIFIER_BY_TICK.put(17, 0.10837501f);
        MODIFIER_BY_TICK.put(18, 0.121500015f);
        MODIFIER_BY_TICK.put(19, 0.13537502f);
        MODIFIER_BY_TICK.put(20, 0.14999998f);
    }
}
