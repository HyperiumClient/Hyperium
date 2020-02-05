package club.ampthedev.versionjson.tasks

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.internal.artifacts.repositories.resolver.MavenUniqueSnapshotComponentIdentifier
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.component.external.model.ModuleComponentArtifactIdentifier
import java.io.File
import java.net.URL
import java.security.MessageDigest

open class CreateVersionJson : DefaultTask() {
    @InputFiles
    val config: Configuration = project.configurations.getByName("library")

    @OutputFile
    val json = File(temporaryDir, "libraries.base.json")

    @TaskAction
    fun createJson() {
        val libraryArray = JsonArray()
        config.resolvedConfiguration.resolvedArtifacts.forEach {
            val id = it.id
            if (id is ModuleComponentArtifactIdentifier) {
                var urlPath = "/${id.componentIdentifier.group.replace(
                    '.',
                    '/'
                )}/${id.componentIdentifier.module}/${id.componentIdentifier.version}/"
                val cid = id.componentIdentifier
                urlPath += if (cid is MavenUniqueSnapshotComponentIdentifier) {
                    "${cid.module}-${cid.version.replace("-SNAPSHOT", "")}-${cid.timestamp}"
                } else {
                    "${cid.module}-${cid.version.replace("-SNAPSHOT", "")}"
                }
                if (it.classifier != null) {
                    urlPath += "-${it.classifier}"
                }
                urlPath += ".${it.extension}"
                var url: URL? = null
                // Gradle doesn't provide a way for us to check where an artifact came from,
                // so we have to check it ourselves.
                for (repo in project.repositories) {
                    if (repo is MavenArtifactRepository) {
                        val artifactUrl = URL(repo.url.toString().removeSuffix("/") + urlPath)
                        try {
                            val stream = artifactUrl.openStream()
                            if (stream != null) {
                                stream.close()
                                url = artifactUrl
                                break
                            }
                        } catch (ignored: Exception) {
                        }
                    }
                }
                if (url == null) {
                    throw GradleException("Failed to find URL for artifact $it")
                }
                val obj = JsonObject()
                var desc =
                    "${id.componentIdentifier.group}:${id.componentIdentifier.module}:${id.componentIdentifier.version}"
                if (it.extension != "jar") {
                    desc += "-${it.extension}"
                }
                if (it.classifier != null) {
                    desc += "-${it.classifier}"
                }
                val downloadsObject = JsonObject()
                val artifactObject = JsonObject()
                artifactObject.addProperty("path", urlPath.removePrefix("/"))
                val file = it.file
                val md = MessageDigest.getInstance("SHA-1")
                val digest = md.digest(file.inputStream().use { f -> f.readBytes() })
                val sha1 = digest.fold("") { str, byte -> str + "%02x".format(byte) }
                artifactObject.addProperty("sha1", sha1)
                artifactObject.addProperty("size", file.length())
                artifactObject.addProperty("url", url.toString())
                downloadsObject.add("artifact", artifactObject)
                obj.add("downloads", downloadsObject)
                obj.addProperty(
                    "name",
                    desc
                )
                libraryArray.add(obj)
            } else {
                logger.warn("Can't add $it to library JSON (no download link)")
            }
        }
        val gson = GsonBuilder().setPrettyPrinting().create()
        json.bufferedWriter().use {
            gson.toJson(libraryArray, it)
        }
    }
}
