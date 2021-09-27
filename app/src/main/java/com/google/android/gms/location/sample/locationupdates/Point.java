package com.google.android.gms.location.sample.locationupdates;


public class Point
{
    private Double x;
    private Double y;
    private String id;

    Point(String id,Double a, Double b)
    {
        this.id = id;
        x=a;
        y=b;
    }
    Point(Double a, Double b)
    {

        x=a;
        y=b;
    }


    public Double getX ()
    {

        return x;

    }


    public Double getY ()
    {

        return y;

    }
    public  String getId(){
        return id;
    }
}
