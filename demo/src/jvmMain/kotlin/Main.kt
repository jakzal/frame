import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.net.URL
import kotlin.time.Duration.Companion.seconds

@Composable
@Preview
fun App(command: MutableState<Command>) {
    var lastCommand by remember { command }
    MaterialTheme {
        Column {
            Media(lastCommand)
        }
    }
}

@Composable
fun Media(command: Command) {
    when (command) {
        is ShowPhoto -> Image(loadImageBitmap(command.url.openStream()), "Photo", modifier = Modifier.fillMaxSize())
    }
}

private fun resource(name: String): URL = object {}::class.java.getResource("$name")

sealed interface Command
data class ShowPhoto(val url: URL) : Command

fun main(): Unit = runBlocking {
    val command: MutableState<Command> = mutableStateOf(ShowPhoto(resource("/koala.jpg")))
    val dispatcher = actor<Command> {
        for (msg in channel) {
            command.value = msg
        }
    }
    async {
        application {
            val state = rememberWindowState(placement = WindowPlacement.Fullscreen)
            Window(onCloseRequest = ::exitApplication, title = "Frame", state = state) {
                App(command)
            }
        }
    }
    async {
        delay(4.seconds)
        dispatcher.send(ShowPhoto(resource("/koala-bis.jpg")))
    }
}
