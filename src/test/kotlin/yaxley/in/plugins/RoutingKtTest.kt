package yaxley.`in`.plugins

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
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
            assertEquals(HttpStatusCode.Created, response.status)
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

    @Test
    fun testGetApiItems() = testApplication {
        application { module() }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }

        }
        client.get("/api/items") {}.apply {
            val response = call.response
            // Do not let this throw
            call.response.body<List<TodoItem>>()
            assertEquals(HttpStatusCode.OK, response.status)
        }
    }

    @Test
    fun testDeleteApiRemoveItemId() = testApplication {
        application { module() }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }

        }
        val itemCount: Int
        client.get("/api/items") {}.apply {
            // Do not let this throw
            itemCount = call.response.body<List<TodoItem>>().count()
        }
        client.post("/api/addItem") {
            contentType(ContentType.Application.Json)
            setBody(TodoItem(99999, "Testing", true))
        }.apply { }
        client.delete("/api/removeItem/1").apply {
            assertEquals(call.response.status, HttpStatusCode.NoContent)
        }
        client.get("/api/items") {}.apply {
            assertEquals(itemCount, call.response.body<List<TodoItem>>().count())
        }
    }

    @Test
    fun testPatchApiDoneId() = testApplication {
        application { module() }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val doneItems: Int
        client.get("/api/items").apply {
            doneItems = call.response.body<List<TodoItem>>().count { it.done }
        }
        client.patch("/api/done/3").apply {
            assertEquals(call.response.status, HttpStatusCode.NoContent)
        }
        client.get("/api/items").apply {
            val items = call.response.body<List<TodoItem>>()
            val newItemCount = items.count { it.done }
            assertEquals(doneItems + 1, newItemCount)
        }

    }
}