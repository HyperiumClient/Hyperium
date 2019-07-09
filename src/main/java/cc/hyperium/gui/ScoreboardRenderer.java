package cc.hyperium.gui;

import cc.hyperium.config.ConfigOpt;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.RenderScoreboardEvent;
import cc.hyperium.utils.RenderUtils;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sk1er
 */
public class ScoreboardRenderer {

    @ConfigOpt
    private double xLocation = 1.0D;
    @ConfigOpt

    private double yLocation = .5D;


    public void render(ScoreObjective objective, ScaledResolution resolution) {
        RenderScoreboardEvent renderEvent = new RenderScoreboardEvent(this.xLocation, this.yLocation, objective, resolution);
        EventBus.INSTANCE.post(renderEvent);
        if (!renderEvent.isCancelled()) {
            Scoreboard scoreboard = objective.getScoreboard();
            Collection<Score> collection = scoreboard.getSortedScores(objective);
            List<Score> list = Lists.newArrayList(collection.stream().filter(p_apply_1_ -> p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#")).collect(Collectors.toList()));


            if (list.size() > 15) {
                collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
            } else {
                collection = list;
            }

            int i = this.getFontRenderer().getStringWidth(objective.getDisplayName());

            for (Score score : collection) {
                ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
                String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
                i = Math.max(i, this.getFontRenderer().getStringWidth(s));
            }

            int i1 = collection.size() * this.getFontRenderer().FONT_HEIGHT;
            int j1 = (int) (resolution.getScaledHeight_double() * getyLocation()) + i1 / 3;
            int k1 = 3;
            int l1 = (int) (resolution.getScaledWidth_double() * getxLocation()) - i - k1;
            int j = 0;

            for (Score score1 : collection) {
                ++j;
                ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
                String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
                String s2 = EnumChatFormatting.RED.toString() + score1.getScorePoints();
                int k = j1 - j * this.getFontRenderer().FONT_HEIGHT;
                int l = (int) (resolution.getScaledWidth_double() * getxLocation()) - k1 + 2;
                RenderUtils.drawRect(l1 - 2, k, l, k + this.getFontRenderer().FONT_HEIGHT, 1342177280);
                this.getFontRenderer().drawString(s1, l1, k, 553648127);
                this.getFontRenderer().drawString(s2, l - this.getFontRenderer().getStringWidth(s2), k, 553648127);


                if (j == collection.size()) {
                    String s3 = objective.getDisplayName();
                    RenderUtils.drawRect(l1 - 2, k - this.getFontRenderer().FONT_HEIGHT - 1, l, k - 1, 1610612736);
                    RenderUtils.drawRect(l1 - 2, k - 1, l, k, 1342177280);
                    this.getFontRenderer().drawString(s3, l1 + i / 2 - this.getFontRenderer().getStringWidth(s3) / 2, k - this.getFontRenderer().FONT_HEIGHT, 553648127);
                }
            }
        }
    }

    public double getxLocation() {
        return xLocation;
    }

    public void setxLocation(double xLocation) {
        this.xLocation = xLocation;
    }

    public double getyLocation() {
        return yLocation;
    }

    public void setyLocation(double yLocation) {
        this.yLocation = yLocation;
    }

    private FontRenderer getFontRenderer() {
        return Minecraft.getMinecraft().fontRendererObj;
    }

}
