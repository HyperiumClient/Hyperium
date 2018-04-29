package cc.hyperium.handlers.handlers;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.utils.CachedString;
import net.minecraft.client.renderer.GLAllocation;

import java.util.HashMap;

public class FontRendererData {
    public static FontRendererData INSTANCE = new FontRendererData();
    public HashMap<String, CachedString> normalStringCache = new HashMap<>();
    public HashMap<String, CachedString> shadowStringCache = new HashMap<>();
    public HashMap<String, Integer> stringWidthCache = new HashMap<>();
    int e = 0;

    private FontRendererData() {

    }

    @InvokeEvent
    public void tickEvent(TickEvent event) {
        this.e++;
        if (e > 20) {
            normalStringCache.forEach((s, cachedString) -> GLAllocation.deleteDisplayLists(cachedString.getListId()));
            shadowStringCache.forEach((s, cachedString) -> GLAllocation.deleteDisplayLists(cachedString.getListId()));
            normalStringCache.clear();
            shadowStringCache.clear();
            e = 0;
        }
    }


}
