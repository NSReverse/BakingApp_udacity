package net.nsreverse.bakingapp;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.nsreverse.bakingapp.data.utils.ApplicationInstance;
import net.nsreverse.bakingapp.ui.MainActivity;
import net.nsreverse.bakingapp.ui.RecipeDetailActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Robert on 6/9/2017.
 */
@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityUnitTest {
    @Rule public ActivityTestRule<MainActivity> mainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);
    @Rule public ActivityTestRule<RecipeDetailActivity> recipeDetailTestRule =
            new ActivityTestRule<>(RecipeDetailActivity.class);

    @Test public void testRecipeDetailActivityRecyclerViewLoading() {
        try {
            Thread.sleep(3500);
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        onView(withId(R.id.recyclerview_main))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1,
                        clickChildViewWithId(R.id.button_view)));

        onView(allOf(withId(R.id.recyclerview_recipe_detail), isDisplayed()));
    }

    @Test public void testRecipeDetailActivityRecyclerViewClick() {
        try {
            Thread.sleep(3500);
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        onView(withId(R.id.recyclerview_main))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1,
                        clickChildViewWithId(R.id.button_view)));

        onView(withId(R.id.recyclerview_recipe_detail))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(allOf(withId(R.id.exoplayer_view), isDisplayed()));
    }

    // https://stackoverflow.com/questions/28476507/using-espresso-to-click-view-inside-recyclerview-item
    private static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }
}
