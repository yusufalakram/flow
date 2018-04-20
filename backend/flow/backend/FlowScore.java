package flow.backend;

import flow.app.Club;
import flow.db.FlowBackend;

public class FlowScore {
	private int flowScore;
	private String primaryName;
	
	public FlowScore(Club club){
		this.primaryName = club.getName();
		calculateFlowScore();
	}
	
	private void calculateFlowScore(){
		
		int usersPresent = 
	}
}
