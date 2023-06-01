package pl.zalas.frame

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
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

    @Test
    fun `learn how to use channels (rendezvous)`() = runTest {
        var messages = mutableListOf<String>();
        val channel = Channel<String>()
        launch {
            channel.send("A1")
            channel.send("A2")
            messages.add("A done")
        }
        launch {
            channel.send("B1")
            messages.add("B done")
        }
        launch {
            repeat(3) {
                val x = channel.receive()
                messages.add(x)
            }
        }
        advanceTimeBy(1.seconds)

        assertEquals(listOf("A1", "B1", "A done", "B done", "A2"), messages)
    }

    @Test
    fun `learn how to use channels (unlimited)`() = runTest {
        var messages = mutableListOf<String>();
        val channel = Channel<String>(UNLIMITED)
        launch {
            channel.send("A1")
            channel.send("A2")
            messages.add("A done")
        }
        launch {
            channel.send("B1")
            messages.add("B done")
        }
        launch {
            repeat(3) {
                val x = channel.receive()
                messages.add(x)
            }
        }
        advanceTimeBy(1.seconds)

        assertEquals(listOf("A done", "B done", "A1", "A2", "B1"), messages)
    }

    @Test
    fun `learn how to use channels (buffered 2)`() = runTest {
        var messages = mutableListOf<String>();
        val channel = Channel<String>(2)
        launch {
            channel.send("A1")
            channel.send("A2")
            messages.add("A done")
        }
        launch {
            channel.send("B1")
            messages.add("B done")
        }
        launch {
            repeat(3) {
                val x = channel.receive()
                messages.add(x)
            }
        }
        advanceTimeBy(1.seconds)

        assertEquals(listOf("A done", "A1", "A2", "B1", "B done"), messages)
    }

    @Test
    fun `learn how to use channels (conflated)`() = runTest {
        var messages = mutableListOf<String>();
        val channel = Channel<String>(CONFLATED)
        launch {
            channel.send("A1")
            channel.send("A2")
            messages.add("A done")
        }
        launch {
            channel.send("B1")
            messages.add("B done")
        }
        launch {
            val x = channel.receive()
            messages.add(x)
        }
        advanceTimeBy(1.seconds)

        assertEquals(listOf("A done", "B done", "B1"), messages)
    }

    @Test
    fun `learn how to use channels (multiple receivers)`() = runTest {
        var messages = mutableListOf<String>();
        val channel = Channel<String>(UNLIMITED)
        launch {
            channel.send("A1")
            channel.send("A2")
            messages.add("A done")
        }
        launch {
            channel.send("B1")
            messages.add("B done")
        }
        val receiver1 = launch {
            repeat(3) {
                val x = channel.receive()
                messages.add("R1$x")
                delay(1.seconds)
            }
        }
        val receiver2 = launch {
            repeat(3) {
                val x = channel.receive()
                messages.add("R2$x")
                delay(1.seconds)
            }
        }
        val receiver3 = launch {
            repeat(3) {
                val x = channel.receive()
                messages.add("R3$x")
                delay(1.seconds)
            }
        }
        advanceTimeBy(10.seconds)
        receiver1.cancelAndJoin()
        receiver2.cancelAndJoin()
        receiver3.cancelAndJoin()

        assertEquals(listOf("A done", "B done", "R1A1", "R2A2", "R3B1"), messages)
    }
}