package cc.hyperium.mods.autotpa;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.mods.autogg.AutoGGListener;
import cc.hyperium.mods.autogg.commands.GGCommand;
import cc.hyperium.mods.autogg.config.AutoGGConfig;
import cc.hyperium.mods.autotpa.commands.AutoTPACommand;
import cc.hyperium.mods.autotpa.config.AutoTPAConfig;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.utils.ChatColor;

import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AutoTPA extends AbstractMod {
    
    private static String trigger;
    private final Metadata meta;
    private AutoTPAConfig config;

    public AutoTPA() {
        Metadata metadata = new Metadata(this, "AutoTPA", "1.0", "SiroQ");
        metadata.setDisplayName(ChatColor.WHITE + "AutoTPA");
        this.meta = metadata;
    }

    @Override
    public AbstractMod init() {
        this.config = new AutoTPAConfig();
        EventBus.INSTANCE.register(new AutoTPAListener(this));
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new AutoTPACommand(this));
        Multithreading.POOL.submit(() -> {
            try {
                // This file will probably be moved away from your
                // github and into the hyperium repo in the future. 
                trigger = IOUtils.toString(
                        new URL("https://gist.githubusercontent.com/SiroQ/d9fa3fd8c5ef76ded5365c2cecdb28e4/raw/tpatrigger.txt"),
                        Charset.forName("UTF-8")
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return meta;
    }

    public String getTrigger() {
        return trigger;
    }


    public AutoTPAConfig getConfig() {
        return this.config;
    }
}
