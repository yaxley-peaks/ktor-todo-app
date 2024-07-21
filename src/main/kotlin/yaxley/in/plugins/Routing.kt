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

fun Application.configureRouting(repository: TodoItemRepository) {
    routing {
        route("/api") {
            get("/items") {
                call.respond(repository.allItems())
            }
            post("/addItem") {
                try {
                    val item = call.receive<TodoItem>()
                    if (item.title.isBlank()) {
                        call.respond(HttpStatusCode.BadRequest)
                        return@post
                    }
                    repository.add(item.title, item.done)
                    call.respond(HttpStatusCode.Created)
                    return@post
                } catch (ex: ContentTransformationException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
            delete("/removeItem/{id}") {
                val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                try {
                    val itemId: Int = id.toInt()
                    repository.remove(itemId)
                    return@delete call.respond(HttpStatusCode.NoContent)
                } catch (ex: NumberFormatException) {
                    return@delete call.respond(HttpStatusCode.BadRequest)
                } catch (ex: Throwable) {
                    return@delete call.respond(HttpStatusCode.InternalServerError)
                }
            }
            patch("/done/{id}") {
                val id = call.parameters["id"] ?: return@patch call.respond(HttpStatusCode.BadRequest)
                try {
                    val itemId: Int = id.toInt()
                    val item = repository.get(itemId)
                    if (item != null) {
                        repository.markAsDone(item)
                    } else {
                        return@patch call.respond(HttpStatusCode.BadRequest)
                    }
                    return@patch call.respond(HttpStatusCode.NoContent)
                } catch (ex: NumberFormatException) {
                    return@patch call.respond(HttpStatusCode.BadRequest)
                }

            }
            patch("/pending/{id}") {
                val id = call.parameters["id"] ?: return@patch call.respond(HttpStatusCode.BadRequest)
                try {
                    val itemId: Int = id.toInt()
                    val item = repository.get(itemId)
                    if (item != null) {
                        repository.markAsPending(item)
                    } else {
                        return@patch call.respond(HttpStatusCode.BadRequest)
                    }
                    return@patch call.respond(HttpStatusCode.NoContent)
                } catch (ex: NumberFormatException) {
                    return@patch call.respond(HttpStatusCode.BadRequest)
                }

            }
        }

        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}
