package flow.app;

import com.google.android.gms.maps.model.Marker;

import flow.backend.Backend;
import flow.backend.FlowScore;
import flow.backend.QueueTime;

/**
 * Created by Ben Amor on 21/02/2018.
 */
public class Club {

    private String primaryName;
    private double[] location;
    private String description;
    private double userRating = 3.2;
    private int[] photos = new int[]{R.drawable.bridge1, R.drawable.bridge2, R.drawable.bridge3};
    private int logo = R.drawable.bridge_logo;
    private FlowScore flowScore;
    private QueueTime q;

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

    public int[] getPhotos() {
        return photos;
    }

    public int getLogo() {
        return logo;
    }

    public double getUserRating() {
        return userRating;
    }

    private double distance = 0.5;

    public double getDistance() {
        return distance;
    }

    private double flowRating = 1.2;

    public double getFlowRating() {
        return flowRating;
    }

    private double queueTime = 24;

    public double getQueueTime() {
        return queueTime;
    }

    private boolean ticketsRequired = false;

    public boolean areTicketsRequired() {
        return ticketsRequired;
    }
    
    public void update(){//rate of entry plus new flow score
    	if(this.q == null){
    		this.q = new QueueTime(this);
    	}
    	this.q.update();
    	this.queueTime = this.q.getQueueTime();
    }
    
    public int getEntityID(){
    	return Backend.db.getIDOfClub(this.primaryName);
    }

}
