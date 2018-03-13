package cc.hyperium.handlers.handlers;

import cc.hyperium.Hyperium;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.JsonHolder;
import cc.hyperium.utils.mods.FPSLimiter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.world.World;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by mitchellkatz on 3/12/18. Designed for production use on Sk1er.club
 */
public class TimeTrackHandler implements Runnable {

    private final DateFormat folderFormat = new SimpleDateFormat("MMMM YYYY");
    private final DateFormat todayFormat = new SimpleDateFormat("dd");
    private JsonHolder data;
    private File today;

    public TimeTrackHandler() {
        loadData();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            save(today, data.toString());
        }));
        Multithreading.schedule(this, 1L, 1L, TimeUnit.SECONDS);
    }

    private void loadData() {
        File file = new File(Hyperium.folder, "timetracking/" + Minecraft.getMinecraft().getSession().getPlayerID() + "/" + folderFormat.format(new Date(System.currentTimeMillis())));

        //check parent exists
        if (!file.exists())
            file.mkdirs();
        today = new File(file, todayFormat.format(new Date(System.currentTimeMillis())));
        //First time running today its fine my dude
        if (!today.exists()) {
            data = new JsonHolder().put("time", System.currentTimeMillis());
            return;
        }
        try {
            FileReader fr = new FileReader(today);
            BufferedReader br = new BufferedReader(fr);
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null)
                builder.append(line);

            String done = builder.toString();
            this.data = new JsonHolder(done);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void run() {
        try {
            checkDate();
            if (Hyperium.INSTANCE.getHandlers().getHypixelDetector().isHypixel()) {
                if (FPSLimiter.getInstance().isLimbo()) {
                    inc("Hypixel", "limbo");
                } else {
                    inc("Hpixel", Hyperium.INSTANCE.getMinigameListener().getCurrentMinigameName());
                }
            } else {
                EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
                if (thePlayer == null) {
                    inc("Unknown1", null);
                    return;
                }
                World theWorld = thePlayer.getEntityWorld();
                if (theWorld == null) {
                    inc("Unknown2", null);
                    return;
                }
                ServerData currentServerData = Minecraft.getMinecraft().getCurrentServerData();
                if (currentServerData != null) {
                    inc("Server", currentServerData.serverIP);
                } else {
                    inc("Singleplayer", Minecraft.getMinecraft().getIntegratedServer().getFolderName());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void save(File file, String data) {
        try {
            File parentFile = file.getParentFile();
            if (!parentFile.exists())
                parentFile.mkdirs();
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data);
            bw.close();
            fw.close();
        } catch (Exception e) {

        }
    }

    private void checkDate() {
        if (!todayFormat.format(new Date(System.currentTimeMillis())).equalsIgnoreCase(today.getName())) {
            save(today, data.toString());
            data = new JsonHolder().put("time", System.currentTimeMillis());
            File file = new File(Hyperium.folder, "timetracking/" + Minecraft.getMinecraft().getSession().getPlayerID() + "/" + folderFormat.format(new Date(System.currentTimeMillis())));

            //check parent exists
            if (!file.exists())
                file.mkdirs();
            today = new File(file, todayFormat.format(new Date(System.currentTimeMillis())));

        }
    }

    private void inc(String catagory, String sub) {
        if (sub == null) {
            data.put(catagory, data.optLong(catagory) + 1);
            return;
        }
        if (!data.has(catagory))
            data.put(catagory, new JsonHolder());
        data.optJSONObject(catagory).put(sub, data.optJSONObject(catagory).optLong(sub) + 1);
    }
}
