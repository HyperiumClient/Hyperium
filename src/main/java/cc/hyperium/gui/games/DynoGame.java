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

package cc.hyperium.gui.games;

import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.utils.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DynoGame extends HyperiumGui {

    private final Random random = new Random();
    private final int GAME_LEVEL = 200;
    private final int DYNO_X = 150;
    private int DELTA_BUSH = 0;
    private boolean dead = false;
    private int score;
    private final List<Bush> bushes = new ArrayList<>();
    private boolean running;
    private int dynoOffset = 0;
    private int dynoVelocity = 0;
    private int ticksSinceAdd = 0;

    @Override
    protected void pack() {
        GuiButton restart;
        reg("RESTART", restart = new GuiButton(0, ResolutionUtil.current().getScaledWidth() / 2 - 100, 40, "Restart"), guiButton -> reset(), guiButton -> guiButton.visible = dead);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);


        RenderUtils.drawLine(80, GAME_LEVEL, ResolutionUtil.current().getScaledWidth() - 80, GAME_LEVEL, 3, Color.BLACK.getRGB());

        for (Bush bush : bushes) {
            GuiBlock hitbox = bush.hitbox;
            Gui.drawRect(hitbox.getLeft(), hitbox.getTop(), hitbox.getRight(), hitbox.getBottom(), Color.BLACK.getRGB());
        }
        Gui.drawRect(DYNO_X - 10, GAME_LEVEL - dynoOffset - 40, DYNO_X + 10, GAME_LEVEL - dynoOffset, Color.RED.getRGB());
        drawCenteredString(fontRendererObj, "Score: " + score, ResolutionUtil.current().getScaledWidth() / 2, GAME_LEVEL - 50, Color.RED.getRGB());
        if (dead) {
            drawCenteredString(fontRendererObj, "You died! Press any key to reset", ResolutionUtil.current().getScaledWidth() / 2, GAME_LEVEL - 70, Color.RED.getRGB());

        }
    }

    private void reset() {
        this.dead = false;
        score = 0;
        running = true;
        bushes.clear();
        bushes.add(new Bush(ResolutionUtil.current().getScaledWidth() - 100));
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        DELTA_BUSH = ResolutionUtil.current().getScaledWidth() / 50;
        if (!running)
            return;
        if (!dead) {
            for (Bush bush : bushes) {
                bush.tick();
                if (bush.hitbox.isMouseOver(DYNO_X, GAME_LEVEL - dynoOffset) || bush.hitbox.isMouseOver(DYNO_X + 10, GAME_LEVEL - dynoOffset)) {
                    //Hit the box
                    endGame();
                }
            }
            bushes.removeIf(bush -> bush.xLoc < 100);
            int GRAVITY = -2;
            dynoVelocity += GRAVITY;
            dynoOffset += dynoVelocity;
            if (dynoOffset < 0) {
                dynoVelocity = 0;
                dynoOffset = 0;
            }
            if (ticksSinceAdd > 15) {
                if (random.nextInt(15) == 10 || ticksSinceAdd > 45) {
                    ticksSinceAdd = 0;
                    bushes.add(new Bush(ResolutionUtil.current().getScaledWidth() - 100));
                }
            }
            score++;
            ticksSinceAdd++;
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && dynoOffset == 0 && dynoVelocity == 0) {
                dynoVelocity = 13;
            }
        }


    }

    private void endGame() {
        dead = true;

    }

    private void togglePause() {
        running = !running;
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        super.handleKeyboardInput();
        if (dead) {
            reset();
        }
        if (!running)
            running = true;

    }

    class Bush {

        private int xLoc;
        private final GuiBlock hitbox;

        public Bush(int xLoc) {
            this.xLoc = xLoc;
            hitbox = new GuiBlock(xLoc - 10, xLoc + 10, GAME_LEVEL - 20, GAME_LEVEL);
        }

        public int getxLoc() {
            return xLoc;
        }

        public void tick() {
            xLoc -= DELTA_BUSH;
            hitbox.setLeft(hitbox.getLeft() - DELTA_BUSH);
            hitbox.setRight(hitbox.getRight() - DELTA_BUSH);
        }

    }
}
