/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.addons.bossbar.gui;

import cc.hyperium.addons.bossbar.config.BossbarConfig;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.boss.BossStatus;

import java.io.IOException;

public class GuiBossbarPosition extends GuiScreen {

    private GuiScreen parent;
    private String oldBossName;
    private int oldStatusBarTime;
    private float oldHealthScale;

    GuiBossbarPosition(GuiScreen parent) {
        this.parent = parent;
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
        if (button.id == 0) {
            mc.displayGuiScreen(parent);
        }
        super.actionPerformed(button);
    }

    private int getRowPos() {
        return this.height - 36;
    }
    public int getCenter() {
        return this.width / 2;
    }
}
