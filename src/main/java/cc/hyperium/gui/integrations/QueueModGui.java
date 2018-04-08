package cc.hyperium.gui.integrations;

import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.GuiBoxItem;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.packets.serverbound.UpdateQueuePacket;
import cc.hyperium.utils.RenderUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import utils.JsonHolder;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QueueModGui extends HyperiumGui {

    private static List<String> selected = new ArrayList<>();
    private List<GuiBoxItem<String>> boxes = new ArrayList<>();
    private int coolDown = 0;

    @Override
    protected void pack() {
        reg("QUEUE", new GuiButton(nextId(), 1, 1, "Update Queue"), guiButton -> {
            coolDown = 100;
            NettyClient.getClient().write(UpdateQueuePacket.build(selected));
        }, guiButton -> {
            guiButton.enabled = coolDown == 0;
        });
        reg("QUEUE1", new GuiButton(nextId(), 1, 22, "Clear Queue"), guiButton -> {
            selected.clear();
            NettyClient.getClient().write(UpdateQueuePacket.build(selected));
        }, guiButton -> {
            guiButton.enabled = coolDown == 0;
        });
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (coolDown > 0)
            coolDown--;

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (GuiBoxItem<String> box : boxes) {
            if (box.getBox().isMouseOver(mouseX, mouseY)) {
                if (selected.contains(box.getObject()))
                    selected.remove(box.getObject());
                else selected.add(box.getObject());
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        boxes.clear();
        JsonHolder games = NettyClient.getClient().getGames();
        int printY = 50 + offset;
        ScaledResolution current = ResolutionUtil.current();
        for (String s : games.getKeys()) {
            JsonHolder holder = games.optJSONObject(s);
            if (holder.has("display")) {
                String display = holder.optString("display");
                int left = current.getScaledWidth() / 2 - 10;
                GuiBlock block = new GuiBlock(left, left + 20, printY, printY + 12);
                block.ensureHeight(20, true);
                drawRect(block.getLeft(), block.getTop(), block.getRight(), block.getBottom(), new Color(0, 0, 0, 100).getRGB());
                if (selected.contains(s)) {
                    RenderUtils.drawLine(block.getLeft(), block.getTop(), block.getRight(), block.getBottom(), 4, Color.RED.getRGB());
                    RenderUtils.drawLine(block.getLeft(), block.getBottom(), block.getRight(), block.getTop(), 4, Color.RED.getRGB());
                }
                block.drawString(display, fontRendererObj, true, true, 45, 6, true, true, Color.RED.getRGB(), true);
                boxes.add(new GuiBoxItem<>(block, s));
                printY += block.getHeight();
            } else {
                int left = current.getScaledWidth() / 2 + 10;

                GuiBlock block1 = new GuiBlock(left, left, printY, printY + 12);
                block1.drawString(s, fontRendererObj, true, true, 0, 0, true, true, Color.RED.getRGB(), true);
                printY += block1.getHeight();
                for (String s1 : holder.getKeys()) {
                    JsonHolder holder1 = holder.optJSONObject(s1);
                    String display = holder1.optString("display");
                    GuiBlock block = new GuiBlock(left, left + 20, printY, printY + 12);
                    block.ensureHeight(20, true);
                    drawRect(block.getLeft(), block.getTop(), block.getRight(), block.getBottom(), new Color(0, 0, 0, 100).getRGB());
                    if (selected.contains(s1)) {
                        RenderUtils.drawLine(block.getLeft(), block.getTop(), block.getRight(), block.getBottom(), 4, Color.RED.getRGB());
                        RenderUtils.drawLine(block.getLeft(), block.getBottom(), block.getRight(), block.getTop(), 4, Color.RED.getRGB());
                    }
                    block.drawString(display, fontRendererObj, true, true, 55, 6, true, true, Color.RED.getRGB(), true);
                    boxes.add(new GuiBoxItem<>(block, s1));
                    printY += block.getHeight();
                }
            }

        }
    }
}
