package me.semx11.autotip.util;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import me.semx11.autotip.Autotip;
import org.apache.commons.io.FilenameUtils;

public class FileUtil {

    private static final DateTimeFormatter OLD_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final Path userDir;
    private final Path statsDir;

    private LocalDate firstDate;

    public FileUtil(Autotip autotip) {
        this.userDir = this.getRawPath("mods/autotip/" + autotip.getGameProfile().getId());
        this.statsDir = this.getPath("stats");
    }

    public void createDirectories() throws IOException {
        if (!Files.exists(statsDir)) {
            Files.createDirectories(statsDir);
        }
    }

    public Path getUserDir() {
        return userDir;
    }

    public Path getStatsDir() {
        return statsDir;
    }

    public boolean exists(String path) {
        return Files.exists(this.getPath(path));
    }

    public void delete(String path) {
        this.delete(this.getPath(path));
    }

    public void delete(File file) {
        this.delete(file.toPath());
    }

    public void delete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            Autotip.LOGGER.error("Could not delete file " + path);
        }
    }

    public File getLegacyStatsFile(LocalDate localDate) {
        return this.getFile(this.statsDir, localDate.format(OLD_FORMAT) + ".at");
    }

    public File getStatsFile(LocalDate localDate) {
        return this.getFile(this.statsDir, localDate.format(ISO_LOCAL_DATE) + ".at");
    }

    public LocalDate getFirstDate() {
        if (firstDate != null) {
            return firstDate;
        }
        try {
            return firstDate = Files.list(this.getStatsDir())
                    .map(this::getDateFromPath)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElseGet(LocalDate::now);
        } catch (IOException e) {
            Autotip.LOGGER.error("Could not list files in stats dir.");
            return LocalDate.now();
        }
    }

    private LocalDate getDateFromPath(Path path) {
        String name = FilenameUtils.getBaseName(path.getFileName().toString());
        try {
            return LocalDate.parse(name);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public File getFile(String path) {
        return this.getPath(path).toFile();
    }

    public Path getPath(String path) {
        return this.getPath(this.userDir, path);
    }

    private File getFile(Path directory, String path) {
        return this.getPath(directory, path).toFile();
    }

    private Path getPath(Path directory, String path) {
        return directory.resolve(this.separator(path));
    }

    private Path getRawPath(String path) {
        return Paths.get(this.separator(path));
    }

    private String separator(String s) {
        return s.replaceAll("///", File.separator);
    }

}