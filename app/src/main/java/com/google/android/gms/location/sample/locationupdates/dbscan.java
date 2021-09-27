package com.google.android.gms.location.sample.locationupdates;


import java.util.*;


public class dbscan
{
    public static Double radius =7.0; /**
        radius in Kilometer
 */
//    public static int e=7;
    public static int minpt = 3;/**
 minimum number of points to form a cluster
 */


    public static Vector<List> resultList = new Vector<List>();

    public static Vector<Point> pointList = Utility.getList();

    public static Vector<Point> Neighbours ;





    public static Vector<List> applyDbscan()
    {
        System.out.println("...........applying dbscan....");
        resultList.clear();
        pointList.clear();
        Utility.VisitList.clear();
        System.out.println("..........getting pointlist from utility.java..........");
        pointList=Utility.getList();
        System.out.println(pointList.toString());

        int index2 =0;


        while (pointList.size()>index2){
            System.out.println("gettting from pointList");
            Point p =pointList.get(index2);

            if(!Utility.isVisited(p)){

                Utility.Visited(p);

                Neighbours =Utility.getNeighbours(p);


                if (Neighbours.size()>=minpt){


                    int ind=0;
                    while(Neighbours.size()>ind){

                        Point r = Neighbours.get(ind);
                        if(!Utility.isVisited(r)){
                            Utility.Visited(r);
                            Vector<Point> Neighbours2 = Utility.getNeighbours(r);
                            if (Neighbours2.size() >= minpt){
                                Neighbours=Utility.Merge(Neighbours, Neighbours2);
                            }
                        } ind++;
                    }



                    System.out.println("N"+Neighbours.size());
                    resultList.add(Neighbours);}


            }index2++;
        }
        System.out.println("exitting applyscan");
        System.out.println("...........printing resultList inplace...........");
//        System.out.println();

        return resultList;
    }

}









