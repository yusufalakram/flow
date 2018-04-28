package flow.backend;

import java.math.BigDecimal;

import flow.app.Club;
import flow.db.FlowDBBackend;

public class DBWrapper extends FlowDBBackend{
	public DBWrapper(){
		connect();
	}
	
	public String logIn(String email, String password){
		String message;
		int errorCode = authenticateUser(email, password);
		switch(errorCode){
			case -3:
				message = "Could not log into database. Please try again later";
			case -2:
				message = "E-mail address not associated with any user";
			case -1:
				message = "Incorrect password. Please try again";
			default:
				message = "Log in sucessful";
		}
		return message;
	}
	
	public int getUserEntityID(String email){
		return getEntityIDOfUser(email);
	}
	
	public int getUserCountWithinRadius(double[] location, int radius){
		BigDecimal latitude = new BigDecimal(location[0]);
		BigDecimal longitude = new BigDecimal(location[1]);
		BigDecimal[] radiusDeg = LocationTranslation.radiusToLocation(latitude, radius);
		return getUsersPresentWithinRadius(latitude, longitude, radiusDeg[0], radiusDeg[1]);
	}
	
	public Club[] getClubsWithinRadius(double[] location, int radius){
		BigDecimal latitude = new BigDecimal(location[0]);
		BigDecimal longitude = new BigDecimal(location[1]);
		BigDecimal[] radiusDeg = LocationTranslation.radiusToLocation(latitude, radius);
		return getClubsWithinRadius(latitude, longitude, radiusDeg[0], radiusDeg[1]);
	}
	
	public int[] getUsersWithinRadius(double[] location, double radius){
		BigDecimal latitude = new BigDecimal(location[0]);
		BigDecimal longitude = new BigDecimal(location[1]);
		BigDecimal[] radiusDeg = LocationTranslation.radiusToLocation(latitude, radius);
		return getUsersWithinRadius(latitude, longitude, radiusDeg[0], radiusDeg[1]);
	}
	
	public boolean updateName(int EntityID, String name){
		return updateUserName(EntityID, name);
	}
	
	public boolean updateEmail(int EntityID, String email){
		return updateUserEmail(EntityID, email);
	}
	
	public boolean updatePassword(int EntityID, String password){
		return updateUserPassword(EntityID, password);
	}
	
	public boolean addFriend(int UserEntityID, int FriendEntityID){
		return addUserFriend(UserEntityID, FriendEntityID);
	}
	
	public String[] getFriends(int EntityID){
		return getUserFriends(EntityID);
	}

	public String[] getUserSettings(int EntityID) {
		return getUserPreferences(EntityID).split("\\|");
	}
	
	public void saveUserSettings(int EntityID, String[] newPreferences){
		saveUserPreferences(EntityID, String.join("\\|", newPreferences));
		
	}
	
	public int getIDOfClub(String name){
		return getEntityIDOfClub(name);
	}
	
}
