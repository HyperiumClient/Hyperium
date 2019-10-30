package me.semx11.autotip.command.impl;

import me.semx11.autotip.Autotip;
import me.semx11.autotip.chat.MessageOption;
import me.semx11.autotip.chat.MessageUtil;
import me.semx11.autotip.command.CommandAbstract;
import me.semx11.autotip.config.Config;
import me.semx11.autotip.config.GlobalSettings;
import me.semx11.autotip.core.SessionManager;
import me.semx11.autotip.core.StatsManager;
import me.semx11.autotip.core.TaskManager;
import me.semx11.autotip.core.TaskManager.TaskType;
import me.semx11.autotip.event.impl.EventClientConnection;
import me.semx11.autotip.stats.StatsDaily;
import me.semx11.autotip.universal.UniversalUtil;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static net.minecraft.command.CommandBase.getListOfStringsMatchingLastWord;

public class CommandAutotip extends CommandAbstract {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("d/M/yyyy");
    private static final DateTimeFormatter WAVE_FORMAT = DateTimeFormatter.ofPattern("mm:ss");

    public CommandAutotip(Autotip autotip) {
        super(autotip);
    }


    @Override
    public String getName() {
        return "autotip";
    }

    @Override
    public String getUsage() {
        return autotip.getLocaleHolder().getKey("command.usage");
    }


    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("at");
    }

    @Override
    public void onExecute(String[] args) {
        Config config = autotip.getConfig();
        MessageUtil messageUtil = autotip.getMessageUtil();
        TaskManager taskManager = autotip.getTaskManager();
        SessionManager manager = autotip.getSessionManager();
        StatsManager stats = autotip.getStatsManager();
        GlobalSettings settings = autotip.getGlobalSettings();

        if (args.length <= 0) {
            messageUtil.sendKey("command.usage");
            return;
        }

        switch (args[0].toLowerCase()) {
            case "s":
            case "stats":
                LocalDate now = LocalDate.now();

                if (args.length <= 1) {
                    stats.get(now).print();
                    return;
                }

                String param = args[1].toLowerCase();
                switch (param) {
                    case "d":
                    case "day":
                    case "daily":
                    case "today":
                        stats.get(now).print();
                        break;
                    case "yd":
                    case "yesterday":
                        stats.get(now.minusDays(1)).print();
                        break;
                    case "w":
                    case "week":
                    case "weekly":
                        stats.getRange(now.with(DayOfWeek.MONDAY), now.with(DayOfWeek.SUNDAY))
                            .print();
                        break;
                    case "m":
                    case "month":
                    case "monthly":
                        stats.getRange(now.withDayOfMonth(1),
                            now.withDayOfMonth(now.lengthOfMonth())).print();
                        break;
                    case "y":
                    case "year":
                    case "yearly":
                        stats.getRange(now.withDayOfYear(1),
                            now.withDayOfYear(now.lengthOfYear())).print();
                        break;
                    case "a":
                    case "all":
                    case "total":
                    case "life":
                    case "lifetime":
                        stats.getAll().print();
                        break;
                    default:
                        if (param.contains("-")) {
                            List<LocalDate> dates = Arrays.stream(param.split("-"))
                                .map(string -> {
                                    try {
                                        return LocalDate.parse(string, DATE_FORMAT);
                                    } catch (DateTimeParseException e) {
                                        return null;
                                    }
                                })
                                .filter(Objects::nonNull)
                                .limit(2)
                                .sorted()
                                .collect(Collectors.toList());
                            if (dates.size() != 2) {
                                messageUtil.sendKey("command.stats.invalidRange");
                                return;
                            }
                            stats.getRange(dates.get(0), dates.get(1)).print();
                        } else if (param.contains("/")) {
                            try {
                                LocalDate date = LocalDate.parse(param, DATE_FORMAT);
                                stats.get(date).print();
                            } catch (DateTimeParseException e) {
                                messageUtil.sendKey("command.stats.invalidDate");
                            }
                        } else {
                            messageUtil.sendKey("command.stats.usage");
                        }
                        break;

                }
                break;
            case "?":
            case "info":
                StatsDaily today = stats.get();
                messageUtil.getKeyHelper("command.info")
                    .separator()
                    .sendKey("version", autotip.getVersion())
                    .withKey("credits", context -> context.getBuilder()
                        .setHover(context.getKey("creditsHover"))
                        .send())
                    .sendKey("status." + (config.isEnabled() ? "enabled" : "disabled"))
                    .sendKey("messages", config.getMessageOption())
                    .sendKey("tipsSent", today.getTipsSent())
                    .sendKey("tipsReceived", today.getTipsReceived())
                    .sendKey("statsCommand")
                    .separator();
                break;
            case "m":
            case "messages":
                try {
                    if (args.length > 1) {
                        MessageOption option = MessageOption.valueOfIgnoreCase(args[1]);
                        config.setMessageOption(option).save();
                    } else {
                        config.nextMessageOption().save();
                    }
                    messageUtil.sendKey("command.messages.next", config.getMessageOption());
                } catch (IllegalArgumentException e) {
                    messageUtil.sendKey("command.messages.error", args.length > 1 ? args[1] : null);
                }
                break;
            case "t":
            case "toggle":
                if (!manager.isOnHypixel()) {
                    config.toggleEnabled().save();
                    messageUtil.getKeyHelper("command.toggle")
                        .sendKey(config.isEnabled() ? "enabled" : "disabled");
                    return;
                }
                if (!config.isEnabled()) {
                    if (!manager.isLoggedIn()) {
                        taskManager.executeTask(TaskType.LOGIN, manager::login);
                        config.setEnabled(true).save();
                        messageUtil.sendKey("command.toggle.enabled");
                    } else {
                        messageUtil.sendKey("command.toggle.error");
                    }
                } else {
                    if (manager.isLoggedIn()) {
                        taskManager.executeTask(TaskType.LOGOUT, manager::logout);
                        config.setEnabled(false).save();
                        messageUtil.sendKey("command.toggle.disabled");
                    } else {
                        messageUtil.sendKey("command.toggle.error");
                    }
                }
                break;
            case "w":
            case "wave":
                if (!config.isEnabled()) {
                    messageUtil.sendKey("error.disabled");
                    return;
                }
                if (!manager.isOnHypixel()) {
                    messageUtil.sendKey("error.disabledHypixel");
                    return;
                }
                if (manager.getNextTipWave() == 0) {
                    messageUtil.sendKey("command.wave.error");
                    return;
                }

                long t = System.currentTimeMillis();
                String next = LocalTime.MIN.plusSeconds((manager.getNextTipWave() - t) / 1000 + 1)
                    .format(WAVE_FORMAT);
                String last = LocalTime.MIN.plusSeconds((t - manager.getLastTipWave()) / 1000)
                    .format(WAVE_FORMAT);

                messageUtil.getKeyHelper("command.wave")
                    .separator()
                    .sendKey("nextWave", next)
                    .sendKey("lastWave", last)
                    .separator();
                break;
            case "changelog":
                messageUtil.getKeyHelper("command.changelog")
                    .separator()
                    .sendKey("version", autotip.getVersion())
                    .withKey("entry", context -> settings.getVersionInfo(autotip.getVersion())
                        .getChangelog()
                        .forEach(context::send))
                    .separator();
                break;
            case "debug":
                EventClientConnection event = autotip.getEvent(EventClientConnection.class);
                Object header = event.getHeader();
                messageUtil.getKeyHelper("command.debug")
                    .separator()
                    .sendKey("serverIp", event.getServerIp())
                    .sendKey("mcVersion", autotip.getMcVersion())
                    .sendKey("header." + (header == null ? "none" : "present"),
                        UniversalUtil.getUnformattedText(header))
                    .separator();
                break;
            case "reload":
                try {
                    autotip.reloadGlobalSettings();
                    autotip.reloadLocale();
                    messageUtil.sendKey("command.reload.success");
                } catch (IllegalStateException e) {
                    messageUtil.sendKey("command.reload.error");
                }
                break;
            default:
                messageUtil.send(getUsage());
                break;
        }
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        switch (args.length) {
            case 1:
                return getListOfStringsMatchingLastWord(args, "stats", "info", "messages", "toggle",
                    "wave", "changelog");
            case 2:
                switch (args[0].toLowerCase()) {
                    case "s":
                    case "stats":
                        return getListOfStringsMatchingLastWord(args, "day", "yesterday", "week",
                            "month", "year", "lifetime");
                }
            default:
                return Collections.emptyList();
        }
    }
}
