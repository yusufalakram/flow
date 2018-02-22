import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FlowBackend {
    private FlowDBConnection flowdb = null;
    enum EntityType {USER, CLUB};
    enum Gender {Male, Female, Other};

    public FlowBackend(){

    }

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
    private int newDBLocation(double lat, double lon){
        try {
            PreparedStatement p = flowdb.newStatementWithKey("INSERT INTO Location (Latitude, Longitude) VALUES (?, ?)");
            p.setDouble(1, lat);
            p.setDouble(2, lon);
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

    protected boolean updateUserLocation(int EntityID, double lat, double lon){
        try {
            PreparedStatement p = flowdb.newStatement("UPDATE Location, FlowUser SET Location.Latitude = ?, Location.Longitude = ? WHERE FlowUser.Location_LocationID = Location.LocationID AND FlowUser.Entity_EntityID = ?");
            p.setDouble(1, lat);
            p.setDouble(2, lon);
            p.setInt(3, EntityID);
            p.executeQuery();

        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected boolean updateClubLocation(int EntityID, double lat, double lon){
        try {
            PreparedStatement p = flowdb.newStatement("UPDATE Location, Club SET Location.Latitude = ?, Location.Longitude = ? WHERE Club.Location_LocationID = Location.LocationID AND Club.Entity_EntityID = ?");
            p.setDouble(1, lat);
            p.setDouble(2, lon);
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
     * @return -3 if an SQL error occurs, -2 if the user is not in the database, -1 if password is incorrect, otherwise the EntityID of the user
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

    protected boolean newDBUser (String firstName, String lastName, String email, String password, Gender g){
        int EntityID = newDBEntity(EntityType.USER);
        int LocationID = newDBLocation(0,0);

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
            return false;
        }
        return true;
    }
}
