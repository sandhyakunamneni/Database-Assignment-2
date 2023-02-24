import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LeafParts {
	
	public static void correctUsage() {
		System.out.println("\nIncorrect number of arguments.\nUsage:\n " + "java   \n");
		System.exit(1);
	}

	//function to get database connection object
	private static Connection getDBConnectionObject(String[] args) throws ClassNotFoundException, SQLException {
		String databaseURL = args[0];
		String username = args[1];
		String password = args[2];
		Class.forName("org.postgresql.Driver"); // loading the driver class
		// creating database connection Object
		Connection db = DriverManager.getConnection("jdbc:postgresql://cisdb/" + databaseURL, username, password);
		return db;
	}

	public static void main(String args[]) {
		if (args.length != 3) // validating arguments
			correctUsage();
		try (Connection conn = getDBConnectionObject(args)) { // getting database connection object
			Statement stmt = conn.createStatement(); // creating statement from connection object
			Scanner obj = new Scanner(System.in);
			System.out.println("Enter input parameter");
			String inputParam = obj.nextLine(); // Getting input parameter from user
			Map<String, Integer> leafPartCounts = new HashMap<String, Integer>();
			getLeafPartCounts(inputParam, stmt, leafPartCounts, 1);
			//Calling function to print Results
			printResults(leafPartCounts);
		} catch (Exception ex) {
			System.out.println("Exception:\n" + ex);
			ex.printStackTrace();
		}
	}

	//Printing the leaf parts counts
	private static void printResults(Map<String, Integer> leafPartCounts) {
		for (Map.Entry<String, Integer> entry : leafPartCounts.entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue());
		}
	}

	// Recursive function to calculate leaf part count
	public static void getLeafPartCounts(String upperPart, Statement stmt, Map<String, Integer> leafPartCounts, int prod)
			throws SQLException {

		String lowerPart = "";
		String sql = "SELECT MINOR_P,QTY FROM PART_STRUCTURE2 WHERE MAJOR_P = ? AND MINOR_P > ? ORDER BY MINOR_P";
		int quantity;
		try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(sql)) {
			pstmt.setString(1, upperPart);
			pstmt.setString(2, lowerPart);
			ResultSet rs = pstmt.executeQuery();
			boolean isPartLeaf = true;

			while (rs.next()) {
				isPartLeaf = false;
				lowerPart = rs.getString("MINOR_P");
				quantity = rs.getInt("QTY");
				getLeafPartCounts(lowerPart, stmt, leafPartCounts, prod * quantity); // Calling the function recursively for lower part with corresponsing quantity
			}
			if (isPartLeaf) {
				//Adding count to part if it is a leaf
				leafPartCounts.put(upperPart, leafPartCounts.getOrDefault(upperPart, 0) + prod);
			}
		}
	}
}
