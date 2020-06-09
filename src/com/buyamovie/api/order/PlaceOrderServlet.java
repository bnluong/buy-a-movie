package com.buyamovie.api.order;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class OrderPlaceServlet
 */
@WebServlet(name = "PlaceOrderServlet", urlPatterns = "/api/order/place")
public class PlaceOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 19L;
    
	// Create a dataSource which registered in web.xml
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource;
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		String userEmail = request.getParameter("user");
		String cardNumber = request.getParameter("cardNumber");
		
		/* TODO: Update transaction table when database is updated 
		String expDate = request.getParameter("expDate");
		String securityCode = request.getParameter("securityCode");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String address = request.getParameter("address");
		String addressOptional = request.getParameter("addressOptional");
		String city = request.getParameter("city");
		String state = request.getParameter("state");
		String zipcode = request.getParameter("zipcode");
		String phoneNumber = request.getParameter("phoneNumber");
		*/

		try {
			// Get a connection from dataSource
			Connection dbConnection = dataSource.getConnection();

			JsonObject resultData = new JsonObject();
			
			String query = "SELECT *\n" + 
					"FROM credit_cards\n" + 
					"WHERE id = ?";
			
			PreparedStatement statement = dbConnection.prepareStatement(query);
			statement.setString(1, cardNumber);

			ResultSet rSet = statement.executeQuery();
			
			if(rSet.next()) {
				String cartID = getCartID(dbConnection, userEmail);
				String customerID = getCustomerID(dbConnection, userEmail);
				JsonArray cart = getCart(dbConnection, cartID);
				recordSale(dbConnection, customerID, cart);
				resultData.addProperty("status", "success");
				resultData.addProperty("message", "Thank you for your Purchase. Order placed successfully.");
			} else {
				resultData.addProperty("status", "fail");
				resultData.addProperty("message", "Failed to place order. Please try again.");
			}

			// Write JSON string to output
			out.write(resultData.toString());
			// Set response status to 200 (OK)
			response.setStatus(200);
			// Close everything
			dbConnection.close();
			//rSet.close();
			//statement.close();
		} catch (Exception e) {
			// Write error message JSON object to output
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			out.write(jsonObject.toString());

			// Set reponse status to 500 (Internal Server Error)
			response.setStatus(500);
		}
		out.close();
	}

	private void recordSale(Connection dbConnection, String customerID, JsonArray cart) throws SQLException {
		for(int i = 0; i < cart.size(); i++) {
			String movieID = cart.get(i).getAsString();
			
			String query = "INSERT INTO sales\n" + 
					"VALUES(NULL,?,?,CURDATE())";
			
			PreparedStatement statement = dbConnection.prepareStatement(query);
			statement.setString(1, customerID);
			statement.setString(2, movieID);
			
			if (statement.executeUpdate() == 0)
				throw new SQLException("Failed to record sale.");
		}
	}
	
	private JsonArray getCart(Connection dbConnection, String cartID) throws SQLException {
		JsonArray cart = new JsonArray();
		String query = "SELECT movie_id \n" + 
				"FROM moviedb.cart_items \n" + 
				"WHERE cart_id = ?";
		PreparedStatement statement = dbConnection.prepareStatement(query);
		statement.setString(1, cartID);

		ResultSet rSet = statement.executeQuery();
		
		while(rSet.next()) {
			cart.add(rSet.getString(1));
		}
		
		return cart;
	}

	private String getCustomerID(Connection dbConnection, String userEmail) throws SQLException {
		String customerID = "";
		String query = "SELECT id\n" + 
				"FROM customers\n" + 
				"WHERE customers.email = ?";
		
		PreparedStatement statement = dbConnection.prepareStatement(query);
		statement.setString(1, userEmail);

		ResultSet rSet = statement.executeQuery();
		
		if(rSet.next()) {
			customerID = rSet.getString(1);
		}
		
		return customerID;
	}

	private String getCartID(Connection dbConnection, String userEmail) throws SQLException {
		String cartID = "";
		String query = "SELECT carts.id\n" + 
				"FROM carts\n" + 
				"WHERE carts.user_email = ?";
		PreparedStatement statement = dbConnection.prepareStatement(query);
		statement.setString(1, userEmail);

		ResultSet rSet = statement.executeQuery();
		
		if (!rSet.next()) {
			query = "INSERT INTO carts (user_email)\n" + 
					"VALUES (?)";

			statement = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			statement.setString(1, userEmail);

			if (statement.executeUpdate() == 0)
				throw new SQLException("Creating new cart failed. No cart created");

			ResultSet generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				cartID = Integer.toString(generatedKeys.getInt(1));
				return cartID;
			}
			else
				throw new SQLException("Creating new cart failed. No cart created");
		} else {
			cartID = rSet.getString("id");
			return cartID;
		}
	}
}