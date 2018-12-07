package me.semx11.autotip.config;

import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Locale;
import javax.annotation.CheckReturnValue;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.chat.MessageOption;
import me.semx11.autotip.gson.exclusion.Exclude;
import me.semx11.autotip.util.FileUtil;
import org.apache.commons.io.FileUtils;

public class Config {

    @Exclude
    private final Autotip autotip;
    @Exclude
    private final File configFile;

    private boolean enabled = true;
    private Locale locale = Locale.forLanguageTag("en-US");
    private MessageOption messageOption = MessageOption.SHOWN;

    public Config(Autotip autotip) {
        this.autotip = autotip;
        this.configFile = autotip.getFileUtil().getFile("config.at");
    }

    public boolean isEnabled() {
        return enabled;
    }

    @CheckReturnValue
    public Config setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @CheckReturnValue
    public Config toggleEnabled() {
        this.enabled = !this.enabled;
        return this;
    }

    public Locale getLocale() {
        return locale;
    }

    @CheckReturnValue
    public Config setLocale(Locale locale) {
        this.locale = locale;
        return this;
    }

    public MessageOption getMessageOption() {
        return messageOption;
    }

    @CheckReturnValue
    public Config nextMessageOption() {
        this.messageOption = messageOption.next();
        return this;
    }

    @CheckReturnValue
    public Config setMessageOption(MessageOption messageOption) {
        this.messageOption = messageOption;
        return this;
    }

    public Config save() {
        try {
            String json = autotip.getGson().toJson(this);
            FileUtils.writeStringToFile(configFile, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Autotip.LOGGER.error("Could not write config to " + configFile, e);
        }
        return this;
    }

    public Config load() {
        try {
            String json = FileUtils.readFileToString(configFile);
            return this.merge(autotip.getGson().fromJson(json, Config.class)).save();
        } catch (FileNotFoundException e) {
            Autotip.LOGGER.info("config.at does not exist, creating...");
        } catch (JsonSyntaxException e) {
            Autotip.LOGGER.warn("config.at has invalid contents, resetting...");
        } catch (IOException e) {
            Autotip.LOGGER.error("Could not read config.at!", e);
        }
        return this.save();
    }

    public Config migrate() {
        FileUtil fileUtil = autotip.getFileUtil();

        // Check if legacy config file exists
        File legacyFile = fileUtil.getFile("options.at");
        if (!legacyFile.exists()) {
            return this;
        }

        try {
            List<String> lines = Files.readAllLines(fileUtil.getPath("options.at"));
            if (lines.size() < 2) {
                return this;
            }

            this.enabled = Boolean.parseBoolean(lines.get(0));
            try {
                this.messageOption = MessageOption.valueOf(lines.get(1));
            } catch (IllegalArgumentException | NullPointerException e) {
                this.messageOption = MessageOption.SHOWN;
            }

            // Deletes old file to complete migration
            fileUtil.delete(legacyFile);

            return this.save();
        } catch (IOException e) {
            Autotip.LOGGER.error("Could not read legacy options.at file!");
            return this.save();
        }
    }

    private Config merge(final Config that) {
        this.enabled = that.enabled;
        this.locale = that.locale == null ? this.locale : that.locale;
        this.messageOption = that.messageOption == null ? this.messageOption : that.messageOption;
        return this;
    }

}
