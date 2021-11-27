import io.ktor.http.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer

import kotlinx.browser.window

val endpoint = window.location.origin // only needed until https://youtrack.jetbrains.com/issue/KTOR-453 is resolved

val jsonClient = HttpClient {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}

suspend fun getTaslList(): List<TaskItem> {
    return jsonClient.get(endpoint + TaskItem.path)
}

suspend fun addTask(forumEntry: TaskItem) {
    jsonClient.post<Unit>(endpoint + TaskItem.path) {
        contentType(ContentType.Application.Json)
        body = forumEntry
    }

}
suspend fun deleteTask(task: TaskItem){
    jsonClient.delete<Unit>(endpoint + TaskItem.path +"/${task.id}")

}
