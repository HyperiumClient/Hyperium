package com.hcc.mods.chromahud.displayitems.hcc.chromahud;


import com.hcc.mods.chromahud.ElementRenderer;
import com.hcc.mods.chromahud.api.Dimension;
import com.hcc.mods.chromahud.api.DisplayItem;
import com.hcc.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by mitchellkatz on 5/30/17.
 */
public class PotionEffects extends DisplayItem {

    public PotionEffects(JsonHolder raw, int ordinal) {
        super(raw, ordinal);
    }

    public Dimension draw(int x, double y, boolean isConfig) {

        int row = 0;
        double scale = ElementRenderer.getCurrentScale();
        Collection<PotionEffect> effects = new ArrayList<>();
        if (isConfig) {
            effects.add(new PotionEffect(1, 100, 1));
            effects.add(new PotionEffect(3, 100, 2));
        } else effects = Minecraft.getMinecraft().thePlayer.getActivePotionEffects();
        List<String> tmp = new ArrayList<>();
        for (PotionEffect potioneffect : effects) {
            Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
             StringBuilder s1 = new StringBuilder(I18n.format(potion.getName()));
            if (potioneffect.getAmplifier() == 1) {
                s1.append(" ").append(I18n.format("enchantment.level.2"));
            } else if (potioneffect.getAmplifier() == 2) {
                s1.append(" ").append(I18n.format("enchantment.level.3"));
            } else if (potioneffect.getAmplifier() == 3) {
                s1.append(" ").append(I18n.format("enchantment.level.4"));
            }

            String s = Potion.getDurationString(potioneffect);
            String text = s1 + " - " + s;
            tmp.add(text);
            ElementRenderer.draw((int) (x / scale), ((y + row * 16)), text);
            row++;
        }
        return new Dimension(isConfig ? ElementRenderer.maxWidth(tmp) : 0, row * 16);

    }


}
