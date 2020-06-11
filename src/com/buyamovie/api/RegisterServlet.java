package com.buyamovie.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet(name = "RegisterServlet", urlPatterns = "/api/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 20L;
	
	// Create a dataSource which registered in web.xml
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource;
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String address = request.getParameter("address");
		String addressOptional = request.getParameter("addressOptional");
		String city = request.getParameter("city");
		String state = request.getParameter("state");
		String zipcode = request.getParameter("zipcode");
		String phoneNumber = request.getParameter("phoneNumber");
		String cardNumber = request.getParameter("cardNumber");
		String expDate = request.getParameter("expDate");
		String securityCode = request.getParameter("securityCode");

		String parsedAddress = address + " " + addressOptional + ", " + city + ", " + state + " " + zipcode;
		
		JsonObject responseData = new JsonObject();

		try {
			// Get a connection from dataSource
			Connection dbConnection = dataSource.getConnection();
			
			registerCustomer(dbConnection, firstName, lastName, cardNumber, parsedAddress, email, password);
			registerCreditCard(dbConnection, cardNumber, firstName, lastName, expDate);

			responseData.addProperty("status", "success");
			responseData.addProperty("message", "Account successfully registered.");
			out.write(responseData.toString());
			dbConnection.close();
		} catch (Exception e) {
			// Write error message JSON object to output
			responseData.addProperty("status", "fail");
			responseData.addProperty("message", e.getMessage());
			out.write(responseData.toString());

			// Set reponse status to 500 (Internal Server Error)
			response.setStatus(500);
		}
		out.close();
	}


	private void registerCreditCard(Connection dbConnection, String cardNumber, String firstName, String lastName,
			String expDate) throws SQLException {
		String query = "INSERT INTO credit_cards\n" + 
				"VALUES (?,?,?,?)";
		PreparedStatement statement = dbConnection.prepareStatement(query);
		statement.setString(1, cardNumber);
		statement.setString(2, firstName);
		statement.setString(3, lastName);
		statement.setString(4, expDate);

		if (statement.executeUpdate() == 0)
			throw new SQLException("Failed to insert credit card.");
	}


	private void registerCustomer(Connection dbConnection, String firstName, String lastName, String cardNumber,
			String address, String email, String password) throws SQLException {
		String query = "INSERT INTO customers\n" + 
				"VALUES (NULL, ?, ?, ?, ?, ?, ?)";
		PreparedStatement statement = dbConnection.prepareStatement(query);
		statement.setString(1, firstName);
		statement.setString(2, lastName);
		statement.setString(3, cardNumber);
		statement.setString(4, address);
		statement.setString(5, email);
		statement.setString(6, password);
				
		if (statement.executeUpdate() == 0)
			throw new SQLException("Failed to create account.");
	}
}
