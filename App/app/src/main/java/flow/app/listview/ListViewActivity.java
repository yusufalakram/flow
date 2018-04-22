package flow.app.listview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import flow.app.Club;
import flow.app.R;
import flow.app.club.ClubPageActivity;
import flow.app.home.HomeActivity;

public class ListViewActivity extends AppCompatActivity {

    //TODO: Load clubs from database

    private List<Club> displayedList;

    private List<Club> searchClubs(String query) {
        List<Club> newList = new ArrayList<>();
        for (int i = 0; i < displayedList.size(); i++) {
            if (displayedList.get(i).getName().toLowerCase().contains(query.toLowerCase()))
                newList.add(displayedList.get(i));
        }
        return newList;
    }

    private List<Club> filterClubs(double distance, double queue, double userRating, double flowRating, boolean ticketsReq) {
        List<Club> tempList = new ArrayList<>(Club.getClubs());
        System.out.println(Club.getClubs().size());
        for (Iterator<Club> it = tempList.iterator(); it.hasNext();) {
            Club club = it.next();
            if (club.getDistance() > distance || club.getQueueTime() > queue
                    || club.getUserRating() < userRating || club.getFlowRating() < flowRating
                    || (!ticketsReq && club.areTicketsRequired()))
                it.remove();
        }
        return tempList;
    }

    private List<Club> sortClubs(final int sortBy, final boolean ascending) {
        //sortBy: 0 = distance, 1 = queue time, 2 = user rating, 3 = flow rating
        Collections.sort(displayedList, new Comparator<Club>() {
            @Override
            public int compare(Club lhs, Club rhs) {
                double x;
                double y;
                if (sortBy == 0) {
                    x = lhs.getQueueTime();
                    y = rhs.getQueueTime();
                } else if (sortBy == 1) {
                    x = lhs.getDistance();
                    y = rhs.getDistance();
                } else if (sortBy == 2) {
                    x = lhs.getUserRating();
                    y = rhs.getUserRating();
                } else {
                    x = lhs.getFlowRating();
                    y = rhs.getFlowRating();
                }
                if (ascending) {
                    // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                    return x > y ? -1 : (x < y) ? 1 : 0;
                }
                return x > y ? 1 : (x < y) ? -1 : 0;
            }
        });
        return displayedList;
    }

