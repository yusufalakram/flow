package flow.app.home;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import flow.app.Club;
import flow.app.R;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap googleMap;
    private TileOverlay mOverlay;

    /**
     * Currently loads dummy data
     * later will load from database
     * @param resource json file to use
     * @return list of locations in LatLng format
     */
    private ArrayList<LatLng> readItems(int resource) {
        ArrayList<LatLng> list = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < dummyClubs.size(); i++) {
            for (int j = 0; j < 15; j++) {
                double random1 = rand.nextDouble() / 10000;
                double random2 = rand.nextDouble() / 10000;
                list.add(new LatLng(dummyClubs.get(i).getLocation()[0] + random1,
                        dummyClubs.get(i).getLocation()[1] + random2));
            }
        }
        return list;
    }

    private boolean performMapSearch(String name) {
        for (int i = 0; i < dummyClubs.size(); i++) {
            if (dummyClubs.get(i).getName().toLowerCase().contains(name.toLowerCase())) {

                //Close the keyboard
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                if (googleMap != null) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(dummyClubs.get(i).getLocation()[0],
                            dummyClubs.get(i).getLocation()[1])));
                }

                dummyClubs.get(i).getMarker().showInfoWindow();
                return true;
            }
        }
        return false;
    }

    private void addHeatMap() {
        List<LatLng> list = null;

        // Get the data: latitude/longitude positions of people.
        list = readItems(R.raw.heatmap);

        // Create a heat map tile provider, passing it the latlngs
        if (list != null) {
            HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder()
                    .radius(20)
                    .data(list)
                    .build();
            // Add a tile overlay to the map, using the heat map tile provider.
            mOverlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final EditText mapSearch = (EditText) findViewById(R.id.mapSearch);
        if (mapSearch != null) {
            mapSearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        performMapSearch(mapSearch.getText().toString());
                        return true;
                    }
                    return false;
                }
            });
        }

        //Load clubs from database
        dummyClubs.add(new Club("Second Bridge", 51.379140, -2.357260, "Queue Time: 30m"));
        dummyClubs.add(new Club("Moles", 51.385047, -2.362571, ""));
        dummyClubs.add(new Club("The Nest", 51.385508, -2.360320, ""));
        dummyClubs.add(new Club("Komedia", 51.381648, -2.362141, ""));
        dummyClubs.add(new Club("Zero Zero", 51.384999, -2.360940, ""));
        dummyClubs.add(new Club("Po Na Na", 51.380825, -2.356816, ""));
        dummyClubs.add(new Club("Khoosoosi", 51.383244, -2.356857, ""));
        dummyClubs.add(new Club("Student Union", 51.379887, -2.326821, ""));

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    //Load these from database later
    private List<Club> dummyClubs = new ArrayList<>();

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;

        LatLng bath = new LatLng(51.380941, -2.360007);

        //Add all clubs to the map
        for (int i = 0; i < dummyClubs.size(); i++) {
            LatLng pos = new LatLng(dummyClubs.get(i).getLocation()[0], dummyClubs.get(i).getLocation()[1]);
            //Investigate using flatten
            Marker marker = this.googleMap.addMarker(new MarkerOptions().position(pos)
                    .title(dummyClubs.get(i).getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker))
                    .snippet(dummyClubs.get(i).getDescription()));
            dummyClubs.get(i).setMarker(marker);
        }

        //Centre map on bath
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(bath));
        this.googleMap.setMaxZoomPreference(20.0f);
        this.googleMap.setMinZoomPreference(15.5f);


        addHeatMap();
    }
}
