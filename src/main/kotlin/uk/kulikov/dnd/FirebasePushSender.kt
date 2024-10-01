package uk.kulikov.dnd

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.AndroidConfig
import com.google.firebase.messaging.ApnsConfig
import com.google.firebase.messaging.Aps
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import java.io.FileInputStream

private val googleCredentials = GoogleCredentials
    .fromStream(FileInputStream("private-key.json"))
    .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"));
private val options = FirebaseOptions.builder()
    .setCredentials(googleCredentials)
    .build()
private val firebaseApp = FirebaseApp.initializeApp(options)

fun sendPush(clientToken: String) {
    val message: Message = Message.builder()
        .setAndroidConfig(
            AndroidConfig.builder()
                .setPriority(AndroidConfig.Priority.HIGH)
                .build()
        )
        .setNotification(Notification.builder().setTitle("Hello!").build())
        .setToken(clientToken)
        .build()

    try {
        val response = FirebaseMessaging.getInstance(firebaseApp).send(message)
        println("Successfully sent message: $response")
    } catch (e: Exception) {
        println("Failed to send message: $e")
    }
}


fun sendDNDStatusPush(clientToken: String, dnd: Boolean, id: String) {
    val message: Message = Message.builder()
        .setAndroidConfig(
            AndroidConfig.builder()
                .setPriority(AndroidConfig.Priority.HIGH)
                .build()
        )
        .setApnsConfig(
            ApnsConfig.builder()
                .setAps(
                    Aps.builder()
                        .setContentAvailable(true)  // For background notifications
                        .build()
                )
                .putHeader("apns-priority", "10")
                .build()
        )
        .setToken(clientToken)
        .putData("dnd", dnd.toString())
        .putData("id", id)
        .build()

    try {
        val response = FirebaseMessaging.getInstance(firebaseApp).send(message)
        println("Successfully sent message: $response")
    } catch (e: Exception) {
        println("Failed to send message: $e")
    }
}