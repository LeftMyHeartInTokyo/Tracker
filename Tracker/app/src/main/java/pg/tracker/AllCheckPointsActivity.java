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

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            String name = currentUser.getEmail();

            if (dataFromEdit[0].equals("delete")){
                checkPointDataBaseHandler.deleteData(dataFromEdit[1], Double.parseDouble(dataFromEdit[2]), Double.parseDouble(dataFromEdit[3]),
                        dataFromEdit[5]);

                databaseReference.child(name.replace(".","_")).child("checkpoints").child(dataFromEdit[1]).removeValue();

                Toast.makeText(getApplicationContext(), "Check Point Deleted", Toast.LENGTH_LONG).show();
            }
            if (dataFromEdit[0].equals("accept")){
                checkPointDataBaseHandler.deleteData(oldCheckPointName, Double.parseDouble(dataFromEdit[2]), Double.parseDouble(dataFromEdit[3]),
                        dataFromEdit[5]);
                checkPointDataBaseHandler.writeData(dataFromEdit[1], Double.parseDouble(dataFromEdit[2]), Double.parseDouble(dataFromEdit[3]),
                        dataFromEdit[5]);

                databaseReference.child(name.replace(".","_")).child("checkpoints").child(dataFromEdit[1]).removeValue();

                LatLng loc = new LatLng( Double.parseDouble(dataFromEdit[2]), Double.parseDouble(dataFromEdit[3]));
                CheckPointEntity checkPointEntity = new CheckPointEntity(dataFromEdit[1], dataFromEdit[5], dataFromEdit[4],
                        Double.parseDouble(dataFromEdit[2]), Double.parseDouble(dataFromEdit[3]), name.replace(".","_"));
                databaseReference.child(name.replace(".","_")).child("checkpoints").child(dataFromEdit[1]).setValue(checkPointEntity);


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
