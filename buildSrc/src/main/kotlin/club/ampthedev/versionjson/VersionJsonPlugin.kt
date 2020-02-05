package club.ampthedev.versionjson

import club.ampthedev.versionjson.tasks.CreateVersionJson
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar

class VersionJsonPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val config = target.configurations.create("library")
        target.configurations.getByName("compile").extendsFrom(config)
        target.afterEvaluate {
            val task = it.tasks.create("createVersionJson", CreateVersionJson::class.java)
            val jarTask = it.tasks.getByName("jar") as Jar
            jarTask.dependsOn(task)
            jarTask.from(task.json)
        }
    }
}
