import java.sql.*;
import java.util.Scanner;

public class DFSSubParts {
	// function to get database connection object
	private static Connection getDBConnectionObject(String[] args) throws ClassNotFoundException, SQLException {
		String databaseURL = args[0];
		String username = args[1];
		String password = args[2];
		Class.forName("org.postgresql.Driver"); // loading the driver class
		// creating database connection Object
		Connection conn = DriverManager.getConnection("jdbc:postgresql://cisdb/" + databaseURL, username, password);
		return conn;
	}

	public static void correctInvalidArguments() {
		System.out.println("\n Invalid number of arguments!! Please provide 3 arguments  \n");
		System.exit(1);
	}

	// Recursive function to log the subparts of a given part using DFS
	public static void getsubParts(String upperPart, Statement stmt) throws SQLException {
		System.out.print(upperPart + " ");
		String lowerPart = "";
		String sql = "SELECT MINOR_P FROM PART_STRUCTURE WHERE MAJOR_P = ? AND MINOR_P > ? ORDER BY MINOR_P";
		try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(sql)) {
			pstmt.setString(1, upperPart);
			pstmt.setString(2, lowerPart);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				lowerPart = rs.getString("MINOR_P");
				getsubParts(lowerPart, stmt); // Calling the function recursively with the lower part
			}
		}
	}

	public static void main(String args[]) {
		if (args.length != 3) // validating arguments
			correctInvalidArguments();
		try (Connection conn = getDBConnectionObject(args)) { // getting database connection object
			Statement stmt = conn.createStatement(); // creating statement from connection object
			Scanner obj = new Scanner(System.in);
			System.out.println("Enter input parameter");
			String inputParam = obj.nextLine(); // Getting input parameter from user
			getsubParts(inputParam, stmt);
			System.out.println("");
		} catch (Exception ex) {
			System.out.println("Exception:\n" + ex);
			ex.printStackTrace();
		}
	}

}