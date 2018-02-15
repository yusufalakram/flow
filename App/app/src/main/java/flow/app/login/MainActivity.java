package flow.app.login;

import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import flow.app.R;
import flow.app.home.HomeActivity;
import flow.app.login.listener.SwipeListener;

public class MainActivity extends AppCompatActivity {

    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDetector = new GestureDetectorCompat(this, new SwipeListener(this));

        final Button loginBtn = (Button) findViewById(R.id.login);
        final EditText emailField = (EditText) findViewById(R.id.emailEntry);
        final EditText passwordField = (EditText) findViewById(R.id.passwordEntry);
        if (loginBtn != null) {
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Do Login
                    if (emailField != null && passwordField != null) {
                        if (attemptLogin(emailField, passwordField)) {
                            //Launch home screen
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            //EditText editText = (EditText) findViewById(R.id.editText);
                            //String message = editText.getText().toString();
                            //intent.putExtra(EXTRA_MESSAGE, message);
                            startActivity(intent);
                        }
                    }
                }
            });
        }
    }

    private boolean attemptLogin(EditText emailField, EditText passwordField) {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        emailField.setError(null);
        passwordField.setError(null);
        if (!email.contains("@") || !email.contains(".")) {
            emailField.setError("Invalid email address");
            return false;
        }
        if (email.length() <= 0) {
            emailField.setError("Please fill in your email address");
            return false;
        }
        if (password.length() <= 0) {
            passwordField.setError("Please fill in your password");
            return false;
        }

        if (!validateCredentials(emailField.getText().toString(), passwordField.getText().toString())) {
            emailField.setError("Incorrect email or password.");
            return false;
        }
        return true;
    }

    /**
     * Connects to the database and checks if credentials are correct
     * @param email
     * @param password
     * @return true if login successful
     */
    private boolean validateCredentials(String email, String password) {
        //Need to contact database to verify credentials.
        String[][] sampleCredentials = {{"a@a.com", "password"}};
        for (String[] credentials : sampleCredentials) {
            if (email.equals(credentials[0]) && password.equals(credentials[1])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


}
