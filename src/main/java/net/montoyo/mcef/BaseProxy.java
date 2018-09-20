package net.montoyo.mcef;

import net.montoyo.mcef.api.*;
import net.montoyo.mcef.utilities.Log;

public class BaseProxy implements API {

    public void onPreInit() {
    }
    
    public void onInit() {
        Log.info("MCEF is running on server. Nothing to do.");
    }

    @Override
    public IBrowser createBrowser(String url, boolean transparent) {
        Log.warning("A mod called API.createBrowser() from server! Returning null...");
        return null;
    }

    @Override
    public IBrowser createBrowser(String url) {
        return createBrowser(url, false);
    }

    @Override
    public void registerDisplayHandler(IDisplayHandler idh) {
        Log.warning("A mod called API.registerDisplayHandler() from server!");
    }

    @Override
    public boolean isVirtual() {
        return true;
    }

    @Override
    public void openExampleBrowser(String url) {
        Log.warning("A mod called API.openExampleBrowser() from server! URL: %s", url);
    }

    @Override
    public void registerJSQueryHandler(IJSQueryHandler iqh) {
        Log.warning("A mod called API.registerJSQueryHandler() from server!");
    }

    @Override
    public String mimeTypeFromExtension(String ext) {
        Log.warning("A mod called API.mimeTypeFromExtension() from server!");
        return null;
    }

    @Override
    public void registerScheme(String name, Class<? extends IScheme> schemeClass, boolean std, boolean local, boolean displayIsolated) {
        Log.warning("A mod called API.registerScheme() from server!");
    }

    @Override
    public boolean isSchemeRegistered(String name) {
        Log.warning("A mod called API.isSchemeRegistered() from server!");
        return false;
    }

    public void onShutdown() {
    }

}
