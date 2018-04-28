package cc.hyperium.mods.chromahud.displayitems.hyperium;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderHUDEvent;
import cc.hyperium.mods.chromahud.DisplayElement;
import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.mods.keystrokes.keys.impl.MouseButton;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import org.lwjgl.input.Mouse;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DoubleCPSDisplay extends DisplayItem {

    public DoubleCPSDisplay(JsonHolder data, int ordinal) {
        super(data, ordinal);
    }

    private FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;

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
