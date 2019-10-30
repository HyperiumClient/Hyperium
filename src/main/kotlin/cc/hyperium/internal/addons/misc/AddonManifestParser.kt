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

package cc.hyperium.internal.addons.misc

import cc.hyperium.internal.addons.AddonManifest
import cc.hyperium.internal.addons.OverlayChecker
import com.google.common.io.Files
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.*
import java.nio.charset.Charset
import java.util.jar.JarFile

/**
 * Parses addon.json into a instance of {@link cc.hyperium.internal.addons.AddonManifest}
 *
 * @since 1.0
 * @author Kevin Brewster
 */
class AddonManifestParser {

    /**
     * Manifest json
     */
    private var json: JsonObject? = null

    /**
     * Gson instance
     */
    private val gson = Gson()

    /**
     * Used when parsing the addon.json inside of a
     * jarfile
     *
     * @param jar jarfile which the manifest should be read from
     */
    constructor(jar: JarFile) {

        var jarInputStream: InputStream? = null
        try {
            val entry = jar.getEntry("addon.json")

            val jsonFile = File.createTempFile("json", "tmp")
            jsonFile.deleteOnExit()

            jarInputStream = jar.getInputStream(entry)
            val os = FileOutputStream(jsonFile)
            copyInputStream(jarInputStream, os)

            val contents = Files.toString(jsonFile, Charset.defaultCharset())

            val parser = JsonParser()
            val json = parser.parse(contents).asJsonObject

            if (!json.has("version") && !json.has("name") && !json.has("mainClass")) {
                throw AddonLoadException("Invalid addon manifest (Must include name, verson and mainClass)")
            }
            this.json = json
        } catch (e: Exception) {
            e.printStackTrace()
            throw AddonLoadException("Exception reading manifest")
        } finally {
            jarInputStream?.close()
            jar.close()
        }
    }

    /**
     * Used when parsing an addon.jsons string
     *
     * @param contents The addons json (addon.json)
     */
    constructor(contents: String) {
        val parser = JsonParser()
        val json = parser.parse(contents).asJsonObject

        if (!json.has("version") && !json.has("name") && !json.has("mainClass")) {
            throw AddonLoadException("Invalid addon manifest (Must include name, version and mainClass)")
        }

        if (json.has("overlay")) {
            OverlayChecker.checkOverlayField(json.get("overlay").asString)
        }
        this.json = json
    }

    /**
     * Gets the AddonManifest instance
     */
    fun getAddonManifest(): AddonManifest = gson.fromJson(json, AddonManifest::class.java)

    /**
     * The String of the json
     *
     * @return addon manifest json in string
     */
    override fun toString(): String = json!!.toString()

    private fun copyInputStream(input: InputStream, output: OutputStream): Int {
        val count = copyLarge(input, output, ByteArray(1024 * 4))
        return if (count > Integer.MAX_VALUE) {
            -1
        } else count.toInt()
    }

    @Throws(IOException::class)
    private fun copyLarge(input: InputStream, output: OutputStream, buffer: ByteArray): Long {
        var count: Long = 0
        var n = 0
        while ({ n = input.read(buffer); n }() != -1) {
            output.write(buffer, 0, n)
            count += n.toLong()
        }
        return count
    }
}
