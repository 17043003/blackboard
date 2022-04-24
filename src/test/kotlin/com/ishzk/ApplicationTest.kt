package com.ishzk

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import io.ktor.serialization.gson.*
import io.ktor.server.plugins.*
import io.ktor.server.auth.*
import io.ktor.util.*
import io.ktor.server.locations.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.server.sessions.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.test.*
import io.ktor.server.testing.*
import com.ishzk.plugins.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.plugins.websocket.WebSockets

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }

    @Test
    fun testWebsocket() = testApplication {
        val client = client.config {
            install(WebSockets)
        }
        client.ws("/") {
            val sendMessage = "connected"
            outgoing.send(Frame.Text(sendMessage))
            val othersMessage = incoming.receive() as? Frame.Text ?: return@ws
            assertEquals("YOU SAID: $sendMessage", othersMessage.readText())
        }
    }
}