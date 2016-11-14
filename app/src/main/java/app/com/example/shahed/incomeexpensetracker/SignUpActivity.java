package app.com.example.shahed.incomeexpensetracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class SignUpActivity extends ActionBarActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private TextView signUpMessageTextView;
    private DatabaseHelper databaseHelper;
    private final String LOG_TAG = SignUpActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper(this);

        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        confirmPasswordEditText = (EditText) findViewById(R.id.confirm_password_edit_text);
        signUpMessageTextView = (TextView) findViewById(R.id.sign_up_message_text_view);
    }

    public void signUpButtonClicked(View view) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (!password.equals(confirmPassword)) {
            signUpMessageTextView.setText("Not matched. Type same one in both password field");
            Log.v(LOG_TAG, "if email: " + email + " pass: " + password + " con: " + confirmPassword);
        } else {
            Log.v(LOG_TAG, "else email: " + email + " pass: " + password + " con: " + confirmPassword);
            Cursor cursor = databaseHelper.checkEmail(email);

            if (cursor.getCount() != 0) {
                signUpMessageTextView.setText("This email already used, Try another one");
                Log.v(LOG_TAG, "else if");
            } else {
                Log.v(LOG_TAG, "else else");
                IncomeExpenseContract.email = email;
                databaseHelper.insertEmailPassword(email, password);
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                emailEditText.setHint("E-mail");
                passwordEditText.setHint("Password");
                confirmPasswordEditText.setHint("Confirm Password");
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
