package net.montoyo.mcef.client;

import net.montoyo.mcef.MCEF;
import net.montoyo.mcef.api.IScheme;
import net.montoyo.mcef.utilities.Log;

import org.cef.CefApp;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefSchemeHandlerFactory;
import org.cef.callback.CefSchemeRegistrar;
import org.cef.handler.CefAppHandlerAdapter;
import org.cef.handler.CefResourceHandler;
import org.cef.network.CefRequest;

import java.util.HashMap;
import java.util.Map;

public class AppHandler extends CefAppHandlerAdapter {

    private static class SchemeData {

        private Class<? extends IScheme> cls;
        private boolean std;
        private boolean local;
        private boolean dispIsolated;

        private SchemeData(Class<? extends IScheme> cls, boolean std, boolean local, boolean dispIsolated) {
            this.cls = cls;
            this.std = std;
            this.local = local;
            this.dispIsolated = dispIsolated;
        }

    }

    private final HashMap<String, SchemeData> schemeMap = new HashMap<>();

    public AppHandler(String[] args) {
        super(args);
    }

    public void registerScheme(String name, Class<? extends IScheme> cls, boolean std, boolean local, boolean dispIsolated) {
        schemeMap.put(name, new SchemeData(cls, std, local, dispIsolated));
    }

    public boolean isSchemeRegistered(String name) {
        return schemeMap.containsKey(name);
    }
    
    @Override
    public void onRegisterCustomSchemes(CefSchemeRegistrar reg) {
        int cnt = 0;

        for(Map.Entry<String, SchemeData> entry : schemeMap.entrySet()) {
            if(reg.addCustomScheme(entry.getKey(), entry.getValue().std, entry.getValue().local, entry.getValue().dispIsolated, true, false, false))
                cnt++;
            else
                Log.error("Could not register scheme %s", entry.getKey());
        }

        Log.info("%d schemes registered", cnt);
    }
    
    @Override
    public void onContextInitialized() {
        CefApp app = MCEF.PROXY.getCefApp();

        for(Map.Entry<String, SchemeData> entry : schemeMap.entrySet())
            app.registerSchemeHandlerFactory(entry.getKey(), "", new SchemeHandlerFactory(entry.getValue().cls));
    }
    
    private static class SchemeHandlerFactory implements CefSchemeHandlerFactory {

        private Class<? extends IScheme> cls;

        private SchemeHandlerFactory(Class<? extends IScheme> cls) {
            this.cls = cls;
        }

        @Override
        public CefResourceHandler create(CefBrowser browser, CefFrame frame, String schemeName, CefRequest request) {
            try {
                return new SchemeResourceHandler(cls.newInstance());
            } catch(Throwable t) {
                t.printStackTrace();
                return null;
            }
        }
        
    }

}
