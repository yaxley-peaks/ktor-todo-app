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
import yaxley.`in`.repositories.addTodoItem

fun Application.configureRouting() {
    routing {
        route("/api") {
            post("/addItem") {
                try {
                    val item = call.receive<TodoItem>()
                    TodoItemRepository.items.addTodoItem(item)
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
