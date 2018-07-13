package cc.hyperium.config

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class SelectorSetting(val name: String, val category: Category = Category.GENERAL, val items: Array<String>, val enabled: Boolean = true)