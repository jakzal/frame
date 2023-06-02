package pl.zalas.frame

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

class LearnFlowsTest {
    @Test
    fun `learn how to define a Flow`() = runTest {
        val flow = flow {
            emit("A1")
            delay(100)
            emit("A2")
            delay(100)
            emit("A3")
            delay(100)
        }

        val result = flow.map { it.substring(1) }.toList()

        assertEquals(listOf("1", "2", "3"), result)
    }

    @Test
    fun `learn how to define a Shared Flow`() = runTest(timeout = 1.seconds) {
        val eventBus = EventBus()

        val firstSubscriberEvents = async {
            // SharedFlow never completes. That's why we need a flow-truncating operation like `take()`.
            eventBus.events.take(3).toList()
        }
        val secondSubscriberEvents = async {
            eventBus.events.take(3).toList()
        }

        // Delay is required to give subscribers a chance to catch the first event.
        // alternatively, MutableSharedFlow could be configured with `replay = 5` to notify subscribers about past events.
        delay(1)

        eventBus.publish(TemperatureRead(22))
        eventBus.publish(TemperatureRead(19))
        eventBus.publish(LightTurnedOn("Kitchen"))
        eventBus.publish(TemperatureRead(20))

        val expectedEvents = listOf(
            TemperatureRead(22),
            TemperatureRead(19),
            LightTurnedOn("Kitchen")
        )

        assertEquals(expectedEvents, firstSubscriberEvents.await())
        assertEquals(expectedEvents, secondSubscriberEvents.await())
    }

    class EventBus() {
        // mutable flow has the emit() capability
        private val _events = MutableSharedFlow<Event>()

        // expose as read-only flow
        val events = _events.asSharedFlow()

        suspend fun publish(event: Event) {
            // suspends until all subscribers receive it:
            _events.emit(event)
        }
    }

    interface Event
    data class TemperatureRead(val degrees: Int) : Event
    data class LightTurnedOn(val name: String) : Event
}