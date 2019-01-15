package cc.hyperium.handlers.handlers.stats;

import cc.hyperium.gui.GuiBlock;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.Icons;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.handlers.handlers.quests.PlayerQuestsGui;
import cc.hyperium.handlers.handlers.stats.display.StatsDisplayItem;
import cc.hyperium.handlers.handlers.stats.fields.ArcadeStats;
import cc.hyperium.handlers.handlers.stats.fields.ArenaStats;
import cc.hyperium.handlers.handlers.stats.fields.BedWarsStats;
import cc.hyperium.handlers.handlers.stats.fields.BlitzStats;
import cc.hyperium.handlers.handlers.stats.fields.BuildBattleStats;
import cc.hyperium.handlers.handlers.stats.fields.CVCStats;
import cc.hyperium.handlers.handlers.stats.fields.CrazyWallsStats;
import cc.hyperium.handlers.handlers.stats.fields.DuelsStats;
import cc.hyperium.handlers.handlers.stats.fields.GeneralStats;
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
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerStatsGui extends HyperiumGui {

    private HypixelApiPlayer player;
    private AbstractHypixelStats hovered;
    private AbstractHypixelStats focused;

    private List<AbstractHypixelStats> fields = new ArrayList<>();
    private Map<AbstractHypixelStats, BufferedImage> texturesImage = new ConcurrentHashMap<>();
    private static Map<AbstractHypixelStats, DynamicTexture> logos = new HashMap<>();
    private ConcurrentHashMap<AbstractHypixelStats, GuiBlock> location = new ConcurrentHashMap<>();

    //TODO make only generate once
    public PlayerStatsGui(HypixelApiPlayer player) {
        this.player = player;
        fields.add(new GeneralStats());
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
                        System.out.println(field.getClass().getName());
                        e.printStackTrace();
                    }
            });

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
        reg("VIEW_ON_BEST_WEBSITE", new GuiButton(nextId(), 1, 1, "View on Sk1er.club"), button -> {
            Desktop desktop = Desktop.getDesktop();
            if (desktop != null) {
                try {
                    desktop.browse(new URL("https://sk1er.club/stats/" + player.getName()).toURI());
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }, button -> {

        });

        reg("VIEW_GUILD", new GuiButton(nextId(), 1, 22, "View Guild"), button -> {
            new GuildStatsGui(player.getGuild()).show();
        }, button -> {
            button.visible = player.getGuild().isLoaded() && player.getGuild().isValid();
        });
        reg("VIEW_FRIENDS", new GuiButton(nextId(), 1, 22 + 21, "View Friends"), button -> {
//TODO
//   new FriendsGui(player.getFriends()).show();
        }, button -> {
        });
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
        HypixelApiGuild guild = player.getGuild();
        if (guild == null) {
            GeneralChatHandler.instance().sendMessage("Player not found!");
            mc.displayGuiScreen(null);
            return;
        }
        boolean isInGuild = guild.isLoaded() && guild.isValid();
        drawScaledText(player.getDisplayString() + (isInGuild ? " " + guild.getFormatedTag() : ""), current.getScaledWidth() / 2, 30, 3, Color.WHITE.getRGB(), true, true);
        if (focused == null) {
            final int blockWidth = 64 + 32;
            int blocksPerLine = (int) (current.getScaledWidth() / (1.2D * blockWidth));
            if (blocksPerLine % 2 == 1) {
                blocksPerLine--;
            }
            final int startX = ResolutionUtil.current().getScaledWidth() / 2 - (blocksPerLine * blockWidth / 2);
            int x = -1;
            int y = 0;
            hovered = null;
            for (AbstractHypixelStats field : fields) {
                DynamicTexture dynamicTexture = logos.get(field);
                x++;
                if (x > blocksPerLine) {
                    x = 0;
                    y++;
                }
                if (dynamicTexture != null) {
                    //Render Image


                    int y1 = 100 + y * blockWidth - 10 - offset;
                    if (y1 < 70)
                        continue;
                    GlStateManager.pushMatrix();
                    GlStateManager.resetColor();
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    int y2 = 100 + y * blockWidth - offset;
                    int x1 = startX + x * blockWidth;
                    GuiBlock value = new GuiBlock(x1, x1 + blockWidth / 2, y2, y2 + blockWidth / 2);
                    if (value.isMouseOver(mouseX, mouseY)) {
                        hovered = field;
                    }
                    location.put(field, value);
                    GlStateManager.translate(x1, y2, 0);
                    GlStateManager.bindTexture(dynamicTexture.getGlTextureId());
                    GlStateManager.scale(.2, .2, .2);
                    drawTexturedModalRect(0, 0, 0, 0, 128 * 2, 128 * 2);
                    GlStateManager.popMatrix();

                    GlStateManager.pushMatrix();
                    GlStateManager.translate(startX + x * blockWidth + 24, y1, 0);
                    drawScaledText(field.getName(), 0, 0, 1.0, Color.RED.getRGB(), true, true);
                    GlStateManager.popMatrix();
                }
            }
            if (hovered != null) {
                List<StatsDisplayItem> preview = hovered.getPreview(player);
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
                float scale = 2.0F;
                GlStateManager.scale(scale, scale, scale);
                int left = block.getRight() - xOffset + yRenderOffset;
                int top = block.getTop();
                int printY = 0;
                if (top + height * 2 > current.getScaledHeight()) {
                    top = current.getScaledHeight() - height * 2 - 50;
                }
                RenderUtils.drawRect((left - 3) / scale, (top - 3) / scale, (left + (width + 3) * scale) / scale, (top + (height + 3) * scale) / scale,
                    new Color(0, 0, 0, 175).getRGB());

                for (StatsDisplayItem statsDisplayItem : preview) {
                    statsDisplayItem.draw((int) (left / scale), (int) ((top) / scale) + printY);
                    printY += statsDisplayItem.height;
                }
                GlStateManager.scale(1 / scale, 1 / scale, 1 / scale);
            }
        } else {
            List<StatsDisplayItem> deepStats = focused.getDeepStats(player);

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

            PlayerQuestsGui.print(current, deepStats, printY);
        }

    }


}
