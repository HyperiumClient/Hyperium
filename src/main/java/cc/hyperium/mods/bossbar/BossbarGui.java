package cc.hyperium.mods.bossbar;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.boss.BossStatus;

import java.io.IOException;

public class BossbarGui extends GuiScreen {

    private boolean dragging;
    private int lastMouseX;
    private int lastMouseY;

    private String previousBossName;
    private int previousStatusBarTime;
    private float previousHealthScale;

    @Override
    public void initGui() {
        buttonList.clear();
        previousBossName = BossStatus.bossName;
        BossStatus.bossName = "Sk1er LLC";
        previousStatusBarTime = BossStatus.statusBarTime;
        BossStatus.statusBarTime = Integer.MAX_VALUE;
        previousHealthScale = BossStatus.healthScale;
        BossStatus.healthScale = 1F;
        buttonList.add(new GuiButton(0, width / 2 - 155 / 2, calculateHeight(0), 155, 20,
            I18n.format("gui.settings.bossbarmod.resetbossbar")));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawCenteredString(mc.fontRendererObj, "Bossbar Mod", width / 2, calculateHeight(-1), -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            Settings.BOSSBAR_ALL = true;
            Settings.BOSSBAR_BAR = true;
            Settings.BOSSBAR_X = .5;
            Settings.BOSSBAR_Y = .05;
            Settings.BOSSBAR_SCALE = 1.0;
            initGui();
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (dragging) {
            Settings.BOSSBAR_X = (Settings.BOSSBAR_X * width + (mouseX - lastMouseX)) / (double) width;
            Settings.BOSSBAR_Y = (Settings.BOSSBAR_Y * height + (mouseY - lastMouseY)) / (double) height;
            if (Settings.BOSSBAR_X * width - (182 * Settings.BOSSBAR_SCALE / 2) < 0) {
                Settings.BOSSBAR_X = (182 * Settings.BOSSBAR_SCALE / 2) / (double) width;
            }

            if (Settings.BOSSBAR_X * width + (182 * Settings.BOSSBAR_SCALE / 2) > width) {
                Settings.BOSSBAR_X = ((width - (182 * Settings.BOSSBAR_SCALE / 2)) / (double) width);
            }

            if (Settings.BOSSBAR_Y * height - 10 < 0) {
                Settings.BOSSBAR_Y = 10 / (double) height;
            }

            if (Settings.BOSSBAR_Y * height + 5 > height) {
                Settings.BOSSBAR_Y = (height - 5) / (double) height;
            }

            lastMouseX = mouseX;
            lastMouseY = mouseY;
        }

        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (!Settings.BOSSBAR_ALL) return;

        int startX = (int) ((Settings.BOSSBAR_X * width) - (182 / 2 * Settings.BOSSBAR_SCALE));
        int startY = (int) (Settings.BOSSBAR_Y * height) - 10;
        int endX = (int) (startX + 182 * Settings.BOSSBAR_SCALE);
        int endY = (int) (startY + (15 * Settings.BOSSBAR_SCALE));

        if (mouseX >= startX && mouseX <= endX && mouseY >= startY && mouseY <= endY) {
            dragging = true;
            lastMouseX = mouseX;
            lastMouseY = mouseY;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        dragging = false;
    }

    @Override
    public void onGuiClosed() {
        BossStatus.bossName = previousBossName;
        BossStatus.statusBarTime = previousStatusBarTime;
        BossStatus.healthScale = previousHealthScale;
        Hyperium.CONFIG.save();
        super.onGuiClosed();
    }

    private int calculateHeight(int row) {
        return 55 + row * 23;
    }
}
