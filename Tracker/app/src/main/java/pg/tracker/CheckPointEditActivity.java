package pg.tracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CheckPointEditActivity extends AppCompatActivity {

    private EditText editTextId, editTextName, editTextLat, editTextLong, editTextAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_point_edit);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextAlarm = (EditText) findViewById(R.id.editTextAlarm);
        Button button = (Button) findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onOkPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        //String[] editData = new String[]{};
        //i.putExtra("dataFromEdit", editData);
        setResult(RESULT_CANCELED,i);
        finish();
    }

    protected void onOkPressed() {
        Intent i = new Intent();
        String[] editData = new String[]{
                editTextName.getText().toString(),
                editTextAlarm.getText().toString(),
        };
        i.putExtra("dataFromEdit", editData);
        setResult(RESULT_OK,i);
        finish();
    }
}
