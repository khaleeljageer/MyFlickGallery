package com.jskhaleel.myflickgallery;

import android.app.Application;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.View;

import com.jskhaleel.myflickgallery.activities.home.FlickrGalleryActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ApplicationTest {
    @Rule
    public ActivityTestRule<FlickrGalleryActivity> mMainActivityRule = new ActivityTestRule<>(FlickrGalleryActivity.class);

    @Test
    public void rotateViews() {
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.scrollToPosition(7));
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.actionOnItemAtPosition(7, click()));
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.actionOnItemAtPosition(6, click()));
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.actionOnItemAtPosition(6, click()));
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.actionOnItemAtPosition(7, click()));
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.scrollToPosition(12));
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.actionOnItemAtPosition(12, click()));
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.actionOnItemAtPosition(12, click()));

        onView(withId(R.id.ic_sync)).perform(click());
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.scrollToPosition(15));
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.scrollToPosition(25));
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.actionOnItemAtPosition(25, click()));
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.actionOnItemAtPosition(25, click()));
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.scrollToPosition(1));
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.scrollToPosition(14));
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.actionOnItemAtPosition(14, click()));
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.actionOnItemAtPosition(14, click()));

        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.scrollToPosition(1));
        onView(withId(R.id.swipe_refresh_layout))
                .perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(85)));
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.scrollToPosition(5));
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.actionOnItemAtPosition(6, click()));
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.actionOnItemAtPosition(6, click()));
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.scrollToPosition(13));
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.actionOnItemAtPosition(13, click()));
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.actionOnItemAtPosition(13, click()));
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.scrollToPosition(18));
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.actionOnItemAtPosition(20, click()));
        onView(withId(R.id.rv_gallery_list)).perform(RecyclerViewActions.actionOnItemAtPosition(20, click()));
    }

    public static ViewAction withCustomConstraints(final ViewAction action, final Matcher<View> constraints) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return constraints;
            }

            @Override
            public String getDescription() {
                return action.getDescription();
            }

            @Override
            public void perform(UiController uiController, View view) {
                action.perform(uiController, view);
            }
        };
    }
}