package cc.hyperium.resources

enum class CapePriority(private val priorityName: String) {
    HYPERIUM("Hyperium"),
    MINECON("Minecon"),
    OPTIFINE("Optifine");

    val priority: String
        get() = priorityName
}