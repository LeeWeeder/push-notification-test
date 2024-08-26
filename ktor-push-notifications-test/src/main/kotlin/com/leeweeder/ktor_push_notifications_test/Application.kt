package com.leeweeder.ktor_push_notifications_test

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.leeweeder.ktor_push_notifications_test.plugins.configureMonitoring
import com.leeweeder.ktor_push_notifications_test.plugins.configureRouting
import com.leeweeder.ktor_push_notifications_test.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureMonitoring()
    configureRouting()

    val serviceAccountStream = this::class.java.classLoader.getResourceAsStream("service_account_key.json")
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
        .build()

    FirebaseApp.initializeApp(options)
}
