package me.semx11.autotip.core;

import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.stats.StatsDaily;
import me.semx11.autotip.stats.StatsRange;
import me.semx11.autotip.util.FileUtil;
import org.apache.commons.io.FileUtils;

public class StatsManager {

    private final Autotip autotip;
    private final FileUtil fileUtil;
    private final Map<LocalDate, StatsDaily> cache = new ConcurrentHashMap<>();

    private LocalDate lastDate;
    private AtomicInteger ticks;

    public StatsManager(Autotip autotip) {
        this.autotip = autotip;
        this.fileUtil = autotip.getFileUtil();
        this.lastDate = LocalDate.now();
        this.ticks = new AtomicInteger(-1);
    }

    /**
     * Get the {@link StatsDaily} for today, based on {@link LocalDate#now()}. Same as {@link
     * #getToday(boolean)}, except that readOnly defaults to false.
     *
     * @return {@link StatsDaily} of today
     */
    public synchronized StatsDaily getToday() {
        return this.getToday(false);
    }

    /**
     * Get the {@link StatsDaily} for today, based on {@link LocalDate#now()}.
     *
     * @param readOnly Set to true to prevent the auto-save
     * @return {@link StatsDaily} of today
     */
    private synchronized StatsDaily getToday(boolean readOnly) {
        LocalDate now = LocalDate.now();
        if (!lastDate.isEqual(now)) {
            this.save(this.get(lastDate));
            lastDate = now;
        }
        if (!readOnly) {
            // Save after 7 seconds (20 ticks/sec) of no access
            ticks.set(7 * 20);
        }
        return this.get(lastDate);
    }

    /**
     * Get the {@link StatsDaily} for the current date without triggering the auto-save. This method
     * is similar to using {@link #get(LocalDate)} with the {@link LocalDate} being today.
     *
     * @return {@link StatsDaily} of today
     * @see #get(LocalDate)
     */
    public StatsDaily get() {
        return this.getToday(true);
    }

    /**
     * Get the {@link StatsDaily} for the specified date. This method uses a cache to reduce the
     * amount of read/write cycles.
     *
     * @param date The {@link LocalDate} of the StatsDaily you want to get
     * @return {@link StatsDaily} for the specified date
     */
    public StatsDaily get(LocalDate date) {
        if (cache.containsKey(date)) {
            return cache.get(date);
        }
        StatsDaily stats = this.load(new StatsDaily(autotip, date));
        cache.put(date, stats);
        return stats;
    }

    /**
     * Get the {@link StatsRange} for the specified date range. This method uses {@link
     * #get(LocalDate)} to get all the {@link StatsDaily} that are contained within this range.
     *
     * @param start The starting {@link LocalDate}
     * @param end The ending {@link LocalDate}
     * @return {@link StatsRange} for the specified date range
     */
    public StatsRange getRange(LocalDate start, LocalDate end) {
        if (start.isBefore(fileUtil.getFirstDate())) {
            start = fileUtil.getFirstDate();
        }
        if (end.isAfter(LocalDate.now())) {
            end = LocalDate.now();
        }
        StatsRange range = new StatsRange(autotip, start, end);
        Stream.iterate(start, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end) + 1)
                .forEach(date -> {
                    range.merge(this.get(date));
                });
        return range;
    }

    /**
     * Get the {@link StatsRange} for all the {@link StatsDaily} files in the current user
     * directory. This method uses {@link FileUtil} to get the starting date, and then calls {@link
     * #getRange(LocalDate, LocalDate) to get the specified StatsRange.
     *
     * @return {@link StatsRange} for the lifetime statistics from the current user directory
     */
    public StatsRange getAll() {
        return this.getRange(fileUtil.getFirstDate(), LocalDate.now());
    }

    /**
     * Save a {@link StatsDaily} to the current user directory.
     *
     * @param stats The {@link StatsDaily} that you want to save
     */
    public void save(StatsDaily stats) {
        cache.put(stats.getDate(), stats);
        File file = stats.getFile();
        try {
            String json = autotip.getGson().toJson(stats);
            FileUtils.writeStringToFile(file, json, StandardCharsets.UTF_8);
            Autotip.LOGGER.info("Saved " + stats.getFile().getName());
        } catch (IOException e) {
            Autotip.LOGGER.error("Could not write to " + file, e);
        }
    }

    /**
     * Load a {@link StatsDaily} from the current user directory.
     *
     * @param stats The {@link StatsDaily} that you want to load
     * @return {@link StatsDaily} that contains the loaded stats, unchanged if there were errors
     */
    private StatsDaily load(StatsDaily stats) {
        File file = stats.getFile();
        try {
            String json = FileUtils.readFileToString(file);
            return stats.merge(autotip.getGson().fromJson(json, StatsDaily.class));
        } catch (FileNotFoundException e) {
            // Skip
            return stats;
        } catch (JsonSyntaxException | IllegalArgumentException e) {
            Autotip.LOGGER.warn(file.getName() + " has invalid contents, resetting...");
        } catch (IOException e) {
            Autotip.LOGGER.error("Could not read " + file.getName() + "!", e);
        }
        this.save(stats);
        return stats;
    }

    /**
     * Method that is called each game-tick to trigger an auto-save for today's stats file.
     */
    public void saveCycle() {
        if (ticks.get() > 0) {
            ticks.decrementAndGet();
            return;
        }
        if (ticks.get() == 0) {
            this.save(this.get(lastDate));
            ticks.decrementAndGet();
        }
    }

}
