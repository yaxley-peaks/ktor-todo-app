package yaxley.`in`

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import yaxley.`in`.plugins.configureRouting
import yaxley.`in`.plugins.configureSecurity
import yaxley.`in`.plugins.configureSerialization
import yaxley.`in`.plugins.configureTemplating

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureTemplating()
    configureSerialization()
    configureSecurity()
    configureRouting()
}
