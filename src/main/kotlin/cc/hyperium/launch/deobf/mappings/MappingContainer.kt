package cc.hyperium.launch.deobf.mappings

import cc.hyperium.launch.patching.PatchManager
import net.minecraft.launchwrapper.Launch
import org.objectweb.asm.ClassReader

/**
 * Represents mapping data used to deobfuscate classes.
 */
data class MappingContainer(private val classes: Map<String, ClassData>) {
    /**
     * A map for looking up deobfuscated names to get their obfuscated counterparts.
     */
    val reverseClassMap = hashMapOf<String, String>()

    /**
     * Set of classes that already have had their parents' mappings merged with theirs.
     * Prevents unnecessary extra lookups and sometimes even [StackOverflowError]s.
     */
    private val fullyMappedClasses = hashSetOf<String>()

    init {
        classes.forEach { reverseClassMap[it.value.deobfuscatedName] = it.key }
    }

    /**
     * Gets the class data for a specified (obfuscated) class name.
     */
    operator fun get(obfuscatedName: String) = classes[obfuscatedName]?.also { findAndMergeParentMappings(it) }

    /**
     * Ensures a subclass is fully mapped.
     * Sometimes mappings don't include data for subclasses and only for parents, which causes
     * deobfuscated interfaces and abstract classes to not behave properly,
     * and sometimes even throw [AbstractMethodError].
     */
    private fun ensureSubclassesAreMapped(classData: ClassData, parents: Array<String>) {
        if (classData.obfuscatedName in fullyMappedClasses) return
        fullyMappedClasses += classData.obfuscatedName
        parents.mapNotNull { classes[it] }.forEach {
            findAndMergeParentMappings(it)
            classData.methods.putAll(it.methods)
            classData.fields.putAll(it.fields)
        }
    }

    /**
     * Tries to find parents for a class and, if found, merges their mappings with the passed class's mappings.
     */
    private fun findAndMergeParentMappings(data: ClassData) {
        if (data.obfuscatedName in fullyMappedClasses) return
        Launch.classLoader.getClassBytes(data.obfuscatedName)?.let { originalClassData ->
            PatchManager.INSTANCE.patch(data.obfuscatedName, originalClassData, false)?.let { patchedClassData ->
                ClassReader(patchedClassData).let { ensureSubclassesAreMapped(data, it.interfaces + it.superName) }
            }
        }
    }

    /**
     * Mapping data for classes and their members (fields and methods).
     */
    data class ClassData(val obfuscatedName: String, val deobfuscatedName: String) {
        /**
         * Mapping data for fields. Keys are the obfuscated names and the values are deobfuscated names.
         */
        val fields = hashMapOf<String, String>()

        /**
         * Mapping data for methods. Keys are the obfuscated names concatenated with obfuscated descriptors and the
         * values are deobfuscated names.
         */
        val methods = hashMapOf<String, String>()
    }
}