import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

@Composable
@Preview
fun App() {
    MaterialTheme {
        Column {
            Image(defaultPhoto(), "Default photo", modifier = Modifier.fillMaxSize())
        }
    }
}

private fun ColumnScope.defaultPhoto(): ImageBitmap =
    loadImageBitmap(this::class.java.getResource("/koala.jpg").openStream())

fun main() = application {
    val state = rememberWindowState(placement = WindowPlacement.Fullscreen)
    Window(onCloseRequest = ::exitApplication, title = "Frame", state = state) {
        App()
    }
}
