package com.buyamovie.api;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import javax.annotation.Resource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.buyamovie.utilities.*;

/**
 * Servlet implementation class IndexServlet
 */
@WebServlet(name = "IndexServlet", urlPatterns = "/api/index")
public class IndexServlet extends HttpServlet {
	private static final long serialVersionUID = 5L;
	
	// Create a dataSource which registered in web.xml
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json"); // Response mime type

		// Output stream to STDOUT
		PrintWriter out = response.getWriter();
		
		try {
			// Get a connection from dataSource
			Connection dbConnection = dataSource.getConnection();
			
			JsonObject resultData = new JsonObject();
			
			JsonArray listOfGenres = getListOfGenres(dbConnection);
			JsonArray randomMovies = getRandomMovies(dbConnection, 3);
			
			IMDBScraper posterScraper = new IMDBScraper();
			for(int i = 0; i <randomMovies.size(); i++) {
				String movieID = randomMovies.get(i).getAsJsonObject().get("movie_id").getAsString();
				String moviePoster = posterScraper.getMoviePoster("https://www.imdb.com/title/" + movieID);
				randomMovies.get(i).getAsJsonObject().addProperty("movie_poster", moviePoster);
			}
			
			resultData.add("randomMovies", randomMovies);
			resultData.add("listOfGenres", listOfGenres);
			
			// Write JSON string to output
			out.write(resultData.toString());
			// Set response status to 200 (OK)
			response.setStatus(200);
			// Close DB Connection
			dbConnection.close();
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
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private JsonArray getRandomMovies(Connection dbConnection, Integer numMovies) throws SQLException {
		String query = 
				"SELECT m1.id, title, year\n" + 
				"FROM movies AS m1\n" + 
				"JOIN (SELECT id FROM movies\n" + 
				"ORDER BY RAND() LIMIT 10) as m2 ON m1.id = m2.id\n" + 
				"LIMIT ?";

		// Prepare the statement
		PreparedStatement statement = dbConnection.prepareStatement(query);
		statement.setInt(1, numMovies); // 1 represents the 1st "?" from the query

		// Execute the query
		ResultSet rSet = statement.executeQuery();

		// Create an JsonArray object to hold the data
		JsonArray jArray = new JsonArray();
		
		// Add the retrieved data to the array
		while(rSet.next()) {			
			// Create a JsonObject based on the data we retrieve from the result set
			JsonObject jObject = new JsonObject();
			jObject.addProperty("movie_id", rSet.getString("id"));
			jObject.addProperty("movie_title", rSet.getString("title"));
			jObject.addProperty("movie_year", rSet.getString("year"));
			
			jArray.add(jObject);
		}

		rSet.close();
		statement.close();
		return jArray;
	}

	private JsonArray getListOfGenres(Connection dbConnection) throws SQLException {
		String query = 
				"SELECT id as genre_id, name as genre_name \n" + 
				"FROM genres \n" + 
				"ORDER by genre_name ASC";

		// Prepare the statement
		PreparedStatement statement = dbConnection.prepareStatement(query);

		// Execute the query
		ResultSet rSet = statement.executeQuery();

		// Create an JsonArray object to hold the data
		JsonArray jArray = new JsonArray();
		
		// Add the retrieved data to the array
		while(rSet.next()) {
			JsonObject jObject = new JsonObject();
			jObject.addProperty("genre_id", rSet.getString("genre_id"));
			jObject.addProperty("genre_name", rSet.getString("genre_name"));

			jArray.add(jObject);
		}

		rSet.close();
		statement.close();
		return jArray;
	}
}