package net.montoyo.mcef.remote;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.montoyo.mcef.setup.FileListing;
import org.cef.OS;

import net.montoyo.mcef.MCEF;
import net.montoyo.mcef.client.ClientProxy;
import net.montoyo.mcef.utilities.IProgressListener;
import net.montoyo.mcef.utilities.Log;
import net.montoyo.mcef.utilities.Util;
import net.montoyo.mcef.utilities.Version;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * A class for updating and parsing the remote configuration file.
 * @author montoyo
 *
 */
public class RemoteConfig {
    
    private static String PLATFORM;
    private ResourceList resources = new ResourceList();
    private ArrayList<String> extract = new ArrayList<String>();
    private String version = null;
    
    public RemoteConfig() {
    }
    
    /**
     * Parses the MCEF configuration file.
     * 
     * @param f The configuration file.
     * @return The parsed configuration file.
     */
    private JsonObject readConfig(File f) {
        try {
            return (new JsonParser()).parse(new FileReader(f)).getAsJsonObject();
        } catch(JsonIOException e) {
            Log.error("IOException while reading remote config.");
            e.printStackTrace();
            return null;
        } catch(FileNotFoundException e) {
            Log.error("Couldn't find remote config.");
            e.printStackTrace();
            return null;
        } catch(Exception e) {
            Log.error("Syntax error in remote config.");
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Updates the MCEF configuration file and parses it.
     * @return The parsed configuration file.
     */
    private JsonObject readConfig() {
        File newCfg = new File(ClientProxy.ROOT, "mcef2.new");
        File cfgFle = new File(ClientProxy.ROOT, "mcef2.json");
        
        boolean ok = Util.download("config2.json", newCfg, null);
        Mirror.reset(); //We may reset mirrors state because config.json is a special case.
        
        if(ok) {
            Util.delete(cfgFle);
            
            if(newCfg.renameTo(cfgFle))
                return readConfig(cfgFle);
            else {
                Log.warning("Couldn't rename mcef2.new to mcef2.json.");
                return readConfig(newCfg);
            }
            
        } else {
            Log.warning("Couldn't read remote config. Using local configuration file.");
            return readConfig(cfgFle);
        }
    }
    
    /**
     * Updates the MCEF configuration file and parses it.
     * Fills the resources, extract and version fields from it.
     */
    public void load() {
        JsonObject json = readConfig();
        if(json == null) {
            Log.error("Could NOT read either remote and local configuration files. Entering virtual mode.");
            ClientProxy.VIRTUAL = true;
            return;
        }
        
        String id;
        if(OS.isWindows())
            id = "win";
        else if(OS.isMacintosh())
            id = "mac";
        else if(OS.isLinux())
            id = "linux";
        else {
            //Shouldn't happen.
            Log.error("Your OS isn't supported by MCEF. Entering virtual mode.");
            ClientProxy.VIRTUAL = true;
            return;
        }
        
        String arch = System.getProperty("sun.arch.data.model");
        if(!arch.equals("32") && !arch.equals("64")) {
            //Shouldn't happen.
            Log.error("Your CPU arch isn't supported by MCEF. Entering virtual mode.");
            ClientProxy.VIRTUAL = true;
            return;
        }
        
        PLATFORM = id + arch;
        Log.info("Detected platform: %s", PLATFORM);

        JsonElement ver = json.get(MCEF.VERSION);
        if(ver == null || !ver.isJsonObject()) {
            Log.error("Config file does NOT contain the latest MCEF version (wtf??). Entering virtual mode.");
            ClientProxy.VIRTUAL = true;
            return;
        }

        JsonObject vData = ver.getAsJsonObject();
        JsonElement cat = vData.get("platforms");
        if(cat == null || !cat.isJsonObject()) {
            Log.error("Config file is missing \"platforms\" object. Entering virtual mode.");
            ClientProxy.VIRTUAL = true;
            return;
        }

        JsonObject catObj = cat.getAsJsonObject();
        JsonElement res = catObj.get(PLATFORM);
        if(res == null || !res.isJsonObject()) {
            Log.error("Your platform isn't supported by MCEF yet. Entering virtual mode.");
            ClientProxy.VIRTUAL = true;
            return;
        }
        
        resources.clear();
        addResources(res.getAsJsonObject(), PLATFORM);

        res = catObj.get("shared");
        if(res != null && res.isJsonObject())
            addResources(res.getAsJsonObject(), "shared");
        
        JsonElement ext = vData.get("extract");
        if(ext != null && ext.isJsonArray()) {
            JsonArray ray = ext.getAsJsonArray();
            
            for(JsonElement e: ray) {
                if(e != null && e.isJsonPrimitive())
                    extract.add(e.getAsString());
            }
        }
        
        JsonElement mcVersions = json.get("latestVersions");
        if(mcVersions != null && mcVersions.isJsonObject()) {
            JsonElement cVer = mcVersions.getAsJsonObject().get(Minecraft.getMinecraft().getVersion());

            if(cVer != null && cVer.isJsonPrimitive())
                version = cVer.getAsString();
        }
    }

    private void addResources(JsonObject res, String pform) {
        Set<Entry<String, JsonElement>> files = res.entrySet();

        for(Entry<String, JsonElement> e: files) {
            if(e.getValue() == null || !e.getValue().isJsonPrimitive())
                continue;

            String key = e.getKey();
            if(key.length() >= 2 && key.charAt(0) == '@') {
                Resource eRes = new Resource(key.substring(1), e.getValue().getAsString(), pform);
                eRes.setShouldExtract();

                resources.add(eRes);
            } else
                resources.add(new Resource(key, e.getValue().getAsString(), pform));
        }
    }
    
    /**
     * Detects missing files, download them, and if needed, extracts them.
     * 
     * @param ipl The progress listener.
     * @return true if the operation was successful.
     */
    public boolean downloadMissing(IProgressListener ipl) {
        if(MCEF.SKIP_UPDATES) {
            Log.warning("NOT downloading resources as specified in the configuration file");
            return true;
        }

        Log.info("Checking for missing resources...");
        resources.removeExistings();

        if(resources.size() > 0) {
            Log.info("Found %d missing resources. Downloading...", resources.size());
            Log.info(Mirror.getCurrent().getMirrorString());
            
            for(Resource r: resources) {
                if(!r.download(ipl))
                    return false;
            }
            
            for(String r: extract) {
                Resource res = resources.fromFileName(r);
                if(res == null) //Not missing; no need to extract
                    continue;
                
                if(!res.extract(ipl)) //Probably not a huge problem if we can't extract some resources... no need to return.
                    Log.warning("Couldn't extract %s. MCEF may not work because of this.", r);
            }
            
            Log.info("Done; all resources were downloaded.");
        } else
            Log.info("None are missing. Good.");
        
        return true;
    }
    
    /**
     * Returns an info string if an MCEF update is available.
     * @return an info string if a newer version exists, null otherwise.
     */
    public String getUpdateString() {
        if(version == null)
            return null;
        
        Version cur = new Version(MCEF.VERSION);
        Version cfg = new Version(version);
        
        if(cfg.isBiggerThan(cur))
            return "New MCEF version available. Current: " + cur + ", latest: " + cfg + '.';
        
        return null;
    }

    /**
     * Writes in a text file all files used by MCEF for uninstall purposes.
     *
     * @param configDir Directory where "mcefFiles.lst" should be located.
     * @param zipOnly Only care about extractable resources.
     * @return true if everything went file.
     */
    public boolean updateFileListing(File configDir, boolean zipOnly) {
        if(resources.isEmpty())
            return true;

        FileListing fl = new FileListing(configDir);
        if(!fl.load())
            Log.warning("Could not load file listing; trying to overwrite...");

        if(!zipOnly) {
            for(Resource r: resources)
                fl.addFile(r.getFileName());
        }

        boolean allOk = true;
        for(String r: extract) {
            File rf = Resource.getLocationOf(r);

            if(rf.exists()) {
                if(!fl.addZip(rf.getAbsolutePath()))
                    allOk = false;
            }
        }

        return fl.save() && allOk;
    }

    public File[] getResourceArray() {
        return resources.stream().map(r -> Resource.getLocationOf(r.getFileName())).toArray(File[]::new);
    }

}
