package cc.hyperium.mixinsimp.renderer;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheWriter;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import net.minecraft.client.renderer.GLAllocation;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.SharedDrawable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class FontFixValues {
    public static FontFixValues INSTANCE;
    public static SharedDrawable drawable;
    private final int MAX = 5000 /* Worth bumping up to 10_000? */;
    public List<StringHash> obfuscated = new ArrayList<>();
    private Cache<StringHash, CachedString> stringCache = Caffeine.newBuilder()
        .writer(new RemovalListener())
        .executor(Multithreading.POOL)
        .maximumSize(MAX)
        .build();
    private Queue<Integer> glRemoval = new ConcurrentLinkedQueue<>();

    public FontFixValues() {
        Multithreading.schedule(() -> {
            try {
                if (drawable == null) {
                    return;
                }
                if (!drawable.isCurrent())
                    try {
                        drawable.makeCurrent();
                    } catch (LWJGLException e) {
                        try {
                            drawable.makeCurrent();
                        } catch (LWJGLException e1) {
                            drawable = null;
                            e.printStackTrace();
                            GeneralChatHandler.instance().sendMessage("Something went wrong with Enhanced Font Renderer. If this issue persists, please open a ticket in the Hyperium Discord using -new in the #commands channel.");
                        }
                        return;
                    } catch (IllegalStateException ex) {
                        ex.printStackTrace();
                    }
                Integer integer;
                int i = 0;
                while ((integer = glRemoval.poll()) != null) {
                    i++;
                    GLAllocation.deleteDisplayLists(integer);
                }
                drawable.releaseContext();

            } catch (Exception e) {
                e.printStackTrace();
                if (drawable != null) {
                    try {
                        drawable.releaseContext();
                    } catch (LWJGLException e1) {
                        e1.printStackTrace();
                    }
                }


            }
        }, 1, 1, TimeUnit.SECONDS);
        System.out.println("STARTED");
    }

    public Queue<Integer> getGlRemoval() {
        return glRemoval;
    }

    @InvokeEvent
    public void tick(TickEvent tickEvent) {
        stringCache.invalidateAll(obfuscated);
        obfuscated.clear();
        if (drawable == null) {
            try {
                drawable = new SharedDrawable(Display.getDrawable());
            } catch (LWJGLException e) {
                e.printStackTrace();
            }
        }
    }


    @Nullable
    public CachedString get(StringHash key) {
        return stringCache.getIfPresent(key);
    }

    public void cache(StringHash key, CachedString value) {
        stringCache.put(key, value);
    }

    private class RemovalListener implements CacheWriter<StringHash, CachedString> {
        @Override
        public void write(@Nonnull StringHash key, @Nonnull CachedString value) {
        }

        @Override
        public void delete(@Nonnull StringHash key, @Nullable CachedString value, @Nonnull RemovalCause cause) {
            if (value == null) return;

            glRemoval.add(value.getListId());

        }
    }
}
