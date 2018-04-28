package flow.backend;

import java.util.Date;

import flow.app.Club;

public class Backend{
	public static DBWrapper db;
	public static Settings se;
	private static int userEntityID;
	private static double[] location;
	private static Club[] clubList;
	private static Thread updateThread;
	
	public static void Init(){
		db = new DBWrapper();
		updateThread = new UpdateThread();
		updateThread.start();
		
		
		
		
	}
	public static void setUserEntityID(int userEntityID){
		Backend.userEntityID = userEntityID;
	}
	
	public static int getUserEntityID(){
		return userEntityID;
	}
	
	public static void setLocation(double[] location){
		if(location.length == 2){
			Backend.location = location;
		}
		else{
			throw new IllegalArgumentException();
		}
	}
	
	public static double[] getLocation(){
		return location;
	}
	
	public static void populateClubList(double[] location){
		clubList = db.getClubsWithinRadius(location, se.getPrefRadius());
		
	}
	
	public static Club[] getClubList(){
		return clubList;
	}
	
	public static class UpdateThread extends Thread{
		
		private static boolean updateThreadRunning;
		private Date lastUpdate;
		// timer in milliseconds
		private final static int UPDATE_TIMER = 300000;
		
		public UpdateThread(){
			updateThreadRunning = true;
			
		}
		public static boolean isRunning(){
			return updateThreadRunning;
		}
		public static void setRunning(boolean running){
			updateThreadRunning = running;
		}
		public static void update(){
			Backend.populateClubList(Backend.getLocation());
			Club[] updateList = Backend.getClubList();
			for(int i = 0; i < updateList.length; i++){
				updateList[i].update();
			}
		}
		
		@Override
		public void run(){
			while(updateThreadRunning){
				update();
				try {
					this.wait(UPDATE_TIMER);
				} catch (InterruptedException e) {
					System.out.println("Update thread was interrupted");
					e.printStackTrace();
				}
			}
		}
	}

}
