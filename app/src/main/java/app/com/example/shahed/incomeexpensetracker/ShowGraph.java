package app.com.example.shahed.incomeexpensetracker;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class ShowGraph extends ActionBarActivity {
    BarChart barChart;
    ArrayList<BarEntry> barEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_graph);

        barChart = (BarChart) findViewById(R.id.myBarChart);

        Bundle receivedBundle = getIntent().getExtras();
        if (receivedBundle == null) {
            return;
        }
        ArrayList<String> dates = receivedBundle.getStringArrayList("dateArrayList");
        ArrayList<Float> amountArrayList = (ArrayList<Float>) getIntent().getSerializableExtra("amountArrayList");

        if (amountArrayList.size() == 0)
            showMessage("Error", "No data found, Enter valid date!!\n");
        else {
            barEntries = new ArrayList<>();
            for (int j = 0; j < amountArrayList.size(); j++) {
                barEntries.add(new BarEntry(amountArrayList.get(j), j));
            }

            BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");
            BarData barData = new BarData(dates, barDataSet);
            barChart.setData(barData);
            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            barChart.animateY(3000); // 3 sec animation(rising) time
//            barChart.setDescription("My First Bar Graph!");
            barChart.setTouchEnabled(true);
            barChart.setDragEnabled(true);
            barChart.setScaleEnabled(true);
        }
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_graph, menu);
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
