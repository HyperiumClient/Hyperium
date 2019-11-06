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

package cc.hyperium.gui.integrations;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.network.server.hypixel.FriendRemoveEvent;
import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.GuiBoxItem;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.mods.chromahud.NumberUtil;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.JsonHolder;
import club.sk1er.website.api.requests.HypixelApiFriendObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;

public class HypixelFriendsGui extends HyperiumGui {
    private static FriendSortType sortType = FriendSortType.NONE;
    private final int topRenderBound = 50;
    private final List<HypixelApiFriendObject> selected = new ArrayList<>();
    private final List<GuiBoxItem<HypixelApiFriendObject>> friendListBoxes = new ArrayList<>();
    private final List<GuiBoxItem<HypixelApiFriendObject>> selectedBoxes = new ArrayList<>();
    private int tick;
    private HypixelFriends friends;
    private GuiTextField textField;
    private GuiBoxItem<HypixelApiFriendObject> selectedItem;
    private int columnWidth;
    private int removeTicks;

    public HypixelFriendsGui() {
        rebuildFriends();
        EventBus.INSTANCE.register(this);
    }

    @Override
    public void initGui() {
        columnWidth = fontRendererObj.getStringWidth("[YOUTUBER] Zyphalopagus1245");
        super.initGui();
    }

    protected void pack() {
        int textWidth = Math.max(ResolutionUtil.current().getScaledWidth() / 9, 100);
        int height = 20;
        if (textField == null)
            textField = new GuiTextField(nextId(), Minecraft.getMinecraft().fontRendererObj, ResolutionUtil.current().getScaledWidth() / 2 - textWidth / 2, 25, textWidth, height);

        reg("SORT", new GuiButton(nextId(), ResolutionUtil.current().getScaledWidth() - 153, 23, 150, 20, "Sort by: "), guiButton -> {
            int ord = sortType.ordinal();
            ord++;
            if (ord >= FriendSortType.values().length)
                ord = 0;
            sortType = FriendSortType.values()[ord];
            rebuildFriends();
            this.friends.sort(sortType);
        }, guiButton -> guiButton.displayString = "Sort by: " + sortType.getName());

        reg("PARTY", new GuiButton(nextId(), ResolutionUtil.current().getScaledWidth() - 153, 23 + 21, 150, 20, "Party Selected"), guiButton -> {
            Iterator<HypixelApiFriendObject> iterator = selected.iterator();
            while (iterator.hasNext()) {
                HypixelApiFriendObject next = iterator.next();
                if (iterator.hasNext())
                    Hyperium.INSTANCE.getHandlers().getCommandQueue().queue("/party invite " + next.getName());
                else
                    Hyperium.INSTANCE.getHandlers().getCommandQueue().register("/party invite " + next.getName(), () -> guiButton.enabled = true);
            }
            selected.clear();

            guiButton.enabled = false;

        }, guiButton -> {
            try {
                if (selected.size() > 10 && !Hyperium.INSTANCE.getHandlers().getDataHandler().getCurrentUser().get().isStaffOrYT()) {
                    guiButton.enabled = false;
                    guiButton.displayString = "Too many players!";
                } else {
                    guiButton.enabled = true;
                    guiButton.displayString = "Party Selected";
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }


        });

        reg("REMOVE", new GuiButton(nextId(), ResolutionUtil.current().getScaledWidth() - 153, 23 + 21 * 2, 150, 20, "Remove (Hold down)"), guiButton -> {

        }, guiButton -> {
            if (guiButton.isMouseOver() && Mouse.isButtonDown(0) && guiButton.enabled) {
                if (selected.isEmpty()) {
                    guiButton.displayString = "Select people first!";
                    return;
                }
                removeTicks++;
                final int totalTick = 100;
                if (removeTicks >= totalTick) {
                    Iterator<HypixelApiFriendObject> iterator = selected.iterator();
                    while (iterator.hasNext()) {
                        HypixelApiFriendObject next = iterator.next();
                        if (iterator.hasNext())
                            Hyperium.INSTANCE.getHandlers().getCommandQueue().queue("/friend remove " + next.getName());
                        else
                            Hyperium.INSTANCE.getHandlers().getCommandQueue().register("/friend remove " + next.getName(), () -> guiButton.enabled = true);
                    }
                    guiButton.enabled = false;
                    selected.clear();
                }
                double remaining = totalTick - removeTicks;
                guiButton.displayString = ChatColor.RED + "Removing in: " + NumberUtil.round(remaining / 20, 1);


            } else {
                removeTicks = 0;
                guiButton.displayString = "Remove (Hold down)";
            }
        });

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
        selectedItem = null;

        GuiBoxItem<HypixelApiFriendObject> remove = null;
        for (GuiBoxItem<HypixelApiFriendObject> selectedBox : selectedBoxes) {
            if (selectedBox.getBox().isMouseOver(mouseX, mouseY)) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    selected.remove(selectedBox.getObject());
                    remove = selectedBox;
                } else selectedItem = selectedBox;
            }
        }
        if (remove != null) {
            selectedBoxes.remove(remove);
        }

