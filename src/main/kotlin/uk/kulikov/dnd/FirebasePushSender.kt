package uk.kulikov.dnd

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.AndroidConfig
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import java.io.FileInputStream

fun sendPush(clientToken: String) {
    val googleCredentials = GoogleCredentials
        .fromStream(FileInputStream("private-key.json"))
        .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"));
    val options = FirebaseOptions.builder()
        .setCredentials(googleCredentials)
        .build()
    val firebaseApp = FirebaseApp.initializeApp(options)

    val message: Message = Message.builder()
        .setAndroidConfig(
            AndroidConfig.builder()
                .setPriority(AndroidConfig.Priority.HIGH)
                .build()
        )
        .setNotification(Notification.builder().setTitle("Hello!").build())
        .setToken(clientToken)
        .build()

    val response = FirebaseMessaging.getInstance(firebaseApp).send(message)

    println("Successfully sent message: $response")
}
