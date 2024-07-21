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
        val allItems: List<TodoItem>
        client.get("/api/items").apply {
            allItems = call.response.body<List<TodoItem>>()
        }
        for(item in allItems) {
            client.patch("/api/done/${item.id}").apply {
                assertEquals(call.response.status, HttpStatusCode.NoContent)
            }
        }
        client.get("/api/items").apply {
            val items = call.response.body<List<TodoItem>>()
            val doneItemCount = items.count { it.done }
            assertEquals(items.count(), doneItemCount)
        }

    }
    @Test
    fun testPatchApiDoneInvalidId() = testApplication {
       application { module() }
        client.patch("/api/done/lmao").apply {
            assertEquals(HttpStatusCode.BadRequest, call.response.status)
        }
    }
    @Test
    fun testPatchApiDoneNonExistentItem() = testApplication {
        application { module() }
        client.patch("/api/done/99999").apply {
            assertEquals(HttpStatusCode.BadRequest, call.response.status)
        }
    }
    @Test
    fun testPatchApiPendingId() = testApplication {
        application { module() }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val allItems: List<TodoItem>
        client.get("/api/items").apply {
            allItems = call.response.body<List<TodoItem>>()
        }
        for (item in allItems) {
            client.patch("/api/pending/${item.id}").apply {
                assertEquals(call.response.status, HttpStatusCode.NoContent)
            }
        }
        client.get("/api/items").apply {
            val items = call.response.body<List<TodoItem>>()
            val newItemCount = items.count { it.done }
            assertEquals(0, newItemCount)
        }

    }
    @Test
    fun testPatchApiPendingInvalidId() = testApplication {
        application { module() }
        client.patch("/api/pending/lmao").apply {
            assertEquals(HttpStatusCode.BadRequest, call.response.status)
        }
    }
    @Test
    fun testPatchApiPendingNonExistentItem() = testApplication {
        application { module() }
        client.patch("/api/pending/99999").apply {
            assertEquals(HttpStatusCode.BadRequest, call.response.status)
        }
    }
}