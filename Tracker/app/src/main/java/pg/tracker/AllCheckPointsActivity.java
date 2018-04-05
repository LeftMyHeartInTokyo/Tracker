package pg.tracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AllCheckPointsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_check_points);
        CheckPointDataBaseHandler checkPointDataBaseHandler = new CheckPointDataBaseHandler(this);

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, checkPointDataBaseHandler.readData());
        ListView lstView = (ListView) findViewById(R.id.listView);
        lstView.setAdapter(itemsAdapter);
    }
}
