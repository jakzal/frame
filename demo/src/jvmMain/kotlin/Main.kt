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
import kotlinx.coroutines.delay
import java.io.InputStream
import kotlin.time.Duration.Companion.seconds

@Composable
@Preview
fun App() {
    var displayedPhoto by remember { mutableStateOf(defaultPhotoStream()) }
    MaterialTheme {
        Column {
            Image(loadImageBitmap(displayedPhoto), "Default photo", modifier = Modifier.fillMaxSize())
        }
        LaunchedEffect(Unit) {
            delay(4.seconds)
            displayedPhoto = object {}::class.java.getResource("/koala-bis.jpg").openStream()
        }
    }
}

private fun defaultPhotoStream(): InputStream =
    object {}::class.java.getResource("/koala.jpg").openStream()

fun main() = application {
    val state = rememberWindowState(placement = WindowPlacement.Fullscreen)
    Window(onCloseRequest = ::exitApplication, title = "Frame", state = state) {
        App()
    }
}
