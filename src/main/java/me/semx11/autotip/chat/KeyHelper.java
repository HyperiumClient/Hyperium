package me.semx11.autotip.chat;

import java.util.function.Consumer;

public class KeyHelper {

    private final MessageUtil messageUtil;
    private final String rootKey;

    KeyHelper(MessageUtil messageUtil, String rootKey) {
        this.messageUtil = messageUtil;
        this.rootKey = rootKey;
    }

    public ChatComponentBuilder getBuilder(String key, Object... params) {
        return messageUtil.getBuilder(this.getKey(key), params);
    }

    public KeyHelper separator() {
        messageUtil.separator();
        return this;
    }

    public KeyHelper sendKey(String key, Object... params) {
        messageUtil.sendKey(this.getAbsoluteKey(key), params);
        return this;
    }

    public KeyHelper withKey(String key, Consumer<KeyContext> consumer) {
        consumer.accept(new KeyContext(this, key));
        return this;
    }

    public String getKey(String key) {
        return messageUtil.getKey(this.getAbsoluteKey(key));
    }

    private String getAbsoluteKey(String relativeKey) {
        return rootKey + "." + relativeKey;
    }

    public static class KeyContext {

        private final KeyHelper keyHelper;
        private final String relativeKey;

        KeyContext(KeyHelper keyHelper, String relativeKey) {
            this.keyHelper = keyHelper;
            this.relativeKey = relativeKey;
        }

        public ChatComponentBuilder getBuilder(Object... params) {
            return keyHelper.getBuilder(relativeKey, params);
        }

        public KeyContext send(Object... params) {
            keyHelper.sendKey(relativeKey, params);
            return this;
        }

        public String getKey(String key) {
            return keyHelper.getKey(key);
        }

    }

}