    private void updateView(final List<Club> clubs) {
        RelativeLayout clubsListLayout = findViewById(R.id.clubsListViewList);

        clubsListLayout.removeAllViews();

        RelativeLayout previous = null;

        for (int i = 0; i < clubs.size(); i++) {

            RelativeLayout listElement = new RelativeLayout(this);
            if (previous != null) {
                RelativeLayout.LayoutParams layoutParams0 = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams0.addRule(RelativeLayout.BELOW, previous.getId());
                listElement.setLayoutParams(layoutParams0);
            } else {
                listElement.setLayoutParams(new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            ImageView clubLogo = new ImageView(this);
            clubLogo.setId((i + 1) * 20);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParams.setMargins(10,0,0,0);
            clubLogo.setBackgroundResource(clubs.get(i).getLogo());
            clubLogo.setLayoutParams(layoutParams);


            listElement.addView(clubLogo);

            TextView clubDistance = new TextView(this);
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layoutParams2.setMargins(0, 0, 10, 0);

            //TODO: Get distance to club
            clubDistance.setText("Distance: "+ clubs.get(i).getDistance() +" Mi");
            clubDistance.setLayoutParams(layoutParams2);

            listElement.addView(clubDistance);

            TextView clubName = new TextView(this);
            RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(
                    500, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams3.addRule(RelativeLayout.RIGHT_OF, clubLogo.getId());
            layoutParams3.setMargins(5, 0, 0, 0);
            clubName.setText(clubs.get(i).getName());
            clubName.setTextColor(Color.parseColor("#036485"));
            clubName.setTextSize(25);
            clubName.setTypeface(Typeface.DEFAULT_BOLD);
            clubName.setLayoutParams(layoutParams3);

            listElement.addView(clubName);

            ImageView separator = new ImageView(this);
            separator.setId((i + 1) * 40);
            RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams4.setMargins(10, 10, 10, 10);
            layoutParams4.addRule(RelativeLayout.BELOW, clubLogo.getId());
            separator.setBackgroundResource(R.drawable.club_page_separator);
            separator.setLayoutParams(layoutParams4);

            listElement.addView(separator);

            TextView clubQueue = new TextView(this);
            RelativeLayout.LayoutParams layoutParams5 = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams5.setMargins(0, 0, 0, 10);
            layoutParams5.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layoutParams5.addRule(RelativeLayout.ABOVE, separator.getId());
            //TODO: Get Queue Time
            clubQueue.setText("Queue Time: "+clubs.get(i).getQueueTime()+"m");
            clubQueue.setLayoutParams(layoutParams5);


            listElement.addView(clubQueue);

            final int id = i;
            listElement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ClubPageActivity.class);
                    intent.putExtra("name", clubs.get(id).getName());
                    intent.putExtra("desc", clubs.get(id).getDescription());
                    intent.putExtra("source", "listview");
                    startActivity(intent);
                }
            });

            listElement.setId(i + 1);
            previous = listElement;

            clubsListLayout.addView(listElement);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        ImageButton sortButton = findViewById(R.id.sortButton);
        if (sortButton != null) {
            sortButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(GravityCompat.END);

                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            });
        }

        final SeekBar distanceSlider = findViewById(R.id.distanceSlider);

        final TextView distanceSliderValue = findViewById(R.id.distanceSliderValue);

        distanceSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distanceSliderValue.setText("Up to: " + (float) progress / 10 + " Mi");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final TextView queueSliderValue = findViewById(R.id.queueSliderValue);

        final SeekBar queueSlider = findViewById(R.id.queueSlider);

        queueSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                queueSliderValue.setText("Up to: " + progress + " Mins");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final TextView fratingSliderValue = findViewById(R.id.fratingSliderValue);

        final SeekBar fratingSlider = findViewById(R.id.fratingSlider);

        fratingSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                fratingSliderValue.setText("Min: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        final TextView uratingSliderValue = findViewById(R.id.uratingSliderValue);

        final SeekBar uratingSlider = findViewById(R.id.uratingSlider);

        uratingSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                uratingSliderValue.setText("Min: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ImageView queueSortUp = findViewById(R.id.queueSortUp);
        ImageView queueSortDown = findViewById(R.id.queueSortDown);
        ImageView distanceSortUp = findViewById(R.id.distanceSortUp);
        ImageView distanceSortDown = findViewById(R.id.distanceSortDown);
        ImageView fratingSortUp = findViewById(R.id.fratingSortUp);
        ImageView fratingSortDown = findViewById(R.id.fratingSortDown);
        ImageView uratingSortUp = findViewById(R.id.uratingSortUp);
        ImageView uratingSortDown = findViewById(R.id.uratingSortDown);

        sortIcons = new ImageView[]{queueSortUp, queueSortDown, distanceSortUp, distanceSortDown, fratingSortUp, fratingSortDown,
                uratingSortUp, uratingSortDown};

        for (int i = 0; i < sortIcons.length; i++) {
            final int x = i;
            sortIcons[x].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateSortIcons(sortIcons[x]);
                }
            });
        }

        ImageView closeSidebar = findViewById(R.id.closeSidebar);
        if (closeSidebar != null) {
            closeSidebar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    drawerLayout.closeDrawer(GravityCompat.END);
                }
            });
        }

        if (drawerLayout != null) {
            drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

                }

                @Override
                public void onDrawerOpened(@NonNull View drawerView) {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
                }

                @Override
                public void onDrawerClosed(@NonNull View drawerView) {

                }

                @Override
                public void onDrawerStateChanged(int newState) {

                }
            });

        }

        ImageButton mapviewButton = findViewById(R.id.mapButton);
        if (mapviewButton != null) {
            mapviewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            });
        }

        final EditText listViewSearch = findViewById(R.id.listviewSearch);
        listViewSearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    updateView(displayedList = searchClubs(listViewSearch.getText().toString()));
                    return true;
                }
                return false;
            }
        });

        Button applyButton = findViewById(R.id.filterApply);
        if (applyButton != null) {
            applyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayedList = filterClubs((float) distanceSlider.getProgress() / 10, queueSlider.getProgress(), 0, fratingSlider.getProgress(), true);
                    updateView(displayedList = sortClubs(currentlySorted, currentSortMode));
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    drawerLayout.closeDrawer(GravityCompat.END);
                }
            });
        }


        final CheckBox ticketsRequiredCheck = findViewById(R.id.ticketsRequired);

        Button resetButton = findViewById(R.id.filterReset);
        if (resetButton != null) {
            resetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    distanceSlider.setProgress(100);
                    queueSlider.setProgress(100);
                    fratingSlider.setProgress(0);
                    uratingSlider.setProgress(0);
                    ticketsRequiredCheck.setChecked(true);
                    updateSortIcons(sortIcons[0]);
                }
            });

        }

        //TODO: remove this once clubsList is set up properly

        updateView(Club.getClubs());

        displayedList = new ArrayList<>(Club.getClubs());

    }

    private ImageView[] sortIcons;

    private int currentlySorted = 0;
    private boolean currentSortMode = false;

    private void updateSortIcons(ImageView clickedIcon) {
        for (int i = 0; i < sortIcons.length; i++) {
            if (i % 2 == 0) {
                if (sortIcons[i].equals(clickedIcon)) {
                    currentlySorted = (int) Math.floor(i/2);
                    sortIcons[i].setImageResource(R.drawable.sort_up);
                    currentSortMode = true;
                } else {
                    sortIcons[i].setImageResource(R.drawable.sort_up1);
                }
            } else {
                if (sortIcons[i].equals(clickedIcon)) {
                    currentlySorted = (int) Math.floor(i/2);
                    sortIcons[i].setImageResource(R.drawable.sort_down);
                    currentSortMode = false;
                } else {
                    sortIcons[i].setImageResource(R.drawable.sort_down1);
                }
            }
        }
    }
}
