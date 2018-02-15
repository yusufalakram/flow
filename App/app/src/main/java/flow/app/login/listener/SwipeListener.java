package flow.app.login.listener;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import flow.app.R;

/**
 * Created by Ben Amor on 11/02/2018.
 * Listens for the swipe up then adjusts page accordingly
 */
@TargetApi(19)
public class SwipeListener extends GestureDetector.SimpleOnGestureListener implements Animation.AnimationListener {

        boolean loginVisible = false;

        Activity source;

        public SwipeListener(Activity source) {
            this.source = source;
        }
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            //Check they've swiped up from near the bottom of the screen.
            if (event1.getY() > 1500 && !loginVisible) {
                loginVisible = true;
                Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
                TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -300);
                animation.setDuration(1000);
                //Changes position after anim
                animation.setFillAfter(true);
                animation.setAnimationListener(this);

                ImageView logoMiddle = (ImageView) source.findViewById(R.id.logo_middle);
                ImageView logoTop = (ImageView) source.findViewById(R.id.logo_top);
                if (logoMiddle != null) {
                    logoMiddle.startAnimation(animation);
                }
                if (logoTop != null) {
                    logoTop.startAnimation(animation);
                }
            }
            return true;
        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
            //Add image into gap
            ImageView logoReplacement = new ImageView(source.getApplicationContext());
            logoReplacement.setImageResource(R.drawable.logo_replace);
            RelativeLayout homeLayout = (RelativeLayout) source.findViewById(R.id.home_layout);
            if (homeLayout != null) {
                homeLayout.addView(logoReplacement);
            }
            RelativeLayout.LayoutParams logoLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 333);
            logoLayoutParams.addRule(RelativeLayout.ABOVE, R.id.logo_bottom);
            logoReplacement.setLayoutParams(logoLayoutParams);
            //Put the image sliding up back in front.
            ImageView logoMiddle = (ImageView) source.findViewById(R.id.logo_middle);
            logoMiddle.bringToFront();

            GridLayout loginForm = (GridLayout) source.findViewById(R.id.loginForm);
            loginForm.setVisibility(View.VISIBLE);
        }
}

