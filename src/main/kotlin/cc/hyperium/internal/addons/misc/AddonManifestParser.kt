package cc.hyperium.internal.addons.misc

import com.google.common.io.Files
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import cc.hyperium.internal.addons.AddonManifest
import org.apache.commons.io.IOUtils

import java.io.File
import java.io.FileOutputStream
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

        try {
            val entry = jar.getEntry("addon.json")

            val jsonFile = File.createTempFile("json", "tmp")
            jsonFile.deleteOnExit()

            val jarInputStream = jar.getInputStream(entry)
            val os = FileOutputStream(jsonFile)
            IOUtils.copy(jarInputStream, os)

            val contents = Files.toString(jsonFile, Charset.defaultCharset())

            val parser = JsonParser()
            val json = parser.parse(contents).asJsonObject

            if (!json.has("version") && !json.has("name") && !json.has("mainClass")) {
                throw AddonLoadException("Invalid addon manifest ( Must include name, verson and mainClass)")
            }
            this.json = json
        } catch (e: Exception) {
            throw AddonLoadException("Exception reading manifest")
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
            throw AddonLoadException("Invalid addon manifest ( Must include name, verson and mainClass)")
        }
        this.json = json
    }

    /**
     * Gets the AddonManifest instance
     */
    fun getAddonManifest(): AddonManifest {
        return gson.fromJson(json, AddonManifest::class.java)
    }

    /**
     * The String of the json
     *
     * @return addon manifest json in string
     */
    override fun toString(): String {
        return json!!.toString()
    }
}
