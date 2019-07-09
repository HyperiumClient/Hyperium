/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods.chromahud.displayitems.chromahud;


import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Sk1er
 */
public class PotionEffects extends DisplayItem {

    public PotionEffects(JsonHolder raw, int ordinal) {
        super(raw, ordinal);
    }

    public void draw(int x, double y, boolean isConfig) {

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
        this.width = isConfig ? ElementRenderer.maxWidth(tmp) : 0;
        this.height = row * 16;

    }


}
