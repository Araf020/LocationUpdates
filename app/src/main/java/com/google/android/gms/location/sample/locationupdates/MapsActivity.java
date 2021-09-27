package com.google.android.gms.location.sample.locationupdates;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaRouter;
import android.os.Build;
import android.os.Looper;
import androidx.core.app.ActivityCompat;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.teamspaghetti.easyroutecalculation.EasyRouteCalculation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback  {

    public GoogleMap mGoogleMap;
//    public EasyRouteCalculation easyRouteCalculation;
    GoogleApiClient googleApiClient;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    protected Location mLastLocation;
    private Geocoder geocoder;
    private ArrayList<Point> pointArrayList;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
//    protected EasyRouteCalculation getEasyRouteCalculation(){
//        return easyRouteCalculation;
//    }
//    protected GoogleMap getmGoogleMap(){
//        return mGoogleMap;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        System.out.println("i m on Maps");


//        getSupportActionBar().setTitle("MapTest");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(this);
        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

    }

//    @Override
//    public void onPause() {
//        super.onPause();
//
//        //stop location updates when Activity is no longer active
//        if (mFusedLocationClient != null) {
//            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
//        }
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

//        EasyRouteCalculation easyRouteCalculation = new EasyRouteCalculation(this,mGoogleMap);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                System.out.println("location granted!..");
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback,
                        Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission

                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback,
                    Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
            System.out.println("location granted!..");

        }
//        easyRouteCalculation = new EasyRouteCalculation(this,mGoogleMap);
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {

            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }
                //move map camera
                //here's the showing part ......
//                LatLng loc = easyRouteCalculation.getCurrentLocation();
//                LatLng loc = locationResult.getLastLocation();
                LatLng latLng2 = new LatLng(24.964991,88.903932);
                LatLng latLng1 = new LatLng(24.833337,88.920580);
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                BitmapDrawable bitmapDrawable =(BitmapDrawable) getResources().getDrawable(R.drawable.images__9___9_) ;
                Bitmap b = bitmapDrawable.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b,84,84,false);

                /**
                 * Getting Locations
                 */
                pointArrayList = ScrollingActivity.choosenOnes;

//                easyRouteCalculation.calculateRouteBetweenTwoPoints(latLng1,latLng2);
                mGoogleMap.addMarker(new MarkerOptions().position(latLng2).title("Hat"));
                mGoogleMap.addMarker(new MarkerOptions().position(latLng).title("You"));
                mGoogleMap.addMarker(new MarkerOptions().position(latLng1).title("nouga"));
                for(Point p : pointArrayList){
                    LatLng lng  = new LatLng(p.getX(),p.getY());
                    try {
                        mGoogleMap.addMarker(new MarkerOptions().position(lng).title((geocoder.getFromLocation(p.getX(),p.getY(),1)).get(0).getAddressLine(0)).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
//                        mGoogleMap.addMarker(new MarkerOptions().position(lng).title((geocoder.getFromLocation(p.getX(),p.getY(),1)).get(0).getFeatureName()).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                LatLng lastpoint = new LatLng(pointArrayList.get(0).getX(),pointArrayList.get(0).getY());
                CameraPosition cameraPosition2 = new CameraPosition.Builder().target(
                        new LatLng(pointArrayList.get(0).getX(),pointArrayList.get(0).getY())).zoom(16).build();

//                easyRouteCalculation.setTravelMode("driving");
//                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latLng1.latitude, latLng1.longitude)).zoom(16).build();

//                System.out.println(easyRouteCalculation.getDurationBetweenTwoPoints(loc,latLng1));
//                System.out.print("distance1: ");
//                System.out.println(easyRouteCalculation.getDistanceBetweenPoints(loc,latLng1));
//                System.out.print("distance2: ");
//                System.out.println(easyRouteCalculation.getDistanceBetweenPoints(loc,latLng2));



//                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

//                GoogleMap map;
////                GoogleMap m = map;
//                try {
//
//
//                    EasyRouteCalculation easyRouteCalculation = new EasyRouteCalculation(this,mGoogleMap);
//                    easyRouteCalculation.calculateRouteBetweenTwoPoints(latLng,latLng2);
//                }
//                catch (Exception e){
//                    e.printStackTrace();
//                }


//
                Marker m = mGoogleMap.addMarker(new MarkerOptions().position(latLng2).draggable(true));
                latLng2 = m.getPosition();
//                CameraPosition cameraPosition2 = new CameraPosition.Builder().target(new LatLng(latLng2.latitude, latLng2.longitude)).zoom(16).build();

                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition2));


//                Marker hat = mGoogleMap.addMarker()
//                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng2));

            }
        }
    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                System.out.println(".......requesting permission..........");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {
                    // if not allow a permission, the application will exit
                    System.out.println("...............................exiting..................................");
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                    System.exit(0);
                }
            }
        }
    }

}