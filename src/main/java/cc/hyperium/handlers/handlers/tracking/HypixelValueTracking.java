package cc.hyperium.handlers.handlers.tracking;

import cc.hyperium.Hyperium;
import cc.hyperium.event.HypixelGetCoinsEvent;
import cc.hyperium.event.HypixelGetXPEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RankedRatingChangeEvent;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.JsonHolder;
import com.google.gson.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class HypixelValueTracking implements JsonDeserializer<ValueTrackingItem>, JsonSerializer<ValueTrackingItem> {

    private final Gson GSON = new GsonBuilder().registerTypeAdapter(ValueTrackingItem.class, this).create();
    private List<ValueTrackingItem> currentCache = new ArrayList<>();

    public HypixelValueTracking() {
        Multithreading.schedule(() -> {
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
                data.add(GSON.toJson(value));
            }
            saveFile(current, data.toString());
        }, 1, 1, TimeUnit.MINUTES);
    }

    public List<ValueTrackingItem> getItemsBetween(long first, long second) {
        List<ValueTrackingItem> items = new ArrayList<>();
        long current = first;
        while (current <= second) {
            List<ValueTrackingItem> allItemsOnDay = getAllItemsOnDay(current);
            items.addAll(allItemsOnDay.stream().filter(valueTrackingItem -> valueTrackingItem.getTime() > first && valueTrackingItem.getTime() < second).collect(Collectors.toList()));
            current += TimeUnit.DAYS.toMillis(1);
        }
        return items;
    }


    private List<ValueTrackingItem> getAllItemsOnDay(long day) {
        JsonHolder jsonHolder = readFile(getFileOnDay(day));
        ArrayList<ValueTrackingItem> valueTrackingItems = new ArrayList<>();
        for (JsonElement data : jsonHolder.optJSONArray("data")) {
            valueTrackingItems.add(GSON.fromJson(data, ValueTrackingItem.class));
        }
        return valueTrackingItems;

    }

    private File getFileOnDay(long day) {
        Date time = new Date(day);
        File year_file = new File(Hyperium.folder, Integer.toString(time.getYear()));
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
        currentCache.add(new ValueTrackingItem(t, value, System.currentTimeMillis()));
    }


    @Override
    public ValueTrackingItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonHolder jsonHolder = new JsonHolder(json.getAsJsonObject());
        return new ValueTrackingItem(ValueTrackingType.parse(jsonHolder.optString("type")), jsonHolder.optInt("amount"), jsonHolder.optInt("time"));
    }

    @Override
    public JsonElement serialize(ValueTrackingItem src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonHolder().put("type", src.getType().name()).put("amount", src.getValue()).put("time", src.getTime()).getObject();
    }
}
