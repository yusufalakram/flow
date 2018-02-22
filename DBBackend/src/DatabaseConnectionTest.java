import java.sql.*;


public class DatabaseConnectionTest {
    private Connection connection;

    public DatabaseConnectionTest(){

    }

	public Connection unsafeConnect(String hostname, String dbName, String dbUser, String dbPass) throws ClassNotFoundException, SQLException {
        Connection conn = null;
            //load mariadb driver from external file
            Class.forName("org.mariadb.jdbc.Driver");

            //construct the URL needed to connect to the database
            String dbURL = "jdbc:mariadb://" + hostname + ":3306/"
                    + dbName
                    + "?user="
                    + dbUser
                    + "&password="
                    + dbPass;

            //connect to the database
            conn = DriverManager.getConnection(dbURL);

            return conn;

    }

    private void updatePassword(Connection connection, int EntityID, String password){
        int saltCollisions = 0;
        do {
            String salt = PasswordSalterAndHasher.generateNewSalt();
            System.out.println("Salt: " +salt);
            String saltedPassword = password+salt;
            System.out.println("Salted: " +saltedPassword);
            String hashedPassword = PasswordSalterAndHasher.hashPassword(salt,password);
            System.out.println("hashed: " +hashedPassword);
            try {
                query(connection, "UPDATE passwordTest SET _passwordSalt='" + salt + "', _passwordHash='" + hashedPassword + "' WHERE idnew_table=" + EntityID + ";");
            } catch (SQLIntegrityConstraintViolationException e) {

                saltCollisions++;
                //if this keeps happening, we have so many users that salt collision is unavoidable
                System.out.println("Salt collision, trying new salt...");
            } catch (SQLException e){
                e.printStackTrace();
                break;
            }
        } while (saltCollisions<=100 && saltCollisions>0);

    }

    private boolean checkPassword(Connection connection, int EntityID, String givenPassword){

        try {
            ResultSet rs = query(connection, "SELECT _passwordSalt, _passwordHash FROM passwordTest WHERE idnew_table=" + EntityID + ";");
            rs.last();
            String salt = rs.getString("_passwordSalt");
            String hash = rs.getString("_passwordHash");
            String hashed = PasswordSalterAndHasher.hashPassword(salt, givenPassword);
            return (hashed.equals(hash));
        } catch (SQLException e){
            e.printStackTrace();

        }
        System.out.println("Something went wrong while trying to check password with database");
        return false;

    }


    private void addTestPassword(Connection conn){
	    String salt = "HELLO";//PasswordSalterAndHasher.generateNewSalt(24);
        try {
            ResultSet rs = query(conn, "INSERT INTO passwordTest (_passwordSalt) VALUES ('" + salt + "');");
            while(rs.next()){
                System.out.println(rs.getStatement());
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("Salt to add is:\n"+salt);

        try {
            ResultSet rs = query(conn, "SELECT * FROM passwordTest WHERE _passwordSalt = '" + salt +"';");
            System.out.println(rs);
            String returnedSalt = "";
            rs.last();
            System.out.println(rs.getRow());
            rs.beforeFirst();
            while(rs.next()) {
                System.out.println(rs.getInt("idnew_table") + " : " + rs.getString("uname") + " : " + rs.getString("_passwordSalt") + " : " + rs.getString("_passwordHash"));
                returnedSalt = rs.getString("_passwordSalt");
            }

            if (salt.equals(returnedSalt)){
                System.out.println("MATCH!");
            } else {
                System.out.println("NO MATCH");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

	public ResultSet query(Connection conn, String query) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet resultSet = st.executeQuery(query);
        return resultSet;
    }

    /**
     * Adds a new location to the Location table, returns the ID of the new location in the database
     * @param conn the database connection
     * @param lat latitude
     * @param lon longitude
     * @return ID of the location added to the table
     * @throws Exception idk
     */
	public int addNewLocation(Connection conn, float lat, float lon) throws Exception{
    return 0;
    }

    public void safeDisconnect(Connection con){
        System.out.print("\nDisconnecting from database...");
        try {
            if (con != null) {
                con.close();
            }
            System.out.println(" -> Disconnected.");
        } catch	(Exception e){
            System.err.println(" -> ERROR: Could not close JDBC Connection");
            e.printStackTrace();
        }
    }

    private void run(){
        String dbName = "FlowDB";
        String dbUser = "FlowDBAdmin";
        String dbPass = "adminpassword";
        String hostname = "192.168.0.9";

        Connection con = null;
        try {
            System.out.print("Connecting to database...");
            con = unsafeConnect(hostname, dbName, dbUser, dbPass);
            System.out.println(" -> Connected.\n");
        }
        catch (ClassNotFoundException e){
            System.out.println("MariaDB driver not found!");
            e.printStackTrace();
        }
        catch (SQLException e){
            System.out.println("SQL Exception!");
            e.printStackTrace();
        }

        if(con!=null){
            System.out.println("Updating user password...");

            updatePassword(con, 21, "password");


            if (checkPassword(con, 21, "password")) {
                System.out.println("yup");
            } else {
                System.out.println(":(");
            }
        }

        //always close connection when done
        safeDisconnect(con);
    }


	public static void main(String args[]){

		DatabaseConnectionTest db = new DatabaseConnectionTest();

		db.run();

	}
}

