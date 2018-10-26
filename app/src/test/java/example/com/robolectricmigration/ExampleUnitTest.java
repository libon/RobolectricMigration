package example.com.robolectricmigration;

import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowApplication;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class ExampleUnitTest {
    @Test
    public void deprecatedCodeTest() {
        Intent nextActivity = ShadowApplication.getInstance().getNextStartedActivity();
        assertNull(nextActivity);
    }
}