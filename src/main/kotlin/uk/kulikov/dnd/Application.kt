package uk.kulikov.dnd

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import io.ktor.server.application.*
import uk.kulikov.dnd.plugins.configureHTTP
import uk.kulikov.dnd.plugins.configureMonitoring
import uk.kulikov.dnd.plugins.configureRouting
import uk.kulikov.dnd.plugins.configureSerialization
import java.io.FileInputStream


private val clientToken =
    "cfl7qPsGRHqVV-ynOBDOHc:APA91bFShF6ZFpu4MCj-62tpxbGmHdBrM20U58AtwQaJxIR2-ftRugsvHf5mgxqQRzy-flVW1hmPF2nt0J-V6Pru9qDuUse2uf7b7oeYIVqMYER0PICMG1gA9k-PocIizEPFYbnC09aW"

fun main() {
    /*embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)*/
    sendPush()
}

fun Application.module() {
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureRouting()
}

private fun sendPush() {
    val googleCredentials = GoogleCredentials
        .fromStream(FileInputStream("private-key.json"))
        .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"));
    val options = FirebaseOptions.builder()
        .setCredentials(googleCredentials)
        .build()
    val firebaseApp = FirebaseApp.initializeApp(options)

    val message: Message = Message.builder()
        .setNotification(Notification.builder().setTitle("Hello!").build())
        .setToken(clientToken)
        .build()

    val response = FirebaseMessaging.getInstance(firebaseApp).send(message)

    println("Successfully sent message: $response")
}
