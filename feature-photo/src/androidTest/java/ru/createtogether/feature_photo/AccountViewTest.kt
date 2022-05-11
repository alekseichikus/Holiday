package ru.createtogether.feature_photo

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.createtogether.common.helpers.extension.isGone
import ru.createtogether.common.helpers.extension.isVisible
import ru.createtogether.feature_photo_utils.PhotoModel

@RunWith(AndroidJUnit4::class)
class AccountViewTest {
    @Rule
    @JvmField
    val activityRule = ActivityTestRule(MockActivity::class.java, true, false)

    @Before
    fun setup() {
        MockActivity.photoModel = PhotoModel(0, "", false)
    }

    @Test
    fun testCheckSelectedStyle() {
        restartActivity()

        onView(withId(R.id.rootPhotoSmallView)).perform(click())
        onView(withId(R.id.llSelectedContainer)).isVisible()
    }

    @Test
    fun testCheckUnSelectedStyle() {
        restartActivity()

        onView(withId(R.id.llSelectedContainer)).isGone()
    }

    private fun restartActivity() {
        if (activityRule.activity != null) {
            activityRule.finishActivity()
        }
        activityRule.launchActivity(Intent())
    }
}
