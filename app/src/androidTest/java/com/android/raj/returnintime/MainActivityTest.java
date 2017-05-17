package com.android.raj.returnintime;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab), isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction textInputEditText = onView(
                withId(R.id.edit_title));
        textInputEditText.perform(scrollTo(), replaceText("Android"), closeSoftKeyboard());

        ViewInteraction textInputEditText2 = onView(
                withId(R.id.edit_type));
        textInputEditText2.perform(scrollTo(), replaceText("Book"), closeSoftKeyboard());

        ViewInteraction textInputEditText3 = onView(
                withId(R.id.edit_return_to));
        textInputEditText3.perform(scrollTo(), replaceText("Raj"), closeSoftKeyboard());

        ViewInteraction textInputEditText4 = onView(
                withId(R.id.edit_checkedout));
        textInputEditText4.perform(scrollTo(), click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction textInputEditText5 = onView(
                withId(R.id.edit_return));
        textInputEditText5.perform(scrollTo(), click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction textInputEditText6 = onView(
                withId(R.id.edit_notify));
        textInputEditText6.perform(scrollTo(), click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton3.perform(scrollTo(), click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.add_button), withText("Add")));
        appCompatButton4.perform(scrollTo(), click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyler_view),
                        withParent(allOf(withId(R.id.recyler_scroll_view),
                                withParent(withId(R.id.fragment))))));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_edit), withContentDescription("Edit"), isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction textInputEditText7 = onView(
                allOf(withId(R.id.edit_return)));
        textInputEditText7.perform(scrollTo(), click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton5.perform(scrollTo(), click());

        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.action_save), withText("SAVE"), withContentDescription("SAVE"), isDisplayed()));
        actionMenuItemView2.perform(click());

        ViewInteraction actionMenuItemView3 = onView(
                allOf(withId(R.id.action_edit), withContentDescription("Edit"), isDisplayed()));
        actionMenuItemView3.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withClassName(is("android.support.v7.widget.AppCompatImageButton")),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withClassName(is("android.support.v7.widget.AppCompatImageButton")),
                        withParent(allOf(withId(R.id.toolbar),
                                withParent(withId(R.id.tool_bar)))),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

    }

}
