/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods;

import org.apache.commons.lang3.StringUtils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

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
    public static class Metadata {

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
         * @param mod     the mod instance
         * @param name    the mod identifier
         * @param version the mod version
         * @param author  the mod author
         */
        public Metadata(AbstractMod mod, String name, String version, String author) {
            checkNotNull(mod, "Mod instance cannot be null");
            checkArgument(!StringUtils.isEmpty(name), "Name cannot be null or empty (" + name + ")");
            checkArgument(!StringUtils.isEmpty(version), "Version cannot be null or empty (" + version + ")");
            checkNotNull(author, "Author cannot be null (" + author + ")");

            this.mod = mod;
            this.name = name;
            this.author = author;
            this.version = version;
            displayName = name;
        }

        /**
         * Getter for the mod instance
         *
         * @return the mod instance
         */
        public AbstractMod getMod() {
            return mod;
        }

        /**
         * Getter for the identifier of the mod
         *
         * @return the identifier for the mod
         */
        public String getName() {
            return name != null ? name : "";
        }

        /**
         * Getter for the version of the mod
         *
         * @return the mod version
         */
        public String getVersion() {
            return version;
        }

        /**
         * Getter for the author of the mod
         *
         * @return the author of the mod
         */
        public String getAuthor() {
            return author != null ? author : "";
        }

        /**
         * Getter for the mods display name
         *
         * @return the display name
         */
        public String getDisplayName() {
            return displayName != null ? displayName : getName();
        }

        /**
         * Setter for the mods display name for the configuration menu
         *
         * @param name the display name to be set
         */
        public void setDisplayName(String name) {
            displayName = name;
        }
    }
}
