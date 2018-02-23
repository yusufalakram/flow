import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

public class FlowDBConnection {
    private Connection connection;
    protected FlowDBConnection() throws ClassNotFoundException{
        connection = null;
        Class.forName("org.mariadb.jdbc.Driver");
    }

    /**
     * Connect to the SQL database using supplied credentials
     * @param hostname address of the database
     * @param dbName name of the database to connect to
     * @param dbUser username
     * @param dbPass password
     * @return true if the connection was successfully made
     */
    protected boolean safeConnect(String hostname, String dbName, String dbUser, String dbPass) {
        //construct the URL needed to connect to the database
        System.out.print("\nConnecting to database...");
        String dbURL = "jdbc:mariadb://" + hostname + ":3306/"
                + dbName
                + "?user="
                + dbUser
                + "&password="
                + dbPass;
        try {
            //connect to the database
            connection = DriverManager.getConnection(dbURL);
            System.out.println(" -> Connected");
            return true;
        } catch (SQLException e) {
            System.out.println(" -> ERROR: Could not connect!");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * disconnect from the SQL database
     */
    protected void safeDisconnect(){
        System.out.print("\nDisconnecting from database...");
        try {
            if (connection != null) {
                connection.close();
            }
            System.out.println(" -> Disconnected.");
        } catch	(Exception e){
            System.out.println(" -> ERROR: Could not close JDBC Connection");
            e.printStackTrace();
        }
    }

    /**
     * returns a PreparedStatement for executing SQL code. if you need to know the primary key of the inserted row, use newStatementWithKey
     * @param query the query to execute
     * @return a PreparedStatement object for the given query
     * @throws SQLException
     */
    protected PreparedStatement newStatement(String query) throws SQLException {
        PreparedStatement st = connection.prepareStatement(query);
        return st;
    }

    /**
     * returns a PreparedStatement for executing SQL code, which also stores the generated primary key
     * @param query the query to execute
     * @return a PreparedStatement object for the given query
     * @throws SQLException
     */
    protected PreparedStatement newStatementWithKey(String query) throws SQLException {
        PreparedStatement st = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        return st;
    }

    /**
     * simply returns the underlying SQL connection
     * @return
     */
   // protected Connection getConnection(){return connection;}
}
