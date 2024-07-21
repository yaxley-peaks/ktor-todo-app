package yaxley.`in`.plugins

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import junit.framework.TestCase.assertEquals
import yaxley.`in`.module
import kotlin.test.Test

class TemplatingKtTest {

    @Test
    fun testGet() = testApplication {
        application {
            module()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }
}