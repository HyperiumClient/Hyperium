package cc.hyperium.addons.bossbar.gui;

import cc.hyperium.addons.bossbar.BossbarAddon;
import cc.hyperium.addons.bossbar.config.BossbarConfig;
import cc.hyperium.addons.sidebar.gui.screen.GuiScreenBackground;
import cc.hyperium.config.ConfigOpt;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.io.IOException;

public class GuiBossbarSetting extends GuiScreen {
    private BossbarAddon addon;
    private GuiSlider sliderScale;

    public GuiBossbarSetting(BossbarAddon addon) {
        this.addon = addon;
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, this.getCenter() - 75, this.getRowPos(1), 150, 20, "Bossbar: " + this.getSuffix(BossbarConfig.bossBarEnabled)));
        this.buttonList.add(new GuiButton(1, this.getCenter() - 75, this.getRowPos(2), 150, 20, "Text: " + this.getSuffix(BossbarConfig.textEnabled)));
        this.buttonList.add(new GuiButton(2, this.getCenter() - 75, this.getRowPos(3), 150, 20, "Bar: " + this.getSuffix(BossbarConfig.barEnabled)));
        this.buttonList.add(new GuiButton(3, this.getCenter() - 75, this.getRowPos(4), 150, 20, "Set Position"));
        this.buttonList.add(new GuiButton(4, this.getCenter() - 75, this.getRowPos(5), 150, 20, "Reset Bossbar"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                BossbarConfig.bossBarEnabled = !BossbarConfig.bossBarEnabled;
                button.displayString = "Bossbar: " + this.getSuffix(BossbarConfig.bossBarEnabled);
                break;
            case 1:
                BossbarConfig.textEnabled = !BossbarConfig.textEnabled;
                button.displayString = "Text: " + this.getSuffix(BossbarConfig.textEnabled);
                break;
            case 2:
                BossbarConfig.barEnabled = !BossbarConfig.barEnabled;
                button.displayString = "Bar: " + this.getSuffix(BossbarConfig.barEnabled);
                break;
            case 3:
                mc.displayGuiScreen(new GuiBossbarPosition(this, this.addon));
                break;
            case 4:
                BossbarConfig.bossBarEnabled = true;
                BossbarConfig.barEnabled = true;
                BossbarConfig.textEnabled = true;
                BossbarConfig.x = -1;
                BossbarConfig.y = 12;
        }
        super.actionPerformed(button);
    }

    public int getRowPos(final int rowNumber) {
        return this.height / 4 + (24 * rowNumber - 24) - 16;
    }

    public int getCenter() {
        return this.width / 2;
    }

    private String getSuffix(final boolean enabled) {
        return enabled ? (EnumChatFormatting.GREEN + "Enabled") : (EnumChatFormatting.RED + "Disabled");
    }
}
