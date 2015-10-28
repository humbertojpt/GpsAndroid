package com.humbertojpt.androidgps.gps;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {
    private LocationManager mLocationManager;
    private TextView tvLat;
    private TextView tvLong;
    private TextView tvPres;
    private TextView lblEstado;
    private GoogleMap mMap;
    private Criteria mCriteria;
    private String bestProvider;
    private String lat;
    private String lon;
    private String prec;

    private ArrayList<String> values;
    private ListView mListViewParse;
    private List<ParseObject> ob;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLat = (TextView) findViewById(R.id.editTextLat);
        tvLong = (TextView) findViewById(R.id.editTextLong);
        tvPres = (TextView) findViewById(R.id.editTextPres);
        lblEstado = (TextView)findViewById(R.id.LblEstado);
        mListViewParse = (ListView) findViewById(R.id.listView);

        mLocationManager = (LocationManager) getSystemService(
                Context.LOCATION_SERVICE);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        time = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());

    }




    public void stopGPS(View view) {
        mLocationManager.removeUpdates(this);
    }

    public void startGPS(View view) {
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                20000, 2
                , this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(this.getClass().getSimpleName(), "Nueva loc " + location.getLatitude() + " " + location.getLongitude());

            mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Marker"));

        lat = location.getLatitude()+"";
        lon = location.getLongitude()+"";
        prec = location.getAccuracy() + "";
        tvLat.setText(lat);
        tvLong.setText(lon);
        tvPres.setText(prec);
        centerMap(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        lblEstado.setText("Provider Status: " + i);
    }

    @Override
    public void onProviderEnabled(String s) {
        lblEstado.setText("Provider ON ");
    }

    @Override
    public void onProviderDisabled(String s) {
        lblEstado.setText("Provider OFF");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mCriteria != null && mLocationManager != null) {
            bestProvider = mLocationManager.getBestProvider(mCriteria, false);

            Location location = mLocationManager.getLastKnownLocation(bestProvider);
            LatLng lastLocation = new LatLng(location.getLatitude(),location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(lastLocation).title("Current location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(lastLocation));

            Toast.makeText(this, "bestProvider is " + bestProvider, Toast.LENGTH_SHORT).show();

            if (location != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17.0f));
            } else {
                Toast.makeText(this, "LastKnownLocation is null", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void centerMap(LatLng mapCenter){
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mapCenter));

        CameraPosition cameraPosition = CameraPosition.builder()
                .target(mapCenter)
                .zoom(18.0f).bearing(0f).tilt(45).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null);
    }

    public void save(View view) {
        Intent intent= new Intent(this,showData.class);
        Bundle bundle = new Bundle();
        bundle.putString("latitud", lat);
        bundle.putString("longitud", lon);
        bundle.putString("precision", prec);
        bundle.putString("tiempo", time);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void api(View view) {
        Intent intent= new Intent(this,GoogleApi.class);
        startActivity(intent);
    }
}
