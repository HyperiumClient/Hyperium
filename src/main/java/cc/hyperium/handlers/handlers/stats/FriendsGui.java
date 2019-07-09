package cc.hyperium.handlers.handlers.stats;

import cc.hyperium.gui.HyperiumGui;
import club.sk1er.website.api.requests.HypixelApiPlayer;

public class FriendsGui extends HyperiumGui {
    private HypixelApiPlayer friends;

    public FriendsGui(HypixelApiPlayer guild) {
        this.friends = guild;
    }

    @Override
    protected void pack() {

    }
}
