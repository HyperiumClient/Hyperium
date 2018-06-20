package cc.hyperium.handlers.handlers;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.mixinsimp.renderer.CachedString;
import net.minecraft.client.renderer.GLAllocation;

import java.util.HashMap;
import java.util.Map;

public class FontRendererData {
    public static final FontRendererData INSTANCE = new FontRendererData();
    public final Map<String, CachedString> normalStringCache = new HashMap<>();
    public final Map<String, CachedString> shadowStringCache = new HashMap<>();
    public final Map<String, Integer> stringWidthCache = new HashMap<>();
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
