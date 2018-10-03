package cc.hyperium.handlers.handlers.data.leaderboards;

public class LeaderboardEntry {

    private int postion;
    private String value;
    private String playerUUID;
    private String playerDisplay;

    public LeaderboardEntry(int position, String value, String playerUUID, String playerDisplay) {
        this.postion = position;
        this.value = value;
        this.playerUUID = playerUUID;
        this.playerDisplay = playerDisplay;
    }

    public int getPostion() {

        return postion;
    }

    public String getValue() {
        return value;
    }

    public String getPlayerUUID() {
        return playerUUID;
    }

    public String getPlayerDisplay() {
        return playerDisplay;
    }

}
