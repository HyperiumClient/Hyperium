package com.hcc.gui.integrations;

import club.sk1er.website.api.requests.HypixelApiFriendObject;
import com.hcc.HCC;
import com.hcc.mods.sk1ercommon.ResolutionUtil;
import com.hcc.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class HypixelFriendsGui extends GuiScreen {

    private int tick;
    private HypixelFriends friends;
    private GuiTextField textField;
    private int id = 0;

    public HypixelFriendsGui() {
        rebuildFriends();
    }

    private int nextId() {
        return (++id);
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
