package flow.app.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import flow.app.Club;
import flow.app.R;
import flow.app.club.ClubPageActivity;
import flow.app.home.adapters.ExpandableListAdapter;
import flow.app.home.adapters.ExpandedMenuModel;
import flow.app.home.listeners.SwipeListener;
import flow.app.listview.ListViewActivity;
import flow.app.profile.MyProfileActivity;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {


    final private float opacity = 0.3f;
    private EditText mapSearch;
    private GoogleMap googleMap;
    private TileOverlay mOverlay;
    private DrawerLayout mDrawerLayout;
    ExpandableListAdapter mMenuAdapter;
    ExpandableListView expandableList;
    List<ExpandedMenuModel> listDataHeader;
    HashMap<ExpandedMenuModel, List<String>> listDataChild;


    private void closeKeyboard() {
        //Close the keyboard
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * Currently loads dummy data
     * later will load from database
     * @param resource json file to use
     * @return list of locations in LatLng format
     */
    private ArrayList<LatLng> readItems(int resource) {
        //TODO: get real data from database
        ArrayList<LatLng> list = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < dummyClubs.size(); i++) {
            int z = 15;
            if (dummyClubs.get(i).getName().equals("Khoosoosi"))
                z = 1;
            if (dummyClubs.get(i).getName().equals("Second Bridge"))
                z = 35;
            if (dummyClubs.get(i).getName().equals("Moles"))
                z = 2;
            if (dummyClubs.get(i).getName().equals("The Nest"))
                z = 7;
            if (dummyClubs.get(i).getName().equals("Komedia"))
                z = 12;
            if (dummyClubs.get(i).getName().equals("Zero Zero"))
                z = 4;
            if (dummyClubs.get(i).getName().equals("Po Na Na"))
                z = 1;
            if (dummyClubs.get(i).getName().equals("Student Union"))
                z = 8;
            for (int j = 0; j < z; j++) {
                double random1 = rand.nextDouble() / 7000;
                double random2 = rand.nextDouble() / 7000;
                list.add(new LatLng(dummyClubs.get(i).getLocation()[0] + random1,
                        dummyClubs.get(i).getLocation()[1] + random2));
            }
            list.add(new LatLng(51.379053, -2.357186));
            list.add(new LatLng(51.379030, -2.357141));
        }
        return list;
    }

    private boolean performMapSearch(String name) {
        for (int i = 0; i < dummyClubs.size(); i++) {
            if (dummyClubs.get(i).getName().toLowerCase().contains(name.toLowerCase())) {

                //Close the keyboard
                closeKeyboard();

                mapSearch.setAlpha(opacity);

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
        // Get the data: latitude/longitude positions of people.
        List<LatLng> list = readItems(R.raw.heatmap);

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

    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //TextView textView = (TextView) findViewById(R.id.sidebarTitle);
        //textView.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_dialog_info, 0, 0, 0);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        expandableList = findViewById(R.id.navigationmenu);
        NavigationView navigationView = findViewById(R.id.nav_view);

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        prepareListData();
        mMenuAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild, expandableList);

        // setting list adapter
        expandableList.setAdapter(mMenuAdapter);

        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                return false;
            }
        });
        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return false;
            }
        });

        TextView sidebarHeader = findViewById(R.id.sidebarHeader);
        sidebarHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyProfileActivity.class);
                startActivity(intent);
            }
        });

        mDetector = new GestureDetectorCompat(this, new SwipeListener(this));

        mapSearch = findViewById(R.id.mapSearch);
        if (mapSearch != null) {
            mapSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mapSearch.setAlpha(0.8f);
                }
            });
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

        ImageView listviewChevron = findViewById(R.id.listviewChevron);
        if (listviewChevron != null) {
            listviewChevron.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ListViewActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
        }

        //TODO: Load clubs from backend
        //Load clubs from database
        dummyClubs.add(new Club("Second Bridge", 51.378960, -2.357198, R.drawable.bridge_logo, 40, 0.1));
        dummyClubs.add(new Club("Moles", 51.385047, -2.362571, R.drawable.moles, 5, 0.5));
        dummyClubs.add(new Club("The Nest", 51.385508, -2.360320, R.drawable.nest, 15, 0.55));
        dummyClubs.add(new Club("Komedia", 51.381648, -2.362141, R.drawable.komedia, 25, 0.3));
        dummyClubs.add(new Club("Zero Zero", 51.384999, -2.360940, R.drawable.zerozero, 5, 0.4));
        dummyClubs.add(new Club("Po Na Na", 51.380825, -2.356816,R.drawable.po_logo, 3, 0.18));
        dummyClubs.add(new Club("Khoosoosi", 51.383244, -2.356857, R.drawable.khosoosi, 0, 0.5));
        dummyClubs.add(new Club("Student Union", 51.379887, -2.326821, R.drawable.su, 15, 1.3));

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        ExpandedMenuModel item1 = new ExpandedMenuModel();
        item1.setIconName("Friends");
        item1.setIconImg(android.R.drawable.ic_menu_mylocation);
        // Adding data header
        listDataHeader.add(item1);

        ExpandedMenuModel item2 = new ExpandedMenuModel();
        item2.setIconName("Nearby Clubs");
        item2.setIconImg(android.R.drawable.ic_menu_directions);
        listDataHeader.add(item2);

        ExpandedMenuModel item3 = new ExpandedMenuModel();
        item3.setIconName("Club Promos");
        item3.setIconImg(android.R.drawable.ic_menu_zoom);
        listDataHeader.add(item3);

        // Adding child data
        //TODO: Load friends from database
        List<String> heading1 = new ArrayList<>();
        heading1.add("Ben - 0.1 Miles (Bridge)");
        heading1.add("Liam - 0.1 Miles (Bridge)");

        //TODO: Load near clubs from database
        List<String> heading2 = new ArrayList<>();
        heading2.add("Second Bridge - 0.1 Miles");
        heading2.add("Po Na Na - 0.18 Miles");

        listDataChild.put(listDataHeader.get(0), heading1);// Header, Child data
        listDataChild.put(listDataHeader.get(1), heading2);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        //revision: this don't works, use setOnChildClickListener() and setOnGroupClickListener() above instead
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    //Load these from database later
    private List<Club> dummyClubs = new ArrayList<>();

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 10001) {
            if (permissions.length == 1 &&
                    permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;

        LatLng bath = new LatLng(51.380941, -2.360007);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    10001);

        }

        LatLng pos = new LatLng(51.379053, -2.357186);
        this.googleMap.addMarker(new MarkerOptions().position(pos)
                .title("Ben")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.friend_icon)));

        LatLng pos2 = new LatLng(51.379030, -2.357141);
        this.googleMap.addMarker(new MarkerOptions().position(pos2)
                .title("Liam")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.friend_icon)));


        //Add all clubs to the map
        for (int i = 0; i < dummyClubs.size(); i++) {
            LatLng posi = new LatLng(dummyClubs.get(i).getLocation()[0], dummyClubs.get(i).getLocation()[1]);
            //Investigate using flatten
            Marker marker = this.googleMap.addMarker(new MarkerOptions().position(posi)
                    .title(dummyClubs.get(i).getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker))
                    .snippet(dummyClubs.get(i).getDescription()));
            dummyClubs.get(i).setMarker(marker);
        }


        this.googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mapSearch.setAlpha(opacity);
                closeKeyboard();
            }
        });

        this.googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mapSearch.setAlpha(opacity);
                closeKeyboard();
                return false;
            }
        });

        //Centre map on bath
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(bath));
        this.googleMap.setMaxZoomPreference(20.0f);
        this.googleMap.setMinZoomPreference(15.5f);

        //Link marker info windows to club page.
        this.googleMap.setOnInfoWindowClickListener(this);

        addHeatMap();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(getApplicationContext(), ClubPageActivity.class);
        intent.putExtra("name", marker.getTitle());
        intent.putExtra("desc", marker.getSnippet());
        intent.putExtra("source", "map");
        startActivity(intent);
    }

    private Club getClub(String name) {
        for (Club club : dummyClubs) {
            if (club.getName().equals(name))
                return club;
        }
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
