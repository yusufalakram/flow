package flow.app.club;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import flow.app.R;

public class ClubPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_page);

        Intent intent = getIntent();
        String name;
        if ((name = intent.getStringExtra("name")).length() <= 0) {
            //Launch list of clubs activity
        } else {
            String desc = intent.getStringExtra("desc");
            //Display club page with name and description etc.
            TextView clubTitle = (TextView) findViewById(R.id.clubTitle);
            if (clubTitle != null)
                clubTitle.setText(name);
        }
    }
}
