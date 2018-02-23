package me.semx11.autotip.event;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.ServerJoinEvent;
import cc.hyperium.event.ServerLeaveEvent;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.misc.StartLogin;
import me.semx11.autotip.util.UniversalUtil;

public class HypixelListener {

    public static String lastIp;

    @InvokeEvent
    public void playerLoggedIn(ServerJoinEvent event) {
        lastIp = UniversalUtil.getRemoteAddress(event).toLowerCase();
        if (lastIp.contains(".hypixel.net") || lastIp.contains("209.222.115.14")) {
            Autotip.onHypixel = true;
            Tipper.waveCounter = 910;
            Autotip.THREAD_POOL.submit(new StartLogin());
        } else {
            Autotip.onHypixel = false;
        }
    }

    @InvokeEvent
    public void playerLoggedOut(ServerLeaveEvent event) {
        Autotip.onHypixel = false;
    }

}