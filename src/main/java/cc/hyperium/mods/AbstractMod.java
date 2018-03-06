/*
 *  Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  Hyperium Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods;

import com.google.common.base.Preconditions;

/**
 * A simple interface which allows a built-in mod
 * to be turned on and off by the user
 */
public abstract class AbstractMod {
    
    /**
     * The init method where all events and commands should
     * be registered. Use this to load configs as well
     *
     * @return the {@link AbstractMod} instance of the mod
     */
    public abstract AbstractMod init();
    
    /**
     * This mods metadata, which will be displayed in the
     * configuration gui and other places
     *
     * @return the mods metadata
     */
    public abstract Metadata getModMetadata();
    
    /**
     * The Metadata implementation, created to identify built-in
     * addons and their other information. For upcoming features
     */
    public class Metadata {
        
        private final AbstractMod mod;
        private final String author;
        private final String name;
        private final String version;
        
        private String displayName;
        
        public Metadata(AbstractMod mod, String name) {
            this(mod, name, "1.0");
        }
        
        public Metadata(AbstractMod mod, String name, String version) {
            this(mod, name, version, "");
        }
    
        /**
         * The best constructor for the Metadata, contains all the useful data
         *
         * @param mod the mod instance
         * @param name the mod identifier
         * @param version the mod version
         * @param author the mod author
         */
        public Metadata(AbstractMod mod, String name, String version, String author) {
            Preconditions.checkArgument(mod != null, "The mod instance cannot be null");
            Preconditions.checkArgument(name != null && !name.isEmpty(), "Name cannot be null or empty");
            Preconditions.checkArgument(version != null && !version.isEmpty(), "Version cannot be null or empty");
            Preconditions.checkArgument(author != null, "Author cannot be null");
            this.mod = mod;
            this.name = name;
            this.author = author;
            this.version = version;
            this.displayName = name;
        }
    
        /**
         * Getter for the mod instance
         *
         * @return the mod instance
         */
        public AbstractMod getMod() {
            return this.mod;
        }
    
        /**
         * Getter for the identifier of the mod
         *
         * @return the identifier for the mod
         */
        public String getName() {
            return this.name != null ? this.name : "";
        }
        
        /**
         * Getter for the version of the mod
         *
         * @return the mod version
         */
        public String getVersion() {
            return this.version;
        }
    
        /**
         * Getter for the author of the mod
         *
         * @return the author of the mod
         */
        public String getAuthor() {
            return this.author != null ? this.author : "";
        }
    
        /**
         * Setter for the mods display name for the configuration menu
         *
         * @param name the display name to be set
         */
        public void setDisplayName(String name) {
            this.displayName = name;
        }
        
        /**
         * Getter for the mods display name
         *
         * @return the display name
         */
        public String getDisplayName() {
            return this.displayName != null ? this.displayName : getName();
        }
    }
}
