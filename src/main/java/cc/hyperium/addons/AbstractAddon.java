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

import org.apache.commons.lang3.StringUtils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractAddon {
    public abstract AbstractAddon init();

    public abstract Metadata getAddonMetadata();

    public class Metadata {
        private final AbstractAddon addon;
        private final String author;
        private final String name;
        private final String version;

        private String description;
        private String displayName;

        private String overlayClassPath;

        public Metadata(AbstractAddon addon, String name) {
            this(addon, name, "1.0");
        }

        public Metadata(AbstractAddon addon, String name, String version) {
            this(addon, name, version, "");
        }

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

        public AbstractAddon getAddon() {
            return this.addon;
        }

        public String getName() {
            return this.name != null ? this.name : "";
        }

        public String getVersion() {
            return this.version;
        }

        public String getAuthor() {
            return this.author != null ? this.author : "";
        }

        public String getDisplayName() {
            return this.displayName != null ? this.displayName : getName();
        }

        public String getDescription() {
            return this.description != null ? this.description : "";
        }

        public String getOverlayClassPath() {
            return this.overlayClassPath;
        }

        public void setDisplayName(String name) {
            this.displayName = name;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setOverlayClassPath(String classPath) {
            this.overlayClassPath = classPath;
        }
    }
}
