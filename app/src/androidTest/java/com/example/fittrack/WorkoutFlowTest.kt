package com.example.fittrack

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WorkoutFlowTest {
    @get:Rule val rule = ActivityScenarioRule(MainActivity::class.java)

    @Test fun userCanAddWorkoutAndSeeItInList() {
        val title = "Espresso Run"
        onView(withId(R.id.nav_activity)).perform(click())
        onView(withId(R.id.addRecord)).perform(click())
        onView(withId(R.id.inputTitle)).perform(replaceText(title))
        onView(withId(R.id.inputType)).perform(replaceText("Running"))
        onView(withId(R.id.inputDuration)).perform(replaceText("30"))
        onView(withId(R.id.inputCalories)).perform(replaceText("250"), closeSoftKeyboard())
        onView(withText("Save")).perform(click())
        onView(withText(title)).check(matches(isDisplayed()))
    }
}
