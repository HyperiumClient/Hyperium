package net.montoyo.mcef.example;

import java.io.IOException;
import java.io.InputStream;

import net.montoyo.mcef.MCEF;
import net.montoyo.mcef.api.*;
import net.montoyo.mcef.utilities.Log;

public class ModScheme implements IScheme {

    private String contentType = null;
    private InputStream is = null;
    
    @Override
    public SchemePreResponse processRequest(String url) {
        url = url.substring("mod://".length());
        
        int pos = url.indexOf('/');
        if(pos < 0)
            return SchemePreResponse.NOT_HANDLED;
        
        String mod = removeSlashes(url.substring(0, pos));
        String loc = removeSlashes(url.substring(pos + 1));

        if(mod.length() <= 0 || loc.length() <= 0 || mod.charAt(0) == '.' || loc.charAt(0) == '.') {
            Log.warning("Invalid URL " + url);
            return SchemePreResponse.NOT_HANDLED;
        }
        
        is = ModScheme.class.getResourceAsStream("/assets/" + mod.toLowerCase() + "/html/" + loc.toLowerCase());
        if(is == null) {
            Log.warning("Resource " + url + " NOT found!");
            return SchemePreResponse.NOT_HANDLED; //Mhhhhh... 404?
        }

        contentType = null;
        pos = loc.lastIndexOf('.');
        if(pos >= 0 && pos < loc.length() - 2)
            contentType = MCEF.PROXY.mimeTypeFromExtension(loc.substring(pos + 1));

        return SchemePreResponse.HANDLED_CONTINUE;
    }
    
    private String removeSlashes(String loc) {
        int i = 0;
        while(i < loc.length() && loc.charAt(i) == '/')
            i++;
        
        return loc.substring(i);
    }
    
    @Override
    public void getResponseHeaders(ISchemeResponseHeaders rep) {
        if(contentType != null)
            rep.setMimeType(contentType);

        rep.setStatus(200);
        rep.setStatusText("OK");
        rep.setResponseLength(-1);
    }
    
    @Override
    public boolean readResponse(ISchemeResponseData data) {
        try {
            int ret = is.read(data.getDataArray(), 0, data.getBytesToRead());
            if(ret <= 0)
                is.close();
            
            data.setAmountRead(Math.max(ret, 0));
            return ret > 0;
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
