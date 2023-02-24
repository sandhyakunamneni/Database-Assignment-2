/**
 * A demo program to show how jdbc works with postgresql
 * Nick Fankhauser 
 * nickf@ontko.com or nick@fankhausers.com
 * This program may be freely copied and modified
 * Please keep this header intact on unmodified versions
 * The rest of the documentation that came with this demo program
 * may be found at http://www.fankhausers.com/postgresql/jdbc
 */



import java.sql.*;
import java.text.*;
import java.io.*;

public class subPartsDFS
{
  Connection       db;        // A connection to the database
  Statement        sql;       // Our statement to run queries with
  DatabaseMetaData dbmd;      // This is basically info the driver delivers
                              // about the DB it just connected to. I use
                              // it to get the DB version to confirm the
                              // connection in this example.

  public subPartsDFS(String argv[])
    throws ClassNotFoundException, SQLException
  {
    String database = argv[0];
    String username = argv[1];
    String password = argv[2];
    Class.forName("org.postgresql.Driver"); //load the driver
    db = DriverManager.getConnection("jdbc:postgresql://cisdb/"+database,username,password); //connect to the db
    dbmd = db.getMetaData(); //get MetaData to confirm connection
    System.out.println("Connection to "+dbmd.getDatabaseProductName()+" "+
                       dbmd.getDatabaseProductVersion()+" successful.\n");
    sql = db.createStatement(); //create a statement that we can use later
      String givenPart = "P1";
  recursion(givenPart, sql);
    db.close();
  }

  public static void correctUsage()
  {
    System.out.println("\nIncorrect number of arguments.\nUsage:\n "+
                       "java   \n");
    System.exit(1);
  }

  public static void main (String args[])
  {
    if (args.length != 3) correctUsage();
    try
    {
        subPartsDFS demo = new subPartsDFS(args);
    }
    catch (Exception ex)
    {
      System.out.println("***Exception:\n"+ex);
      ex.printStackTrace();
    }
  }
      // Recursive function to print out the subparts of a given part using depth-first search
      public static void recursion(String upperPart, Statement stmt) throws SQLException {
          System.out.print(upperPart + " "); // print the upper part
          String lowerPart = "";
          // Create a prepared statement to retrieve the subparts for the given upper part
          String sql = "SELECT MINOR_P# FROM PART_STRUCTURE WHERE MAJOR_P# = ? AND MINOR_P# > ? ORDER BY MINOR_P#";
          try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(sql)) {
              pstmt.setString(1, upperPart);
              pstmt.setString(2, lowerPart);
              ResultSet rs = pstmt.executeQuery();
              while (rs.next()) {
                  lowerPart = rs.getString("MINOR_P#");
                  recursion(lowerPart, stmt); // recursively call the function with the lower part
              }
          }
      }
}



