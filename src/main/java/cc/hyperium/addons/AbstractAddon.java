/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.addons;

import cc.hyperium.gui.main.HyperiumOverlay;
import org.apache.commons.lang3.StringUtils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractAddon {

    /**
     * Initialization of the addon. This is invoked on client start-up
     *
     * @return The addon instance
     */
    public abstract AbstractAddon init();

    /**
     * Returns the metadata of the addon. The metadata contains information about the
     * addon (author, version, etc.)
     *
     * @return The addon metadata
     */
    public abstract Metadata getAddonMetadata();

    /**
     * Metadata for modifications
     */
    public class Metadata {

        /**
         * The addon instance
         */
        private final AbstractAddon addon;

        /**
         * The addon author
         */
        private final String author;

        /**
         * The addon name
         */
        private final String name;

        /**
         * The addon version
         */
        private final String version;

        /**
         * A description about the addon
         */
        private String description;

        /**
         * Display name of the addon
         */
        private String displayName;

        /**
         * The classpath for the overlay class.
         */
        private String overlayClassPath;

        public Metadata(AbstractAddon addon, String name) {
            this(addon, name, "1.0");
        }

        /**
         * Initiates a new Metadata with no author value.
         *
         * @param addon   Addon instance
         * @param name    Addon name
         * @param version Addon version
         */
        public Metadata(AbstractAddon addon, String name, String version) {
            this(addon, name, version, "");
        }

        /**
         * Initiates a new Metadata from the given parameters.
         *
         * @param addon   Addon instance
         * @param name    Addon name
         * @param version Addon version
         * @param author  Addon author
         */
        public Metadata(AbstractAddon addon, String name, String version, String author) {
            checkNotNull(addon, "Addon instance cannot be null");
            checkArgument(!StringUtils.isEmpty(name), "Name cannot be null or empty (" + name + ")");
            checkArgument(!StringUtils.isEmpty(version), "Version cannot be null or empty (" + version + ")");
            checkNotNull(author, "Author cannot be null (" + author + ")");

            this.addon = addon;
            this.name = name;
            this.author = author;
            this.version = version;
            this.displayName = name;
        }

        /**
         * Returns the addon instance
         *
         * @return The addon instance
         */
        public AbstractAddon getAddon() {
            return this.addon;
        }

        /**
         * Returns the addon name
         *
         * @return The addon name
         */
        public String getName() {
            return name != null ? name : "";
        }

        /**
         * Returns the addon version
         *
         * @return The addon version
         */
        public String getVersion() {
            return version;
        }

        /**
         * Returns the addon author
         *
         * @return The addon author
         */
        public String getAuthor() {
            return author != null ? author : "";
        }

        /**
         * Returns the display name of the addon
         *
         * @return Display name of the addon
         */
        public String getDisplayName() {
            return displayName != null ? displayName : getName();
        }

        /**
         * Returns the description of the addon
         *
         * @return The addon description
         */
        public String getDescription() {
            return description != null ? description : "";
        }

        /**
         * Returns the classpath to the subclass of {@link HyperiumOverlay}, which is added to
         * the Overlay menu
         *
         * @return The overlay classpath
         */
        public String getOverlayClassPath() {
            return overlayClassPath;
        }

        /**
         * Sets the display name of the addon
         *
         * @param name New name to set
         */
        public void setDisplayName(String name) {
            this.displayName = name;
        }

        /**
         * Sets the description of the addon
         *
         * @param description New description to set
         */
        public void setDescription(String description) {
            this.description = description;
        }

        /**
         * Sets the overlay classpath of the addon
         *
         * @param classPath New classpath to set
         */

        public void setOverlayClassPath(String classPath) {
            this.overlayClassPath = classPath;
        }
    }
}
