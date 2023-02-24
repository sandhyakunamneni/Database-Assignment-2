import java.sql.*;
import java.util.Scanner;

public class StudentSim {

	public static void correctInvalidArguments() {
		System.out.println("\n Invalid number of arguments!! Please provide 3 arguments  \n");
		System.exit(1);
	}

	public static void main(String[] args) throws ClassNotFoundException {
		if (args.length != 3) // validating arguments
			correctInvalidArguments();
		// getting student details from user
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter student name ");
		String studentName = scanner.nextLine();
		System.out.print("Enter student number ");
		String studentNumber = scanner.nextLine();
		System.out.print("Enter student date of birth (YYYY-MM-DD) ");
		String studentDoB = scanner.nextLine();

		// getting database connection object
		try (Connection conn = getDBConnectionObject(args)) {
			Statement stmt = conn.createStatement(); // creating statement from connection object
			// Build SQL query based on user input
			String sql = getQuery(studentName, studentNumber, studentDoB);
			// Execute query and print results
			ResultSet rs = stmt.executeQuery(sql);
			printResults(rs);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// function to build query based on user input
	private static String getQuery(String studentName, String studentNumber, String studentDoB) {
		String sql = "SELECT * FROM Students WHERE 1=1";
		if (!studentName.isBlank()) {
			sql += " AND Sname = '" + studentName + "'";
		}
		if (!studentNumber.isBlank()) {
			sql += " AND Snumber = '" + studentNumber + "'";
		}
		if (!studentDoB.isBlank()) {
			sql += " AND DoB = '" + studentDoB + "'";
		}
		return sql;
	}

	// function to print the results
	private static void printResults(ResultSet rs) throws SQLException {
		while (rs.next()) {
			int snumber = rs.getInt("Snumber");
			String sname = rs.getString("Sname");
			String sdob = rs.getString("DoB");
			System.out.println(snumber + "\t" + sname + "\t" + sdob);
		}
	}

	// function to get database connection object
	private static Connection getDBConnectionObject(String[] args) throws ClassNotFoundException, SQLException {
		String databaseURL = args[0];
		String username = args[1];
		String password = args[2];
		Class.forName("org.postgresql.Driver"); // load the driver
		Connection conn = DriverManager.getConnection("jdbc:postgresql://cisdb/" + databaseURL, username, password);
		return conn;
	}
}