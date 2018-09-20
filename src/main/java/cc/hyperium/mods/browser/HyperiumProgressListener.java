package cc.hyperium.mods.browser;

import cc.hyperium.SplashProgress;
import net.montoyo.mcef.utilities.IProgressListener;

public class HyperiumProgressListener implements IProgressListener {

    private int oldProgress = -1;

    @Override
    public void onProgressed(double d) {
        SplashProgress.PROGRESS = (int) d;

        SplashProgress.update();
    }

    @Override
    public void onTaskChanged(String name) {
        if (oldProgress == -1) {
            oldProgress = SplashProgress.PROGRESS;
        }

        SplashProgress.MAX = 100;
        SplashProgress.CURRENT = name;
        SplashProgress.update();
    }

    @Override
    public void onProgressEnd() {
        SplashProgress.MAX = SplashProgress.DEFAULT_MAX;
        SplashProgress.PROGRESS = oldProgress;
        SplashProgress.CANCEL_IF_MAX = true;
    }
}
