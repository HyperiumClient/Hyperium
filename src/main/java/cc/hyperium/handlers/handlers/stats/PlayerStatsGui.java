package cc.hyperium.handlers.handlers.stats;

import cc.hyperium.gui.HyperiumGui;
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
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import club.sk1er.website.api.requests.HypixelApiPlayer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerStatsGui extends HyperiumGui {

    private HypixelApiPlayer player;
    private AbstractHypixelStats focused;
    private List<AbstractHypixelStats> fields = new ArrayList<>();
    private Map<AbstractHypixelStats, BufferedImage> texturesImage = new ConcurrentHashMap<>();
    private Map<AbstractHypixelStats, DynamicTexture> logos = new HashMap<>();

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
        boolean isInGuild = player.getGuild().isLoaded() && player.getGuild().isValid();
        drawScaledText(player.getDisplayString() + (isInGuild ? " " +player.getGuild().getFormatedTag() : ""), current.getScaledWidth() / 2, 30, 3, Color.WHITE.getRGB(), true, true);

        final int blockWidth = 64 + 32;
        int blocksPerLine = (int) (current.getScaledWidth() / (1.2D * blockWidth));
        if (blocksPerLine % 2 == 1) {
            blocksPerLine--;
        }
        final int startX = ResolutionUtil.current().getScaledWidth() / 2 - (blocksPerLine * blockWidth / 2);
        int x = -1;
        int y = 0;

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
                GlStateManager.translate(startX + x * blockWidth, 100 + y * blockWidth - offset, 0);
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
    }

}
