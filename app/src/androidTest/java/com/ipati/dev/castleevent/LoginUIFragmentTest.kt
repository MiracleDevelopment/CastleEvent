package com.ipati.dev.castleevent

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewAction
import android.support.test.espresso.ViewAssertion
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import android.support.test.espresso.matcher.ViewMatchers
import java.util.*

@RunWith(AndroidJUnit4::class)
class LoginUIFragmentTest {
    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule(LoginActivity::class.java)

    @Before
    fun init() {
        activityTestRule.activity.supportFragmentManager.beginTransaction()
    }

    @Test
    @Throws(Exception::class)
    fun checkClickableUI() {
        when (Locale.getDefault().language) {
            "en" -> {
                onView(ViewMatchers.withText("Login")).check(matches(ViewMatchers.isDisplayed()))
            }
        }
    }

}