package flow.backend;

public class Backend {
	public static DBWrapper db;
	public static Settings se;
	private static int userEntityID;
	private static double[] location;
	
	public Backend(){
		db = new DBWrapper();
		se = new Settings();
		
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
}
