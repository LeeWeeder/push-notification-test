package com.leeweeder.ktor_push_notifications_test.plugins

import com.google.firebase.messaging.FirebaseMessaging
import com.leeweeder.ktor_push_notifications_test.MessageDto
import com.leeweeder.ktor_push_notifications_test.toMessage
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Application.configureRouting() {
    routing {
        static("/uploads") {
            files("uploads") // Point to the directory outside src
        }

        post("/send") {
            val formContent = call.receiveMultipart()
            val parts = mutableMapOf<String, String>()

            var imagePath = ""

            formContent.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        parts[part.name ?: ""] = part.value
                    }

                    is PartData.FileItem -> {
                        val name = part.originalFileName!!
                        val file = File("uploads/$name")
                        imagePath = file.path

                        // use InputStream from part to save file
                        part.streamProvider().use { its ->
                            // copy the stream to the file with buffering
                            file.outputStream().buffered().use {
                                // note that this is blocking
                                its.copyTo(it)
                            }
                        }
                    }

                    else -> {}
                }
                part.dispose()
            }

            val file = File(imagePath)
            if (file.exists()) {
                val params = MessageDto(
                    to = parts["to"] ?: "",
                    imageUrl = "http://192.168.8.117:8080/$imagePath"
                )

                FirebaseMessaging.getInstance().send(params.toMessage())

                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
