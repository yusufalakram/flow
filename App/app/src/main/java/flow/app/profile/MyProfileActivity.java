package flow.app.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import flow.app.home.HomeActivity;
import flow.backend.Backend;
import flow.backend.app.R;

/**
 * Created by Ben Amor on 12/04/2018.
 */
public class MyProfileActivity extends AppCompatActivity {

    private void closeKeyboard() {
        //Close the keyboard
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        if (getCurrentFocus() != null) {
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);


        final ImageButton backBtn = findViewById(R.id.back_circle_prof);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        final EditText nameField = findViewById(R.id.profName);
        nameField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameField.setText(nameField.getHint());
                nameField.setFocusable(true);
                nameField.setFocusableInTouchMode(true);
            }
        });

        final TextView userName = findViewById(R.id.profileTitle);

        final ImageView saveName = findViewById(R.id.saveNameBtn);
        saveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Backend.db.updateName(Backend.getUserEntityID(), nameField.getText());
                nameField.setHint(nameField.getText());
                userName.setText(nameField.getText().toString().split(" ")[0]);
                nameField.setText("");
                nameField.setFocusable(false);
                nameField.setFocusableInTouchMode(false);
                closeKeyboard();
            }
        });
        
        final EditText emailField = findViewById(R.id.profEmail);
        emailField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailField.setText(emailField.getHint());
                emailField.setFocusable(true);
                emailField.setFocusableInTouchMode(true);
            }
        });

        final ImageView saveEmail = findViewById(R.id.saveEmailBtn);
        saveEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Backend.db.updateEmail(Backend.getUserEntityID(), emailField.getText());
                emailField.setHint(emailField.getText());
                emailField.setText("");
                emailField.setFocusable(false);
                emailField.setFocusableInTouchMode(false);
                closeKeyboard();
            }
        });

        final EditText passwordField = findViewById(R.id.profPass);
        passwordField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordField.setText("");
                passwordField.setFocusable(true);
                passwordField.setFocusableInTouchMode(true);
            }
        });

        final ImageView savePass = findViewById(R.id.savePassBtn);
        savePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Backend.db.updatePassword(Backend.getUserEntityID(), passwordField.getText());
                passwordField.setHint("********");
                passwordField.setText("");
                passwordField.setFocusable(false);
                passwordField.setFocusableInTouchMode(false);
                closeKeyboard();
            }
        });

        final CheckBox autoCheckIn = findViewById(R.id.profClubCheckIn);
        autoCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Update auto check in in database
            }
        });

        final ImageView visibleSaveBtn = findViewById(R.id.saveVisBtn);
        visibleSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Update visibility in database
            }
        });


        //a

        final EditText addFriendField = findViewById(R.id.addFriendField);
        addFriendField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriendField.setText("");
                addFriendField.setFocusable(true);
                addFriendField.setFocusableInTouchMode(true);
            }
        });

        final TextView friendsList = findViewById(R.id.profFriendsList);

        final ImageView addFriendBtn = findViewById(R.id.addFriendBtn);
        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	addFriend(Backend.getUserEntityID(), Backend.db.getUserEntityID(addFriendField.getText()));
                String newFriend = addFriendField.getText().toString().split("@")[0];
                addFriendField.setHint("joe@bloggs.com");
                addFriendField.setText("");
                addFriendField.setFocusable(false);
                addFriendField.setFocusableInTouchMode(false);
                friendsList.setText(friendsList.getText().toString() + "\n" + newFriend);
                closeKeyboard();
            }
        });


    }
}
