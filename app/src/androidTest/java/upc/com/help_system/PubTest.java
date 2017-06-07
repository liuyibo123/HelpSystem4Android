package upc.com.help_system;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.upc.help_system.view.activity.PubActivity;

import org.junit.Rule;
import org.junit.runner.RunWith;

/**
 * Created by Administrator on 2017/5/25.
 */
@RunWith(AndroidJUnit4.class)
public class PubTest {
    @Rule
    public ActivityTestRule<PubActivity> mActivityRule = new ActivityTestRule<>(
            PubActivity.class);

    public void testPub() {

    }
}
