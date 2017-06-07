package com.upc.help_system.view.activity;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.upc.help_system.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PubActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void pubActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html


        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        withParent(allOf(withId(R.id.coordinator),
                                withParent(withId(R.id.drawer_layout)))),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction autoCompleteTextView = onView(
                allOf(withId(R.id.express_company),
                        withParent(withId(R.id.linearLayout10))));
        autoCompleteTextView.perform(scrollTo(), click());

        ViewInteraction autoCompleteTextView2 = onView(
                allOf(withId(R.id.express_company),
                        withParent(withId(R.id.linearLayout10))));
        autoCompleteTextView2.perform(scrollTo(), replaceText("圆通"), closeSoftKeyboard());


        ViewInteraction editText = onView(
                allOf(withId(R.id.take_number),
                        withParent(withId(R.id.linearLayout9))));
        editText.perform(scrollTo(), replaceText("123"), closeSoftKeyboard());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.name),
                        withParent(withId(R.id.linearLayout8))));
        editText2.perform(scrollTo(), replaceText("刘宜博"), closeSoftKeyboard());

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.usr_name),
                        withParent(withId(R.id.linearLayout7))));
        editText3.perform(scrollTo(), replaceText("17854227898"), closeSoftKeyboard());

        ViewInteraction editText4 = onView(
                allOf(withId(R.id.destination_to),
                        withParent(withId(R.id.linearLayout3))));
        editText4.perform(scrollTo(), replaceText("北门"), closeSoftKeyboard());

        ViewInteraction button = onView(
                allOf(withId(R.id.btn_makesure), withText("确定"),
                        withParent(withId(R.id.linearLayout))));
        button.perform(scrollTo(), click());


    }

}
