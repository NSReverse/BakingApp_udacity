package net.nsreverse.bakingapp;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.nsreverse.bakingapp.ui.MainActivity;
import net.nsreverse.bakingapp.ui.RecipeDetailActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This class tests UI of the MainActivity.
 *
 * @author Robert
 * Created on 6/9/2017.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityUnitTest {
    @Rule public ActivityTestRule<MainActivity> mainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test public void testMainActivityRecyclerViewLoading() {
        try {
            Thread.sleep(3500);
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        onView(allOf(withId(R.id.recyclerview_main), isDisplayed()));

        RecyclerView recyclerView = (RecyclerView)mainActivityTestRule.getActivity()
                .findViewById(R.id.recyclerview_main);
        int size = recyclerView.getAdapter().getItemCount();

        assertNotNull(recyclerView);
        assertNotEquals(0, size);
    }

    @Test public void testMainActivityToDetailActivity() {
        onView(withId(R.id.recyclerview_main))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1,
                        clickChildViewWithId(R.id.button_view)));

        onView(allOf(withId(R.id.recyclerview_recipe_detail), isDisplayed()));
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
