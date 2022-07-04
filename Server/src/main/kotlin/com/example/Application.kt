package com.example

import io.ktor.server.engine.*
import io.ktor.server.cio.*
import com.example.plugins.*
import com.github.sarxos.webcam.Webcam
import com.github.sarxos.webcam.WebcamResolution
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import javax.imageio.ImageIO

fun main() {


    embeddedServer(CIO, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureSockets()
    }.start(wait = true)
}
