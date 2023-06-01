package pl.zalas.frame

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

class LearnCoroutinesTest {
    @Test
    fun `learn how to launch a coroutine`() = runTest {
        var message = "";

        launch {
            delay(1000L)
            message += " World!"
        }
        message += "Hello"

        advanceTimeBy(2.seconds)

        assertEquals("Hello World!", message)
    }
}