import kotlinx.serialization.Serializable

@Serializable
data class TaskItem(val text: String) {
    val id: Int = text.hashCode()

    companion object {
        const val path = "/forumEntries"
    }
}
