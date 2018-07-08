package cc.hyperium.handlers.handlers.animation.cape;

import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.WorldChangeEvent;
import cc.hyperium.installer.utils.DownloadTask;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.netty.utils.Utils;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.CapeUtils;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.UUIDUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.SharedDrawable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CapeHandler {

    public static final ReentrantLock LOCK = new ReentrantLock();
    private final ConcurrentHashMap<UUID, ICape> capes = new ConcurrentHashMap<>();
    private final ResourceLocation loadingResource = new ResourceLocation("");
    private final File CACHE_DIR;
    private ConcurrentLinkedQueue<Runnable> actions = new ConcurrentLinkedQueue<>();
    private SharedDrawable drawable;


    public CapeHandler() {
        try {
            drawable = new SharedDrawable(Display.getDrawable());
            Multithreading.runAsync(() -> {
                try {
                    drawable.makeCurrent();
                } catch (LWJGLException e) {
                    e.printStackTrace();
                }
                while (true) {
                    Runnable poll;
                    while ((poll = actions.poll()) != null) {
                        try {
                            poll.run();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        CACHE_DIR = new File(Hyperium.folder, "CACHE_DIR");
        CACHE_DIR.mkdir();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            CACHE_DIR.delete();
        }));
    }

    @InvokeEvent
    public void worldSwap(WorldChangeEvent event) {
        UUID id = UUIDUtil.getClientUUID();
        ICape selfCape = capes.get(id);
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        try {
            LOCK.lock();

            for (ICape cape : capes.values()) {
//                if (selfCape != null && selfCape.equals(cape))
//                    continue;
                cape.delete(Minecraft.getMinecraft().getTextureManager());
            }
            capes.clear();
//            if (selfCape != null)
//                capes.put(id, selfCape);
        } finally {
            LOCK.unlock();
        }
    }

    public void loadStaticCape(final UUID uuid, String url, boolean reforamt) {
        if (capes.get(uuid) != null && !capes.get(uuid).equals(NullCape.INSTANCE))
            return;
        capes.put(uuid, NullCape.INSTANCE);

        ResourceLocation resourceLocation = new ResourceLocation(
                String.format("hyperium/capes/%s.png", System.nanoTime())
        );
        System.out.println("downloading " + url);

        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        ThreadDownloadImageData threadDownloadImageData = new ThreadDownloadImageData(null, url, null, new IImageBuffer() {

            @Override
            public BufferedImage parseUserSkin(BufferedImage image) {
                return CapeUtils.parseCape(image);
            }

            @Override
            public void skinAvailable() {
                CapeHandler.this.setCape(uuid, new StaticCape(resourceLocation));
            }
        });
        try {
            LOCK.lock();
            textureManager.loadTexture(resourceLocation, threadDownloadImageData);
        } catch (Exception e) {

        } finally {
            LOCK.unlock();
        }
    }

    public void setCape(UUID uuid, ICape cape) {
        capes.put(uuid, cape);
    }

    public synchronized void loadDynamicCape(final UUID uuid, String url) throws IOException, ExecutionException, InterruptedException {
        if (capes.get(uuid) != null && !capes.get(uuid).equals(NullCape.INSTANCE))
            return;
        capes.put(uuid, NullCape.INSTANCE);

        File file = new File(CACHE_DIR, uuid.toString());
        if (file.exists())
            FileUtils.deleteDirectory(file);
         file.mkdirs();
        DownloadTask task = new DownloadTask(
                url,
                file.getAbsolutePath());
        task.execute();
        task.get();
        List<BufferedImage> images = new ArrayList<>();
        unzip(new File(file, task.getFileName()).getAbsolutePath(), file.getAbsolutePath());
        int img = 0;
        File tmp;
        while ((tmp = new File(file, "img" + img + ".png")).exists()) {
            images.add(ImageIO.read(tmp));
            img++;
        }

        ArrayList<ResourceLocation> locations = new ArrayList<>();
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        final int[] i = {0};
        try {
            for (BufferedImage image : images) {
                actions.add(() -> {
                    ResourceLocation resourceLocation = new ResourceLocation(
                            String.format("hyperium/dynamic_capes/%s_%s.png", file.getName(), i[0]));
                    locations.add(resourceLocation);

                    CapeTexture capeTexture = new CapeTexture(image);
                    textureManager.loadTexture(resourceLocation, capeTexture);
                    capeTexture.clearTextureData();
                    i[0]++;
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int finalImg = img;
        JsonHolder holder = Utils.get("https://api.hyperium.cc/cape/" + uuid.toString());

        actions.add(() -> {
            setCape(uuid, new DynamicCape(locations, holder.optInt("delay"), finalImg));
        });
    }


    public ResourceLocation getCape(final AbstractClientPlayer player) {
        UUID uuid = player.getUniqueID();

        if (isRealPlayer(uuid)) {
            ICape cape = capes.getOrDefault(uuid, null);
            if (cape == null) {
                setCape(player.getUniqueID(), NullCape.INSTANCE);
                Multithreading.runAsync(() -> {
                    HyperiumPurchase hyperiumPurchase = PurchaseApi.getInstance()
                            .getPackageSync(uuid);
                    JsonHolder holder = hyperiumPurchase.getPurchaseSettings().optJSONObject("cape");
                    String s = holder.optString("type");
                    if (s.equalsIgnoreCase("CUSTOM_GIF")) {
                        String url = holder.optString("url");
                        if (!url.isEmpty()) {
                            try {
                                loadDynamicCape(uuid, url);
                            } catch (IOException | ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                    } else if (s.equalsIgnoreCase("CUSTOM_IMAGE")) {
                        loadStaticCape(uuid, holder.optString("url"), false);
                        return;
                    } else if (!s.isEmpty()) {
                        JsonHolder jsonHolder = PurchaseApi.getInstance().getCapeAtlas()
                                .optJSONObject(s);
                        String url = jsonHolder.optString("url");
                        if (!url.isEmpty()) {
                            loadStaticCape(uuid, url, true);
                            return;
                        }
                    }
                    loadStaticCape(uuid,
                            "http://s.optifine.net/capes/" + player.getGameProfile().getName()
                                    + ".png", true);
                });
                return capes.getOrDefault(uuid, NullCape.INSTANCE).get();
            }

            if (cape.equals(NullCape.INSTANCE)) {
                return null;
            }
            return cape.get();
        } else {
            return null;
        }
    }

    public boolean isRealPlayer(UUID uuid) {
        String s = uuid.toString().replace("-", "");
        if (s.length() == 32 && s.charAt(12) != '4') {
            return false;
        } else {
            return true;
        }
    }


    public void deleteCape(UUID id) {
        this.capes.remove(id);
    }

    private void unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if (!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println("Unzipping to " + newFile.getAbsolutePath());
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
