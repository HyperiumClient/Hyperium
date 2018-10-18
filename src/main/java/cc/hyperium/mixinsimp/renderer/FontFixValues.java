package cc.hyperium.mixinsimp.renderer;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import net.minecraft.client.renderer.GLAllocation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class FontFixValues {
    public static FontFixValues INSTANCE = new FontFixValues();
    private final int max = 1000;
    public Map<StringHash, CachedString> stringCache = new HashMap<>();
    public List<StringHash> obfuscated = new ArrayList<>();
    public PriorityQueue<StringHash> usageCounter = new PriorityQueue<>(1, Comparator.comparingLong(o -> o.time));

    public Map<String, Integer> widthCache = new HashMap<>();
    public boolean opt = true;
    private long time;
    private int e;
    private long count = 0;

    private FontFixValues() {

    }

    @InvokeEvent
    public void tick(TickEvent tickEvent) {

        opt = true;
        e++;
        for (StringHash hash : obfuscated) {
            CachedString cachedString = stringCache.get(hash);
            stringCache.remove(hash);
            if (cachedString != null)
                GLAllocation.deleteDisplayLists(cachedString.getListId());
        }
        obfuscated.clear();
        if (e >= 20) {
            e = 0;
            count = 0;
            time = 0;
            while (stringCache.size() > max) {
                StringHash hash = usageCounter.poll();
                CachedString cachedString = stringCache.remove(hash);
                if (cachedString != null)
                    GLAllocation.deleteDisplayLists(cachedString.getListId());

            }
        }


    }

    public void incTime(long l) {
        time += l;
        count++;
    }
}
