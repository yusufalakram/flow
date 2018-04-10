package flow.app.listview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import flow.app.Club;
import flow.app.R;
import flow.app.club.ClubPageActivity;
import flow.app.home.HomeActivity;

public class ListViewActivity extends FragmentActivity {

    private List<Club> clubsList = new ArrayList<>();

    //TODO: Maybe rework this - decide if String#contains is adequate for searching.
    private List<Club> searchClubs(String query) {
        List<Club> newList = new ArrayList<>();
        for (int i = 0; i < clubsList.size(); i++) {
            if (clubsList.get(i).getName().toLowerCase().contains(query.toLowerCase()))
                newList.add(clubsList.get(i));
        }
        return newList;
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
            clubDistance.setText("Distance: 0.5 Mi");
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
            clubQueue.setText("Queue Time: 20m");
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
            drawerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Hi", "Jmail");
                    if (!drawerLayout.isDrawerOpen(drawerLayout)) {
                        Log.d("Hi", "Heorhe");
                    }
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
                    updateView(searchClubs(listViewSearch.getText().toString()));
                    return true;
                }
                return false;
            }
        });

        //Dummy creation of clubs to test list view.
        Club bridge = new Club("Bridge", 0,0, "");
        Club po = new Club("Po Na Na", 0,0, "");
        clubsList.add(bridge);
        clubsList.add(po);

        updateView(clubsList);

    }
}
