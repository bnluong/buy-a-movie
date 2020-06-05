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
import javax.sql.DataSource;

import com.buyamovie.utilities.IMDBScraper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class CartRetrieveServlet
 */
@WebServlet(name = "/CartRetrieveServlet", urlPatterns = "/api/cart/retrieve")
public class CartRetrieveServlet extends HttpServlet {
	private static final long serialVersionUID = 15L;
    
	// Create a dataSource which registered in web.xml
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		String userEmail = request.getParameter("user");
		
		try {
			// Get a connection from dataSource
			Connection dbConnection = dataSource.getConnection();

			JsonArray resultData;

			String cartID = getCartID(dbConnection, userEmail);
			
			resultData = getCartContent(dbConnection, cartID);

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
	
	private JsonArray getCartContent(Connection dbConnection, String cartID) throws SQLException {
		JsonArray cartContent = new JsonArray();
		String query = "SELECT cart_items.movie_id, cart_items.quantity, cart_items.price, movies.title, movies.year\n" + 
				"FROM cart_items\n" + 
				"INNER JOIN movies ON movies.id = cart_items.movie_id\n" + 
				"WHERE cart_items.cart_id = ?";
		PreparedStatement statement = dbConnection.prepareStatement(query);
		statement.setString(1, cartID);
		
		ResultSet rSet = statement.executeQuery();
		while(rSet.next()) {
			JsonObject jObject = new JsonObject();
			jObject.addProperty("movie_id", rSet.getString(1));
			jObject.addProperty("movie_quantity", rSet.getString(2));
			jObject.addProperty("movie_price", rSet.getString(3));
			jObject.addProperty("movie_title", rSet.getString(4));
			jObject.addProperty("movie_year", rSet.getString(5));
			//jObject.addProperty("movie_poster", rSet.getString(6));
			IMDBScraper imdbScraper = new IMDBScraper("https://www.imdb.com/title/" + rSet.getString(1));
			jObject.addProperty("movie_poster", imdbScraper.getMoviePoster());
			cartContent.add(jObject);
		}
		return cartContent;
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
