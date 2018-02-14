package com.hcc.handlers.handlers.chat;

import com.hcc.utils.SafeNumberParsing;
import net.minecraft.util.IChatComponent;

import java.util.regex.Matcher;

/**
 * Created by mitchellkatz on 2/14/18. Designed for production use on Sk1er.club
 */
public class RankedRatingChatHandler extends HCCChatHandler {

    @Override
    public boolean chatReceived(IChatComponent component, String text) {
        Matcher matcher = skywarsRankedRating.matcher(text);
        if (matcher.find()) {
            getHcc().getHandlers().getValueHandler().setRankedRating(SafeNumberParsing.safeParseInt(matcher.group("rating"), getHcc().getHandlers().getValueHandler().getRankedRating()));
            getHcc().getHandlers().getValueHandler().setDeltaRankedRating(SafeNumberParsing.safeParseInt(matcher.group("change"), getHcc().getHandlers().getValueHandler().getDeltaRankedRating()));
        }
        return false;
    }
}
