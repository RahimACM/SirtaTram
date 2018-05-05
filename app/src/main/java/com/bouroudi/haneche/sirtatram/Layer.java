package com.bouroudi.haneche.sirtatram;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.kml.KmlContainer;
import com.google.maps.android.kml.KmlGeometry;
import com.google.maps.android.kml.KmlLayer;
import com.google.maps.android.kml.KmlPlacemark;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by dell on 29/12/2017.
 */

public class Layer {
    private KmlLayer layer;
    private List<LatLng> points = new ArrayList<>();

    public Layer(KmlLayer layer){
        this.layer = layer;
        for (KmlContainer c : layer.getContainers()) {
            for (KmlPlacemark p : c.getPlacemarks()) {
                KmlGeometry g = p.getGeometry();
                if (g.getGeometryType().equals("LineString")) {
                    this.points.addAll((Collection<? extends LatLng>) g.getGeometryObject());
                }
            }
        }
    }

    public KmlLayer getLayer() {
        return layer;
    }

    public double get_distance() {
        double distance = 0.0;
        int nbr_points = this.points.size();
        for(int i=0;i<nbr_points-1;i++)    distance = distance + SphericalUtil.computeDistanceBetween(this.points.get(i), this.points.get(i + 1));
        return distance;
    }
}
