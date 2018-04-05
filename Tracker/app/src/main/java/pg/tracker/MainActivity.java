package pg.tracker;

import android.*;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean addingCheckPoints;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        addingCheckPoints = false;
        Button clickButtonPunKon = (Button) findViewById(R.id.button1);
        clickButtonPunKon.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view){
                addingCheckPoints=!addingCheckPoints;
                if(addingCheckPoints) findViewById(R.id.button1).getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
                else findViewById(R.id.button1).getBackground().clearColorFilter();
            }
        });

        Button button = (Button) findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Location location = getMyLocation();
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }
        });

        CheckPointDataBaseHandler checkPointDataBaseHandler = new CheckPointDataBaseHandler(this);
        checkPointDataBaseHandler.writeData("test2", 30.0, 40.0);
        ArrayList<String> data = checkPointDataBaseHandler.readData();

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
       // mMap.setOnMyLocationChangeListener(onMyLocationChange);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            public void onMapClick(LatLng point) {
                if(addingCheckPoints)
                addMarkerOnLocation(point);
            }
        });

    }

    //Dodawanie CheckPointu
    private void addMarkerOnLocation(LatLng loc){
        Marker mMarker = mMap.addMarker(new MarkerOptions().position(loc));
    }

    //Pobiera aktualna lokacje telefonu
    private Location getMyLocation() {
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (myLocation == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            String provider = lm.getBestProvider(criteria, true);
            myLocation = lm.getLastKnownLocation(provider);
        }
        return myLocation;
    }

    private void writeToDatabase() {

    }



}
