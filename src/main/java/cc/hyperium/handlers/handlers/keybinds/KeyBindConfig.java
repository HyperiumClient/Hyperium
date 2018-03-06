package cc.hyperium.handlers.handlers.keybinds;

import cc.hyperium.Hyperium;
import cc.hyperium.utils.BetterJsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * KeyBinding config, basically just a modified ToggleChat config
 *
 * @author boomboompower
 */
@SuppressWarnings("ResultOfMethodCallIgnored") // Suppress because we don't care
public class KeyBindConfig {
    
    private BetterJsonObject keyBindJson = new BetterJsonObject();
    
    private KeyBindHandler handler;
    
    private File keybindFile;
    
    protected KeyBindConfig(KeyBindHandler handler, File directory) {
        if (!directory.exists()) {
            directory.mkdirs();
        }
    
        this.handler = handler;
        this.keybindFile = new File(directory, "keybinds.json");
    }
    
    public void save() {
        if (!exists(this.keybindFile)) {
            this.keybindFile.getParentFile().mkdirs();
        }
        
        try {
            this.keybindFile.createNewFile();
            
            for (HyperiumBind base : this.handler.getKeybinds().values()) {
                this.keyBindJson.addProperty(base.getRealDescription(), base.getKeyCode());
            }
    
            this.keyBindJson.writeToFile(this.keybindFile);
        } catch (IOException ex) {
            Hyperium.LOGGER.warn("An error occured while saving the Hyperium KeyBlinds, this is not good.");
        }
    }
    
    public void load() {
        if (exists(this.keybindFile)) {
            try {
                FileReader fileReader = new FileReader(this.keybindFile);
                BufferedReader reader = new BufferedReader(fileReader);
                StringBuilder builder = new StringBuilder();
        
                String current;
                while ((current = reader.readLine()) != null) {
                    builder.append(current);
                }
                this.keyBindJson = new BetterJsonObject(builder.toString());
            } catch (Exception ex) {
                // Error occured while loading
                save();
                return;
            }
    
            for (HyperiumBind bind : this.handler.getKeybinds().values()) {
                bind.setKeyCode(this.keyBindJson.optInt(bind.getRealDescription(), bind.getDefaultKeyCode()));
            }
        } else {
            // Config file doesn't exist, yay!
            save();
        }
    }
    
    public void attemptKeyBindLoad(HyperiumBind bind) {
        if (this.keyBindJson.has(bind.getRealDescription())) {
            bind.setKeyCode(this.keyBindJson.optInt(bind.getRealDescription(), bind.getDefaultKeyCode()));
        }
    }
    
    private boolean exists(File file) {
        return file != null && Files.exists(Paths.get(file.getPath()));
    }
}
