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
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.io.InputStream
import kotlin.time.Duration.Companion.seconds

@Composable
@Preview
fun App(currentPhoto: MutableState<InputStream>) {
    var displayedPhoto by remember { currentPhoto }
    MaterialTheme {
        Column {
            Image(loadImageBitmap(displayedPhoto), "Photo", modifier = Modifier.fillMaxSize())
        }
    }
}

private fun resourceStream(name: String): InputStream =
    object {}::class.java.getResource("/$name")?.openStream() ?: InputStream.nullInputStream()

fun main(): Unit = runBlocking {
    val currentPhoto = mutableStateOf(resourceStream("koala.jpg"))
    async {
        application {
            val state = rememberWindowState(placement = WindowPlacement.Fullscreen)
            Window(onCloseRequest = ::exitApplication, title = "Frame", state = state) {
                App(currentPhoto)
            }
        }
    }
    async {
        delay(4.seconds)
        currentPhoto.value = resourceStream("koala-bis.jpg")
    }
}
