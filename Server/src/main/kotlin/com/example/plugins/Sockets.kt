package com.example.plugins

import com.github.sarxos.webcam.Webcam
import com.github.sarxos.webcam.WebcamResolution
import com.soywiz.korim.awt.toAwtNativeImage
import com.soywiz.korim.awt.toBMP32
import com.soywiz.korim.format.PNG
import com.soywiz.korim.format.QOI
import com.soywiz.korim.format.writeBitmap
import com.soywiz.korio.file.std.resourcesVfs
import java.time.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import java.io.File
import javax.imageio.ImageIO

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    val nonStandardResolutions = arrayOf(
        WebcamResolution.FHD.size,
    )

    val webcam = Webcam.getWebcamByName("USB Video 1")
    println("kiet here opening webcam")
    println("webcam: $webcam")
    webcam.setCustomViewSizes(*nonStandardResolutions)
    webcam.viewSize = WebcamResolution.FHD.size
    while (true) {
        try {
            webcam.open(true)
        } catch (e: Exception) {
            println("Failed to open webcam: $e")
        } finally {
            println("Successfully opened webcam: $webcam")
            break
        }
    }

    //    ImageIO.write(webcam.getImage(), "PNG", File("hello-world.png"));

    routing {
        webSocket("/camera") { // websocketSession
            println("Connected to a client!")
            //            webcam.image.toBMP32()
            var frameCount = 0L
            while (true) {
                outgoing.send(Frame.Binary(true, QOI.encode(webcam.image.toAwtNativeImage())))
                println("Sent frame ${frameCount++}")
                delay(100)
            }
            //            for (frame in incoming) {
            //                when (frame) {
            //                    is Frame.Text -> {
            //                        val text = frame.readText()
            //                        outgoing.send(Frame.Text("YOU SAID: $text"))
            //                        if (text.equals("bye", ignoreCase = true)) {
            //                            close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
            //                        }
            //                    }
            //                    else -> {
            //
            //                    }
            //                }
            //            }
        }
    }
}
