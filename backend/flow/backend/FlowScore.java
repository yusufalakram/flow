package flow.backend;

import flow.app.Club;
import flow.db.FlowBackend;

public class FlowScore {
	private static final int CLUB_RADIUS;
	private int flowScore;
	private String primaryName;
	private double[] location;
	
	
	public FlowScore(Club club){
		this.primaryName = club.getName();
		this.location = club.getLocation();
		calculateFlowScore();
	}
	
	private void calculateFlowScore(){
		
		int usersPresent = Backend.db.getUserCountWithinRadius(location, Backend.se.getPrefRadius());
	}
}