package pl.zalas.frame

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import kotlin.coroutines.coroutineContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

class LearnEventBusTest {
    @Test
    fun `it publishes events to subscribers`() = runTest(timeout = 1.seconds) {
        val eventBus = EventBus()

        val firstSubscriber = async {
            val events = mutableListOf<Event>()
            eventBus.subscribe<Event> { event ->
                events.add(event)
            }
            events.toList()
        }
        val secondSubscriber = async {
            val events = mutableListOf<TemperatureRead>()
            eventBus.subscribe<TemperatureRead> { event ->
                events.add(event)
            }
            events.toList()
        }

        delay(1)

        eventBus.publish(TemperatureRead(22))
        eventBus.publish(TemperatureRead(19))
        eventBus.publish(LightTurnedOn("Kitchen"))
        eventBus.publish(TemperatureRead(20))
        eventBus.publish(EventBus.SystemShuttingDown)

        assertEquals(
            listOf(
                TemperatureRead(22),
                TemperatureRead(19),
                LightTurnedOn("Kitchen"),
                TemperatureRead(20)
            ),
            firstSubscriber.await()
        )
        assertEquals(
            listOf(
                TemperatureRead(22),
                TemperatureRead(19),
                TemperatureRead(20)
            ),
            secondSubscriber.await()
        )
    }

    @Test
    fun `it publishes events to subscribers with state`() = runTest(timeout = 1.seconds) {
        val eventBus = EventBus()

        val subscriber = async {
            eventBus.subscribe<TemperatureState, TemperatureRead>(TemperatureState(0)) { state, event ->
                TemperatureState(event.degrees, state.previousReads + listOf(state.lastRead))
            }
        }

        delay(1)

        eventBus.publish(TemperatureRead(22))
        eventBus.publish(TemperatureRead(19))
        eventBus.publish(LightTurnedOn("Kitchen"))
        eventBus.publish(TemperatureRead(20))
        eventBus.publish(EventBus.SystemShuttingDown)

        assertEquals(TemperatureState(20, listOf(0, 22, 19)), subscriber.await())
    }

    class EventBus {
        object SystemShuttingDown

        private val _events = MutableSharedFlow<Any>()

        val events = _events.asSharedFlow()

        suspend fun publish(event: Any) {
            _events.emit(event)
        }

        // Inspired by https://dev.to/mohitrajput987/event-bus-pattern-in-android-using-kotlin-flows-la
        suspend inline fun <reified T> subscribe(crossinline subscriber: suspend (T) -> Unit) {
            events.takeWhile { event -> event !is SystemShuttingDown }
                .filterIsInstance<T>()
                .collect { event ->
                    coroutineContext.ensureActive()
                    subscriber(event)
                }
        }

        suspend inline fun <reified STATE, reified EVENT> subscribe(
            state: STATE,
            crossinline subscriber: suspend (STATE, EVENT) -> STATE
        ): STATE = events.takeWhile { event -> event !is SystemShuttingDown }
            .filterIsInstance<EVENT>()
            .fold(state) { state, event ->
                coroutineContext.ensureActive()
                subscriber(state, event)
            }
    }

    interface Event
    data class TemperatureRead(val degrees: Int) : Event
    data class LightTurnedOn(val name: String) : Event

    data class TemperatureState(val lastRead: Int = 0, val previousReads: List<Int> = emptyList())
}