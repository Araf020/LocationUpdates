package com.google.android.gms.location.sample.locationupdates;

import java.util.Comparator;

class SortByDistance implements Comparator<Point>
{
    // Used for sorting in ascending order of
    // roll number


    @Override
    public int compare(Point o1, Point o2) {
        return Integer.parseInt( Double.toString(Utility.getDistance(o1,o2)));
    }
}

