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
package cc.hyperium.launch.deobf

import cc.hyperium.launch.deobf.mappings.MappingContainer
import org.objectweb.asm.commons.Remapper

/**
 * ASM remapper that deobfuscates Notch mappings to their MCP counterparts
 */
class DeobfuscatingRemapper(val mappings: MappingContainer) : Remapper() {
    override fun mapFieldName(owner: String, name: String, desc: String) = mappings[owner]?.fields?.get(name) ?: name

    override fun map(typeName: String) = mappings[typeName]?.deobfuscatedName ?: typeName

    override fun mapMethodName(owner: String, name: String, desc: String) = mappings[owner]?.methods?.get(name + desc) ?: name

    override fun mapSignature(signature: String?, typeSignature: Boolean) = signature?.takeIf { "!*" !in it }?.let { super.mapSignature(it, typeSignature) }
}