        for (GuiBoxItem<HypixelApiFriendObject> selectedBox : friendListBoxes) {
            if (selectedBox.getBox().isMouseOver(mouseX, mouseY)) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    if (selected.contains(selectedBox.getObject()))
                        continue;
                    selected.add(selectedBox.getObject());
                    GuiBoxItem<HypixelApiFriendObject> e = new GuiBoxItem<>(new GuiBlock(2 + 5, columnWidth + 5, topRenderBound + 1 + (selected.size()) * 11, topRenderBound + 1 + (selected.size() + 1) * 11), selectedBox.getObject());
                    selectedBoxes.add(e);
                    selectedItem = e;
                } else selectedItem = selectedBox;
            }
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
        try {
            this.friends = new HypixelFriends(Hyperium.INSTANCE.getHandlers().getDataHandler().getFriendsForCurrentUser().get().getData());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        this.friends.sort(sortType);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        selectedBoxes.clear();
        friendListBoxes.clear();
        friends.removeIf(hypixelApiFriendObject -> !hypixelApiFriendObject.getDisplay().toLowerCase().contains(textField.getText().toLowerCase()));
        super.drawScreen(mouseX, mouseY, partialTicks);

        textField.drawTextBox();


        //Some long name
        final int bottomRenderBound = ResolutionUtil.current().getScaledHeight() / 9 * 8;

        if (selectedItem != null) {
            GuiBlock box = selectedItem.getBox();
            int left = box.getLeft() - 2;
            int right = box.getLeft() + fontRendererObj.getStringWidth(selectedItem.getObject().getDisplay()) + 2;
            int top = box.getTop() - 2;
            int bottom = top + 10;
            if (top >= topRenderBound && bottom <= bottomRenderBound) {
                Gui.drawRect(left, top, right, top + 1, Color.RED.getRGB());
                Gui.drawRect(left, bottom, right, bottom + 1, Color.RED.getRGB());
                Gui.drawRect(right, top, right - 1, bottom, Color.RED.getRGB());
                Gui.drawRect(left, top, left + 1, bottom, Color.RED.getRGB());
            }
        }
        GuiBlock namesBlock = new GuiBlock(2, columnWidth, topRenderBound, topRenderBound);
        int row = 1;
        namesBlock.drawString("Currently selected: ", fontRendererObj, false, true, namesBlock.getWidth() / 2, 1, true, true, Color.RED.getRGB(), true);
        for (HypixelApiFriendObject object : selected) {
            namesBlock.drawString(object.getDisplay(), fontRendererObj, false, false, 5, 1 + row * 11, true, true, Color.WHITE.getRGB(), true);
            selectedBoxes.add(new GuiBoxItem<>(new GuiBlock(2 + 5, namesBlock.getRight() + 5, namesBlock.getTop() + 1 + row * 11, namesBlock.getTop() + 1 + (row + 1) * 11), object));
            row++;
        }
        GuiBlock friendsBlock = new GuiBlock(namesBlock.getRight() + 15, ResolutionUtil.current().getScaledWidth() - 100, topRenderBound, bottomRenderBound);
        int drawX = friendsBlock.getLeft();
        int drawY = friendsBlock.getTop() - offset;
        if (drawY > bottomRenderBound) {
            offset = 0;
        }

        int cols = 1;
        while (drawX + columnWidth * cols < friendsBlock.getRight()) {
            cols++;
        }
        cols -= 1;
        if (cols <= 0)
            return;

        for (HypixelApiFriendObject object : friends.get()) {
            if (drawX + columnWidth > friendsBlock.getRight()) {
                drawX = friendsBlock.getLeft();
                drawY += 11;
            }
            if (selectedItem != null && selectedItem.getObject().equals(object) && !selected.contains(selectedItem.getObject())) {
                selectedItem = new GuiBoxItem<>(new GuiBlock(drawX, drawX + columnWidth, drawY + friendsBlock.getTop(), drawY + friendsBlock.getTop() + 11), object);
            }
            if (friendsBlock.drawString(object.getDisplay(), fontRendererObj, false, false, drawX - friendsBlock.getLeft(), drawY, false, false, Color.WHITE.getRGB(), true)) {
                GuiBoxItem<HypixelApiFriendObject> e = new GuiBoxItem<>(new GuiBlock(drawX, drawX + columnWidth, drawY + friendsBlock.getTop(), drawY + friendsBlock.getTop() + 11), object);
                friendListBoxes.add(e);

            }
            drawX += columnWidth;
        }

        // After first wave, if bottom of people is still not on screen, fix
        if (drawY < topRenderBound)
            offset = 0;
    }

    @InvokeEvent
    public void onRemove(FriendRemoveEvent event) {
        JsonHolder friends = null;
        try {
            friends = Hyperium.INSTANCE.getHandlers().getDataHandler().getFriendsForCurrentUser().get().getData();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        String key = null;
        for (Map.Entry<String, JsonElement> stringJsonElementEntry : friends.getObject().entrySet()) {
            if (!(stringJsonElementEntry.getValue() instanceof JsonObject))
                continue;
            String display = stringJsonElementEntry.getValue().getAsJsonObject().get("display").getAsString();

            if (EnumChatFormatting.getTextWithoutFormattingCodes(display).contains(event.getFullName()))
                key = stringJsonElementEntry.getKey();
        }
        if (key != null) {
            friends.remove(key);
        }
    }

    enum FriendSortType implements Comparator<HypixelApiFriendObject> {

        ALPHABETICAL("Alphabetical") {
            @Override
            public int compare(HypixelApiFriendObject o1, HypixelApiFriendObject o2) {
                return o1.getName().compareTo(o2.getName());
            }
        },
        RANK("Rank") {
            @Override
            public int compare(HypixelApiFriendObject o1, HypixelApiFriendObject o2) {
                int compare = Integer.compare(o1.rankOrdinal(), o2.rankOrdinal());
                if (compare == 0) {
                    return o1.getName().compareTo(o2.getName());
                }
                return compare;
            }
        },
        DATE_ADDED("Date Added") {
            @Override
            public int compare(HypixelApiFriendObject o1, HypixelApiFriendObject o2) {
                return Long.compare(o1.getAddedOn(), o2.getAddedOn());
            }
        }, LOGOFF("Latest Logoff") {
            @Override
            public int compare(HypixelApiFriendObject o1, HypixelApiFriendObject o2) {
                //Reverse it so most recent is first.
                return Long.compare(o1.getLogoff(), o2.getLogoff()) * -1;
            }
        }, NONE("NONE") {
            @Override
            public int compare(HypixelApiFriendObject o1, HypixelApiFriendObject o2) {
                return 0;
            }
        },
        ;
        String name;

        FriendSortType(String name) {
            this.name = name;

        }

        public String getName() {
            return name;
        }

    }

    static class HypixelFriends {
        private final List<HypixelApiFriendObject> all = new ArrayList<>();
        private List<HypixelApiFriendObject> working = new ArrayList<>();

        HypixelFriends(JsonHolder data) {
            for (String s : data.getKeys()) {
                JsonHolder jsonHolder = data.optJSONObject(s);
                jsonHolder.put("uuid", s);
                all.add(new HypixelApiFriendObject(jsonHolder));
            }
            reset();

        }

        public void sort(FriendSortType type) {
            reset();
            all.sort(type);
            working = all;
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
