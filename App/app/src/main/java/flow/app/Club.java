package flow.app;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by Ben Amor on 21/02/2018.
 */
public class Club {

    private String primaryName;
    private double[] location;
    private String description;

    //Marker on the map for the club
    private Marker marker;

    public Club(String name, double[] location, String description) {
        this.primaryName = name;
        this.location = location;
        this.description = description;
    }

    public Club(String name, double lat, double lon, String description) {
        this.primaryName = name;
        this.location = new double[] {lat, lon};
        this.description = description;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Marker getMarker() {
        return marker;
    }

    public String getName() {
        return primaryName;
    }

    public double[] getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
