package yaxley.`in`.plugins

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import junit.framework.TestCase.assertEquals
import yaxley.`in`.module
import yaxley.`in`.repositories.TodoItem
import kotlin.test.Test

class RoutingKtTest {

    @Test
    fun testPostApiAddItem() = testApplication {

        application { module() }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }

        }
        client.post("/api/addItem") {
            contentType(ContentType.Application.Json)
            setBody(TodoItem(99999, "Test", true))
        }.apply {
            val response = call.response
            assertEquals(HttpStatusCode.NoContent, response.status)
        }
    }

    @Test
    fun testPostApiInvalidAddItem() = testApplication {
        application { module() }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }

        }
        client.post("/api/addItem") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("hello" to false))
        }.apply {
            val response = call.response
            assertEquals(HttpStatusCode.BadRequest, response.status)
        }
    }

    @Test
    fun testPostApiEmptyAddItem() = testApplication {
        application { module() }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }

        }
        client.post("/api/addItem") {
            contentType(ContentType.Application.Json)
            setBody(TodoItem(99999, "", true))
        }.apply {
            val response = call.response
            assertEquals(HttpStatusCode.BadRequest, response.status)
        }
    }
}