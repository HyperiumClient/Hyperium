package cc.hyperium.mods.chromahud.displayitems.hyperium;

import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.util.ArrayList;
import java.util.List;

public class DoubleCPSDisplay extends DisplayItem {

    private FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;

    public DoubleCPSDisplay(JsonHolder data, int ordinal) {
        super(data, ordinal);
    }

    @Override
    public void draw(int x, double y, boolean config) {
        List<String> list = new ArrayList<>();
        int leftCps = ElementRenderer.getCPS();
        int middleCps = ElementRenderer.getMiddleCPS();
        int rightCps = ElementRenderer.getRightCPS();
        list.add("CPS:");
        list.add("Left CPS: " + leftCps);
        list.add("Middle CPS: " + middleCps);
        list.add("Right CPS: " + rightCps);
        list.add("Total CPS: " + (leftCps + middleCps + rightCps));
        this.height = fr.FONT_HEIGHT * list.size();
        int maxWidth = 0;
        for (String line : list) {
            if (fr.getStringWidth(line) > maxWidth) maxWidth = fr.getStringWidth(line);
        }
        this.width = maxWidth;
        ElementRenderer.draw(x, y, list);
    }


}
