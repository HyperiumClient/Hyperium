package com.chattriggers.ctjs.utils.config

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
annotation class ConfigOpt(val name: String, val x: Int, val y: Int, val type: KClass<*>)
