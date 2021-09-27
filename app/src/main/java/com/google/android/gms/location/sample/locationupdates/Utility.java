package com.google.android.gms.location.sample.locationupdates;


import java.util.Iterator;
import java.util.Vector;

public class Utility{

    public static Vector<Point> VisitList = new Vector<Point>();


    private static double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    public static Double getDistance (Point p, Point q)
    {

//        int dx = p.getX()-q.getX();
//
//        int dy = p.getY()-q.getY();

        return distance(p.getX(),p.getY(),q.getX(),q.getY(),'K')*1000;



    }



    /**
     neighbourhood points of any point p
     **/


    public static Vector<Point> getNeighbours(Point p)
    {
        Vector<Point> neigh =new Vector<Point>();
        Iterator<Point> points = dbscan.pointList.iterator();
        while(points.hasNext()){
            Point q = points.next();
            if(getDistance(p,q)<= dbscan.radius){
                neigh.add(q);
            }
        }
        return neigh;
    }

    public static void Visited(Point d){
        VisitList.add(d);

    }

    public static boolean isVisited(Point c)
    {
        if (VisitList.contains(c))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static Vector<Point> Merge(Vector<Point> a,Vector<Point> b)
    {

        Iterator<Point> it5 = b.iterator();
        while(it5.hasNext()){
            Point t = it5.next();
            if (!a.contains(t) ){
                a.add(t);
            }
        }
        return a;
    }



//  Returns PointsList to DBscan.java

    public static Vector<Point> getList() {
        System.out.println("......getList is called....");

        Vector<Point> newList =new Vector<Point>();
        newList.clear();
        System.out.println("....getting it from ProcessPoints.hset........");
        newList.addAll(ProcessPoints.getHset());
        return newList;
    }

    public static Boolean equalPoints(Point m , Point n) {
//        System.out.println(m.getX());
//        System.out.println(n.getX());
//        System.out.println(m.getY());
//        System.out.println(n.getY());
        if((m.getX()==(n.getX()))&&(m.getY()==(n.getY())))
            return true;
        else
            return false;
    }

}
