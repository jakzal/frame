package pl.zalas.frame

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

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
}