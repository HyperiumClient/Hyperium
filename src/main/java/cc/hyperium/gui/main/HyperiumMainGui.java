package cc.hyperium.gui.main;

import cc.hyperium.config.Settings;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.utils.HyperiumFontRenderer;

import java.awt.Font;

/*
 * Created by Cubxity on 20/05/2018
 */
public class HyperiumMainGui extends HyperiumGui {

    private HyperiumFontRenderer font = new HyperiumFontRenderer(Settings.GUI_FONT, Font.PLAIN, 14);
    private HyperiumFontRenderer title = new HyperiumFontRenderer(Settings.GUI_FONT, Font.PLAIN, 20);

    @Override
    protected void pack() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        title.drawCenteredString("Hyperium Settings", width / 2, 5, 0xffffff);
    }

    public HyperiumFontRenderer getFont() {
        return font;
    }

    public HyperiumFontRenderer getTitle() {
        return title;
    }
}
