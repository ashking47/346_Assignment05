package clark.king.lawrence;

import java.sql.*;

public class SQLManager {
    private Connection conn;
    private Statement stmt;
    private ResultSet rset;
    private int count;

    public SQLManager() throws SQLException {

        //DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
        String serverName = "csor12c.dhcp.bsu.edu";
        String portNumber = "1521";
        String sid = "or12cdb";
        String url = "jdbc:oracle:thin:@" + serverName + ":" + portNumber + ":" + sid;
        conn = DriverManager.getConnection(url, "aeking2", "7661");

        DatabaseMetaData DBMD = conn.getMetaData();
        }

    public ResultSet createQuery(String query){
        try{
            stmt = conn.createStatement();
            rset = stmt.executeQuery(query);
            return rset;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return rset;
    }

    public int setCount() throws SQLException {
        ResultSetMetaData rsmd = rset.getMetaData();
        count = rsmd.getColumnCount();
        return count;
    }

    public void closeConnection(boolean close){
        try{
            if(close){
                stmt.execute("Drop Table Listings");
            }
            stmt.close();
            conn.close();
        } catch (SQLException e){
            System.out.println("SQL Exception Caught");
            e.printStackTrace();
        }
    }

}
