package uk.kulikov.dnd

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import uk.kulikov.dnd.plugins.configureHTTP
import uk.kulikov.dnd.plugins.configureMonitoring
import uk.kulikov.dnd.plugins.configureRouting
import uk.kulikov.dnd.plugins.configureSerialization

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureRouting()
}
