package cc.hyperium.update;

import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.JoinHypixelEvent;
import cc.hyperium.gui.CrashReportGUI;
import cc.hyperium.gui.settings.items.GeneralSetting;
import cc.hyperium.update.UpdateUtils;
/*
    by ConorTheDev
 */

public class UpdateNotification {

    UpdateUtils update = new UpdateUtils();

    @InvokeEvent
    public void sendNotification(JoinHypixelEvent e) {
        if (!update.isUpdated() && GeneralSetting.showUpdateNotifications) {
            Hyperium.INSTANCE.getNotification().display("Hyperium Update", "Hyperium needs an update! Go to https://hyperium.cc", 5);
        } else {
            System.out.println("updated");
        }
    }

}
