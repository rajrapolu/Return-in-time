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
import static android.support.test.espresso.action.ViewActions.longClick;
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
public class ApplicationTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void applicationTest() {
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
        textInputEditText3.perform(scrollTo(), replaceText("Name"), closeSoftKeyboard());

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
                allOf(withId(R.id.edit_notify)));
        textInputEditText7.perform(scrollTo(), click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton5.perform(scrollTo(), click());

        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.action_save), withText("Save"), withContentDescription("Save"), isDisplayed()));
        actionMenuItemView2.perform(click());

        ViewInteraction actionMenuItemView3 = onView(
                allOf(withId(R.id.action_edit), withContentDescription("Edit"), isDisplayed()));
        actionMenuItemView3.perform(click());

        ViewInteraction textInputEditText8 = onView(
                withId(R.id.edit_checkedout));
        textInputEditText8.perform(scrollTo(), click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton6.perform(scrollTo(), click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withClassName(is("android.support.v7.widget.AppCompatImageButton")),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction actionMenuItemView4 = onView(
                allOf(withId(R.id.action_delete), withContentDescription("Delete"), isDisplayed()));
        actionMenuItemView4.perform(click());

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(android.R.id.button1), withText("DELETE")));
        appCompatButton7.perform(scrollTo(), click());

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.fab), isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction textInputEditText9 = onView(
                withId(R.id.edit_title));
        textInputEditText9.perform(scrollTo(), replaceText("Go for Swimming"), closeSoftKeyboard());

        ViewInteraction textInputEditText10 = onView(
                withId(R.id.edit_type));
        textInputEditText10.perform(scrollTo(), replaceText("Swimming"), closeSoftKeyboard());

        ViewInteraction textInputEditText11 = onView(
                withId(R.id.edit_return_to));
        textInputEditText11.perform(scrollTo(), replaceText("Name"), closeSoftKeyboard());

        ViewInteraction textInputEditText12 = onView(
                withId(R.id.edit_checkedout));
        textInputEditText12.perform(scrollTo(), click());

        ViewInteraction appCompatButton8 = onView(
                allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton8.perform(scrollTo(), click());

        ViewInteraction textInputEditText13 = onView(
                withId(R.id.edit_return));
        textInputEditText13.perform(scrollTo(), click());

        ViewInteraction appCompatButton9 = onView(
                allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton9.perform(scrollTo(), click());

        ViewInteraction textInputEditText14 = onView(
                withId(R.id.edit_notify));
        textInputEditText14.perform(scrollTo(), click());

        ViewInteraction appCompatButton10 = onView(
                allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton10.perform(scrollTo(), click());

        ViewInteraction appCompatButton11 = onView(
                allOf(withId(R.id.add_button), withText("Add")));
        appCompatButton11.perform(scrollTo(), click());

        ViewInteraction floatingActionButton3 = onView(
                allOf(withId(R.id.fab), isDisplayed()));
        floatingActionButton3.perform(click());

        ViewInteraction textInputEditText15 = onView(
                withId(R.id.edit_title));
        textInputEditText15.perform(scrollTo(), replaceText("Start to airport"), closeSoftKeyboard());

        ViewInteraction textInputEditText16 = onView(
                withId(R.id.edit_type));
        textInputEditText16.perform(scrollTo(), replaceText("Travel"), closeSoftKeyboard());

        ViewInteraction textInputEditText17 = onView(
                withId(R.id.edit_return_to));
        textInputEditText17.perform(scrollTo(), replaceText("Co-Passenger"), closeSoftKeyboard());

        ViewInteraction textInputEditText18 = onView(
                withId(R.id.edit_checkedout));
        textInputEditText18.perform(scrollTo(), click());

        ViewInteraction appCompatButton12 = onView(
                allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton12.perform(scrollTo(), click());

        ViewInteraction textInputEditText19 = onView(
                withId(R.id.edit_checkedout));
        textInputEditText19.perform(scrollTo(), click());

        ViewInteraction appCompatButton13 = onView(
                allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton13.perform(scrollTo(), click());

        ViewInteraction textInputEditText20 = onView(
                withId(R.id.edit_return));
        textInputEditText20.perform(scrollTo(), click());

        ViewInteraction appCompatButton14 = onView(
                allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton14.perform(scrollTo(), click());

        ViewInteraction textInputEditText21 = onView(
                withId(R.id.edit_notify));
        textInputEditText21.perform(scrollTo(), click());

        ViewInteraction appCompatButton15 = onView(
                allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton15.perform(scrollTo(), click());

        ViewInteraction appCompatButton16 = onView(
                allOf(withId(R.id.add_button), withText("Add")));
        appCompatButton16.perform(scrollTo(), click());

    }

}
