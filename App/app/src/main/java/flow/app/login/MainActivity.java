package flow.app.login;

import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import flow.app.home.HomeActivity;
import flow.app.login.listeners.SwipeListener;
import flow.backend.Backend;
import flow.backend.app.R;

public class MainActivity extends AppCompatActivity {

    private GestureDetectorCompat mDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Backend.Init();

        mDetector = new GestureDetectorCompat(this, new SwipeListener(this));

        final Button loginBtn = findViewById(R.id.login);
        final EditText emailField = findViewById(R.id.emailEntry);
        final EditText passwordField = findViewById(R.id.passwordEntry);
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
        String logInMessage = Backend.db.logIn(email, password);
        emailField.setError(logInMessage);
        passwordField.setError(null);
        if(logInMessage.startsWith("Log")){
        	Backend.setUserEntityID(Backend.db.getUserEntityID(email));
        	Backend.se = new flow.backend.Settings(Backend.db.getUserEntityID(email));
        	return true;
        }
        else{
        	return false;
        }
    }

    //TODO: do login via backend
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