package uk.kulikov.dnd.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import uk.kulikov.dnd.sendPush

private val tokens = HashSet<String>()

fun Application.configureRouting() {
    routing {
        get("/register") {
            val token = call.request.queryParameters["token"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            if (tokens.add(token)) {
                call.respondText("OK")
            } else {
                call.respondText("Already created")
            }
        }
        get("/clear") {
            val mapSize = tokens.size
            tokens.clear()
            call.respondText("Clear $mapSize tokens")
        }
        get("/broadcast") {
            tokens.forEach { token ->
                sendPush(token)
            }
            call.respondText("Send broadcast to ${tokens.size} devices")
        }
    }
}
