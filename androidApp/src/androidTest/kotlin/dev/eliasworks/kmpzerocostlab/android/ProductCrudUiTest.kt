package dev.eliasworks.kmpzerocostlab.android

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductCrudUiTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun createEditDeleteProductThroughNativeUi() {
        composeRule.onNodeWithTag("qa-product-count")
            .assertTextContains("0 products")

        composeRule.onNodeWithTag("qa-add-product").performClick()
        composeRule.onNodeWithTag("qa-name-input").performTextReplacement("QA Widget")
        composeRule.onNodeWithTag("qa-quantity-input").performTextReplacement("2")
        composeRule.onNodeWithTag("qa-price-input").performTextReplacement("450")
        composeRule.onNodeWithTag("qa-save-product").performClick()

        waitForText("QA Widget")
        composeRule.onNodeWithText("QA Widget").assertIsDisplayed()
        composeRule.onNodeWithText("Quantity 2 · $4.50").assertIsDisplayed()
        composeRule.onNodeWithTag("qa-product-count")
            .assertTextContains("1 products")

        composeRule.onNodeWithTag("qa-edit-product").performClick()
        composeRule.onNodeWithTag("qa-name-input")
            .performTextReplacement("QA Widget Updated")
        composeRule.onNodeWithTag("qa-quantity-input").performTextReplacement("5")
        composeRule.onNodeWithTag("qa-price-input").performTextReplacement("900")
        composeRule.onNodeWithTag("qa-save-product").performClick()

        waitForText("QA Widget Updated")
        composeRule.onNodeWithText("QA Widget Updated").assertIsDisplayed()
        composeRule.onNodeWithText("Quantity 5 · $9.00").assertIsDisplayed()

        composeRule.onNodeWithTag("qa-delete-product").performClick()
        waitForTextToDisappear("QA Widget Updated")

        composeRule.onNodeWithText("QA Widget Updated").assertDoesNotExist()
        composeRule.onNodeWithTag("qa-product-count")
            .assertTextContains("0 products")
    }

    private fun waitForText(text: String) {
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodesWithText(text)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    private fun waitForTextToDisappear(text: String) {
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodesWithText(text)
                .fetchSemanticsNodes()
                .isEmpty()
        }
    }
}
