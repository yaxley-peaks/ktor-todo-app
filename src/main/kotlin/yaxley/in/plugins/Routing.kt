@file:Suppress("DEPRECATION")

package yaxley.`in`.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import yaxley.`in`.repositories.TodoItem
import yaxley.`in`.repositories.TodoItemRepository

fun Application.configureRouting() {
    routing {
        route("/api") {
            post("/addItem") {
                try {
                    val item = call.receive<TodoItem>()
                    if (item.title.isBlank()) {
                        call.respond(HttpStatusCode.BadRequest)
                        return@post
                    }
                    TodoItemRepository.items.addTodoItem(item.title, item.done)
                    call.respond(HttpStatusCode.NoContent)
                    return@post
                } catch (ex: ContentTransformationException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }

        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}
