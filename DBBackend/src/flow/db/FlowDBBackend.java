package flow.db;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import flow.app.Club;


public class FlowDBBackend {
    private FlowDBConnection flowdb = null;
    enum EntityType {USER, CLUB}
    enum Gender {Male, Female, Other}

    public FlowDBBackend(){

    }

    /**
     * connect to the database using the database wrapper as admin. Please call disconnect() when finished
     * @return boolean stating if the connection was successful or not
     */
    protected boolean connect(){
        try {
        flowdb = new FlowDBConnection();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
            return false;
        }
        String dbName = "FlowDB";
        String dbUser = "FlowDBAdmin";
        String dbPass = "flowadminpassword";
        String hostname = "flowdb.cqn1n95dmb1r.eu-west-2.rds.amazonaws.com";

        return flowdb.safeConnect(hostname,dbName, dbUser, dbPass);
    }

    /**
     * disconnect from the underlying database connection wrapper
     */
    protected void disconnect(){
        flowdb.safeDisconnect();
    }



    /**
     * Creates a new record in the Entity table, for either a club or user
     * @param type The type of entity to create, either EntityType.USER or EntityType.CLUB
     * @return the EntityID to be used in future references to this entity, -1 if failure
     */
    private int newDBEntity(EntityType type){
        try {
            PreparedStatement p = flowdb.newStatementWithKey("INSERT INTO Entity (EntityType) VALUES (?)");
            if (type == EntityType.USER){
                p.setString(1, "User");
            } else {
                p.setString(1, "Club");
            }
            p.executeQuery();
            ResultSet rs = p.getGeneratedKeys();
            while (rs.next()){
                return rs.getInt(1);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Creates a new record in the Location table
     * @param lat location latitude
     * @param lon location longitude
     * @return the LocationID to be used in future references to this location, -1 if failure
     */
    private int newDBLocation(BigDecimal lat, BigDecimal lon){
        try {
            PreparedStatement p = flowdb.newStatementWithKey("INSERT INTO Location (Latitude, Longitude) VALUES (?, ?)");
            p.setBigDecimal(1, lat);
            p.setBigDecimal(2, lon);
            p.executeQuery();

            ResultSet rs = p.getGeneratedKeys();
            while (rs.next()){
                return rs.getInt(1);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }
    

    /**
     * update a users location
     * @param EntityID the entity ID to update
     * @param lat new latitude
     * @param lon new longitude
     * @return true if the update was applied successfully
     */
    protected boolean updateUserLocation(int EntityID, BigDecimal lat, BigDecimal lon){
        try {
            PreparedStatement p = flowdb.newStatement("UPDATE Location, FlowUser SET Location.Latitude = ?, Location.Longitude = ? WHERE FlowUser.Location_LocationID = Location.LocationID AND FlowUser.Entity_EntityID = ?");
            p.setBigDecimal(1, lat);
            p.setBigDecimal(2, lon);
            p.setInt(3, EntityID);
            p.executeQuery();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * update the location of a club (probably not needed but could come in handy if a location is wrong)
     * @param EntityID the entity ID of the club to update
     * @param lat new latitude
     * @param lon new logitude
     * @return true if the update was applied successfully
     */
    protected boolean updateClubLocation(int EntityID, BigDecimal lat, BigDecimal lon){
        try {
            PreparedStatement p = flowdb.newStatement("UPDATE Location, Club SET Location.Latitude = ?, Location.Longitude = ? WHERE Club.Location_LocationID = Location.LocationID AND Club.Entity_EntityID = ?");
            p.setBigDecimal(1, lat);
            p.setBigDecimal(2, lon);
            p.setInt(3, EntityID);
            p.executeQuery();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Authenticates a user by first finding their account using their email, then checks the given password against the password in the database
     * @param email the unique email address of the user
     * @param testPassword the password to check
     * @return -3 if an SQL error occurs, -2 if the user is not in the database, -1 if password is incorrect, otherwise returns the EntityID of the user
     */
    protected int authenticateUser(String email, String testPassword){
        String salt = null;
        String actualHash = null;
        int EntityID = -1;
        try {
            PreparedStatement p = flowdb.newStatement("SELECT Entity_EntityID, _PasswordSalt, _Password FROM FlowUser WHERE Email = ?");
            p.setString(1, email);
            ResultSet resultSet = p.executeQuery();
            while (resultSet.next()){
                EntityID = resultSet.getInt("Entity_EntityID");
                salt = resultSet.getString("_PasswordSalt");
                actualHash = resultSet.getString("_Password");
            }
            if (actualHash == null || salt == null || EntityID == -1){
                return -2;
            }
            String testHash = PasswordSalterAndHasher.hashPassword(salt, testPassword);
            if (testHash.equals(actualHash)){
                return EntityID ;
            } else {
                return -1;
            }
        } catch (Exception e){
            e.printStackTrace();
            return -3;
        }
    }

    /**
     * change a users password, generating a new unique salt and storing the hash of the salted password
     * @param EntityID
     * @param newPassword
     * @return
     */
    protected boolean updateUserPassword (int EntityID, String newPassword){
        String passSalt = PasswordSalterAndHasher.generateNewSalt();
        String passHash = PasswordSalterAndHasher.hashPassword(passSalt, newPassword);
        try {
            PreparedStatement p = flowdb.newStatement("UPDATE FlowUser SET _PasswordSalt = ?, _Password = ? WHERE Entity_EntityID = ?");
            p.setString(1, passSalt);
            p.setString(2, passHash);
            p.setInt(3, EntityID);
            p.executeQuery();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * create a new user on the database
     * @param firstName new users first name
     * @param lastName new users last name
     * @param email new users email address, must be unique to the database
     * @param password users password, will be salted and hashed, then stored
     * @param g gender of the user, may be null if not known
     * @return EntityID of the added user, -1 on error
     */
    protected int newDBUser (String firstName, String lastName, String email, String password, Gender g){
        int EntityID = newDBEntity(EntityType.USER);
        int LocationID = newDBLocation(new BigDecimal(0), new BigDecimal(0));

        String passSalt = PasswordSalterAndHasher.generateNewSalt();
        String passHash = PasswordSalterAndHasher.hashPassword(passSalt, password);
        try {
            PreparedStatement p = flowdb.newStatement("INSERT INTO FlowUser (Entity_EntityID, Location_LocationID, FirstName, Surname, Email, _PasswordSalt, _Password, Gender, UserType) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            p.setInt(1, EntityID);
            p.setInt(2, LocationID);
            p.setString(3, firstName);
            p.setString(4, lastName);
            p.setString(5, email);
            p.setString(6, passSalt);
            p.setString(7, passHash);
            if(g == Gender.Male) {
                p.setString(8, "M");
            } else if (g == Gender.Female){
                p.setString(8, "F");
            } else {
                p.setString(8, "O");
            }
            p.setString(9, "Regular");
            p.executeQuery();
        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }
        return EntityID;
    }

    /**
     *
     * @param name the name of the club
     * @param description a description of the club
     * @return EntityID of the added club, -1 on error
     */
    protected int newDBClub (String name, String description){
        int EntityID = newDBEntity(EntityType.CLUB);
        int LocationID = newDBLocation(new BigDecimal(0), new BigDecimal(0));

        try {
            PreparedStatement p = flowdb.newStatement("INSERT INTO Club (Entity_EntityID, Location_LocationID, Name, Description) VALUES (?, ?, ?, ?)");
            p.setInt(1, EntityID);
            p.setInt(2, LocationID);
            p.setString(3, name);
            p.setString(4, description);
            p.executeQuery();
        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }
        return EntityID;
    }
    
    protected boolean updateUserName(int EntityID, String newName) {
        try {
            PreparedStatement p = flowdb.newStatement("UPDATE User SET Name = ? WHERE Entity_EntityID = ?");
            p.setString(1, newName);
            p.setInt(2, EntityID);
            p.executeQuery();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    protected boolean updateUserEmail(int EntityID, String newEmail) {
        try {
            PreparedStatement p = flowdb.newStatement("UPDATE User SET Email = ? WHERE Entity_EntityID = ?");
            p.setString(1, newEmail);
            p.setInt(2, EntityID);
            p.executeQuery();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected boolean updateClubName(int EntityID, String newName) {
        try {
            PreparedStatement p = flowdb.newStatement("UPDATE Club SET Name = ? WHERE Entity_EntityID = ?");
            p.setString(1, newName);
            p.setInt(2, EntityID);
            p.executeQuery();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected boolean updateClubDescription(int EntityID, String newDescription) {
        try {
            PreparedStatement p = flowdb.newStatement("UPDATE Club SET Description = ? WHERE Entity_EntityID = ?");
            p.setString(1, newDescription);
            p.setInt(2, EntityID);
            p.executeQuery();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected boolean updateClubSafety(int EntityID, int newSafety) {
        try {
            PreparedStatement p = flowdb.newStatement("UPDATE Club SET SafetyRating = ? WHERE Entity_EntityID = ?");
            p.setInt(1, newSafety);
            p.setInt(2, EntityID);
            p.executeQuery();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected boolean updateClubBusyness(int EntityID, int newBusyness) {
        try {
            PreparedStatement p = flowdb.newStatement("UPDATE Club SET Busyness = ? WHERE Entity_EntityID = ?");
            p.setInt(1, newBusyness);
            p.setInt(2, EntityID);
            p.executeQuery();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected boolean updateClubUsersPresent(int EntityID, int newPresent) {
        try {
            PreparedStatement p = flowdb.newStatement("UPDATE Club SET UsersPresent = ? WHERE Entity_EntityID = ?");
            p.setInt(1, newPresent);
            p.setInt(2, EntityID);
            p.executeQuery();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected boolean updateClubQueueTime(int EntityID, int newTime) {
        try {
            PreparedStatement p = flowdb.newStatement("UPDATE Club SET QueueTime = ? WHERE Entity_EntityID = ?");
            p.setInt(1, newTime);
            p.setInt(2, EntityID);
            p.executeQuery();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    protected int getUsersPresentWithinRadius(BigDecimal lat, BigDecimal lon, BigDecimal latRad, BigDecimal lonRad){
    	try{
    		int retVal = 0;
    		PreparedStatement p = flowdb.newStatement("SELECT Entity_EntityID FROM FlowUser INNER JOIN Location ON FlowUser.Location_LocationID = Location.LocationID "
    				+ "WHERE (Location.Latitude < ? OR Location.Latitude > ?) AND (Location.Longitude < ? OR Location.Longitude > ?)");
    		p.setBigDecimal(1, (lat.add(latRad)));
    		p.setBigDecimal(2, (lat.subtract(latRad)));
    		p.setBigDecimal(3, (lon.add(lonRad)));
    		p.setBigDecimal(4, (lon.subtract(lonRad)));
    		ResultSet resultSet = p.executeQuery();
    		while(resultSet.next()){
    			retVal++;
    		}
    		return retVal;
    	}
    	catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
    
    protected int[] getUsersWithinRadius(BigDecimal lat, BigDecimal lon, BigDecimal latRad, BigDecimal lonRad){
    	try{
    		int[] retVal;
    		PreparedStatement p = flowdb.newStatement("SELECT Entity_EntityID, Location_LocationID FROM FlowUser INNER JOIN Location ON FlowUser.Location_LocationID = Location.LocationID "
    				+ "WHERE (Location.Latitude < ? OR Location.Latitude > ?) AND (Location.Longitude < ? OR Location.Longitude > ?)");
    		p.setBigDecimal(1, (lat.add(latRad)));
    		p.setBigDecimal(2, (lat.subtract(latRad)));
    		p.setBigDecimal(3, (lon.add(lonRad)));
    		p.setBigDecimal(4, (lon.subtract(lonRad)));
    		ResultSet resultSet = p.executeQuery();
    		int resultSetLength = 0;
    		if (resultSet.last()) {
    			resultSetLength = resultSet.getRow();
    		  resultSet.beforeFirst(); 
    		}
    		retVal = new int[resultSetLength];
    		while(resultSet.next()){
    			retVal[resultSet.getRow() - 1] = resultSet.getInt("Entity_EntityID");
    		}
    		return retVal;
    		
    	}
    	catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    protected Club[] getClubsWithinRadius(BigDecimal lat, BigDecimal lon, BigDecimal latRad, BigDecimal lonRad){
    	try{
    		Club[] retVal;
    		PreparedStatement p = flowdb.newStatement("SELECT Name, Location_LocationID, Description FROM Club INNER JOIN Location ON FlowUser.Location_LocationID = Location.LocationID "
    				+ "WHERE (Location.Latitude < ? OR Location.Latitude > ?) AND (Location.Longitude < ? OR Location.Longitude > ?)");
    		p.setBigDecimal(1, (lat.add(latRad)));
    		p.setBigDecimal(2, (lat.subtract(latRad)));
    		p.setBigDecimal(3, (lon.add(lonRad)));
    		p.setBigDecimal(4, (lon.subtract(lonRad)));
    		PreparedStatement p2 = flowdb.newStatement("SELECT Latitude, Longitude FROM Location WHERE LocationID = ?");
    		ResultSet resultSet = p.executeQuery();
    		int resultSetLength = 0;
    		if (resultSet.last()) {
    			resultSetLength = resultSet.getRow();
    		  resultSet.beforeFirst(); 
    		}
    		retVal = new Club[resultSetLength];
    		while(resultSet.next()){
    			String name = resultSet.getString("Name");
    			String description = resultSet.getString("Description");
    			double retLat = 0;
    			double retLon = 0;
    			p2.setInt(1, resultSet.getInt("Location_LocationID"));
    			ResultSet resultSet2 = p2.executeQuery();
    			while(resultSet2.next()){
    				retLat = resultSet2.getBigDecimal("Latitude").doubleValue();
    				retLon = resultSet2.getBigDecimal("Longitude").doubleValue();
    			}
    			retVal[resultSet.getRow() - 1] = new Club(name, retLat, retLon, description);
    		}
    		return retVal;
    		
    	}
    	catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    protected int getEntityIDOfUser(String email){
    	try{
    		int retVal = -1;
    		PreparedStatement p = flowdb.newStatement("Select Entity_EntityID FROM FlowUser WHERE Email = ?");
    		p.setString(0, email);
    		ResultSet resultSet = p.executeQuery();
    		while(resultSet.next()){
    			retVal = resultSet.getInt("Entity_EntityID");
    		}
    		return retVal;
    	}
    	catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
    
    protected int getEntityIDOfClub(String name){
    	try{
    		int retVal = -1;
    		PreparedStatement p = flowdb.newStatement("Select Entity_EntityID FROM Clubs WHERE Name = ?");
    		p.setString(0, name);
    		ResultSet resultSet = p.executeQuery();
    		while(resultSet.next()){
    			retVal = resultSet.getInt("Entity_EntityID");
    		}
    		return retVal;
    	}
    	catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
    
    protected String getUserNameFromEntityID(int UserEntityID){
    	try{
    		String retVal = "";
    		PreparedStatement p = flowdb.newStatement("Select Name FROM FlowUser WHERE Entity_EntityID = ?");
    		p.setInt(0, UserEntityID);
    		ResultSet resultSet = p.executeQuery();
    		while(resultSet.next()){
    			retVal = resultSet.getString("Name");
    		}
    		return retVal;
    	}
    	catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
    
    protected boolean addUserFriend(int UserEntityID, int FriendEntityID){
    	try{
    		PreparedStatement p = flowdb.newStatement("INSERT INTO Friend (UserID, FriendID) VALUES(?, ?)");
    		p.setInt(1, UserEntityID);
    		p.setInt(2, FriendEntityID);
    		p.executeUpdate();
    		return true;
    	}catch (Exception e){
            e.printStackTrace();
            return false;
        }
    	
    }
    
    protected String[] getUserFriends(int UserEntityID){
    	try{
    		String[] retVal;
    		PreparedStatement p = flowdb.newStatement("SELECT FriendID FROM Friend WHERE UserID = ?");
    		p.setInt(1, UserEntityID);
    		ResultSet resultSet = p.executeQuery();
    		int resultSetLength = 0;
    		if (resultSet.last()) {
    			resultSetLength = resultSet.getRow();
    		  resultSet.beforeFirst(); 
    		}
    		retVal = new String[resultSetLength];
    		while(resultSet.next()){
    			retVal[resultSet.getRow() - 1] = getUserNameFromEntityID(resultSet.getInt("FriendID"));
    		}
    		return retVal;
    		
    	}
    	catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    protected String getUserPreferences(int UserEntityID){
    	try{
    		String retVal = null;
    		PreparedStatement p = flowdb.newStatement("SELECT Preferences FROM FlowUser WHERE Entity_EntityID = ?");
    		p.setInt(1, UserEntityID);
    		ResultSet resultSet = p.executeQuery();
    		while(resultSet.next()){
    			retVal = resultSet.getString("Preferences");
    		}
    		return retVal;
    		
    	}
    	catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    protected boolean saveUserPreferences(int UserEntityID, String newPreferences){
    	try{
    		PreparedStatement p = flowdb.newStatement("UPDATE FlowUser SET Preferences = ? WHERE Entity_EntityID = ?");
    		p.setString(1, newPreferences);
    		p.setInt(2, UserEntityID);
    		p.executeQuery();
    		return true;
    	}
    	catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    

}
