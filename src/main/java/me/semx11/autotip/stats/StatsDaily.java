package me.semx11.autotip.stats;

import me.semx11.autotip.Autotip;
import me.semx11.autotip.core.MigrationManager.LegacyState;
import me.semx11.autotip.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StatsDaily extends Stats {

    private static final Pattern TIPS_PATTERN = Pattern
        .compile("^(?<sent>\\d+)(:(?<received>\\d+))?$");
    private static final Pattern GAME_PATTERN = Pattern
        .compile("^(?<game>[\\w\\s]+):(?<sent>\\d+)(:(?<received>\\d+))?$");

    protected LocalDate date;

    public StatsDaily(Autotip autotip) {
        super(autotip);
    }

    public StatsDaily(Autotip autotip, LocalDate date) {
        super(autotip);
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDateString() {
        return DATE_FORMATTER.format(date);
    }

    public File getFile() {
        return autotip.getFileUtil().getStatsFile(date);
    }

    @Override
    public StatsDaily merge(final Stats that) {
        assert that instanceof StatsDaily : "Cannot merge with StatsDaily";
        StatsDaily stats = (StatsDaily) that;
        assert date.isEqual(stats.date) : "Dates do not match!";
        super.merge(that);
        return this;
    }

    /**
     * Wrapped version of {@link me.semx11.autotip.core.StatsManager#save(StatsDaily)}.
     *
     * @see me.semx11.autotip.core.StatsManager#save(StatsDaily)
     */
    public void save() {
        autotip.getStatsManager().save(this);
    }

    public void migrate() {
        // Check if legacy stats file exists
        FileUtil fileUtil = autotip.getFileUtil();
        File file = fileUtil.getLegacyStatsFile(date);
        if (!file.exists()) return;

        LegacyState state = autotip.getMigrationManager().getLegacyState(date);

        try {
            // Reads the contents of the file. If the file has less than 2 lines, ignore file.
            List<String> lines = Files.readAllLines(file.toPath());
            if (lines.size() < 2) {
                fileUtil.delete(file);
                return;
            }

            // Parses the first line of the file to tips sent and received (e.g. "124:119").
            Matcher tipMatcher = TIPS_PATTERN.matcher(lines.get(0));
            if (tipMatcher.matches()) {
                tipsSent = Integer.parseInt(tipMatcher.group("sent"));
                if (tipMatcher.group("received") != null) tipsReceived = Integer.parseInt(tipMatcher.group("received"));
            }

            // This is to fix the wrong tips count in the period between the XP change, and the Autotip fix.
            if (state == LegacyState.BACKTRACK) tipsReceived /= 2;

            // Every tip you send is worth 50 XP.
            xpSent = tipsSent * 50;
            // This is to account for tips received before the XP change, as they gave you 30 XP, not 60 XP.
            xpReceived = (state == LegacyState.BEFORE ? 30 : 60) * tipsReceived;

            // Parses each line with game-data (e.g. "Arcade:2900:2400") to a Map.
            gameStatistics = lines.stream()
                .skip(2)
                .filter(s -> GAME_PATTERN.matcher(s).matches())
                .collect(Collectors.toMap(
                    s -> s.split(":")[0],
                    s -> {
                        String[] split = s.split(":");
                        int sent = Integer.parseInt(split[1]);
                        int received = split.length > 2 ? Integer.parseInt(split[2]) : 0;
                        return new Coins(sent, received);
                    }));

            // Remove grouped stats
            settings.getGameGroups().stream().filter(group -> gameStatistics.containsKey(group.getName())).forEach(group -> {
                Coins coins = gameStatistics.get(group.getName());
                group.getGames().forEach(game -> addCoins(game, coins));
            });

            // Convert aliases
            settings.getGameAliases().forEach(alias -> alias.getAliases().stream()
                .filter(aliasAlias -> gameStatistics.containsKey(aliasAlias))
                .map(aliasAlias -> gameStatistics.get(aliasAlias))
                .forEach(coins -> alias.getGames().forEach(aliasGame -> addCoins(aliasGame, coins))));

            // Deletes old file to complete migration.
            fileUtil.delete(file);

            Autotip.LOGGER.info("Migrated legacy stats file " + file.getName());
            save();
        } catch (IOException e) {
            Autotip.LOGGER.error("Could not read file " + file.getName(), e);
            save();
        }
    }

}
