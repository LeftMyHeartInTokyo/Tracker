package pg.tracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AccountsConnectionsListActivity extends AppCompatActivity {


    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts_connections_list);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase.child("connections").addValueEventListener(connectionsListener);

        ListView lstView = (ListView) findViewById(R.id.listViewConnectedAccounts);
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                deleteConnection(currentUser.getEmail(), item.split(" ")[1]);
                Toast.makeText(getApplicationContext(), "Account connection deleted: " + item, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteConnection(String firstEmail, String secondEmail) {
        ConnectionEntity connection = new ConnectionEntity(firstEmail, secondEmail);
        mDatabase.child("connections").child(firstEmail.replaceAll("\\.", "_") + " " + secondEmail.replaceAll("\\.", "_")).removeValue();
        mDatabase.child("connections").child(secondEmail.replaceAll("\\.", "_") + " " + firstEmail.replaceAll("\\.", "_")).removeValue();
    }

    ValueEventListener connectionsListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ArrayList<String> connections = new ArrayList<>();
            for (DataSnapshot ds : dataSnapshot.getChildren()){
                ConnectionEntity connection = ds.getValue(ConnectionEntity.class);
                connections.add(connection.getFirstEmail() + " " + connection.getSecondEmail());
            }
            refreshListContent(connections);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    private void refreshListContent(ArrayList<String> connections){
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, connections);
        ListView lstView = (ListView) findViewById(R.id.listViewConnectedAccounts);
        lstView.setAdapter(itemsAdapter);
    }
}
