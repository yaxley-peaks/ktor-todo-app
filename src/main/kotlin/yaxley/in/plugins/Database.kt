package yaxley.`in`.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

@Suppress("UnusedReceiverParameter")
fun Application.configureDatabase() {
    Database.connect(
        url = "jdbc:mysql://localhost:3306/TodoApp",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "yax",
        password = "12345678"
    )
}