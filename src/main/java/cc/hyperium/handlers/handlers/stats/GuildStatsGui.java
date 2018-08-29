package cc.hyperium.handlers.handlers.stats;

import cc.hyperium.C;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import club.sk1er.website.api.requests.HypixelApiGuild;
import club.sk1er.website.api.requests.HypixelApiPlayer;
import club.sk1er.website.utils.WebsiteUtils;
import net.hypixel.api.GameType;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.Color;
import java.util.Date;
import java.util.List;

public class GuildStatsGui extends HyperiumGui {
    private HypixelApiGuild guild;

    public GuildStatsGui(HypixelApiGuild guild) {
        this.guild = guild;
    }

    @Override
    protected void pack() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution current = ResolutionUtil.current();
        drawScaledText(guild.getName() + " " + guild.getFormatedTag(), current.getScaledWidth() / 2, 30, 3, Color.WHITE.getRGB(), true, true);

        int collumnX = current.getScaledWidth() / 50;
        int offsetLeftColumn = offset + 50;
        fontRendererObj.drawString(boldText("Founded: ", HypixelApiPlayer.DMY.format(new Date(guild.getCreateDate()))), collumnX, 100 - offsetLeftColumn, Color.WHITE.getRGB(), true);
        fontRendererObj.drawString(boldText("Members: ", WebsiteUtils.comma(guild.getMembers().size())), collumnX, 110 - offsetLeftColumn, Color.WHITE.getRGB(), true);
        fontRendererObj.drawString(boldText("Guild Coins: ", WebsiteUtils.comma(guild.getTotalGuildCoins())), collumnX, 120 - offsetLeftColumn, Color.WHITE.getRGB(), true);
        fontRendererObj.drawString(boldText("Legacy Ranking: ", WebsiteUtils.comma(guild.getLegacyRanking())), collumnX, 130 - offsetLeftColumn, Color.WHITE.getRGB(), true);
        List<String> strings = fontRendererObj.listFormattedStringToWidth(boldText("Description: ", guild.getDescription()), current.getScaledWidth() / 4);
        int i = 0;
        for (String string : strings) {
            fontRendererObj.drawString(string, collumnX, 140 + i - offsetLeftColumn, Color.WHITE.getRGB(), true);
            i += 10;
        }
        fontRendererObj.drawString(boldText("Guild Level: ", Double.toString(guild.getLevel())), collumnX, 150 + i - offsetLeftColumn, Color.WHITE.getRGB(), true);
        fontRendererObj.drawString(boldText("Total Wins: ", Integer.toString(guild.getWins("total"))), collumnX, 170 + i - offsetLeftColumn, Color.WHITE.getRGB(), true);
        for (GameType gameType : GameType.values()) {
            if (gameType == GameType.UNKNOWN || gameType == GameType.LEGACY)
                continue;
            fontRendererObj.drawString(boldText(gameType.getName() + " Wins: ", Integer.toString(guild.getWins(gameType.name()))), collumnX, 180 + i - offsetLeftColumn, Color.WHITE.getRGB(), true);
            i += 10;
        }

        List<HypixelApiGuild.GuildPlayer> inOrder = guild.getInOrder();
        int memberX = 2 * current.getScaledWidth() / 6;
        int memberStart = 50 - offset;

        int memberWidth = current.getScaledWidth() - memberX - 40;
        fontRendererObj.drawString(C.BOLD + "Name", memberX, memberStart, Color.WHITE.getRGB(), true);
        fontRendererObj.drawString(C.BOLD + "Rank", memberX + memberWidth / 3, memberStart, Color.WHITE.getRGB(), true);
        fontRendererObj.drawString(C.BOLD + "Join date", memberX + memberWidth / 3 * 2, memberStart, Color.WHITE.getRGB(), true);
        memberStart += 10;
        for (HypixelApiGuild.GuildPlayer guildPlayer : inOrder) {
            String display = guildPlayer.getDisplay();
            fontRendererObj.drawString(display, memberX, memberStart, Color.WHITE.getRGB(), true);
            String rank = guildPlayer.getRank();
            if (rank.equals("GUILDMASTER"))
                rank = "Guild Master";
            if (rank.equals("OFFICER"))
                rank = "Officer";
            if (rank.equals("MEMBER"))
                rank = "Member";
            fontRendererObj.drawString(rank, memberX + memberWidth / 3, memberStart, Color.WHITE.getRGB(), true);
            fontRendererObj.drawString(HypixelApiPlayer.DMYHHMMSS.format(guildPlayer.getJoinLong()), memberX + memberWidth / 3 * 2, memberStart, Color.WHITE.getRGB(), true);

            memberStart += 10;

        }

    }

}
