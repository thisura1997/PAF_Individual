
package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Report {

	private Connection connect() {
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Report?serverTimezone=UTC", "root", "");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return con;
	}

	public String readReport() {
		String output = "";

		try {
			Connection con = connect();

			if (con == null) {
				return "Can not connect to the database.";
			}

			// Prepare the html table to be displayed
			output = "<table border='2'>" + "<tr><th>Full Name</th>" + "<th>Reason</th>"
					+ "<th>Address</th>" + "<th>Phone Number</th>" + "<th>Update</th>" + "<th>Remove</th></tr>";

			String query = "select * from Report";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			// iterate through the rows in the result set
			while (rs.next()) {
				String patientID = Integer.toString(rs.getInt("patientID"));
				String name = rs.getString("name");
				String dep = rs.getString("dep");
				String address = rs.getString("address");
				String number = rs.getString("number");

				// Add into the html table
				output += "<tr><td><input id='hidItemIDUpdate'name='hidItemIDUpdate' type='hidden' value='" + patientID
						+ "'>" + name + "</td>";
				output += "<td>" + dep + "</td>";
				output += "<td>" + address + "</td>";
				output += "<td>" + number + "</td>";

				// buttons
				output += "<td><input name='btnUpdate'type='button' "
						+ "value='Update'class='btnUpdate btn btn-secondary'></td>"
						+ "<td><input name='btnRemove'type='button' "
						+ "value='Remove'class='btnRemove btn btn-danger'data-itemid='" + patientID + "'>"
						+ "</td></tr>";
			}

			con.close();

			// Complete the html table
			output += "</table>";

		} catch (Exception e) {
			output = "Error while reading the Report Info.";
			System.err.println(e.getMessage());
		}

		return output;
	}

	
	public String insertReport(String name, String dep, String address, String number) {
		String output = "";
		try {
			Connection con = connect();

			if (con == null) {
				return "Can not connect to the database for inserting info.";
			}

			// create a prepared statement
			String query = " insert into patient(`patientID`,`name`,`dep`,`address`,`number`) values (?, ?, ?, ?, ?)";

			PreparedStatement preparedStmt = con.prepareStatement(query);

			// binding values
			preparedStmt.setInt(1, 0);
			preparedStmt.setString(2, name);
			preparedStmt.setString(3, dep);
			preparedStmt.setString(4, address);
			preparedStmt.setString(5, number);

			// execute the statement
			preparedStmt.execute();
			con.close();

			String newItems = readReport();
			output = "{\"status\":\"success\", \"data\": \"" + newItems + "\"}";
		} catch (Exception e) {
			output = "{\"status\":\"error\", \"data\": \"Can not insert the Report info.\"}";
			System.err.println(e.getMessage());
		}

		return output;
	}

	
	public String updatePatient(String patientID, String name, String dep, String address, String number) {
		String output = "";

		try {
			Connection con = connect();

			if (con == null) {
				return "Can not connect to the database for update patient info.";
			}

			// create a prepared statement
			String query = "UPDATE doctor SET name=?,dep=?,address=?,cvc=? WHERE DoctorID=?";
			PreparedStatement preparedStmt = con.prepareStatement(query);

			// binding values
			preparedStmt.setString(1, name);
			preparedStmt.setString(2, dep);
			preparedStmt.setString(3, address);
			preparedStmt.setString(4, number);
			preparedStmt.setInt(5, Integer.parseInt(patientID));

			// execute the statement
			preparedStmt.execute();
			con.close();

			String newItems = readReport();
			output = "{\"status\":\"success\", \"data\": \"" + newItems + "\"}";
		} catch (Exception e) {
			output = "{\"status\":\"error\", \"data\": \"Can not update the patient info.\"}";
			System.err.println(e.getMessage());
		}

		return output;
	}

	
	public String deletepatient(String patientID) {
		String output = "";

		try {
			Connection con = connect();

			if (con == null) {
				return "Can not connect to the database for delete patient info.";
			}

			// create a prepared statement
			String query = "delete from patient where patientID=?";
			PreparedStatement preparedStmt = con.prepareStatement(query);

			// binding values
			preparedStmt.setInt(1, Integer.parseInt(patientID));

			// execute the statement
			preparedStmt.execute();
			con.close();

			String newItems = readReport();
			output = "{\"status\":\"success\", \"data\": \"" + newItems + "\"}";
		} catch (Exception e) {
			output = "{\"status\":\"error\", \"data\": \"Can not delete the patient info.\"}";
			System.err.println(e.getMessage());
		}

		return output;
	}
}
