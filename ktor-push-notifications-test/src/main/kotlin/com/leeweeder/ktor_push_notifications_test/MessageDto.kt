package com.leeweeder.ktor_push_notifications_test

import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val to: String,
    val imageUrl: String
)

fun MessageDto.toMessage(): Message {
    return Message.builder()
        .setNotification(
            Notification.builder()
                .setTitle("New message")
                .setBody(imageUrl)
                .setImage(imageUrl)
                .build()
        )
        .setToken(to)
        .build()
}