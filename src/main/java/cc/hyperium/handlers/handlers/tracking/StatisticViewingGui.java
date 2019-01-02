package cc.hyperium.handlers.handlers.tracking;

import cc.hyperium.Hyperium;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.utils.RenderUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StatisticViewingGui extends HyperiumGui {

    private static ValueTrackingType currentType = ValueTrackingType.COINS;
    private final int DATA_POINTS = 200;
    int timeFac = 0;
    private long masterTimeOne = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1);
    private long masterTimeTwo = System.currentTimeMillis();
    private List<ValueTrackingItem> masterDataSet;
    private List<ValueTrackingType> types = new ArrayList<>(Arrays.asList(ValueTrackingType.values())); //so we get full arraylist operations

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

        //Put data into different slots based on time
        ArrayList<ValueTrackingItem> tmp = new ArrayList<>(masterDataSet);
        masterDataSet.clear();
        long delta = (masterTimeTwo - masterTimeOne) / DATA_POINTS;
        HashMap<Integer, List<ValueTrackingItem>> itemMap = new HashMap<>();
        for (ValueTrackingItem valueTrackingItem : tmp) {
            itemMap.computeIfAbsent((int) ((valueTrackingItem.getTime() - masterTimeOne) / delta), integer -> new ArrayList<>()).add(valueTrackingItem);
        }

        //Handle data collision
        HashMap<Integer, List<ValueTrackingItem>> dataPoints = new HashMap<>();
        for (int integer = 0; integer < DATA_POINTS; integer++) {

            List<ValueTrackingItem> valueTrackingItems = itemMap.get(integer);
            if (valueTrackingItems == null) // No data = no collision
                continue;
            HashMap<ValueTrackingType, List<ValueTrackingItem>> map = new HashMap<>();
            for (ValueTrackingItem valueTrackingItem : valueTrackingItems) {
                map.computeIfAbsent(valueTrackingItem.getType(), valueTrackingType -> new ArrayList<>()).add(valueTrackingItem);
            }
            for (ValueTrackingType type : map.keySet()) {
                if (type.getCompressionType() == CompressionType.SUM) {
                    int sum = 0;
                    for (ValueTrackingItem valueTrackingItem : valueTrackingItems) {
                        if (valueTrackingItem.getType() == type)
                            sum += valueTrackingItem.getValue();
                    }
                    ValueTrackingItem e = new ValueTrackingItem(type, sum, masterTimeOne + delta * (long) integer);
                    dataPoints.computeIfAbsent(integer, integer1 -> new ArrayList<>()).add(e);
                    masterDataSet.add(e);
                } else if (type.getCompressionType() == CompressionType.MAX) {
                    int max = -Integer.MAX_VALUE;
                    for (ValueTrackingItem valueTrackingItem : valueTrackingItems) {
                        if (valueTrackingItem.getType() == type)
                            max = Math.max(max, valueTrackingItem.getValue());
                    }
                    ValueTrackingItem e = new ValueTrackingItem(type, max, masterTimeOne + delta * (long) integer);
                    dataPoints.computeIfAbsent(integer, integer1 -> new ArrayList<>()).add(e);
                    masterDataSet.add(e);
                }
            }
        }

        //Fill in missing data
        for (int integer = 0; integer < DATA_POINTS; integer++) {
            List<ValueTrackingItem> valueTrackingItems = dataPoints.get(integer);
            if (valueTrackingItems == null) {
                for (ValueTrackingType type : types) {
                    MissingDataHandling missingDataHandling = type.getMissingDataHandling();
                    if (missingDataHandling == MissingDataHandling.ZERO) {
                        masterDataSet.add(new ValueTrackingItem(type, 0, masterTimeOne + delta * (long) integer));
                    } else {
                        //Find average of values on both sides.
                        ValueTrackingItem left = null;
                        int lI = 0;
                        ValueTrackingItem right = null;
                        int rI = DATA_POINTS - 1;
                        OUTSIDE1:
                        for (int j = integer; j >= 0; j--) {
                            List<ValueTrackingItem> tmp1 = dataPoints.get(j);
                            if (tmp1 != null) {
                                for (ValueTrackingItem valueTrackingItem : tmp1) {
                                    if (valueTrackingItem.getType() == type) {
                                        left = valueTrackingItem;
                                        lI = j;
                                        break OUTSIDE1;
                                    }
                                }
                            }
                        }
                        OUTSIDE2:
                        for (int j = integer; j < DATA_POINTS; j++) {
                            List<ValueTrackingItem> tmp1 = dataPoints.get(j);
                            if (tmp1 != null) {
                                for (ValueTrackingItem valueTrackingItem : tmp1) {
                                    if (valueTrackingItem.getType() == type) {
                                        right = valueTrackingItem;
                                        rI = j;
                                        break OUTSIDE2;
                                    }
                                }
                            }
                        }
                        if (left == null && right == null) {
                            masterDataSet.add(new ValueTrackingItem(type, 0, masterTimeOne + delta * (long) integer));
                            continue;
                        }
                        if (right == null) {
                            right = new ValueTrackingItem(type, left.getValue(), masterTimeTwo);
                        }
                        if (left == null) {
                            left = new ValueTrackingItem(type, right.getValue(), masterTimeOne);
                        }

                        int delta1 = right.getValue() - left.getValue();
                        rI -= lI;
                        int temp = integer - lI;
                        double percent = (double) temp / (double) rI;
                        double v = left.getValue() + (percent * ((double) delta1));
                        masterDataSet.add(new ValueTrackingItem(type, (int) v, masterTimeOne + delta * (long) integer));

                    }

                }
            }
        }
        masterDataSet.sort(Comparator.comparingLong(ValueTrackingItem::getTime));
    }

    @Override
    protected void pack() {
        reg("Next Graph", new GuiButton(nextId(), 1, 1, "Next Graph"), button -> {
            int i = types.indexOf(currentType);
            i++;
            if (i > types.size() - 1)
                i = 0;
            currentType = types.get(i);
        }, button -> {

        });
        reg("Time", new GuiButton(nextId(), 1, 22, "Change Time"), button -> {
            timeFac++;
            if (timeFac > 3)
                timeFac = 0;
            masterTimeTwo = System.currentTimeMillis();
            if (timeFac == 0) {
                masterTimeOne = masterTimeTwo - TimeUnit.HOURS.toMillis(1);

            } else if (timeFac == 1) {
                masterTimeOne = masterTimeTwo - TimeUnit.DAYS.toMillis(1);

            } else if (timeFac == 2) {
                masterTimeOne = masterTimeTwo - TimeUnit.DAYS.toMillis(7);

            } else if (timeFac == 3) {
                masterTimeOne = masterTimeTwo - TimeUnit.DAYS.toMillis(30);
            }
            refreshData();

        }, button -> {
            String tmp = "Set range to: ";
            if (timeFac == 0) {
                tmp += "1 day";
            } else if (timeFac == 1) {
                tmp += "1 week";

            } else if (timeFac == 2) {
                tmp += "1 month";

            } else if (timeFac == 3) {
                tmp += "1 hour";
            }
            button.displayString = tmp;
        });
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawScaledText(currentType.getDisplay(), width / 2, 10, 2.0F, Color.WHITE.getRGB(), true, true);
        DateFormat df = new SimpleDateFormat("MM/dd/YYYY HH:mm:ss");
        String formattedOne = df.format(new Date(masterTimeOne));
        String formattedTwo = df.format(new Date(masterTimeTwo));
        drawScaledText(formattedOne + " - " + formattedTwo, width / 2, 30, 1.5, Color.WHITE.getRGB(), true, true);
        int xg = width / 10;
        int yg = height / 7;
        ArrayList<ValueTrackingItem> currentDataSet = new ArrayList<>(masterDataSet);
        currentDataSet.removeIf(item -> item.getType() != currentType);
        int max = 0;
        for (ValueTrackingItem valueTrackingItem : currentDataSet) {
            max = Math.max(max, valueTrackingItem.getValue());
        }
        if (max == 0)
            max = 100;
        drawScaledText(Integer.toString(max), xg - fontRendererObj.getStringWidth(Integer.toString(max)), yg, 1.0, Color.WHITE.getRGB(), true, true);
        drawScaledText("0", xg - fontRendererObj.getStringWidth("0"), yg * 6 - 10, 1.0, Color.WHITE.getRGB(), true, true);

        float angle = 30;
        int stringWidth = fontRendererObj.getStringWidth(formattedOne);
        float scale2 = (float) ((xg - 5) / ((double) stringWidth));

        GlStateManager.pushMatrix();
        GlStateManager.translate(xg - stringWidth * MathHelper.cos((float) Math.toRadians(angle)) * scale2, yg * 6 + fontRendererObj.getStringWidth(formattedOne) * MathHelper.sin((float) Math.toRadians(angle)) * scale2, 0);
        GlStateManager.rotate(-angle, 0, 0, 1.0F);
        GlStateManager.scale(scale2, scale2, scale2);
        fontRendererObj.drawString(formattedOne, 0, 0, Color.WHITE.getRGB());
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(xg * 9 - fontRendererObj.getStringWidth(formattedTwo) * MathHelper.cos((float) Math.toRadians(angle)) * scale2, yg * 6 + fontRendererObj.getStringWidth(formattedTwo) * MathHelper.sin((float) Math.toRadians(angle)) * scale2, 0);
        GlStateManager.rotate(-angle, 0, 0, 1.0F);
        GlStateManager.scale(scale2, scale2, scale2);
        fontRendererObj.drawString(formattedTwo, 0, 0, Color.WHITE.getRGB());
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        RenderUtils.drawRect(xg, yg - 5, xg * 9, yg * 6, new Color(0, 0, 0, 100).getRGB());
        GlStateManager.translate(xg, yg, 0);
        int chartWidth = xg * 8;
        long delta = masterTimeTwo - masterTimeOne;

        int size = currentDataSet.size();
        GlStateManager.resetColor();
        GlStateManager.color(66 / 255F, 244 / 255F, 241 / 255F);
        GlStateManager.translate(0, -5, 0);
        ValueTrackingItem closest = null;
        int closeDistance = Integer.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            ValueTrackingItem valueTrackingItem = currentDataSet.get(i);
            GlStateManager.pushMatrix();
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(2848);
            GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);


            long time = valueTrackingItem.getTime();
            time -= masterTimeOne;
            double v = (double) time / (double) delta;
            int xPos = (int) (v * (double) chartWidth);
            int yPos = (int) ((double) valueTrackingItem.getValue() / (double) max * (double) yg * 5D);

            GL11.glBegin(GL11.GL_LINES);
            GL11.glLineWidth(6);
            int x2 = chartWidth;
            int y2 = yPos;
            if (i + 1 < size) {
                ValueTrackingItem valueTrackingItem1 = currentDataSet.get(i + 1);
                long time1 = valueTrackingItem1.getTime();
                time1 -= masterTimeOne;
                x2 = (int) ((double) time1 / (double) delta * (double) chartWidth);
                y2 = (int) ((double) valueTrackingItem1.getValue() / (double) max * (double) yg * 5D);
            }
            double tmpDistance = Math.pow((mouseY - yg) - (yg * 5 - yPos), 2) + Math.pow((mouseX - xg) - xPos, 2);
            if (tmpDistance < closeDistance) {
                closeDistance = ((int) tmpDistance);
                closest = valueTrackingItem;
            }
            GL11.glVertex2d(xPos, yg * 5 - yPos);
