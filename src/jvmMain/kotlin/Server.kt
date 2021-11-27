import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.respondHtml
import io.ktor.http.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import kotlinx.html.*

fun HTML.index() {
    head {
        title("Fancy Forum")
    }
    body {
        div {
            +"Forum"
        }
        div {
            id = "root"
        }
        script(src = "/static/Forum.js") {}
    }
}
val forumEntries= mutableListOf(
    TaskItem("Aufgabe erledeigne"),
    TaskItem("Wie entwickle ein Multiplatform projekt mit Kotlin")
)
fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        install(ContentNegotiation){
            json()
        }
        install(CORS){
            method(HttpMethod.Get)
            method(HttpMethod.Post)
            method(HttpMethod.Delete)
            anyHost()
        }
        install(Compression){
            gzip()
        }
        routing {
            route(TaskItem.path){
                get { call.respond(forumEntries) }
                post {
                    forumEntries+=call.receive<TaskItem>()
                    call.respond(HttpStatusCode.OK)
                }
                delete("/{id}") {
                    val id=call.parameters["id"]?.toInt()?: error("Invalid delete request")
                    forumEntries.removeIf { it.id==id }
                    call.respond(HttpStatusCode.OK)
                }
            }
            get("/") {
                call.respondHtml(HttpStatusCode.OK, HTML::index)
            }
            static("/static") {
                resources()
            }
        }
    }.start(wait = true)
}