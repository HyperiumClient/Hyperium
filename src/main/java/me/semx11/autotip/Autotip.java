package me.semx11.autotip;

import com.hcc.HCC;
import com.hcc.event.EventBus;
import com.hcc.commands.BaseCommand;
import me.semx11.autotip.command.AutotipCommand;
import me.semx11.autotip.command.LimboCommand;
import me.semx11.autotip.command.TipHistoryCommand;
import me.semx11.autotip.event.ChatListener;
import me.semx11.autotip.event.HypixelListener;
import me.semx11.autotip.event.Tipper;
import me.semx11.autotip.misc.AutotipThreadFactory;
import me.semx11.autotip.util.FileUtil;
import me.semx11.autotip.util.Hosts;
import me.semx11.autotip.util.MessageOption;
import me.semx11.autotip.util.Version;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Autotip {

    public static final String MODID = "autotip";
    public static final String VERSION_STRING = "2.0.3";
    public static final Version VERSION = new Version(VERSION_STRING);
    public static final ExecutorService THREAD_POOL = Executors
            .newCachedThreadPool(new AutotipThreadFactory());
    public static String USER_DIR = "";

    public static Minecraft mc = Minecraft.getMinecraft();

    public static MessageOption messageOption = MessageOption.SHOWN;
    public static String playerUUID = "";
    public static boolean onHypixel = false;
    public static boolean toggle = true;

    public static int totalTipsSent;
    public static List<String> alreadyTipped = new ArrayList<>();

    public Autotip init() {
        try {
            playerUUID = Minecraft.getMinecraft().getSession().getProfile().getId().toString();
            USER_DIR = "mods" + File.separator + "autotip" + File.separator + playerUUID
                    + File.separator;
            this.registerEvents(
                    new Tipper(),
                    new HypixelListener(),
                    new ChatListener()
            );
            this.registerCommands(
                    new AutotipCommand()

            );
            this.registerCommands(
                    new TipHistoryCommand());
            this.registerCommands(
                    new LimboCommand()
            );

            FileUtil.getVars();
            Hosts.updateHosts();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e2) {
            HCC.LOGGER.debug("[Auto-GG] Invalid UUID detected; Not logged in?.");
        }
        return this;
    }

    private void registerEvents(Object... events) {
        Arrays.asList(events).forEach(EventBus.INSTANCE::register);
    }

    private void registerCommands(BaseCommand... commands) {
        Arrays.asList(commands).forEach((e) -> HCC.INSTANCE.getHandlers().getHCCCommandHandler().registerCommand(e));
    }

}