package com.google.android.gms.location.sample.locationupdates;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.json.JSONObject;
import  org.json.JSONArray;
import  org.json.JSONException;




public class ScrollingActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private Geocoder geocoder;
    public static Location lastLocation;
    private FusedLocationProviderClient mflp;
    public TextView userinfo;
    public TextView nearest;
    public TextView others;
    public static ArrayList<Object> databaseList;
    public static ArrayList<Point> choosenOnes;
    public static HashMap<String,Double> Distances;
    public static  String UserInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("BUET-BUS");
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle("Distances");
        FloatingActionButton fab = findViewById(R.id.fab);
        databaseList = new ArrayList<>();
        choosenOnes = new ArrayList<>();
        geocoder = new Geocoder(this);

        mflp = LocationServices.getFusedLocationProviderClient(this);



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
        Task task = mflp.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                if (o!=null){
                    lastLocation = (Location) o;
                }
            }
        });

/*
 *    JSONObject json = null;
      try {
            json = readJsonFromUrl("http://dev.virtualearth.net/REST/V1/Routes/Driving?wp.0=23.747447%2C90.370657&wp.1=23.745323%2C90.347842&avoid=minimizeTolls&key=Aq2XEbcC_LjXALn4-d85iC3PyeIqCP1p-jcDvxLPNz0-_dqFyQFwnX81ocqkjvn_");
       } catch (IOException e) {
          e.printStackTrace();
       } catch (JSONException e) {
           e.printStackTrace();
       }

 */

//        try {
//        assert json != null;
//        System.out.println(json.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("lol! couldn't get json data to mappppppppp");
//        }

        databaseReference = FirebaseDatabase.getInstance().getReference("person");
        userinfo = findViewById(R.id.you);
        nearest = findViewById(R.id.nearest);




        System.out.println("i m on Scroll");
//        for(String s:getUserDetalis(getID())) {
//            texts.setText(s);
//        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextAct();
//                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
//                startActivity(intent);

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        System.out.println("......reading data on ScrollActivity........");

        //Calling for user details....

        UserInfo = getUserInfoByLocation(23.747417,90.370665);

        userinfo.setText(((UserInfo != null) ? UserInfo:"Something Went Wrong (-_-) \n No Info. Found!"));
        System.out.println("-------USer Info extracted----------");
        read();

        System.out.println("..........applying dbscan.........");



    }



    public void addPerson(String id, String zone, String lat, String lang){
        Person person = new Person(id,zone,lat,lang);
        databaseReference.child(id).setValue(person);
//        FirebaseDatabase.getInstance().getReference().child("person").child(id).setValue(person);
//
    }


    private String getID(){
        return getIntent().getStringExtra("ID");
    }

    private void getUserDetalis(String id){

        System.out.println(".........getting user details.....");
        findPerson(id);
    }

    public   void findPerson(String id){
        System.out.println("....findperson is called.");
        Query searchQuery = databaseReference.orderByChild("id").equalTo(id);
//        final ArrayList<String> details = new ArrayList<>();
        String y;
        final String[] details = {" "};
        searchQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                System.out.println("........I m in OnchildAdded!.......");
                Iterable<DataSnapshot> snapshotIterator = snapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                int i=0;
                StringBuilder x = new StringBuilder();
                while (iterator.hasNext()){
                    System.out.print("Found: ");
//                    System.out.println(iterator.next().getValue().toString());
                    x.append(iterator.next().getValue().toString()).append(" ");



                }
                ArrayList<Address> addresses = null;
                try {
//                    addresses = (ArrayList<Address>) geocoder.getFromLocation(lastLocation.getLatitude(),lastLocation.getLongitude(),3);
//                    addresses = (ArrayList<Address>) geocoder.getFromLocation(23.863588,90.396012,1);
                    addresses = (ArrayList<Address>) geocoder.getFromLocation(23.747417,90.370665,1);
//                    addresses = (ArrayList<Address>) geocoder.getFromLocation(23.725957,90.391803,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String feature = addresses.get(0).getFeatureName();
                String locality = addresses.get(0).getLocality();
                String  subLocality = addresses.get(0).getSubLocality();
                String road = addresses.get(0).getThoroughfare();
                StringBuilder street = new StringBuilder();
                for (int j =0; j< addresses.get(0).getMaxAddressLineIndex();j++){
                    street.append(addresses.get(0).getAddressLine(i)).append(" ");
                }

                String dets = Double.toString(lastLocation.getLatitude()) + ", " + Double.toString(lastLocation.getLongitude()) +
                       "\n" + feature +", " +  locality +  "\n" + road + "\n" + street.toString();
                System.out.println(dets);
                System.out.println("chudina");
                System.out.println(addresses.get(0).getAddressLine(0));

                System.out.println("chudina");
                System.out.println(street.toString());
                System.out.println("addresses printed.........!");
                String textstoSet = "You are currently at " +(subLocality!= null ? (subLocality.toUpperCase() +", "):"") +
                        addresses.get(0).getAddressLine(0).toUpperCase() + " (approx.)";
                UserInfo = textstoSet;
//                userinfo.setText(textstoSet);
//                System.out.println(databaseList.toString());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("not found");
            }
        });

    }

    private void nextAct()
    {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent);
    }

