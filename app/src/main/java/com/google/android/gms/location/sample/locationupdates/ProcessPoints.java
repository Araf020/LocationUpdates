package com.google.android.gms.location.sample.locationupdates;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.*;
import java.util.List;

import androidx.annotation.NonNull;

public class ProcessPoints {
    public static int minpoints;
    public static int tdistance;
    public static Boolean a;
    public static Double x1;
    public static Double y1;
    public static Double x2;
    public static Double y2;
    private static String id;
    public static Vector<Point> hset = new Vector<Point>();
    public static Vector<List> trl = new Vector<List>();
    static ArrayList<Point> temp =new ArrayList<Point>();
    private final static String newline = "\n";
    static Boolean Y = false;
    static FirebaseDatabase database;
    static DatabaseReference databaseReference;
    public static ArrayList<Object> databaseList;

    /**
     * Constructor
     */

    public ProcessPoints() {

    }

    /**
        ============Utility method for maping of retrieved info from database=========
     */
    public static void  CollectZone(Map<String,Object> users){
        System.out.println("collecting data.....");
        ArrayList<Object> zones = new ArrayList<>();
        for (Map.Entry<String,Object> entry : users.entrySet()){
//            HashMap singleuser = (HashMap) entry.getValue();
//            zones.add((String) singleuser.get("zone"));
            zones.add(entry.getValue());
        }
        System.out.println(zones.toString());
        databaseList = zones;
    }

    /**
        extracting data from Database
     */
    public static   void read(){
        System.out.println("reading data in ProcessPoint Class.....");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("person");

        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                CollectZone((Map<String, Object>) (snapshot.getValue()));

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
        System.out.println("........exiting read() in Processpoint class.....");

    }


    public static void addpoints() {
//       read();
       System.out.println("...i m in addpoints....");
       databaseList = ScrollingActivity.databaseList;
       System.out.println("....got databaseList from ScrollingActivity.class....");
       try {
           System.out.println(databaseList.toString());
       }
       catch (Exception e){
           System.out.println("..........couldn't get the databaseList...");
           e.printStackTrace();
       }
       for (Object map:databaseList) {
           Map<String, Object> hmap = (Map<String, Object>) map;


//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.getValue()!=null){
//                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
//                        String k = " "+ dataSnapshot.getValue().toString();
//                        System.out.println(k);
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
           try {
               System.out.println("...getting the coordinates.....");
               x1 =Double.parseDouble(hmap.get("lat").toString());
               y1 = Double.parseDouble(hmap.get("lang").toString());
               id =(String) hmap.get("id");
//               System.out.println(x1);
//               System.out.println(y1);
               a = true;
           } catch (Exception e) {
               a = false;
           }
           Point np = new Point(id,x1, y1);

           if (a) {
               System.out.println("...got the coordinates.....");
               for (Point f : hset) {

                   if (Utility.equalPoints(f, np)) {
                       Y = true;
                       System.out.println("already exists!");
                       break;
                   } else {
                       Y = false;
                   }

               }

               if (!Y) {
                   System.out.println("...adding to hset...");
                   hset.add(np);
                   System.out.println("...added!...");


               }

           } else {
               System.out.println("Somethig went Wrong! ");

           }
       }
    }

    public static Vector<Point> getHset(){
        addpoints();
        return hset;
    }
}
