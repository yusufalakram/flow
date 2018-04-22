package flow.app;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben Amor on 21/02/2018.
 */
public class Club {


    private static List<Club> clubsList = new ArrayList<>();
    private String primaryName;
    private double[] location;
    private String description;
    private double userRating = 3.2;
    private int[] photos = new int[]{R.drawable.bridge1, R.drawable.bridge2, R.drawable.bridge3};
    private int logo;

    //Marker on the map for the club
    private Marker marker;

    public Club(String name, double[] location, String description) {
        this.primaryName = name;
        this.location = location;
        this.description = description;
        clubsList.add(this);
    }

    public Club(String name, double lat, double lon, int logo, double queueTime, double distance) {
        this.primaryName = name;
        this.location = new double[] {lat, lon};
        this.description = "Queue Time: " + queueTime + "m";
        this.logo = logo;
        this.queueTime = queueTime;
        this.distance = distance;
        if (clubsList.size() < 8) {
            clubsList.add(this);
        }
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

    public int[] getPhotos() {
        return photos;
    }

    public int getLogo() {
        return logo;
    }

    public double getUserRating() {
        return userRating;
    }

    private double distance;

    public double getDistance() {
        return distance;
    }

    private double flowRating = 1.2;

    public double getFlowRating() {
        return flowRating;
    }

    private double queueTime;

    public double getQueueTime() {
        return queueTime;
    }

    private boolean ticketsRequired = false;

    public boolean areTicketsRequired() {
        return ticketsRequired;
    }


    public static List<Club> getClubs() {
        return clubsList;
    }

    public static Club getClub(String name) {
        for (Club club : clubsList) {
            if (club.getName().equals(name)) {
                return club;
            }
        }
        return null;
    }


}
