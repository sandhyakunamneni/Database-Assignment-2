import java.sql.*;
import java.util.Scanner;

public class SQLInterface {

	public static void correctInvalidArguments() {
		System.out.println("\n Invalid number of arguments!! Please provide 3 arguments  \n");
		System.exit(1);
	}

	// function to get database connection object
	public static Connection getDataBaseConnectionObject(String[] args) throws SQLException, ClassNotFoundException {
		String database = args[0];
		String username = args[1];
		String password = args[2];
		Class.forName("org.postgresql.Driver");// loading the driver class
		// creating database connection Object
		Connection conn = DriverManager.getConnection("jdbc:postgresql://cisdb/" + database, username, password);
		return conn;
	}

	public static void main(String[] args) throws ClassNotFoundException {
		if (args.length != 3) // validating arguments
			correctInvalidArguments();

		// getting database connection object
		try (Connection conn = getDataBaseConnectionObject(args)) {
			System.out.println("======Connection successful======== \n");

			Statement stmt = conn.createStatement(); // creating statement from connection object

			// Starting the dynamic loop
			while (true) {
				// Simulating sql interface
				System.out.print("SQL> ");
				Scanner myObj = new Scanner(System.in);
				String input = myObj.nextLine();

				if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("q")) {
					break;// exiting the SQL interface
				}
				try {
					boolean hasRecords = stmt.execute(input);// executing query
					if (hasRecords) {
						ResultSet rs = stmt.getResultSet();// getting records if exists
						ResultSetMetaData rsmd = rs.getMetaData();
						printResult(rs, rsmd); // calling function to print out results
						rs.close();
					} else {
						int rowsEffected = stmt.getUpdateCount();
						System.out.println(rowsEffected + " rows effected.");
					}
				} catch (SQLException e) {
					System.out.println("Error: " + e.getMessage());
				}
			}

			stmt.close();
			conn.close(); // closing the database connection
			System.out.println("*******Disconnected***************");
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public static void printResult(ResultSet rs, ResultSetMetaData rsmd) throws SQLException {
		int columnCount = rsmd.getColumnCount();

		// Printing the column names
		for (int i = 1; i <= columnCount; i++) {
			System.out.print(rsmd.getColumnName(i) + "\t");
		}
		System.out.println("\n" + " =====================================================================");

		// Printing the records
		while (rs.next()) {
			for (int i = 1; i <= columnCount; i++) {
				System.out.print(rs.getString(i) + "\t");
			}
			System.out.println();
		}
	}
}