/**
 *  JSONObject reading from URL

 */

public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
    Map<String, Object> retMap = new HashMap<String, Object>();

    if(json != JSONObject.NULL) {
        retMap = toMap(json);
    }
    return retMap;
}
    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext())
        {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }

            map.put(key, value);
        }
        return map;
    }
    private  static String readAll(Reader rd) throws IOException{
        StringBuilder sb = new StringBuilder();
        int cp ;
        while ((cp = rd.read()) != -1){
            sb.append((char) cp);

        }
        return sb.toString();
    }
    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);

        } finally {
            is.close();
        }
    }
    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
    public void CollectZone(Map<String,Object> users){
    System.out.println("collecting info.");
        ArrayList<Object> zones = new ArrayList<>();
        for (Map.Entry<String,Object> entry : users.entrySet()){
//            HashMap singleuser = (HashMap) entry.getValue();
//            zones.add((String) singleuser.get("zone"));
            zones.add(entry.getValue());
        }
        System.out.println("..........printing extracted data...");
        System.out.println(zones.toString());
        databaseList=zones;

//        for (Object map:databaseList) {
//            Map<String, Object> hmap = (Map<String, Object>) map;
//            try {
//                String  x1 = (String) hmap.get("lat");
//                String y1 = (String) hmap.get("lang");
//                System.out.println(x1);
//                System.out.println(y1);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }
    public  void read(){
    System.out.println("reading data.......");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("person");

        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CollectZone((Map<String, Object>) (snapshot.getValue()));
                try {
                    Vector<List> result = dbscan.applyDbscan();
                    System.out.println("...getting the groups...");
                    boolean a = false;
                    for(List l : result){
                        System.out.println("............newGroup.............");
                        System.out.println(l.size());
                        System.out.println("**********************************");
                        for(Object p : l){
                            Point point = (Point) p;
                            System.out.print(point.getX());
                            System.out.print(", ");
                            System.out.println(point.getY());
                            if(a==false){
                                choosenOnes.add(point);
                                a= true;
                            }
                        }
                        a = false;
                    }
                    System.out.println(".........choosen ones and  them.......");
                    System.out.println(choosenOnes.size());
                    System.out.println("************************************");
                    for(Point p : choosenOnes){
                        System.out.print(p.getX());
                        System.out.print(", ");
                        System.out.println(p.getY());
                    }
                }
                catch (Exception e){
                    System.out.println("........databaselist is empty...");
                    e.printStackTrace();
                }

                try {
                    showSortedLocations(choosenOnes);
                    setOthers();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {

                    setOthers();

                } catch (IOException e) {
                    e.printStackTrace();
                }

//                if (snapshot.getValue()!=null){
//                    ArrayList<Person> p = new ArrayList<>();
//                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
////                        System.out.println(dataSnapshot.getChildren().toString());
////                        System.out.println(dataSnapshot.getChildren().toString());
//
////                        Person k = dataSnapshot.getValue(Person.class);
////                        p.add(k);
//                    }
////                    for(Person pk: p ){
////                        System.out.println(pk.getID());
////                        System.out.println(pk.getZone());
////                    }
//                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        System.out.println("exitting read()....");
        return;
    }


    public  void showSortedLocations(ArrayList<Point> points) throws IOException {

        Distances = new HashMap<>();
        Point source = new Point(lastLocation.getLatitude(),lastLocation.getLongitude());

        for(Point p: points){

            Double dist = Utility.getDistance(source,p);
            if (dist!=0){
                Distances.put(p.getId(), dist);
                System.out.print(p.getId());
                System.out.println(": ");
                System.out.println(dist);
            }
        }
        HashMap<String,Double> temp;
        Distances = sortHashmapByValue(Distances);
        System.out.println("***************nearest*************");
//        System.out.println(temp);
        System.out.println("****************nearest dist****************");
//        System.out.println(((LinkedList<Double>)(temp.values())).get(0));
        System.out.println((Distances.values().toArray()[0]));
        System.out.println("***********choosen ones dist************");
        System.out.println(Distances);
        LatLng nearestLocation = NearestPoint(choosenOnes);

        String details = geocoder.getFromLocation(nearestLocation.latitude,nearestLocation.longitude,1).get(0).getAddressLine(0).toUpperCase();


//        String dets =  "Nearest one is at " + NearestPoint(choosenOnes).latitude + ", " + NearestPoint(choosenOnes).longitude  + "\n"
//                + "and " + Double.toString(Math.round(((Double) Distances.values().toArray()[0])*100.0)/100.0) + "m away";
        String dets =  "Nearest one is at " + details+ "\n"
                + "and " + Double.toString(Math.round(((Double) Distances.values().toArray()[0])*100.0)/100.0) + " m away";

        nearest.setText(dets);

//        System.out.println(Distances.get(((ArrayList<String>)temp.keySet()).get(0)));


    }
    public  static  HashMap<String,Double> sortHashmapByValue(HashMap<String,Double> hm){
        List<Map.Entry<String,Double>> list =
                new LinkedList<Map.Entry<String, Double>> (hm.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        HashMap<String,Double> temp  = new LinkedHashMap<String, Double>();

        for (Map.Entry<String,Double> aa: list){
            temp.put(aa.getKey(),aa.getValue());
        }
        return temp;
    }



    public static LatLng NearestPoint(ArrayList<Point> points ){
        if(Distances != null) {
            LatLng lng ;
            String id = (String) Distances.keySet().toArray()[0];
            for (Point p : points) {
                if (p.getId() == id) {
                    lng = new LatLng(p.getX(), p.getY());
                    return lng;
                }
            }


        }
        return null;
    }

    public  String getUserInfoByLocation(Double Lat, Double Long){

        ArrayList<Address> addresses = null;
        try {
//                    addresses = (ArrayList<Address>) geocoder.getFromLocation(lastLocation.getLatitude(),lastLocation.getLongitude(),3);
//                    addresses = (ArrayList<Address>) geocoder.getFromLocation(23.863588,90.396012,1);
            addresses = (ArrayList<Address>) geocoder.getFromLocation(Lat,Long,1); //23.747417,90.370665
//                    addresses = (ArrayList<Address>) geocoder.getFromLocation(23.725957,90.391803,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String feature = addresses.get(0).getFeatureName();
        String locality = addresses.get(0).getLocality();
        String  subLocality = addresses.get(0).getSubLocality();
        String road = addresses.get(0).getThoroughfare();
        StringBuilder street = new StringBuilder();
        for (int j =0; j< addresses.get(0).getMaxAddressLineIndex();j++){
            street.append(addresses.get(0).getAddressLine(j)).append(" ");
        }

//        String dets = Double.toString(lastLocation.getLatitude()) + ", " + Double.toString(lastLocation.getLongitude()) +
//                "\n" + feature +", " +  locality +  "\n" + road + "\n" + street.toString();
//        System.out.println(dets);
//        System.out.println("chudina");
//        System.out.println(addresses.get(0).getAddressLine(0));

        System.out.println("chudina");
        System.out.println(street.toString());
        System.out.println("addresses printed.........!");
        String textstoSet = "You are currently at " +(subLocality!= null ? (subLocality.toUpperCase() +", "):"")
                + addresses.get(0).getAddressLine(0).toUpperCase() + " (approx.)";
       return  textstoSet;

    }

    public void  setOthers() throws IOException {
    System.out.println("------setothers called-------");

        LatLng latLng = null;
        String[] othersLocations = new String[choosenOnes.size()];
        StringBuilder ss = new StringBuilder(" ");
        if(Distances!=null){

            for (int i =1;i<Distances.size();i++){
                String id = (String) Distances.keySet().toArray()[i];
                latLng = getLocationByid(choosenOnes,id);
                Location loc = new Location("");
                loc.setLatitude(latLng.latitude);
                loc.setLongitude(latLng.longitude);
//                String s = Integer.toString(i) + ". at " +  geocoder.getFromLocation(latLng.latitude,latLng.longitude,1).get(0).getAddressLine(0).toUpperCase() + " and " + lastLocation.distanceTo(loc);
                String s = Integer.toString(i) + ". at " +  geocoder.getFromLocation(latLng.latitude,latLng.longitude,1).get(0).getAddressLine(0).toUpperCase() + " and " + Double.toString((Math.round(Distances.get(id))*100.0)/100.0) + "m away.\n";
                othersLocations[i] = s;
//                othersLocations.add(s);
                ss.append(s);
            }
        }
        System.out.println("---------others-------------");
//        System.out.println(othersLocations);
        others = findViewById(R.id.others);
        others.setText(ss.toString());

//        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.activity_scrolling,othersLocations);
//        ListView listView = findViewById(R.id.lists);
//        listView.setAdapter(adapter);

    }
    public LatLng getLocationByid(ArrayList<Point> points,String id){

        for(Point p : points){
            if (p.getId() == id){
                LatLng latLng = new LatLng(p.getX(),p.getY());
                return latLng;
            }


        }
        return null;

    }


}
