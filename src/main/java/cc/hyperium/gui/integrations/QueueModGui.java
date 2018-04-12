package cc.hyperium.gui.integrations;

import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.GuiBoxItem;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.netty.NettyClient;
import cc.hyperium.netty.packet.packets.serverbound.UpdateQueuePacket;
import utils.JsonHolder;
import net.minecraft.client.gui.GuiButton;

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
        reg("QUEUE", new GuiButton(nextId(), width - 201, height - 21, "Update Queue"), guiButton -> {
            coolDown = 100;
            NettyClient.getClient().write(UpdateQueuePacket.build(selected));
        }, guiButton -> {
            guiButton.enabled = coolDown == 0;
        });
        reg("QUEUE1", new GuiButton(nextId(), width - 201, height - 42, "Clear Queue"), guiButton -> {
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
        
        if (NettyClient.getClient() == null) {
            return;
        }
        
        JsonHolder games = NettyClient.getClient().getGames();
        
        if (games == null || games.getKeys().isEmpty()) {
            return;
        }
        
        int y = 50 + offset;
        for (String s : games.getKeys()) {
            JsonHolder holder = games.optJSONObject(s);
            fontRendererObj.drawString(s, width / 2 - fontRendererObj.getStringWidth(s) / 2, y + 6, 0xFFFFFF, true);
            y += 20;
            for (String g : holder.getKeys()) {
                JsonHolder gHolder = holder.optJSONObject(g);
                String display = gHolder.optString("display");
                GuiBlock block = new GuiBlock(width / 2 - 100, width / 2 + 100, y, y + 12);
                block.ensureHeight(20, true);
                drawRect(block.getLeft(), block.getTop(), block.getRight(), block.getBottom(), new Color(0, 0, 0, 100).getRGB());
                if (selected.contains(g))
                    drawRect(block.getLeft(), block.getTop(), block.getLeft() + 3, block.getBottom(), new Color(149, 201, 144).getRGB());
                fontRendererObj.drawString(display, width / 2 - fontRendererObj.getStringWidth(display) / 2, 6 + block.getTop(), 0xFFFFFF, true);
                boxes.add(new GuiBoxItem<>(block, g));
                y += block.getHeight();
            }
        }
    }
}