//            GL11.glVertex2d(xPos, yg * 5 + 5);
//            GL11.glVertex2d(x2, yg * 5 + 5);
            GL11.glVertex2d(x2, yg * 5 - y2);

            GL11.glEnd();
            GL11.glEnable(3553);
            GL11.glDisable(3042);
            GL11.glDisable(2848);
            GlStateManager.popMatrix();

        }

        if (closest != null) {
            long time = closest.getTime();
            time -= masterTimeOne;
            double v = (double) time / (double) delta;
            int xPos = (int) (v * (double) chartWidth);
            int yPos = (int) ((double) closest.getValue() / (double) max * (double) yg * 5D);

            yPos = (yg * 5 - yPos);
            RenderUtils.drawFilledCircle(xPos, yPos, 4, Color.RED.getRGB());
            List<String> lines = new ArrayList<>();
            lines.add(df.format(new Date(closest.getTime())));
            lines.add("Value: " + closest.getValue());
            int maxWidth = 0;
            for (String line : lines) {
                maxWidth = Math.max(maxWidth, fontRendererObj.getStringWidth(line));
            }
            GlStateManager.translate(0, -lines.size() * 10 - 10, 0);
            RenderUtils.drawRect(xPos - maxWidth / 2 - 2, yPos - 10 * lines.size() / 2 - 2, xPos + maxWidth / 2 + 2, yPos + 10 * lines.size() / 2 + 2, new Color(0, 0, 0, 100).getRGB());
            int l = 0;
            for (String line : lines) {
                fontRendererObj.drawString(line, xPos - maxWidth / 2, yPos - 10 * lines.size() / 2 + (l * 10), Color.WHITE.getRGB(), true);
                l++;
            }

        }
        GlStateManager.popMatrix();
    }

    enum CompressionType {
        MAX,
        SUM,
    }

    enum MissingDataHandling {
        ZERO,
        AVERAGE
    }


}

