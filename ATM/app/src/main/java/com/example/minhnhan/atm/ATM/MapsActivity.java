package com.example.minhnhan.atm.ATM;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.com.minhnhan.models.AtmDetail;
import com.com.minhnhan.models.DistanceAB;
import com.example.minhnhan.atm.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.minhnhan.sever.AsyncAtm;
import com.minhnhan.sever.AsyncListener;
import com.minhnhan.sever.AsyncWay;
import com.minhnhan.sever.CheckConnect;
import com.minhnhan.sever.DataManager;
import com.minhnhan.sever.DrawWay;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "MapsActivity";
    private LatLng myCurrent = new LatLng(0, 0);//my LatLong position on map
    private GoogleMap mMap;
    private AtmDetail data;//all atm data on server

    //draw annination on map
    Polyline line;
    Circle circle;

    //Google API Client for get position
    LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    Marker currLocationMarker;

    //all ATM marker on map
    ArrayList<Marker> atmListMarker = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        final ImageButton atmNearBy = (ImageButton) findViewById(R.id.imgbtn_atm_nearby);
        atmNearBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove all atm mark in my position range
                for (int i = 0; i < atmListMarker.size(); i++) {
                    atmListMarker.get(i).remove();
                }
                atmListMarker.clear();
                //remove line path on map
                if(line!=null)
                    line.remove();
                //get atm list
                atmNearBy(data);
                //move to my position
                moveToMyLocation(myCurrent);
                decoMyLocation(myCurrent);
            }
        });
    }

    private void getMyLocation() {
        buildGoogleApiClient();
        mGoogleApiClient.connect();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("debug", "maps is ready");
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                if (circle != null)
                    circle.remove();
                final LatLng destinaton = new LatLng(arg0.getPosition().latitude, arg0.getPosition().longitude);
                if (myCurrent.latitude != destinaton.latitude && myCurrent.longitude != destinaton.longitude) {
                    String findWayLink = DrawWay.getDirectionsUrl(myCurrent, destinaton);
                    AsyncWay getData = new AsyncWay(new AsyncListener() {
                        @Override
                        public void onAsyncComplete() {
                            //data line path from google
                            String wayData = DataManager.getInstance().getWayJson();
                            //draw path to the map
                            line = DrawWay.drawPath(wayData, mMap, line, myCurrent, destinaton);
                        }
                    });
                    getData.execute(findWayLink);
                }
                return false;
            }
        });
        // my location
        CheckConnect connect = new CheckConnect();
        if (connect.isNetworkAvailable(this)) {
            if (connect.isGPSEnabled(this)) {
                getMyLocation();
                // lay ds ATM
                String link = "http://find-atm.apphb.com/v1/GetAtmList";
                AsyncAtm getData = new AsyncAtm(new AsyncListener() {
                    @Override
                    public void onAsyncComplete() {
                        atmNearBy(DataManager.getInstance().getAtmDetail("atm"));

                    }
                });
                getData.execute(link);
            } else {
                //Show message about GPS problem and exit this app
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                dlgAlert.setMessage("Xin vui lòng kết nối GPS!");
                dlgAlert.setTitle("Không thể truy cập GPS!");
                dlgAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //dismiss the dialog
                                //finish();
                                //System.exit(0);
                            }
                        });
                dlgAlert.create().show();
            }


        } else {
            //Show message about Internet problem and exit this app
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Xin vui lòng kết nối Internet!");
            dlgAlert.setTitle("Không thể truy cập Internet!");
            dlgAlert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog
                            //finish();
                            //System.exit(0);
                        }
                    });
            dlgAlert.create().show();
        }


    }

    private void moveToMyLocation(LatLng myNewCurrent) {
        Log.d("debug", "move to current location");
        if (currLocationMarker != null)
            currLocationMarker.remove();
        myCurrent = myNewCurrent;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(myCurrent);
        markerOptions.title("You are here!").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_user_marker));
        currLocationMarker = mMap.addMarker(markerOptions);
    }

    private void addLocationMarker(double lat, double lng, String discription) {
        LatLng latLng = new LatLng(lat, lng);

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(discription)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_atm_marker));
        Marker atmLocationMarker = mMap.addMarker(options);
        atmListMarker.add(atmLocationMarker);
    }


    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "onConnected", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            //place marker at current position
            //mGoogleMap.clear();
            LatLng myNewLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            moveToMyLocation(myNewLocation);
            double a = DistanceAB.distance(myCurrent, myNewLocation);
            decoMyLocation(myCurrent);

        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(6000); //60 seconds
        mLocationRequest.setFastestInterval(5000); //50 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "onConnectionSuspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        //place marker at current position
        //mGoogleMap.clear();

        LatLng myNewLocation = new LatLng(location.getLatitude(), location.getLongitude());
        moveToMyLocation(myNewLocation);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "onConnectionFailed", Toast.LENGTH_SHORT).show();
    }

    protected synchronized void buildGoogleApiClient() {
        Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    //draw marker for all atm nearby 1km
    public void atmNearBy(AtmDetail atmDetail) {
        data = atmDetail;
        for (int i = 0; i < data.atmDetail.size(); i++) {
            LatLng temp = new LatLng(data.atmDetail.get(i).lat, data.atmDetail.get(i).lng);
            if (DistanceAB.distance(myCurrent, temp) < 1000)
                addLocationMarker(data.atmDetail.get(i).lat, data.atmDetail.get(i).lng, data.atmDetail.get(i).name);
        }
    }

    //draw circle position range and move camera to my position
    public void decoMyLocation(LatLng myNewCurrent) {
        //Range 1km from my position
        if (circle != null)
            circle.remove();
        circle = mMap.addCircle(new CircleOptions()
                .center(myNewCurrent)
                .radius(1000)
                .strokeColor(Color.rgb(0, 179, 253))
                .strokeWidth(1)
                .fillColor(Color.argb(18, 0, 179, 253)));
        //zoom to current position:
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(myNewCurrent).zoom(14).build();

        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }
}
