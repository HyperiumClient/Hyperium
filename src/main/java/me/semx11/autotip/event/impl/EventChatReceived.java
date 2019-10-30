package me.semx11.autotip.event.impl;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.network.chat.ServerChatEvent;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.chat.MessageOption;
import me.semx11.autotip.command.impl.CommandLimbo;
import me.semx11.autotip.config.Config;
import me.semx11.autotip.config.GlobalSettings;
import me.semx11.autotip.event.Event;
import me.semx11.autotip.message.Message;
import me.semx11.autotip.message.MessageMatcher;
import me.semx11.autotip.message.StatsMessageMatcher;
import me.semx11.autotip.stats.StatsDaily;
import me.semx11.autotip.universal.UniversalUtil;

public class EventChatReceived implements Event {

    private final Autotip autotip;

    public EventChatReceived(Autotip autotip) {
        this.autotip = autotip;
    }

    @InvokeEvent
    public void onChat(ServerChatEvent event) {
        Config config = autotip.getConfig();

        if (!autotip.getSessionManager().isOnHypixel()) {
            return;
        }

        String msg = UniversalUtil.getUnformattedText(event);

        CommandLimbo limboCommand = autotip.getCommand(CommandLimbo.class);
        if (limboCommand.hasExecuted()) {
            if (msg.startsWith("A kick occurred in your connection")) {
                event.setCancelled(true);
            } else if (msg.startsWith("Illegal characters in chat")) {
                event.setCancelled(true);
                limboCommand.setExecuted(false);
            }
        }

        if (!config.isEnabled()) return;

        GlobalSettings settings = autotip.getGlobalSettings();
        MessageOption option = config.getMessageOption();

        for (Message message : settings.getMessages()) {
            MessageMatcher matcher = message.getMatcherFor(msg);
            if (matcher.matches()) {
                event.setCancelled(message.shouldHide(option));
                return;
            }
        }

        String hover = UniversalUtil.getHoverText(event);
        settings.getStatsMessages().forEach(message -> {
            StatsMessageMatcher matcher = message.getMatcherFor(msg);
            if (!matcher.matches()) return;
            StatsDaily stats = getStats();
            matcher.applyStats(stats);
            message.applyHoverStats(hover, stats);
            event.setCancelled(message.shouldHide(option));
        });
    }

    private StatsDaily getStats() {
        return autotip.getStatsManager().getToday();
    }

}
