package pg.tracker;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AllCheckPointsActivity extends AppCompatActivity {

    private CheckPointDataBaseHandler checkPointDataBaseHandler;
    private String oldCheckPointName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_check_points);
        checkPointDataBaseHandler = new CheckPointDataBaseHandler(this);

        refreshListContent();

        ListView lstView = (ListView) findViewById(R.id.listView);
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String items[] = ((String) parent.getItemAtPosition(position)).split(", ");
                oldCheckPointName = items[0];
                Intent i = new Intent(getApplicationContext(), CheckPointEditActivity.class);
                i.putExtra("nameLatLon", items);
                startActivityForResult(i,998);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==998 && resultCode==RESULT_OK){
            String[] dataFromEdit;
            dataFromEdit = data.getStringArrayExtra("dataFromEdit");
            if (dataFromEdit[0].equals("delete")){
                checkPointDataBaseHandler.deleteData(dataFromEdit[1], Double.parseDouble(dataFromEdit[2]), Double.parseDouble(dataFromEdit[3]));
                Toast.makeText(getApplicationContext(), "Check Point Deleted", Toast.LENGTH_LONG).show();
            }
            if (dataFromEdit[0].equals("accept")){
                checkPointDataBaseHandler.deleteData(oldCheckPointName, Double.parseDouble(dataFromEdit[2]), Double.parseDouble(dataFromEdit[3]));
                checkPointDataBaseHandler.writeData(dataFromEdit[1], Double.parseDouble(dataFromEdit[2]), Double.parseDouble(dataFromEdit[3]));
                Toast.makeText(getApplicationContext(), "Check Point Edited", Toast.LENGTH_LONG).show();
            }
        }
        if(requestCode==998 && resultCode==RESULT_CANCELED){
            //message that edit was cancelled
            Toast.makeText(getApplicationContext(), "Check Point Edit Cancelled", Toast.LENGTH_LONG).show();
        }
        refreshListContent();
    }

    private void refreshListContent(){
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, checkPointDataBaseHandler.readData());
        ListView lstView = (ListView) findViewById(R.id.listView);
        lstView.setAdapter(itemsAdapter);
    }
}
