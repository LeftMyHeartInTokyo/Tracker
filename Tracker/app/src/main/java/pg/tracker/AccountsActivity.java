package pg.tracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        Button buttonRegisterNew = (Button) findViewById(R.id.buttonRegisterNew);
        Button buttonLogIn = (Button) findViewById(R.id.buttonLogIn);
        Button buttonLogOut = (Button) findViewById(R.id.buttonLogOut);
        Button buttonAllAccounts = (Button) findViewById(R.id.buttonAllAccounts);
        Button buttonTrackedAccounts = (Button) findViewById(R.id.buttonTrackedAccounts);
        Button buttonChangePassword = (Button) findViewById(R.id.buttonChangePassword);
        buttonRegisterNew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser != null){
                    Toast.makeText(getApplicationContext(), "You must be logged out before you register in new account.", Toast.LENGTH_LONG).show();
                } else {
                    startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                }
            }
        });
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser != null){
                    Toast.makeText(getApplicationContext(), "You must be logged out before you log in again.", Toast.LENGTH_LONG).show();
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            }
        });
        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                mAuth.signOut();
                Toast.makeText(getApplicationContext(), "Now you are logged out.", Toast.LENGTH_LONG).show();
            }
        });
        buttonAllAccounts.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AccountsListActivity.class));
            }
        });
        buttonTrackedAccounts.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AccountsConnectionsListActivity.class));
            }
        });
        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChangePasswordActivity.class));
            }
        });
    }
}
