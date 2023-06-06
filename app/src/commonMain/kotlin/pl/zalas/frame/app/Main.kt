package pl.zalas.frame.app

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.loadImageBitmap

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
