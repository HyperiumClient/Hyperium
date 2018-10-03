package net.montoyo.mcef.remote;

import java.util.ArrayList;

/**
 * An {@link ArrayList} extended with utility functions for {@link Resource}
 * @author montoyo
 *
 */
public class ResourceList extends ArrayList<Resource> {
    
    /**
     * Gets a resource from its filename.
     * 
     * @param file The filename of the resource.
     * @return The resource if it was found, or null.
     */
    public Resource fromFileName(String file) {
        for(Resource f: this) {
            if(f.getFileName().equalsIgnoreCase(file))
                return f;
        }
        
        return null;
    }
    
    /**
     * Checks if every resources exists.
     * If they exists and if their checksum are matching, they are removed from the list.
     */
    public void removeExistings() {
        for(int i = 0; i < size(); i++) {
            if(get(i).exists())
                remove(i--);
        }
    }

}
