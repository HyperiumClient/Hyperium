package cc.hyperium.gui.hyperium.components;

import cc.hyperium.Hyperium;
import cc.hyperium.mixinsimp.client.GlStateModifier;
import cc.hyperium.utils.HyperiumFontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import java.util.ArrayList;
import java.util.List;

public class LinkComponent extends AbstractTabComponent {
    private final String label;
    private List<String> lines = new ArrayList<>();
    private GuiScreen linkedGuiScreen;


    public LinkComponent(AbstractTab tab, List<String> tags, String label, GuiScreen linkedGuiScreen) {
        super(tab, tags);
        tag(label);
        this.label = label;
        this.linkedGuiScreen = linkedGuiScreen;
    }


    @Override
    public void render(int x, int y, int width, int mouseX, int mouseY) {
        HyperiumFontRenderer font = tab.gui.getFont();

        lines.clear();

        lines = font.splitString(label,
            (width + 25) / 2); //16 for icon, 3 for render offset and then some more

        GlStateManager.pushMatrix();
        if (hover) {
            Gui.drawRect(x, y, x + width, y + 18 * lines.size(), 0xa0000000);
        }
        GlStateManager.popMatrix();

        int line1 = 0;
        for (String line : lines) {
            font.drawString(line.replaceAll("_", " ").toUpperCase(), x + 3, y + 5 + 17 * line1,
                0xffffff);
            line1++;
        }
        GlStateModifier.INSTANCE.reset();
    }

    @Override
    public int getHeight() {
        return 18 * lines.size();

    }


    @Override
    public void onClick(int x, int y) {
        if (y < 18 * lines.size()) {
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(linkedGuiScreen);
        }


    }

    public String getLabel() {
        return label;
    }
}
