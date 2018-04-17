package pg.tracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CheckPointEditActivity extends AppCompatActivity {

    private EditText editTextName, editTextAlarm;
    private String nameLatLonValues[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_point_edit);

        nameLatLonValues = getIntent().getExtras().getStringArray("nameLatLon");

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextName.setText(nameLatLonValues[0]);
        editTextAlarm = (EditText) findViewById(R.id.editTextAlarm);
        Button buttonAcc = (Button) findViewById(R.id.button5);
        buttonAcc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onOkPressed();
            }
        });
        Button buttonDel = (Button) findViewById(R.id.button6);
        buttonDel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onDelPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        setResult(RESULT_CANCELED,i);
        finish();
    }

    public void onDelPressed() {
        Intent i = new Intent();
        String[] editData = new String[]{
                "delete",
                editTextName.getText().toString(),
                nameLatLonValues[1],
                nameLatLonValues[2],
                editTextAlarm.getText().toString()
        };
        i.putExtra("dataFromEdit", editData);
        setResult(RESULT_OK,i);
        finish();
    }

    protected void onOkPressed() {
        Intent i = new Intent();
        String[] editData = new String[]{
                "accept",
                editTextName.getText().toString(),
                nameLatLonValues[1],
                nameLatLonValues[2],
                editTextAlarm.getText().toString()
        };
        i.putExtra("dataFromEdit", editData);
        setResult(RESULT_OK,i);
        finish();
    }
}
