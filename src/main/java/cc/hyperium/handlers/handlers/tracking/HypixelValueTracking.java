package cc.hyperium.handlers.handlers.tracking;

import cc.hyperium.Hyperium;
import cc.hyperium.event.HypixelGetCoinsEvent;
import cc.hyperium.event.HypixelGetXPEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RankedRatingChangeEvent;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.JsonHolder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class HypixelValueTracking {

    private final Gson GSON = new GsonBuilder().create();
    private List<ValueTrackingItem> currentCache = new ArrayList<>();

    public HypixelValueTracking() {
        Multithreading.schedule(() -> {
            if (currentCache.isEmpty())
                return;
            ArrayList<ValueTrackingItem> values = new ArrayList<>(currentCache);
            currentCache.clear();
            File current = getCurrent();
            JsonHolder jsonHolder = readFile(current);
            if (!jsonHolder.has("data")) {
                //idk for some reason the method in Json holder for this is private
                jsonHolder.getObject().add("data", new JsonArray());
            }
            JsonArray data = jsonHolder.optJSONArray("data");
            for (ValueTrackingItem value : values) {
                data.add(new JsonHolder(GSON.toJson(value)).getObject());
            }
            saveFile(current, jsonHolder.toString());
        }, 1, 1, TimeUnit.SECONDS);
    }

    public List<ValueTrackingItem> getItemsBetween(long first, long second) {
        List<ValueTrackingItem> items = new ArrayList<>();
        long current = first;
        while (current <= second) {
            List<ValueTrackingItem> allItemsOnDay = getAllItemsOnDay(current);
            items.addAll(allItemsOnDay.stream().filter(valueTrackingItem -> valueTrackingItem.getTime() > first && valueTrackingItem.getTime() < second).collect(Collectors.toList()));
            current += TimeUnit.DAYS.toMillis(1);
        }
        items.sort(Comparator.comparingLong(ValueTrackingItem::getTime));
        return items;
    }


    private List<ValueTrackingItem> getAllItemsOnDay(long day) {
        JsonHolder jsonHolder = readFile(getFileOnDay(day));
        ArrayList<ValueTrackingItem> valueTrackingItems = new ArrayList<>();
        for (JsonElement data : jsonHolder.optJSONArray("data")) {
            valueTrackingItems.add(GSON.fromJson(data.getAsJsonObject(), ValueTrackingItem.class));
        }
        return valueTrackingItems;

    }

    private File getFileOnDay(long day) {
        Date time = new Date(day);
        File year_file = new File(Hyperium.folder,/* "stats" + File.pathSeparator + UUIDUtil.getClientUUID() + File.pathSeparator + */Integer.toString(time.getYear()));
        if (!year_file.exists()) {
            year_file.mkdirs();
        }
        File month_file = new File(year_file, Integer.toString(time.getYear()));
        if (!month_file.exists()) {
            month_file.mkdirs();
        }

        return new File(month_file, Integer.toString(time.getDate()));
    }

    private void saveFile(File file, String data) {
        try {
            FileUtils.write(file, data, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonHolder readFile(File file) {
        if (!file.exists()) {
            return new JsonHolder();
        }
        try {
            return new JsonHolder(FileUtils.readFileToString(file, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonHolder();

    }

    private File getCurrent() {
        return getFileOnDay(System.currentTimeMillis());
    }

    @InvokeEvent
    public void ratingChangeEvent(RankedRatingChangeEvent event) {
        post(ValueTrackingType.RANKED_RATING, event.getRating());
    }

    @InvokeEvent
    public void getCoinsEvent(HypixelGetCoinsEvent event) {
        post(ValueTrackingType.COINS, event.getCoins());
    }

    @InvokeEvent
    public void getXPEvent(HypixelGetXPEvent event) {
        post(ValueTrackingType.EXPERIENCE, event.getXp());
    }

    public void post(ValueTrackingType t, int value) {
        post(t, value, System.currentTimeMillis());
    }

    public void post(ValueTrackingType item, int value, long time) {
        currentCache.add(new ValueTrackingItem(item, value, time));
    }


}
