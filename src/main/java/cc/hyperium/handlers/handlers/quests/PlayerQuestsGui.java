package cc.hyperium.handlers.handlers.quests;

import cc.hyperium.C;
import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.Icons;
import cc.hyperium.handlers.handlers.stats.AbstractHypixelStats;
import cc.hyperium.handlers.handlers.stats.display.StatsDisplayItem;
import cc.hyperium.handlers.handlers.stats.fields.ArcadeStats;
import cc.hyperium.handlers.handlers.stats.fields.ArenaStats;
import cc.hyperium.handlers.handlers.stats.fields.BedWarsStats;
import cc.hyperium.handlers.handlers.stats.fields.BlitzStats;
import cc.hyperium.handlers.handlers.stats.fields.BuildBattleStats;
import cc.hyperium.handlers.handlers.stats.fields.CVCStats;
import cc.hyperium.handlers.handlers.stats.fields.CrazyWallsStats;
import cc.hyperium.handlers.handlers.stats.fields.DuelsStats;
import cc.hyperium.handlers.handlers.stats.fields.MegaWallsStats;
import cc.hyperium.handlers.handlers.stats.fields.MurderMysteryStats;
import cc.hyperium.handlers.handlers.stats.fields.PaintballStats;
import cc.hyperium.handlers.handlers.stats.fields.QuakecraftStats;
import cc.hyperium.handlers.handlers.stats.fields.SkyClashStats;
import cc.hyperium.handlers.handlers.stats.fields.SkyWarsStats;
import cc.hyperium.handlers.handlers.stats.fields.SmashHeroesStats;
import cc.hyperium.handlers.handlers.stats.fields.SpeedUHCStats;
import cc.hyperium.handlers.handlers.stats.fields.TKRStats;
import cc.hyperium.handlers.handlers.stats.fields.TNTGamesStats;
import cc.hyperium.handlers.handlers.stats.fields.UHCStats;
import cc.hyperium.handlers.handlers.stats.fields.VampireZStats;
import cc.hyperium.handlers.handlers.stats.fields.WallsStats;
import cc.hyperium.handlers.handlers.stats.fields.WarlordsStats;
import cc.hyperium.mixinsimp.client.GlStateModifier;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.utils.RenderUtils;
import club.sk1er.website.api.requests.HypixelApiGuild;
import club.sk1er.website.api.requests.HypixelApiPlayer;
import club.sk1er.website.utils.WebsiteUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerQuestsGui extends HyperiumGui {

    private static Map<AbstractHypixelStats, DynamicTexture> logos = new HashMap<>();
    private HypixelApiPlayer player;
    private AbstractHypixelStats hovered;
    private AbstractHypixelStats focused;
    private List<AbstractHypixelStats> fields = new ArrayList<>();
    private Map<AbstractHypixelStats, BufferedImage> texturesImage = new ConcurrentHashMap<>();
    private ConcurrentHashMap<AbstractHypixelStats, GuiBlock> location = new ConcurrentHashMap<>();
    HypixelApiGuild guild;

    //TODO make only generate once
    public PlayerQuestsGui(HypixelApiPlayer player) {
        this.player = player;
        fields.add(new ArcadeStats());
        fields.add(new ArenaStats());
        fields.add(new BedWarsStats());
        fields.add(new BlitzStats());
        fields.add(new BuildBattleStats());
        fields.add(new CrazyWallsStats());
        fields.add(new CVCStats());
        fields.add(new DuelsStats());
        fields.add(new MegaWallsStats());
        fields.add(new MurderMysteryStats());
        fields.add(new PaintballStats());
        fields.add(new QuakecraftStats());
        fields.add(new SkyClashStats());
        fields.add(new SkyWarsStats());
        fields.add(new SmashHeroesStats());
        fields.add(new SpeedUHCStats());
        fields.add(new TKRStats());
        fields.add(new TNTGamesStats());
        fields.add(new UHCStats());
        fields.add(new VampireZStats());
        fields.add(new WallsStats());
        fields.add(new WarlordsStats());
        for (AbstractHypixelStats field : fields) {
            Multithreading.runAsync(() -> {
                if (!logos.containsKey(field))
                    try {
                        URL url = new URL("https://static.sk1er.club/hypixel_games/" + field.getImage() + ".png");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setUseCaches(true);
                        connection.addRequestProperty("User-Agent", "Mozilla/4.76 Hyperium ");
                        connection.setReadTimeout(15000);
                        connection.setConnectTimeout(15000);
                        connection.setDoOutput(true);
                        InputStream is = connection.getInputStream();
                        BufferedImage img = ImageIO.read(ImageIO.createImageInputStream(is));
                        texturesImage.put(field, img);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            });

        }

        guild = player.getGuild();

        //Init
        for (AbstractHypixelStats field : fields) {
            field.getQuests(player);
        }
    }

    public static void print(ScaledResolution current, List<StatsDisplayItem> deepStats, int printY) {
        for (StatsDisplayItem statsDisplayItem : deepStats) {
            GlStateManager.pushMatrix();
            GlStateManager.resetColor();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            int y = (95) + printY;
            if (y > 73 + 64 && y < current.getScaledHeight() - 50)
                statsDisplayItem.draw(current.getScaledWidth() / 2 - statsDisplayItem.width / 2, y);
            printY += statsDisplayItem.height;
            GlStateManager.popMatrix();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        boolean flag = false;
        for (GuiButton guiButton : buttonList) {
            if (guiButton.isMouseOver())
                flag = true;
        }
        boolean flag2 = focused == null;
        if (!flag && flag2)
            focused = null;

        ScaledResolution current = ResolutionUtil.current();
        if (focused != null && new GuiBlock((current.getScaledWidth() / 2 - 22 - 64), (current.getScaledWidth() / 2 - 22), 73, 73 + 64).isMouseOver(mouseX, mouseY)) {
            focused = null;
            offset = 0;
        }
        if (flag2)
            if (mouseButton == 0) {
                for (AbstractHypixelStats abstractHypixelStats : location.keySet()) {
                    if (location.get(abstractHypixelStats).isMouseOver(mouseX, mouseY)) {
                        focused = abstractHypixelStats;
                        hovered = null;
                        offset = 0;
                    }
                }
            }

    }

    @Override
    protected void pack() {


    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (!texturesImage.isEmpty()) {
            for (AbstractHypixelStats s : texturesImage.keySet()) {
                if (!logos.containsKey(s))
                    logos.put(s, new DynamicTexture(texturesImage.get(s)));
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution current = ResolutionUtil.current();
        boolean isInGuild = guild.isLoaded() && guild.isValid();
        double leftTextScale = 1.25;
        drawScaledText(player.getDisplayString() + (isInGuild ? " " + guild.getFormatedTag() : ""), 5, 10, leftTextScale, Color.WHITE.getRGB(), true, false);
        String s1 = "Quests Completed: ";
        drawScaledText(s1, 5, 55, leftTextScale, Color.CYAN.getRGB(), true, false);
        drawScaledText(WebsiteUtils.comma(player.getTotalQuests()), (int) (5 + fontRendererObj.getStringWidth(s1) * leftTextScale), 55, leftTextScale, Color.YELLOW.getRGB(), true, false);


        if (focused == null) {
            float scaleMod = 4 / 5F;
            GlStateManager.scale(scaleMod, scaleMod, scaleMod);
            final int blockWidth = 64 + 32;
            int blocksPerLine = (int) (current.getScaledWidth() / (1.2D * blockWidth));
            if (blocksPerLine % 2 == 1) {
                blocksPerLine--;
            }
            final int startX = ResolutionUtil.current().getScaledWidth() / 2 - (blocksPerLine * blockWidth / 2) + 15;
            int x = 0;
            int y = 0;
            hovered = null;

            for (AbstractHypixelStats field : fields) {
                field.getQuests(player);

                DynamicTexture dynamicTexture = logos.get(field);
                x++;
                if (x > blocksPerLine + 2) {
                    x = 1;
                    y++;
                }
                if (dynamicTexture != null) {
                    //Render Image

                    double spacing = 1.1;
                    int offsetY = 35;
                    int y1 = (int) (offsetY + (y * blockWidth * spacing) - 10 - offset);
                    GlStateManager.pushMatrix();
                    GlStateManager.resetColor();
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    int y2 = (int) ((offsetY + y * blockWidth * spacing) - offset);
                    int x1 = startX + x * blockWidth;
                    GuiBlock value = new GuiBlock((int) (x1 * scaleMod), (int) ((x1 + blockWidth / 2) * scaleMod), (int) (y2 * scaleMod), (int) ((y2 + blockWidth / 2) * scaleMod));
                    if (value.isMouseOver(mouseX, mouseY)) {
                        hovered = field;
                    }
//                    value.scalePosition(1/10F);
                    location.put(field, value);
                    GlStateManager.translate(x1, y2, 0);
                    GlStateManager.bindTexture(dynamicTexture.getGlTextureId());
                    GlStateManager.scale(.2, .2, .2);
                    drawTexturedModalRect(0, 0, 0, 0, 128 * 2, 128 * 2);
                    GlStateManager.popMatrix();

                    GlStateManager.pushMatrix();
                    GlStateManager.translate(startX + x * blockWidth + 24, y1, 0);
                    String text = field.getName();
                    List<String> strings = fontRendererObj.listFormattedStringToWidth(text, blockWidth / 2 + 10);
                    int size = strings.size() - 1;
                    GlStateManager.translate(0, -size * 10, 0);
                    for (String string : strings) {
                        drawScaledText(C.BOLD + string, 0, 0, 1.0, Color.YELLOW.getRGB(), true, true);
                        GlStateManager.translate(0, 10, 0);
                    }
                    GlStateManager.translate(0, blockWidth / 2F + 5, 0);

                    float dailyPercent = Math.round(field.getCompletedDaily() / ((float) field.getTotalDaily()) * 100F);
                    String percent = field.getCompletedDaily() + "/" + field.getTotalDaily() + " (" + (int) dailyPercent + "%)";
                    float hue = dailyPercent / 100F / 3F;
                    drawScaledText(percent, 0, 0, 1.0, Color.HSBtoRGB(hue, 1.0F, 1.0F), true, true);
                    GlStateManager.translate(0, 10, 0);
                    float weeklyPercent = Math.round(field.getCompletedWeekly() / ((float) field.getTotalWeekly()) * 100F);
                    percent = field.getCompletedWeekly() + "/" + field.getTotalWeekly() + " (" + (int) weeklyPercent + "%)";
                    drawScaledText(percent, 0, 0, 1.0, Color.HSBtoRGB(weeklyPercent / 100F / 3F, 1.0F, 1.0F), true, true);


                    GlStateManager.popMatrix();
                }
            }
            GlStateManager.scale(1 / scaleMod, 1 / scaleMod, 1 / scaleMod);


            if (hovered != null) {
                List<StatsDisplayItem> preview = hovered.getQuests(player);
                int width = 0;
                int height = 0;
                for (StatsDisplayItem statsDisplayItem : preview) {
                    width = Math.max(width, statsDisplayItem.width);
                    height += statsDisplayItem.height;
                }
                GuiBlock block = location.get(hovered);
                int xOffset = 0;
                int yRenderOffset = 16;
                int rightSide = block.getRight() + (width + yRenderOffset) * 2;
                if (rightSide > current.getScaledWidth()) {
                    xOffset = rightSide - current.getScaledWidth();
                }

                float scale = 1.25F;
                GlStateManager.scale(scale, scale, scale);
                int left = block.getRight() - xOffset + yRenderOffset;
                int top = block.getTop();
                int printY = 0;
                if (top + height * 2 > current.getScaledHeight()) {
                    top = current.getScaledHeight() - height * 2 - 50;
                }
                left = 5;
                top = 80;
                RenderUtils.drawRect((left - 3) / scale, (top - 3) / scale, (left + (width + 3) * scale) / scale, (top + (height + 3) * scale) / scale,
                    new Color(0, 0, 0, 175).getRGB());

                for (StatsDisplayItem statsDisplayItem : preview) {
                    statsDisplayItem.draw((int) (left / scale), (int) ((top) / scale) + printY);
                    printY += statsDisplayItem.height;
                }
                GlStateManager.scale(1 / scale, 1 / scale, 1 / scale);
            }
        } else {
            List<StatsDisplayItem> deepStats = focused.getQuests(player);

            DynamicTexture dynamicTexture = logos.get(focused);
            GlStateManager.resetColor();
            GlStateManager.pushMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.bindTexture(dynamicTexture.getGlTextureId());
            GlStateManager.translate(current.getScaledWidth() / 2 - 24, 80, 0);
            GlStateManager.scale(.2, .2, .2);
            drawTexturedModalRect(0, 0, 0, 0, 128 * 2, 128 * 2);
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            drawScaledText(focused.getName(), current.getScaledWidth() / 2, 60, 2.0, Color.RED.getRGB(), true, true);
            Icons.EXTENSION.bind();
            GlStateModifier.INSTANCE.reset();
            Icons.EXIT.bind();
            float scale = 4.0F;
            GlStateManager.scale(scale, scale, scale);
            GlStateManager.translate(current.getScaledWidth() / 2 / scale - 90 / scale, (73) / scale, 0);
            GlStateManager.rotate(180, 0.0F, 0.0F, 1.0F);
            GlStateManager.translate(-16, -16, 0);
            drawScaledCustomSizeModalRect(0, 0, 0, 0, 64, 64, 16, 16, 64, 64);
            GlStateManager.popMatrix();
            int printY = 55 - offset;

            print(current, deepStats, printY);
        }

        float totalDaily = 0;
        float completedDaily = 0;
        float totalWeekly = 0;
        float completedWeekly = 0;
        for (AbstractHypixelStats field : fields) {
            field.getQuests(player);
            totalDaily += field.getTotalDaily();
            completedDaily += field.getCompletedDaily();

            totalWeekly += field.getTotalWeekly();
            completedWeekly += field.getCompletedWeekly();
        }
        if (totalDaily == 0)
            totalDaily = 1;
        if (totalWeekly == 0)
            totalWeekly = 1;

        float dailyPercent = Math.round(completedDaily / totalDaily * 100F);
        String s = "Daily Quests: ";
        drawScaledText(s, 5, 25, leftTextScale, Color.CYAN.getRGB(), true, false);
        drawScaledText((int) completedDaily + "/" + (int) totalDaily + " (" + (int) dailyPercent + "%)", (int) (5 + fontRendererObj.getStringWidth(s) * leftTextScale), 25, leftTextScale, Color.HSBtoRGB(dailyPercent / 100F / 3F, 1.0F, 1.0F), true, false);

        float weeklyPercent = Math.round(completedWeekly / totalWeekly * 100F);
        String text = "Weekly Quests: ";
        drawScaledText(text, 5, 40, leftTextScale, Color.CYAN.getRGB(), true, false);
        drawScaledText((int) completedWeekly + "/" + (int) totalWeekly + " (" + (int) weeklyPercent + "%)", (int) (5 + fontRendererObj.getStringWidth(text) * leftTextScale), 40, leftTextScale, Color.HSBtoRGB(weeklyPercent / 100F / 3F, 1.0F, 1.0F), true, false);


    }


}
