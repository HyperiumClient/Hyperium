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

package cc.hyperium.optifine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflector {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final ReflectorClass<?> CONFIG = new ReflectorClass<>("Config");
    public static final ReflectorField<String, ?> OF_VERSION = new ReflectorField<>(CONFIG, "VERSION");


    private static abstract class ReflectorData<T, R> {
        private T data;

        protected abstract String describeType();

        protected abstract String describeValue();

        protected abstract R getRaw() throws Exception;

        @SuppressWarnings("unchecked")
        public void refresh() {
            try {
                data = (T) getRaw();
            } catch (Exception ignored) {
            }
            if (data == null) LOGGER.debug("{} not present: {}", describeType(), describeValue());
        }

        public boolean exists() {
            return data != null;
        }

        public T get() {
            return data;
        }
    }

    public static class ReflectorClass<T extends Class<?>> extends ReflectorData<T, Class<?>> {
        private final String name;

        public ReflectorClass(String name) {
            this.name = name;
        }

        @Override
        protected String describeType() {
            return "Class";
        }

        @Override
        protected String describeValue() {
            return name;
        }

        @Override
        protected Class<?> getRaw() throws Exception {
            return Class.forName(name);
        }
    }

    public static class ReflectorField<T, O extends Class<?>> extends ReflectorData<Field, Field> {
        private final ReflectorClass<O> owner;
        private final String name;

        public ReflectorField(ReflectorClass<O> owner, String name) {
            this.owner = owner;
            this.name = name;
        }

        @SuppressWarnings("unchecked")
        public T get(O owner) {
            try {
                return (T) get().get(owner);
            } catch (IllegalAccessException ignored) {
                return null;
            }
        }

        @Override
        protected String describeType() {
            return "Field";
        }

        @Override
        protected String describeValue() {
            return owner.name + "#" + name;
        }

        @Override
        protected Field getRaw() throws Exception {
            if (!owner.exists()) return null;
            Field f = owner.get().getDeclaredField(name);
            f.setAccessible(true);
            return f;
        }
    }

    public static class ReflectorMethod<T, O extends Class<?>> extends ReflectorData<Method, Method> {
        private final ReflectorClass<O> owner;
        private final String name;
        private final ReflectorClass<?>[] parameterTypes;

        public ReflectorMethod(ReflectorClass<O> owner, String name, ReflectorClass<?>[] parameterTypes) {
            this.owner = owner;
            this.name = name;
            this.parameterTypes = parameterTypes;
        }

        public T invokeStatic(Object... parameters) {
            return invoke(null, parameters);
        }

        @SuppressWarnings("unchecked")
        public T invoke(O owner, Object... parameters) {
            try {
                return (T) get().invoke(owner, parameters);
            } catch (IllegalAccessException e) {
                return null;
            } catch (InvocationTargetException e) {
                Throwable target = e.getTargetException();
                if (target instanceof RuntimeException) {
                    throw (RuntimeException) target;
                } else {
                    throw new RuntimeException(target);
                }
            }
        }

        @Override
        protected String describeType() {
            return "Method";
        }

        @Override
        protected String describeValue() {
            return owner.name + name;
        }

        @Override
        protected Method getRaw() throws Exception {
            if (!owner.exists()) return null;
            Class<?>[] convertedParameterTypes = new Class<?>[parameterTypes.length];
            for (int i = 0; i < convertedParameterTypes.length; i++) {
                ReflectorClass<?> c = parameterTypes[i];
                if (!c.exists()) return null;
                convertedParameterTypes[i] = c.get();
            }
            Method m = owner.get().getDeclaredMethod(name, convertedParameterTypes);
            m.setAccessible(true);
            return m;
        }
    }
}
