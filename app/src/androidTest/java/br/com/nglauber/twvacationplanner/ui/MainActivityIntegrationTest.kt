package br.com.nglauber.twvacationplanner.ui


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import br.com.nglauber.twvacationplanner.R
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityIntegrationTest {

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    private var activity: MainActivity? = null

    @Before
    fun setActivity() {
        activity = activityTestRule.activity
    }

    @Test
    fun realExampleWithRio() {
        val cityToSearch = "Rio de Janeiro"
        val weatherToSelect = listOf("Clear", "Hot", "Partly cloudy", "Fair")
        val expectedResult = listOf(
            "22" to ("10/11/2019" to "01/12/2019")
        )
        realExampleWithExpectedResult(cityToSearch, weatherToSelect, expectedResult)
    }

    @Test
    fun realExampleWithPortoAlegre() {
        val cityToSearch = "Porto Alegre"
        val weatherToSelect = listOf("Clear", "Partly cloudy", "Cold")
        val expectedResult = listOf(
            "16" to ("02/04/2019" to "17/04/2019"),
            "18" to ("02/04/2019" to "26/05/2019"),
            "21" to ("20/06/2019" to "10/07/2019")
        )
        realExampleWithExpectedResult(cityToSearch, weatherToSelect, expectedResult)
    }

    @Test
    fun realExampleReturnsEmpty() {
        val cityToSearch = "Recife"
        val weatherToSelect = listOf("Clear")
        realExampleWithExpectedResult(cityToSearch, weatherToSelect, null)
    }

    private fun realExampleWithExpectedResult(
        cityToSearch: String,
        weatherToSelect: List<String>,
        expectedResult: List<Pair<String, Pair<String, String>>>?
    ) {
        // Type text "Porto" on City field
        onView(withId(br.com.nglauber.twvacationplanner.R.id.actvCity))
            .perform(typeText(cityToSearch), closeSoftKeyboard())

        // Check if "Porto Alegre" is displayed on the pop-up list
        onView(withText(cityToSearch))
            .inRoot(withDecorView(not(`is`(activity?.window?.decorView))))
            .check(matches(isDisplayed()))

        // Click on "Porto Alegre" in the list.
        onView(withText(cityToSearch))
            .inRoot(withDecorView(not(`is`(activity?.window?.decorView))))
            .perform(click())

        // By clicking on the auto complete items, the text should be filled in.
        onView(withId(R.id.actvCity))
            .check(matches(withText(cityToSearch)))

        // Typing each weather type
        weatherToSelect.forEach { wt ->
            // Typing weather type
            onView(withId(R.id.actvWeatherType))
                .perform(typeText(wt))

            // Check if if appears on the suggestions popup
            onView(withText(wt))
                .inRoot(withDecorView(not(`is`(activity?.window?.decorView))))
                .check(matches(isDisplayed()))

            // Tap on a suggestion.
            onView(withText(wt))
                .inRoot(withDecorView(not(`is`(activity?.window?.decorView))))
                .perform(click())

            // When it is selected, the AutoCompleteTextView is cleared.
            onView(withId(R.id.actvWeatherType))
                .check(matches(withText(isEmptyString())))

            // Check if chip was added to the group
            onView(withId(R.id.cgWeatherType))
                .check(matches(withChild(withText(wt))))
        }
        // Close the keyboard
        onView(withId(R.id.actvWeatherType))
            .perform(closeSoftKeyboard())

        // Search!!!
        onView(withId(R.id.btnFind)).perform(click())

        // Check results...
        if (expectedResult != null) {
            expectedResult.forEach { expected ->
                onView(withId(R.id.rvResults))
                    .check(matches(hasDescendant(withText(expected.second.first)))) // Start
                    .check(matches(hasDescendant(withText(expected.second.second)))) // End
                    .check(matches(hasDescendant(withText(expected.first)))) // number of days
            }
        } else {
            onView(withId(R.id.vwEmpty))
                .check(matches(isDisplayed()))
        }
    }
}
