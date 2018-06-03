package cc.hyperium.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.JoinHypixelEvent;

public class UpdateNotification {

    CrashReportGUI crashReportGUI = new CrashReportGUI();

    @InvokeEvent
    public void sendNotification(JoinHypixelEvent e) {
        if (!crashReportGUI.isUpdated()) {
            Hyperium.INSTANCE.getNotification().display("Hyperium Update", "Hyperium needs an update! Go to https://hyperium.cc", 5);
        } else {
            System.out.println("updated");
        }
    }

}
