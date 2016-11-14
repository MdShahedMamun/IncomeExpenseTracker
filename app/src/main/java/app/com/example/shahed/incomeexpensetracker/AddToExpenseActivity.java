package app.com.example.shahed.incomeexpensetracker;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class AddToExpenseActivity extends ActionBarActivity {
    private EditText nameEditText;
    private EditText amountEditTextNumber;
    private EditText dateEditText;
    private TextView errorMessageTextView;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_expense);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper(this);

        errorMessageTextView = (TextView) findViewById(R.id.error_message_text_view);
        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        amountEditTextNumber = (EditText) findViewById(R.id.amount_edit_text_number);
        dateEditText = (EditText) findViewById(R.id.date_edit_text);
    }

    public void addExpenseClicked(View view) {
        String itemName = nameEditText.getText().toString();
        String date = dateEditText.getText().toString();
        String amountString = amountEditTextNumber.getText().toString();
        Float amountFloat = Float.parseFloat(amountString);
        int incomeExpenseType = 0;

        if (!databaseHelper.validateItemName(itemName, date)) {
            errorMessageTextView.setText("This name is used in this date. Try a little bit different one");
        } else {
            databaseHelper.insertData(itemName, date, amountFloat, incomeExpenseType);
            nameEditText.setHint("Enter name");
            amountEditTextNumber.setHint("Enter amount");
            dateEditText.setHint("Enter date");
            Toast.makeText(this, "data added", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_to_expense, menu);
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
