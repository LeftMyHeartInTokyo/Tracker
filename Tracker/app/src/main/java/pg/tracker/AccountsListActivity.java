package pg.tracker;

import android.content.Intent;
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

public class AccountsListActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts_list);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase.child("users").addValueEventListener(connectionsListener);

        ListView lstView = (ListView) findViewById(R.id.listViewAllAccounts);
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                writeConnection(currentUser.getEmail(), item);
                Toast.makeText(getApplicationContext(), "Connected to account: " + item, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void writeConnection(String firstEmail, String secondEmail) {
        ConnectionEntity connection = new ConnectionEntity(firstEmail, secondEmail);

        mDatabase.child("connections").child(firstEmail.replaceAll("\\.", "_") + " " + secondEmail.replaceAll("\\.", "_")).setValue(connection);
    }

    ValueEventListener connectionsListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ArrayList<String> users = new ArrayList<>();
            for (DataSnapshot ds : dataSnapshot.getChildren()){
                UserEntity user = ds.getValue(UserEntity.class);
                users.add(user.getUser());
            }
            refreshListContent(users);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    private void refreshListContent(ArrayList<String> users){
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, users);
        ListView lstView = (ListView) findViewById(R.id.listViewAllAccounts);
        lstView.setAdapter(itemsAdapter);
    }
}
