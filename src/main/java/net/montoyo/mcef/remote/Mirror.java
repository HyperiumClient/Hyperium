package net.montoyo.mcef.remote;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import net.montoyo.mcef.MCEF;
import net.montoyo.mcef.utilities.Log;

/**
 * An enumeration of mirror website where JCEF resources (library & packages) can be downloaded.
 * A random mirror is selected at the beginning. If it is marked as broken, another mirror is chosen.
 * 
 * @author montoyo
 *
 */
public enum Mirror {
    
    MONTOYO("http://montoyo.net/jcef", "montoyo.net");

    private static Mirror current = pickRandom();
    private final String url;
    private final String name;
    private boolean broken;
    
    Mirror(String url, String name) {
        this.url = url;
        this.name = name;
        broken = false;
    }
    
    /**
     * Checks if a mirror is broken or not.
     * @return true if the mirror is broken.
     */
    public boolean isBroken() {
        return broken;
    }
    
    /**
     * Returns a "thanks" string.
     * @return The "thanks" string.
     */
    public String getMirrorString() {
        if(MCEF.FORCE_MIRROR == null)
            return "Mirror kindly provided by " + name;
        else
            return "Mirror location was set to " + MCEF.FORCE_MIRROR;
    }
    
    /**
     * Opens a connection to the mirror's resource corresponding to the URL.
     * 
     * @param name The URL of the resource, relative to the root of the mirror website.
     * @return A connection to this resource, with timeout set up.
     * @throws MalformedURLException if the mirror's URL is invalid or if name is invalid.
     * @throws IOException if an I/O exception occurs.
     */
    public HttpURLConnection getResource(String name) throws MalformedURLException, IOException {
        HttpURLConnection ret = (HttpURLConnection) (new URL((MCEF.FORCE_MIRROR == null ? url : MCEF.FORCE_MIRROR) + '/' + name)).openConnection();
        ret.setConnectTimeout(30000);
        ret.setReadTimeout(15000);
        ret.setRequestProperty("User-Agent", "MCEF");
        
        return ret;
    }
    
    private static Mirror pickRandom() {
        Mirror[] lst = values();
        int idx = (new Random()).nextInt(lst.length);
        
        return lst[idx];
    }
    
    private static Mirror pickWorking() {
        for(Mirror m: values()) {
            if(!m.broken)
                return m;
        }
        
        return null;
    }
    
    /**
     * Marks the current mirror as broken.
     * @return a new mirror that isn't broken, or null if there's none.
     */
    public static Mirror markAsBroken() {
        current.broken = true;
        
        Mirror old = current;
        current = pickWorking();
        Log.info("Mirror %s marked as broken; using %s", old.url, (current == null) ? "NULL" : current.url);
        
        return current;
    }
    
    /**
     * Gets the current working mirror.
     * @return The current non-broken mirror, or null if all mirrors are broken.
     */
    public static Mirror getCurrent() {
        return current;
    }
    
    /**
     * Marks all mirrors as working (= not broken).
     * If all mirrors were broken, it chooses a new random mirror.
     * 
     * @return The current (or new) mirror.
     */
    public static Mirror reset() {
        for(Mirror m: values())
            m.broken = false;
        
        if(current == null)
            current = pickRandom();
        
        return current;
    }

}
