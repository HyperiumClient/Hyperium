package cc.hyperium.mods.chromahud.displayitems.hyperium;

import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import java.util.ArrayList;
import java.util.List;

public class BedwarsResourcesDisplay extends DisplayItem {

    public BedwarsResourcesDisplay(JsonHolder data, int ordinal) {
        super(data, ordinal);
        this.height = 5 * 10;
        this.width = 6.5 * 10;
    }

    @Override
    public void draw(int x, double y, boolean config) {
        List<String> list = new ArrayList<>();
        InventoryPlayer inventoryPlayer = Minecraft.getMinecraft().thePlayer.inventory;
        int totalAmount = 0;
        for (int i = 0; i < inventoryPlayer.getSizeInventory(); i++) {
            if (inventoryPlayer.getStackInSlot(i) == null || inventoryPlayer.getStackInSlot(i).getItem() == null)
                continue;
            if (inventoryPlayer.getStackInSlot(i).getItem() instanceof ItemBlock) {
                totalAmount += inventoryPlayer.getStackInSlot(i).stackSize;
            }
        }
        list.add("BedWars Resources:");
        list.add("Blocks: " + totalAmount);
        list.add("Iron: " + getAmountOfType(Items.iron_ingot));
        list.add("Gold: " + getAmountOfType(Items.gold_ingot));
        list.add("Diamond: " + getAmountOfType(Items.diamond));
        list.add("Emerald: " + getAmountOfType(Items.emerald));
        ElementRenderer.draw(x, y, list);
    }

    private int getAmountOfType(Item item) {
        InventoryPlayer inventoryPlayer = Minecraft.getMinecraft().thePlayer.inventory;
        int totalAmount = 0;
        for (int i = 0; i < inventoryPlayer.getSizeInventory(); i++) {
            if (inventoryPlayer.getStackInSlot(i) == null || inventoryPlayer.getStackInSlot(i).getItem() == null)
                continue;
            if (inventoryPlayer.getStackInSlot(i).getItem().getUnlocalizedName().equalsIgnoreCase(item.getUnlocalizedName())) {
                totalAmount += inventoryPlayer.getStackInSlot(i).stackSize;
            }
        }
        return totalAmount;
    }


}
