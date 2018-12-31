package me.semx11.autotip.core;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.config.Config;
import me.semx11.autotip.stats.StatsDaily;
import me.semx11.autotip.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class MigrationManager {

    private static final DateTimeFormatter OLD_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final Autotip autotip;
    private final FileUtil fileUtil;
    private final Config config;

    private final File upgradeDateFile;
    private final LocalDate xpChangeDate;

    public MigrationManager(Autotip autotip) {
        this.autotip = autotip;
        this.fileUtil = autotip.getFileUtil();
        this.config = autotip.getConfig();
        this.upgradeDateFile = fileUtil.getFile("upgrade-date.at");
        this.xpChangeDate = autotip.getGlobalSettings().getXpChangeDate();
    }

    public void migrateLegacyFiles() {
        if (fileUtil.exists("options.at")) {
            config.migrate();
        }
        if (fileUtil.exists("tipped.at")) {
            fileUtil.delete("tipped.at");
        }
        this.migrateStats();
        if (fileUtil.exists("upgrade-date.at")) {
            fileUtil.delete("upgrade-date.at");
        }
    }

    private void migrateStats() {
        try {
            Files.walk(fileUtil.getStatsDir(), 1)
                    .filter(path -> !path.toFile().isDirectory())
                    .map(path -> FilenameUtils.removeExtension(path.getFileName().toString()))
                    .map(filename -> {
                        try {
                            return Optional.of(LocalDate.parse(filename, OLD_FORMAT));
                        } catch (DateTimeParseException e) {
                            return Optional.empty();
                        }
                    })
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(date -> new StatsDaily(autotip, (LocalDate) date).migrate());
        } catch (IOException e) {
            Autotip.LOGGER.error("Could not migrate stats files", e);
        }
    }

    public LegacyState getLegacyState(LocalDate date) {
        if (date.isBefore(xpChangeDate)) {
            return LegacyState.BEFORE;
        } else if (date.isBefore(this.getUpgradeDate())) {
            return LegacyState.BACKTRACK;
        } else {
            return LegacyState.AFTER;
        }
    }

    private LocalDate getUpgradeDate() {
        if (!upgradeDateFile.exists()) {
            return xpChangeDate;
        }
        try {
            String date = FileUtils.readFileToString(upgradeDateFile, StandardCharsets.UTF_8);
            return LocalDate.parse(date, OLD_FORMAT);
        } catch (IOException | DateTimeParseException | NullPointerException e) {
            return xpChangeDate;
        }
    }

    public enum LegacyState {
        BEFORE, BACKTRACK, AFTER
    }

}
