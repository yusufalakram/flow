import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

public class FlowDBConnection {
    private Connection connection;
    protected FlowDBConnection() throws ClassNotFoundException{
        connection = null;
        Class.forName("org.mariadb.jdbc.Driver");
    }

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

//    protected ResultSet runQuery(String query, Object[] args ) throws SQLException {
//        PreparedStatement st = connection.prepareStatement(query);
//        for (int i = 0; i < args.length; i++){
//            if (args[i] instanceof Integer){
//                st.setInt(i+1, (Integer)args[i]);
//            }
//        }
//        ResultSet resultSet = st.executeQuery(query);
//        return resultSet;
//    }

    protected PreparedStatement newStatement(String query) throws SQLException {
        PreparedStatement st = connection.prepareStatement(query);
        return st;
    }

    protected PreparedStatement newStatementWithKey(String query) throws SQLException {
        PreparedStatement st = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        return st;
    }

    protected Connection getConnection(){return connection;}
}
