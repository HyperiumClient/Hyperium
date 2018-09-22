package cc.hyperium.handlers.handlers.tracking;

import cc.hyperium.Hyperium;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.utils.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StatisticViewingGui extends HyperiumGui {

    private static ValueTrackingType currentType = ValueTrackingType.COINS;
    private final int MAX_DATA = 100;
    private long masterTimeOne = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1);
    private long masterTimeTwo = System.currentTimeMillis();
    private List<ValueTrackingItem> masterDataSet;
    private List<ValueTrackingType> types = Arrays.asList(ValueTrackingType.values());

    public StatisticViewingGui() {
        types.remove(ValueTrackingType.ERROR);
    }

    @Override
    public void initGui() {
        super.initGui();
        refreshData();
    }

    private void refreshData() {
        //Guaranteed to be in ascending order
        masterDataSet = Hyperium.INSTANCE.getHandlers().getHypixelValueTracking().getItemsBetween(masterTimeOne, masterTimeTwo);
        if (masterDataSet.size() > MAX_DATA) {
            ArrayList<ValueTrackingItem> tmp = new ArrayList<>(masterDataSet);
            masterDataSet.clear();
            long delta = masterTimeTwo - masterTimeOne;
            HashMap<Integer, List<ValueTrackingItem>> itemMap = new HashMap<>();
            for (ValueTrackingItem valueTrackingItem : tmp) {
                itemMap.computeIfAbsent((int) ((valueTrackingItem.getTime() - masterTimeOne) / delta), integer -> new ArrayList<>()).add(valueTrackingItem);
            }
            for (Integer integer : itemMap.keySet()) {
                List<ValueTrackingItem> valueTrackingItems = itemMap.get(integer);
                HashMap<ValueTrackingType, List<ValueTrackingItem>> map = new HashMap<>();
                for (ValueTrackingItem valueTrackingItem : valueTrackingItems) {
                    map.computeIfAbsent(valueTrackingItem.getType(), valueTrackingType -> new ArrayList<>()).add(valueTrackingItem);
                }
                for (ValueTrackingType type : map.keySet()) {
                    if (type.getCompressionType() == CompressionType.SUM) {
                        int sum = 0;
                        for (ValueTrackingItem valueTrackingItem : valueTrackingItems) {
                            sum += valueTrackingItem.getValue();
                        }
                        masterDataSet.add(new ValueTrackingItem(type, sum, masterTimeOne + delta * (long) integer));
                    } else if (type.getCompressionType() == CompressionType.MAX) {
                        int max = -Integer.MAX_VALUE;
                        for (ValueTrackingItem valueTrackingItem : valueTrackingItems) {
                            max = Math.max(max, valueTrackingItem.getValue());
                        }
                        masterDataSet.add(new ValueTrackingItem(type, max, masterTimeOne + delta * (long) integer));
                    }
                }
            }
        }
    }


    @Override
    protected void pack() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        int xg = width / 10;
        int yg = height / 7;
        GlStateManager.pushMatrix();
        GlStateManager.translate(xg, yg * 6, 0);
        RenderUtils.drawRect(xg, yg, xg * 9, yg * 6, new Color(0, 0, 0, 100).getRGB());
        int chartWidth = xg * 8;
        long delta = masterTimeTwo - masterTimeOne;
        int max = 0;
        for (ValueTrackingItem valueTrackingItem : masterDataSet) {
            max = Math.max(max, valueTrackingItem.getValue());
        }
        for (int i = 0; i < masterDataSet.size(); i++) {
            ValueTrackingItem valueTrackingItem = masterDataSet.get(i);
            GlStateManager.pushMatrix();
            long time = valueTrackingItem.getTime();
            time -= masterTimeOne;
            int xPos = (int) ((double) delta / (double) time * (double) chartWidth);
            int yPos = (int) ((double) valueTrackingItem.getValue() / (double) max * (double) yg * 6D);
            GL11.glBegin(GL11.GL_QUADS);
            GlStateManager.popMatrix();

        }
        GlStateManager.popMatrix();
    }

    enum CompressionType {
        MAX,
        SUM,
    }


}

