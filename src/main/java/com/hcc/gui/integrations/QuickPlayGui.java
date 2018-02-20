package com.hcc.gui.integrations;

import com.hcc.HCC;
import com.hcc.gui.HCCGui;
import com.hcc.handlers.handlers.remoteresources.HCCResource;
import com.hcc.mods.sk1ercommon.ResolutionUtil;
import com.hcc.utils.JsonHolder;

import java.util.ArrayList;
import java.util.List;

public class QuickPlayGui extends HCCGui {

    private HCCResource data = HCC.INSTANCE.getHandlers().getRemoteResourcesHandler().getResourceSync("quickplay_data.json");
    private List<QuickplayGame> games = new ArrayList<>();

    public QuickPlayGui() {
        JsonHolder jsonHolder = data.getasJson();
        JsonHolder games = jsonHolder.optJSONObject("games");
        for (String dbName : games.getKeys()) {
            JsonHolder game = games.optJSONObject(dbName);
            String name = game.optString("name");
            JsonHolder modes = game.optJSONObject("modes");
            QuickplayGame quickplayGame = new QuickplayGame(name);
            for (String mode1 : modes.getKeys()) {
                JsonHolder modeData = modes.optJSONObject(mode1);
                quickplayGame.addMode(new QuickplayMode(modeData.optString("name"), modeData.optString("queue")));
            }
            this.games.add(quickplayGame);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        int size = ResolutionUtil.current().getScaledWidth() / 10;
        for (QuickplayGame game : games) {

        }
    }

    @Override
    protected void pack() {

    }

    private class QuickplayGame {
        List<QuickplayMode> modes = new ArrayList<>();
        private String name;

        private QuickplayGame(String name) {
            this.name = name;
        }

        public List<QuickplayMode> getModes() {
            return modes;
        }

        public String getName() {
            return name;
        }

        public void addMode(QuickplayMode mode) {
            this.modes.add(mode);
        }
    }

    private class QuickplayMode {
        private String name;
        private String queue;

        private QuickplayMode(String name, String queue) {
            this.name = name;
            this.queue = queue;
        }

        public String getName() {
            return name;
        }

        public String getQueue() {
            return queue;
        }
    }
}
