package uk.kulikov.dnd.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import uk.kulikov.dnd.sendDNDStatusPush
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
        get("dnd") {
            val statusRaw = call.request.queryParameters["status"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val status = when (statusRaw) {
                "on" -> true
                "off" -> false
                else -> return@get call.respond(HttpStatusCode.BadRequest)
            }

            tokens.forEach { token ->
                sendDNDStatusPush(token, status)
            }
            call.respondText("Send dnd status to ${tokens.size} devices")
        }
    }
}
