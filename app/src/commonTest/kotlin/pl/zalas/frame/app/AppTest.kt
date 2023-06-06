package pl.zalas.frame.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import kotlin.test.Test

class AppTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `it displays the default image`() {
        composeTestRule.setContent {
            App()
        }

        composeTestRule.onNode(hasContentDescription("Main Content")).assertIsDisplayed()
    }
}