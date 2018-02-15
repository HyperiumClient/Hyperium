package com.hcc.gui.integrations;

import club.sk1er.website.api.requests.HypixelApiFriendObject;
import com.hcc.HCC;
import com.hcc.gui.HCCGui;
import com.hcc.mods.sk1ercommon.ResolutionUtil;
import com.hcc.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class HypixelFriendsGui extends HCCGui {

    private int tick;
    private HypixelFriends friends;
    private GuiTextField textField;
    private int offset = 0;

    public HypixelFriendsGui() {
        rebuildFriends();
    }


    @Override
    public void initGui() {
        super.initGui();
        //TODO remove direct address to MC font renderer
        int textWidth = ResolutionUtil.current().getScaledWidth() / 9;
        int height = 20;
        textField = new GuiTextField(nextId(), Minecraft.getMinecraft().fontRendererObj, ResolutionUtil.current().getScaledWidth() / 2 - textWidth / 2, 25, textWidth, height);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        textField.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        textField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if (i < 0) {
            offset += 10;
        } else if (i > 0) {
            offset -= 10;
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        tick++;
        if (tick % 20 == 0) {
            rebuildFriends();
        }


    }

    private void rebuildFriends() {
        this.friends = new HypixelFriends(HCC.INSTANCE.getHandlers().getDataHandler().getFriends());

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        friends.removeIf(hypixelApiFriendObject -> !hypixelApiFriendObject.getDisplay().toLowerCase().contains(textField.getText().toLowerCase()));
        super.drawScreen(mouseX, mouseY, partialTicks);
        textField.drawTextBox();
        final int topRenderBound = 49 + offset;
        final int bottomRenderBound = ResolutionUtil.current().getScaledHeight() / 9 * 8;
        int xInterval = ResolutionUtil.current().getScaledWidth() / 9;
        int startX = xInterval * 2;
        int yInterval = 15;
        int startY = 60;
        int sliderX = 0;

        for (HypixelApiFriendObject hypixelApiFriendObject : friends.get()) {
            if (sliderX > 2) {
                sliderX = 0;
                startY += yInterval;
            }
            if (startY < topRenderBound || startY > bottomRenderBound)
                continue;
            drawCenteredString(fontRendererObj, hypixelApiFriendObject.getDisplay(), startX + xInterval + (sliderX * 2 * xInterval), startY, Color.WHITE.getRGB());
            sliderX++;
        }
    }

    class HypixelFriends {
        private final List<HypixelApiFriendObject> all = new ArrayList<>();
        private List<HypixelApiFriendObject> working = new ArrayList<>();

        HypixelFriends(JsonHolder data) {
            for (String s : data.getKeys()) {
                all.add(new HypixelApiFriendObject(data.optJSONObject(s)));
            }
            reset();

        }

        public void removeIf(Predicate<? super HypixelApiFriendObject> e) {
            reset();
            working.removeIf(e);
        }

        public void reset() {
            working = all;
        }

        public List<HypixelApiFriendObject> get() {
            return working;
        }
    }
}
