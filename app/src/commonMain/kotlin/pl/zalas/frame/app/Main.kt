package pl.zalas.frame.app

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.runBlocking

@Composable
@Preview
fun App() {
    MaterialTheme {
        Image(
            loadImageBitmap(object {}::class.java.getResource("/koala.jpg").openStream()),
            "Main Content",
            modifier = Modifier.fillMaxSize()
        )
    }
}

fun main(): Unit = runBlocking {
    application {
        val state = rememberWindowState(placement = WindowPlacement.Fullscreen)
        Window(onCloseRequest = ::exitApplication, title = "Frame", state = state) {
            App()
        }
    }
}
