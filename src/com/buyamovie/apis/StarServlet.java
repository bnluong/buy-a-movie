package com.buyamovie.apis;
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

import com.buyamovie.utilities.*;

/**
 * Servlet implementation class BrowsingServlet
 */
@WebServlet(name = "StarServlet", urlPatterns = "/api/star")
public class StarServlet extends HttpServlet {
	private static final long serialVersionUID = 12L;

	// Create a dataSource which registered in web.xml
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		String starID = request.getParameter("id");

		try {
			// Get a connection from dataSource
			Connection dbConnection = dataSource.getConnection();

			JsonObject resultData = getStarInfo(dbConnection, starID);

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

	private JsonObject getStarInfo(Connection dbConnection, String starID) throws SQLException {
		JsonObject starInfo = new JsonObject();

		String query =  "SELECT *\n" + 
						"FROM stars s\n" + 
						"WHERE s.id = ?";

		PreparedStatement statement = dbConnection.prepareStatement(query);
		statement.setString(1, starID);

		ResultSet rSet = statement.executeQuery();
		
		while(rSet.next()) {
			IMDBScraper imdbScraper = new IMDBScraper("https://www.imdb.com/name/" + rSet.getString("id"));

			starInfo.addProperty("star_id", rSet.getString("id"));
			starInfo.addProperty("star_name", rSet.getString("name"));
			starInfo.addProperty("star_portrait", imdbScraper.getStarPortrait()); 			// TODO: Update database
			starInfo.addProperty("star_profession", imdbScraper.getStarProfession());		// TODO: Update database
			starInfo.addProperty("star_gender", "Placeholder");								// TODO: Update database
			starInfo.addProperty("star_birthyear", rSet.getString("birth_year"));		
			starInfo.addProperty("star_bio", imdbScraper.getStarBio());						// TODO: Update database
			starInfo.add("star_known_for", getStarKnownFor(starID, dbConnection));			// TODO: Update database
			starInfo.add("star_filmography", getStarFilmography(starID, dbConnection));
		}

		rSet.close();
		statement.close();

		return starInfo;
	}

	private JsonArray getStarKnownFor(String starID, Connection dbConnection) throws SQLException {
		JsonArray knownFor = new JsonArray();

		String query =  "SELECT m.*, r.rating\n" + 
						"FROM movies m\n" + 
						"INNER JOIN stars_in_movies sim ON m.id = sim.movie_id\n" + 
						"LEFT JOIN ratings r ON m.id = r.movie_id\n" + 
						"WHERE sim.star_id = ?\n" + 
						"ORDER BY r.rating DESC\n" + 
						"LIMIT 3;";

		PreparedStatement statement = dbConnection.prepareStatement(query);
		statement.setString(1, starID);

		ResultSet rSet = statement.executeQuery();

		IMDBScraper posterScraper = new IMDBScraper(); // Too slow T_T
		
		while(rSet.next()) {     
			JsonObject jObject = new JsonObject();

			jObject.addProperty("movie_id", rSet.getString("id"));
			jObject.addProperty("movie_title", rSet.getString("title"));
			jObject.addProperty("movie_poster", posterScraper.getMoviePoster("https://www.imdb.com/title/" + rSet.getString("id")));

			knownFor.add(jObject);
		}

		rSet.close();
		statement.close();

		return knownFor;	
	}

	private JsonArray getStarFilmography(String starID, Connection dbConnection) throws SQLException {
		JsonArray filmography = new JsonArray();

		String query =  "SELECT m.*\n" + 
						"FROM movies m\n" + 
						"INNER JOIN stars_in_movies sim ON m.id = sim.movie_id\n" + 
						"WHERE sim.star_id = ?" +
						"ORDER BY m.year DESC\n";

		PreparedStatement statement = dbConnection.prepareStatement(query);
		statement.setString(1, starID);

		ResultSet rSet = statement.executeQuery();

		while(rSet.next()) {     
			JsonObject jObject = new JsonObject();

			jObject.addProperty("movie_id", rSet.getString("id"));
			jObject.addProperty("movie_title", rSet.getString("title"));
			jObject.addProperty("movie_year", rSet.getString("year"));
			jObject.addProperty("movie_star_as", "Placeholder");			// TODO: Update database

			filmography.add(jObject);
		}


		rSet.close();
		statement.close();

		return filmography;	
	}

}