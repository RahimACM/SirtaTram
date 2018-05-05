package com.bouroudi.haneche.sirtatram;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by dell on 28/12/2017.
 */

public class Station {
    private String name;
    private LatLng lat_lng;

    public Station(String name, LatLng lat_lng){
        this.name = name;
        this.lat_lng = lat_lng;
    }

    public String getName() {
        return name;
    }

    public LatLng getLat_lng() {
        return lat_lng;
    }
}