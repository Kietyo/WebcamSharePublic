import com.soywiz.kmem.arraycopy
import com.soywiz.korge.Korge
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.image
import com.soywiz.korim.bitmap.Bitmap32
import com.soywiz.korim.bitmap.slice
import com.soywiz.korim.color.BGRA
import com.soywiz.korim.color.ColorFormat
import com.soywiz.korim.color.ColorFormat24
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGB
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.color.RgbaArray
import com.soywiz.korim.color.decode
import com.soywiz.korim.color.toRGBA
import com.soywiz.korim.format.PNG
import com.soywiz.korim.format.QOI
import com.soywiz.korio.async.launch
import com.soywiz.korma.geom.degrees
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.util.Identity.decode
import io.ktor.websocket.*
import java.awt.image.BufferedImage
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

@OptIn(ExperimentalTime::class)
suspend fun main() = Korge(width = 1920, height = 1080, bgcolor = Colors["#2b2b2b"]) {

    val client = HttpClient {
        install(WebSockets)
    }

    val minDegrees = (-16).degrees
    val maxDegrees = (+16).degrees

    //    val image = image(resourcesVfs["korge.png"].readBitmap()) {
    //        rotation = maxDegrees
    //        anchor(.5, .5)
    //        scale(.8)
    //        position(256, 256)
    //    }

    //    val webcams = Webcam.getWebcams()
    //    println("webcams: $webcams")
    //    println(webcams[1].name)

    val bitmap = Bitmap32(1920, 1080)
    val image = image(bitmap)

    client.webSocket(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/camera") {
        println("Connected to websocket!")
        val incomingJob = launch {
            try {
                for (message in incoming) {
                    println("Received frame!")
                    when (message) {
                        is Frame.Binary -> {
                            val binary = message.readBytes()
                            val convertedBitmap = QOI.decode(binary)
                            val copyBitmapTime = measureTime {
                                convertedBitmap.copy(0, 0, bitmap, 0, 0, 1920, 1080)
                            }
                            // image.bitmap = convertedBitmap.slice()
                            bitmap.contentVersion++
                            println("Got binary! length: ${binary.size}")
                            println("copyBitmapTime: $copyBitmapTime")
                        }
                        is Frame.Text -> {
                            val text = message.readText()
                            println("Got text: $text")
                        }
                        is Frame.Close -> TODO()
                        is Frame.Ping -> TODO()
                        is Frame.Pong -> TODO()
                        else -> TODO()
                    }
                }
            } catch (e: Exception) {
                println("Error while receiving messages: $e")
            }
        }
        incomingJob.join()

        //        while(true) {
        //            val othersMessage = incoming.receive() as? Frame.Text ?: continue
        //            println(othersMessage.readText())
        //            val myMessage = readLine()
        //            if(myMessage != null) {
        //                send(myMessage)
        //            }
        //        }
    }

    //    addUpdater {
    //        val webcamImage = measureTimedValue {
    //            webcam.image
    //        }
    //
    //        val toAwtNativeImageTime = measureTimedValue {
    //            webcamImage.value.toAwtNativeImage()
    //        }
    //
    //        val updateBitmapTime = measureTime {
    //            toAwtNativeImageTime.value.copyUnchecked(0, 0, bitmap, 0, 0, 1920, 1080)
    //            bitmap.contentVersion++
    //        }
    //        println("webcamImage: ${webcamImage.duration}")
    //        println("toAwtNativeImageTime: ${toAwtNativeImageTime.duration}")
    //        println("updateBitmapTime: $updateBitmapTime")
    //    }

}