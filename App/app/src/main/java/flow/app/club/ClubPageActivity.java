package flow.app.club;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import flow.app.Club;
import flow.app.R;
import flow.app.home.HomeActivity;
import flow.app.listview.ListViewActivity;

public class ClubPageActivity extends AppCompatActivity {

    private List<String[]> drinksList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_page);

        //TODO: Lookup club object from club name then display specific club data

        Intent intent = getIntent();
        String name;
        Club club;
        final String source = intent.getStringExtra("source");
        if ((name = intent.getStringExtra("name")).length() <= 0) {
            //Launch list of clubs activity
        } else {
            //String desc = intent.getStringExtra("desc");
            //Display club page with name and description etc.
            club = Club.getClub(name);
            TextView clubTitle = findViewById(R.id.clubTitle);
            if (clubTitle != null)
                clubTitle.setText(club.getName());
            TextView clubDistance = findViewById(R.id.clubDistance);
            clubDistance.setText(club.getDistance() + " Miles");
            TextView clubQueue = findViewById(R.id.clubQueueTime);
            clubQueue.setText(club.getQueueTime() + " Minutes");
            TextView flowRating = findViewById(R.id.clubFlowRating);
            //TODO: Make actual flow rating
            flowRating.setText(String.format("%.2g%n", Math.random()*club.getQueueTime()));
        }

        ImageButton backButton = findViewById(R.id.back_circle);
        if (backButton != null) {
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (source.equals("listview")) {
                        Intent intent = new Intent(getApplicationContext(), ListViewActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                    }
                    //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            });
        }

        //TODO: Check if already checked in, then change button
        final Button checkInButton = findViewById(R.id.clubCheckIn);
        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInButton.setText("Checked In!");
                checkInButton.setEnabled(false);
            }
        });

        //TODO: Get real data from club object
        //Dummy Data
        drinksList.add(new String[]{"Carling", "£3"});
        drinksList.add(new String[]{"Amstel", "£4"});
        drinksList.add(new String[]{"Carlsberg", "£2.50"});
        drinksList.add(new String[]{"Peroni", "£5"});
        drinksList.add(new String[]{"Birra Moretti", "£5.50"});
        drinksList.add(new String[]{"Vodka Mixer", "£3.50"});
        drinksList.add(new String[]{"Double Vodka Mixer", "£5.50"});
        drinksList.add(new String[]{"Red Stripe", "£4"});
        drinksList.add(new String[]{"Stella", "£4"});

        //Setting up drinks imported from database for certain club
        TableLayout tableLayout = findViewById(R.id.drinksTable);
        for (int i = 0; i < drinksList.size(); i++) {
            TableRow tableRow = new TableRow(this);
            if (i % 2 == 1) {
                tableRow.setBackgroundColor(Color.parseColor("#0099CC"));
            }
            TextView drinkName = new TextView(this);
            drinkName.setText(drinksList.get(i)[0]);
            drinkName.setTextSize(25.0f);
            drinkName.setTextColor(Color.WHITE);
            drinkName.setTypeface(Typeface.DEFAULT_BOLD);

            TextView drinkPrice = new TextView(this);
            drinkPrice.setText(drinksList.get(i)[1]);
            drinkPrice.setTextSize(25.0f);
            drinkPrice.setTextColor(Color.WHITE);
            drinkPrice.setTypeface(Typeface.DEFAULT_BOLD);
            drinkPrice.setGravity(Gravity.RIGHT);

            tableRow.addView(drinkName);
            tableRow.addView(drinkPrice);
            tableLayout.addView(tableRow);
        }

    }
}
