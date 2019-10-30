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

package cc.hyperium.addons;

import org.apache.commons.lang3.StringUtils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractAddon {
    public abstract AbstractAddon init();

    public abstract Metadata getAddonMetadata();

    public static class Metadata {
        private final AbstractAddon addon;
        private final String author;
        private final String name;
        private final String version;

        private String description;
        private String displayName;

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
            displayName = name;
        }

        public AbstractAddon getAddon() {
            return addon;
        }

        public String getName() {
            return name != null ? name : "";
        }

        public String getVersion() {
            return version;
        }

        public String getAuthor() {
            return author != null ? author : "";
        }

        public String getDisplayName() {
            return displayName != null ? displayName : getName();
        }

        public String getDescription() {
            return description != null ? description : "";
        }

        public void setDisplayName(String name) {
            displayName = name;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
