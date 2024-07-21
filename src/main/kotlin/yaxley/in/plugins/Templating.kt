package yaxley.`in`.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import yaxley.`in`.repositories.TodoItemRepository

fun Application.configureTemplating(itemRepository: TodoItemRepository) {
    install(Thymeleaf) {
        setTemplateResolver(ClassLoaderTemplateResolver().apply {
            prefix = "templates/thymeleaf/"
            suffix = ".html"
            characterEncoding = "utf-8"
        })
    }
    routing {
        get("/") {
            call.respond(ThymeleafContent("index", mapOf("items" to itemRepository.allItems())))
        }
    }
}
