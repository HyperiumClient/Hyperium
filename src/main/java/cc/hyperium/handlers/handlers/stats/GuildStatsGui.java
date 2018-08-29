package cc.hyperium.handlers.handlers.stats;

import cc.hyperium.gui.HyperiumGui;
import club.sk1er.website.api.requests.HypixelApiGuild;

public class GuildStatsGui extends HyperiumGui {
    private HypixelApiGuild guild;

    public GuildStatsGui(HypixelApiGuild guild) {
        this.guild = guild;
    }

    @Override
    protected void pack() {

    }
}
