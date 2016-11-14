package app.com.example.shahed.incomeexpensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;


public class ReportActivity extends ActionBarActivity {
    private EditText fromDateEditText;
    private EditText toDateEditText;
    private DatabaseHelper databaseHelper;
    private String reportType;
    private TextView errorMessageTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper(this);

        fromDateEditText = (EditText) findViewById(R.id.from_date_edit_text);
        toDateEditText = (EditText) findViewById(R.id.to_date_edit_text);
        errorMessageTextView = (TextView) findViewById(R.id.error_message_text_view);
        Spinner reportSpinner = (Spinner) findViewById(R.id.report_spinner);

        String[] reportItems = new String[]{"Income", "Expense"};


        ArrayAdapter<String> deptAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, reportItems);

        reportSpinner.setAdapter(deptAdapter);

        reportSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                reportType = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
                errorMessageTextView.setText("Nothing selected from dropdown");
            }
        });
    }

    public void generateButtonClicked(View view) {
        String fromDate = fromDateEditText.getText().toString();
        String toDate = toDateEditText.getText().toString();
        fromDateEditText.setHint("From Date");
        toDateEditText.setHint("To Date");
        int incomeExpenseType = 2;

        if (reportType.compareTo("Income") == 0)
            incomeExpenseType = 1;
        else if (reportType.compareTo("Expense") == 0)
            incomeExpenseType = 0;


        //call databaseHelper object for data
//        Cursor cursorResult = databaseHelper.getItemData();
//        StringBuffer stringBuffer = new StringBuffer();

//        if (cursorResult.getCount() == 0) {
//            showMessage("Error", "Nothing found");
//        }
//        while (cursorResult.moveToNext()) {
//            stringBuffer.append("item_id: " + cursorResult.getString(0) + "\n");
//            stringBuffer.append("item_name: " + cursorResult.getString(1) + "\n\n");
//        }
//        showMessage("Item Data", stringBuffer.toString());
//
//        cursorResult = databaseHelper.getDateData();
//        if (cursorResult.getCount() == 0) {
//            showMessage("Error", "Nothing found");
//        }
//        while (cursorResult.moveToNext()) {
//            stringBuffer.append("date_id: " + cursorResult.getString(0) + "\n");
//            stringBuffer.append("date_name: " + cursorResult.getString(1) + "\n\n");
//        }
//        showMessage("Date Data see", stringBuffer.toString());
//        cursorResult = databaseHelper.getItemDateData();
//        if (cursorResult.getCount() == 0) {
//            showMessage("Error", "Nothing found");
//        }
//        while (cursorResult.moveToNext()) {
//            stringBuffer.append("item_date_id: " + cursorResult.getString(0) + "\n");
//            stringBuffer.append("item_id: " + cursorResult.getString(1) + "\n");
//            stringBuffer.append("date_id: " + cursorResult.getString(2) + "\n\n");
//        }
//        showMessage("ItemDate table Data", stringBuffer.toString());
//
//        cursorResult = databaseHelper.getPersonData();
//        if (cursorResult.getCount() == 0) {
//            showMessage("Error", "Nothing found");
//        }
//        stringBuffer = new StringBuffer();
//        while (cursorResult.moveToNext()) {
//            stringBuffer.append("person_id: " + cursorResult.getString(0) + "\n");
//            stringBuffer.append("email: " + cursorResult.getString(1) + "\n");
//            stringBuffer.append("password: " + cursorResult.getString(2) + "\n\n");
//        }
//        showMessage("Person Data", stringBuffer.toString());
//
//        cursorResult = databaseHelper.getMainData();
//        if (cursorResult.getCount() == 0) {
//            showMessage("Error", "Nothing found");
//        }
//        while (cursorResult.moveToNext()) {
//            stringBuffer.append("main_id: " + cursorResult.getString(0) + "\n");
//            stringBuffer.append("item_date_id: " + cursorResult.getString(1) + "\n");
//            stringBuffer.append("person_id: " + cursorResult.getString(2) + "\n");
//            stringBuffer.append("amount: " + cursorResult.getString(3) + "\n");
//            stringBuffer.append("incomeExpenseType: " + cursorResult.getString(4) + "\n\n");
//        }
//        showMessage("Main Data", stringBuffer.toString());

        // show amountArrayListData
        ArrayList<Float> amountArrayList = databaseHelper.getAmountArrayList(fromDate, toDate, incomeExpenseType);
        ArrayList<String> dateArrayList = databaseHelper.getDateArrayList();
        Intent intent = new Intent(this, ShowGraph.class);
        intent.putStringArrayListExtra("dateArrayList", dateArrayList);
        intent.putExtra("amountArrayList", amountArrayList);
        startActivity(intent);
//        StringBuffer stringBuffer;
//        if (amountArrayList.size() == 0) {
//            showMessage("Error", "Nothing found in amountArrayList!!\n");
//        } else {
//            stringBuffer = new StringBuffer();
//            for (int i = 0; i < amountArrayList.size(); i++) {
//                stringBuffer.append(amountArrayList.get(i)).append("\n");
//            }
//            showMessage("amountArrayListData: ", stringBuffer.toString());
//        }
//        // show dateArrayListData
//        if (dateArrayList.size() == 0) {
//            showMessage("Error", "Nothing found in dateArrayList!!\n");
//        } else {
//            stringBuffer = new StringBuffer();
//            for (int i = 0; i < dateArrayList.size(); i++) {
//                stringBuffer.append(dateArrayList.get(i) + "\n");
//            }
//            showMessage("dateArrayListData: ", stringBuffer.toString());
//        }
    }

//    public void showMessage(String title, String message) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(true);
//        builder.setTitle(title);
//        builder.setMessage(message);
//        builder.show();
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report, menu);
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
