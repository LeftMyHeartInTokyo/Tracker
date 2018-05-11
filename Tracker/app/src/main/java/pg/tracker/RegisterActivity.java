package pg.tracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        username = (EditText) findViewById(R.id.nameText1);
        password = (EditText) findViewById(R.id.passwordText1);
    }
    public void haveAccount(View view) {
        finish();
    }
    public void register(View view) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final Context ctx = this;
        mAuth.createUserWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            writeUserToDB(username.getText().toString());
                            Toast.makeText(ctx, "Registered", Toast.LENGTH_LONG).show();
//                            try {
//                                wait(1000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(ctx, "Cannot register", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void writeUserToDB(String email) {
        UserEntity user = new UserEntity(email);

        mDatabase.child("users").child(email.replaceAll("\\.", "_")).setValue(user);
    }
}
