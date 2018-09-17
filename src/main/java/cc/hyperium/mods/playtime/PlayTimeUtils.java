package cc.hyperium.mods.playtime;

public class PlayTimeUtils {
    static String fancyTime(long seconds) {
        PlayTime playTime = new PlayTime();
        String fancyTime = "";

        if (seconds / 86400 != 0) {
            fancyTime = fancyTime.concat(String.valueOf((playTime.sessionPlayTime / 86400))).concat("d ");
        }

        if (seconds / 3600 != 0) {
            fancyTime = fancyTime.concat(String.valueOf((playTime.sessionPlayTime / 3600) - ((playTime.sessionPlayTime / 86400) * 24))).concat("h ");
        }

        if (seconds / 60 != 0) {
            fancyTime = fancyTime.concat(String.valueOf((playTime.sessionPlayTime / 60) - ((playTime.sessionPlayTime / 3600) * 60))).concat("m ");
        }

        if (seconds != 0) {
            fancyTime = fancyTime.concat(String.valueOf((playTime.sessionPlayTime) - ((playTime.sessionPlayTime / 60) * 60))).concat("s ");
        }

        return fancyTime;
    }
}
