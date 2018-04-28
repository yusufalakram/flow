package flow.backend;

import flow.app.Club;
import flow.db.FlowDBBackend;

public class FlowScore {
	private final static double CLUB_RADIUS = 0.05;
	private double flowScore;
	private String primaryName;
	private double[] location;
	
	
	public FlowScore(Club club){
		this.primaryName = club.getName();
		this.location = club.getLocation();
		calculateFlowScore();
	}
	
	private void calculateFlowScore(){
		
		int usersPresent = Backend.db.getUserCountWithinRadius(location, Backend.se.getPrefRadius());
		int[] usersNearClub = Backend.db.getUsersWithinRadius(location, CLUB_RADIUS);
		this.flowScore = usersNearClub.length / usersPresent;
	}

	public double getFlow() {
		return this.flowScore;
	}
	
	public void update(){
		calculateFlowScore();
	}
}
