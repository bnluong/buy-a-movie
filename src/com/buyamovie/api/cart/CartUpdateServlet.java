package com.buyamovie.api.cart;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.buyamovie.usersession.UserSession;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class CartUpdateServlet
 */
@WebServlet(name = "CartUpdateServlet", urlPatterns = "/api/cart/update")
public class CartUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 16L;
	
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
		String cartItemID = request.getParameter("id");
		String quantity = request.getParameter("quantity");
		
		HttpSession session = request.getSession();
		UserSession currentUser = (UserSession) session.getAttribute("user_session");
		if(!currentUser.getUserEmail().equalsIgnoreCase(userEmail)) {
			JsonObject resultData = new JsonObject();

			resultData.addProperty("status", "fail");
			resultData.addProperty("message", "User not found");

			// Write JSON string to output
			out.write(resultData.toString());
			// Set response status to 200 (OK)
			response.setStatus(200);
			out.close();
			return;
		}
		
		if(Integer.parseInt(quantity) <= 0) {
			JsonObject resultData = new JsonObject();

			resultData.addProperty("status", "fail");
			resultData.addProperty("message", "Invalid item quantity");

			// Write JSON string to output
			out.write(resultData.toString());
			// Set response status to 200 (OK)
			response.setStatus(200);
			out.close();
			return;
		}
		
		try {
			// Get a connection from dataSource
			Connection dbConnection = dataSource.getConnection();

			JsonObject resultData = new JsonObject();
			
			String cartID = getCartID(dbConnection, userEmail);
			
			String query = 
					"UPDATE cart_items\n" + 
					"SET\n" + 
					"	quantity = ?\n" + 
					"WHERE\n" + 
					"	id = ? AND cart_id = ?";
			
			PreparedStatement statement = dbConnection.prepareStatement(query);
			statement.setInt(1, Integer.parseInt(quantity));
			statement.setString(2, cartItemID);
			statement.setString(3, cartID);
			
			if (statement.executeUpdate() == 0)
				throw new SQLException("Failed to update cart");
			
			resultData.addProperty("status", "success");
			resultData.addProperty("message", "Cart updated successfully");

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
