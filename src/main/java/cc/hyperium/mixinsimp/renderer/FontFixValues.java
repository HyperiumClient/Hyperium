package cc.hyperium.mixinsimp.renderer;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheWriter;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import net.minecraft.client.renderer.GLAllocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class FontFixValues {
    public static FontFixValues INSTANCE = new FontFixValues();

    private final int MAX = 1000;

    private Cache<StringHash, CachedString> stringCache = Caffeine.newBuilder()
            .writer(new RemovalListener())
            .maximumSize(MAX)
            .build();
    public List<StringHash> obfuscated = new ArrayList<>();

    private FontFixValues() {}

    @InvokeEvent
    public void tick(TickEvent tickEvent) {
        stringCache.invalidateAll(obfuscated);
        obfuscated.clear();
    }

    public @Nullable CachedString get(StringHash key) {
        return stringCache.getIfPresent(key);
    }

    public void cache(StringHash key, CachedString value) {
        stringCache.put(key, value);
    }

    private class RemovalListener implements CacheWriter<StringHash, CachedString> {
        @Override
        public void write(@Nonnull StringHash key, @Nonnull CachedString value) { }

        @Override
        public void delete(@Nonnull StringHash key, @Nullable CachedString value, @Nonnull RemovalCause cause) {
            if (value == null) return;

            GLAllocation.deleteDisplayLists(value.getListId());
        }
    }
}
