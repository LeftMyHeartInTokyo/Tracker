package pg.tracker;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener {

    private GoogleMap mMap;
    private boolean addingCheckPoints;
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private String[] dataFromEdit;
    CheckPointDataBaseHandler checkPointDataBaseHandler;
    private boolean isLocationTrackerEnabled = false;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            startActivity(new Intent(this, RegisterActivity.class));
            startActivity(new Intent(this, LoginActivity.class));
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        addingCheckPoints = false;
        Button clickButtonPunKon = (Button) findViewById(R.id.button1);
        clickButtonPunKon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentUser != null){
                    addingCheckPoints = !addingCheckPoints;
                    if (addingCheckPoints)
                        findViewById(R.id.button1).getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
                    else findViewById(R.id.button1).getBackground().clearColorFilter();
                } else {
                    Toast.makeText(getApplicationContext(), "You must be logged in to add Checkpoints", Toast.LENGTH_LONG).show();
                }
            }
        });


        Button buttonAccounts = (Button) findViewById(R.id.button2);
        buttonAccounts.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AccountsActivity.class));
            }
        });

        Button button = (Button) findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isLocationTrackerEnabled = !isLocationTrackerEnabled;
            }
        });

        Button buttonPointsList = (Button) findViewById(R.id.button5);
        buttonPointsList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showCheckPointsList();
            }
        });
        checkPointDataBaseHandler = new CheckPointDataBaseHandler(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }
        // TODO: Before enabling the My Location layer, you must request
        // location permission from the user. This sample does not include
        // a request for location permission.
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(this);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            public void onMapClick(LatLng point) {
                if (addingCheckPoints) {
                    //Go to Checkpoint Edit Activity
                    Intent i = new Intent(getApplicationContext(), CheckPointEditActivity.class);
                    i.putExtra("nameLatLon", new String[]{"", String.valueOf(point.latitude), String.valueOf(point.longitude)});
                    startActivityForResult(i, 999);
                }
            }
        });

        refreshCheckPointsOnMap();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 999 && resultCode == RESULT_OK) {
            dataFromEdit = data.getStringArrayExtra("dataFromEdit");
            if (dataFromEdit[0].equals("delete")) {
                //Dont know if it is needed here...
                //checkPointDataBaseHandler.deleteData(dataFromEdit[1], Double.parseDouble(dataFromEdit[2]), Double.parseDouble(dataFromEdit[3]),
                // dataFromEdit[5]);
                Toast.makeText(getApplicationContext(), "Check Point Deleted", Toast.LENGTH_LONG).show();
            }
            if (dataFromEdit[0].equals("accept")) {
                checkPointDataBaseHandler.writeData(dataFromEdit[1], Double.parseDouble(dataFromEdit[2]), Double.parseDouble(dataFromEdit[3]),
                        dataFromEdit[5]);
                Toast.makeText(getApplicationContext(), "Check Point Added", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == 999 && resultCode == RESULT_CANCELED) {
            //message that edit was cancelled
            Toast.makeText(getApplicationContext(), "Check Point Addition Cancelled", Toast.LENGTH_LONG).show();
        }
        refreshCheckPointsOnMap();
    }

    private void addMarkerOnLocation(LatLng loc, String color) {
        if(color.equals("Czerwony")) {
            Marker mMarker = mMap.addMarker(new MarkerOptions().position(loc).icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
        else if(color.equals("Niebieski")) {
            Marker mMarker = mMap.addMarker(new MarkerOptions().position(loc).icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }
        else if(color.equals("Zielony")) {
            Marker mMarker = mMap.addMarker(new MarkerOptions().position(loc).icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }
        else if(color.equals("Zolty")) {
            Marker mMarker = mMap.addMarker(new MarkerOptions().position(loc).icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        }
    }

    private Location getMyLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (myLocation == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            String provider = lm.getBestProvider(criteria, true);
            myLocation = lm.getLastKnownLocation(provider);
        }
        return myLocation;
    }

    public void showCheckPointsList() {
        Intent intent = new Intent(this, AllCheckPointsActivity.class);
        String message = "msg";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    @Override
    public void onMyLocationChange(Location location) {
        if(isLocationTrackerEnabled) {
            Location newLocation = getMyLocation();
            LatLng loc = new LatLng(newLocation.getLatitude(), newLocation.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 17.0f));
            DatabaseReference  Database = FirebaseDatabase.getInstance().getReference();
            DatabaseReference mMessageReference = FirebaseDatabase.getInstance().getReference("users");
            mMessageReference.child("test").child("position").setValue(loc);
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        if(mMap != null) refreshCheckPointsOnMap();
    }

    private void refreshCheckPointsOnMap(){
        mMap.clear();
        ArrayList<String> allCheckPoints = checkPointDataBaseHandler.readData();
        for(String checkPoint : allCheckPoints){
            String data[] = checkPoint.split(", ");
            LatLng checkPointPos;
            try
            {
                checkPointPos = new LatLng(Double.parseDouble(data[1]), Double.parseDouble(data[2]));
            }
            catch(NumberFormatException e)
            {
                //wrong data
                continue;
            }
            addMarkerOnLocation(checkPointPos, data[3]);
        }
    }


}
