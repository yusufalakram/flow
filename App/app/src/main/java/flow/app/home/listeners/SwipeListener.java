package flow.app.home.listeners;

import android.app.Activity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by Ben Amor on 28/02/2018.
 */
public class SwipeListener extends GestureDetector.SimpleOnGestureListener {

    Activity source;

    public SwipeListener(Activity source) {
        this.source = source;
    }
    private static final String DEBUG_TAG = "Gestures";

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        //Check they've swiped up from near the bottom of the screen.
        Log.d("start:", event1.toString());
        Log.d("end:", event2.toString());
        return true;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

}
