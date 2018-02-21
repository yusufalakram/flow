package flow.app.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import flow.app.Club;
import flow.app.R;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        //Load clubs from database
        dummyClubs.add(new Club("Second Bridge", 51.379140, -2.357260));
        dummyClubs.add(new Club("Moles", 51.385047, -2.362571));
        dummyClubs.add(new Club("The Nest", 51.385508, -2.360320));
        dummyClubs.add(new Club("Komedia", 51.381648, -2.362141));
        dummyClubs.add(new Club("Zero Zero", 51.384999, -2.360940));
        dummyClubs.add(new Club("Po Na Na", 51.380825, -2.356816));
        dummyClubs.add(new Club("Khoosoosi", 51.383244, -2.356857));
        dummyClubs.add(new Club("Student Union", 51.379887, -2.326821));

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


        // Add a marker in Bath.
        // and move the map's camera to the same location.
        LatLng bath = new LatLng(51.380941, -2.360007);

        //Add all clubs to the map
        for (int i = 0; i < dummyClubs.size(); i++) {
            LatLng pos = new LatLng(dummyClubs.get(i).getLocation()[0], dummyClubs.get(i).getLocation()[1]);
            googleMap.addMarker(new MarkerOptions().position(pos)
                    .title(dummyClubs.get(i).getName()));
        }

        //Centre map on bath
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(bath));
        googleMap.setMaxZoomPreference(20.0f);
        googleMap.setMinZoomPreference(15.5f);
    }
}
