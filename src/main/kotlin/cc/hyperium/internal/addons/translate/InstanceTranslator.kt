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
