package com.example;

import com.hcc.addons.annotations.Addon;
import com.hcc.event.ChatEvent;
import com.hcc.event.InvokeEvent;

@Addon(modid="AddonExample",version = "1.0")
public class AddonExample {
    @InvokeEvent
    public static void onChat(ChatEvent event){
        System.out.println("[EXAMPLE] " + event.getChat().getUnformattedText());
    }
}
