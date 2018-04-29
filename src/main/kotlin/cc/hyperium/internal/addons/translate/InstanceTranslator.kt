package cc.hyperium.internal.addons.translate

import cc.hyperium.internal.addons.AddonManifest

class InstanceTranslator : AbstractTranslator() {

    override fun translate(manifest: AddonManifest) {
        /*  val instance = Class.forName(manifest.mainClass).newInstance()
          val clazz = instance.javaClass
          val fields = clazz.fields
          for (field in fields) {
              val annotation = field.getAnnotation(Instance::class.java)
              if (annotation != null) {
                  field.isAccessible = true
                  field.set(instance, instance)
              }
          }*/
    }

}