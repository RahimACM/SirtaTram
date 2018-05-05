package com.bouroudi.haneche.sirtatram;

import android.content.res.Resources;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Station> stations = new ArrayList<>();
    private List<Layer> layers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.stations_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinners
        Spinner spinner = (Spinner)findViewById(R.id.source_station);
        spinner.setAdapter(adapter);
        spinner = (Spinner)findViewById(R.id.destination_station);
        spinner.setAdapter(adapter);


        stations.add(new Station("Ben Abdelmalek Remdane (Terminus)", new LatLng(36.35834950, 6.60567797)));
        stations.add(new Station("Belle Vue", new LatLng(36.35602055, 6.60349124)));
        stations.add(new Station("Kadous Boumedous", new LatLng(36.35075267, 6.60093988)));
        stations.add(new Station("Emir Abdelkader", new LatLng(36.34591075, 6.60236143)));
        stations.add(new Station("Fadhila Saadane", new LatLng(36.34850011, 6.60788824)));
        stations.add(new Station("Z.I Palma", new LatLng(36.34643589, 6.61212162)));
        stations.add(new Station("Université Mentouri", new LatLng(36.34072394, 6.61771750)));
        stations.add(new Station("Résidence Universitaire Mentouri", new LatLng(36.33374303, 6.61669502)));
        stations.add(new Station("Cité Kheznadar", new LatLng(36.32268517, 6.62109795)));
        stations.add(new Station("Zouaghi Sliman (Terminus)", new LatLng(36.31036182, 6.61940645)));

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            Switch mySwitch = (Switch) findViewById(R.id.mode_nuit_switch);
            mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        boolean success = mMap.setMapStyle(
                                MapStyleOptions.loadRawResourceStyle(
                                        getApplicationContext(), R.raw.night_style_json));

                        if (!success) {
                            //Log.e(TAG, "Style parsing failed.");
                        }
                    }
                    else {
                        boolean success = mMap.setMapStyle(
                                MapStyleOptions.loadRawResourceStyle(
                                        getApplicationContext(), R.raw.standard_style_json));

                        if (!success) {
                            //Log.e(TAG, "Style parsing failed.");
                        }
                    }
                }
            });
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            //Log.e(TAG, "Can't find style. Error: ", e);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;

            KmlLayer layer = new KmlLayer(mMap, R.raw.tram_constantine_itineraire, getApplicationContext());
            layer.addLayerToMap();


            List<KmlLayer> list = new ArrayList<>();
            list.add(new KmlLayer(mMap, R.raw.tram_constantine_morceau1, getApplicationContext()));
            list.add(new KmlLayer(mMap, R.raw.tram_constantine_morceau2, getApplicationContext()));
            list.add(new KmlLayer(mMap, R.raw.tram_constantine_morceau3, getApplicationContext()));
            list.add(new KmlLayer(mMap, R.raw.tram_constantine_morceau4, getApplicationContext()));
            list.add(new KmlLayer(mMap, R.raw.tram_constantine_morceau5, getApplicationContext()));
            list.add(new KmlLayer(mMap, R.raw.tram_constantine_morceau6, getApplicationContext()));
            list.add(new KmlLayer(mMap, R.raw.tram_constantine_morceau7, getApplicationContext()));
            list.add(new KmlLayer(mMap, R.raw.tram_constantine_morceau8, getApplicationContext()));
            list.add(new KmlLayer(mMap, R.raw.tram_constantine_morceau9, getApplicationContext()));

            for (KmlLayer kml_layer:list
                 ) {
                layers.add(new Layer(kml_layer));
            }

            for (Station station:stations
                 ) {
                mMap.addMarker(new MarkerOptions()
                        .position(station.getLat_lng())
                        .title(station.getName())
                        .snippet("Latitude : "+station.getLat_lng().latitude+"    Longitude : "+station.getLat_lng().longitude)
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.if_citycons_train_1342923))
                );
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClick_submit_button(View view) {
        int source_pos = ((Spinner) findViewById(R.id.source_station)).getSelectedItemPosition();
        int destination_pos = ((Spinner) findViewById(R.id.destination_station)).getSelectedItemPosition();

        if(source_pos != destination_pos){
            final int max_pos = source_pos>destination_pos? source_pos : destination_pos;
            final int min_pos = source_pos<destination_pos? source_pos : destination_pos;
            Double distance = 0.0;
            for (Layer layer:layers
                 ) {
                layer.getLayer().removeLayerFromMap();
            }

            try {
                for(int i=min_pos;i<max_pos;i++){
                    distance = distance + layers.get(i).get_distance();
                    layers.get(i).getLayer().addLayerToMap();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            int time = (int)(distance*3*60/1000);
            distance = distance/1000;
            Snackbar.make(view, "Distance : "+distance.toString().substring(0,distance.toString().indexOf(".")+4)+" KM\n" +
                    "Temps estimé : "+(time/60)+" m, "+(time%60)+" s", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETOUR", new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            for(int i=min_pos;i<max_pos;i++)
                                layers.get(i).getLayer().removeLayerFromMap();
                        }
                    })
                    .show();

            mMap.moveCamera(CameraUpdateFactory.newLatLng(stations.get(source_pos).getLat_lng()));
        }
    }
}