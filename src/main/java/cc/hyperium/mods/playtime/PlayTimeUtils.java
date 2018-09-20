package cc.hyperium.mods.playtime;

public class PlayTimeUtils {
    static String fancyTime(long seconds) {
        String fancyTime = "";

        if (seconds / 86400 != 0) {
            fancyTime = fancyTime.concat(String.valueOf((seconds / 86400))).concat("d ");
        }

        if (seconds / 3600 != 0) {
            fancyTime = fancyTime.concat(String.valueOf((seconds / 3600) - ((seconds / 86400) * 24))).concat("h ");
        }

        if (seconds / 60 != 0) {
            fancyTime = fancyTime.concat(String.valueOf((seconds / 60) - ((seconds / 3600) * 60))).concat("m ");
        }

        if (seconds != 0) {
            fancyTime = fancyTime.concat(String.valueOf((seconds) - ((seconds / 60) * 60))).concat("s ");
        }

        return fancyTime;
    }
}
