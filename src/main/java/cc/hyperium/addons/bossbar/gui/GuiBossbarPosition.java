package cc.hyperium.addons.bossbar.gui;

import cc.hyperium.addons.bossbar.BossbarAddon;
import cc.hyperium.addons.bossbar.config.BossbarConfig;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.boss.BossStatus;

import java.io.IOException;

public class GuiBossbarPosition extends GuiScreen {
    private GuiScreen parent;
    private BossbarAddon addon;
    private String oldBossName;
    private int oldStatusBarTime;
    private float oldHealthScale;

    public GuiBossbarPosition(GuiScreen parent, BossbarAddon addon) {
        this.parent = parent;
        this.addon = addon;
    }

    @Override
    public void onGuiClosed() {
        BossStatus.bossName = oldBossName;
        BossStatus.statusBarTime = oldStatusBarTime;
        BossStatus.healthScale = oldHealthScale;
        super.onGuiClosed();
    }

    @Override
    public void initGui() {
        oldBossName = BossStatus.bossName;
        BossStatus.bossName = "Sample";
        oldStatusBarTime = BossStatus.statusBarTime;
        BossStatus.statusBarTime = Integer.MAX_VALUE;
        oldHealthScale = BossStatus.healthScale;
        BossStatus.healthScale = 0.5F;
        this.buttonList.add(new GuiButton(0, this.getCenter() - 100, this.getRowPos(), "Done"));
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        BossbarConfig.x = mouseX;
        BossbarConfig.y = mouseY;
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                mc.displayGuiScreen(parent);
                break;
        }
        super.actionPerformed(button);
    }

    public int getRowPos() {
        return this.height - 36;
    }

    public int getCenter() {
        return this.width / 2;
    }
}
