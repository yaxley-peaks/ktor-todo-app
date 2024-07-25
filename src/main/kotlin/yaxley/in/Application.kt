package yaxley.`in`

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import yaxley.`in`.plugins.*
import yaxley.`in`.repositories.MySqlTodoItemRepository
import yaxley.`in`.repositories.TodoItemRepository

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val itemRepository : TodoItemRepository = MySqlTodoItemRepository()
    configureDatabase()
    configureTemplating(itemRepository)
    configureSerialization()
    configureSecurity()
    configureRouting(itemRepository)
}
