package cc.hyperium.mods.chromahud.displayitems.hyperium;

import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import cc.hyperium.config.Settings;

import java.util.ArrayList;
import java.util.List;

public class DoubleCPSDisplay extends DisplayItem {

    private final FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;

    public DoubleCPSDisplay(JsonHolder data, int ordinal) {
        super(data, ordinal);
    }

    @Override
    public void draw(int x, double y, boolean config) {
        List<String> list = new ArrayList<>();
        int leftCps = ElementRenderer.getCPS();
        int rightCps = ElementRenderer.getRightCPS();
        if (!Settings.CHROMAHUD_SQUAREBRACE_PREFIX_OPTION) {
            list.add("CPS:");
            list.add("Left CPS: " + leftCps);
            list.add("Right CPS: " + rightCps);
            list.add("Total CPS: " + (leftCps + rightCps));
        } else {
            list.add("[CPS]");
            list.add("[Left CPS] " + leftCps);
            list.add("[Right CPS] " + rightCps);
            list.add("[Total CPS] " + (leftCps + rightCps));
        }
        this.height = fr.FONT_HEIGHT * list.size();
        int maxWidth = 0;
        for (String line : list) {
            if (fr.getStringWidth(line) > maxWidth) maxWidth = fr.getStringWidth(line);
        }
        this.width = maxWidth;
        ElementRenderer.draw(x, y, list);
    }


}
