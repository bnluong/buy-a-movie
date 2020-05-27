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

import com.buyamovie.utilities.PosterScrapper;

/**
 * Servlet implementation class BrowsingServlet
 */
@WebServlet(name = "MovieServlet", urlPatterns = "/api/movie")
public class MovieServlet extends HttpServlet {
	private static final long serialVersionUID = 11L;

	// Create a dataSource which registered in web.xml
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		String movieID = request.getParameter("id");
		
		try {
			// Get a connection from dataSource
			Connection dbConnection = dataSource.getConnection();

			JsonObject resultData = getMovieInfo(dbConnection, movieID);

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

	private JsonObject getMovieInfo(Connection dbConnection, String movieID) throws SQLException {
		JsonObject resultData = new JsonObject();
		
		String query =  "SELECT m.*, r.*\n" + 
						"FROM movies m\n" + 
						"LEFT JOIN ratings r ON m.id=r.movie_id\n" + 
						"WHERE m.id = ?";
		
		PreparedStatement statement = dbConnection.prepareStatement(query);
		statement.setString(1, movieID);
		
		ResultSet rSet = statement.executeQuery();
		
		while(rSet.next()) {
			resultData.addProperty("movie_id", rSet.getString("id"));
			resultData.addProperty("movie_title", rSet.getString("title"));
			resultData.addProperty("movie_year", rSet.getString("year"));
			resultData.addProperty("movie_director", rSet.getString("director"));
			resultData.addProperty("movie_rating", rSet.getString("rating"));
		}
		
		resultData.add("movie_genres", getMovieGenres(dbConnection, movieID));
		resultData.add("movie_stars", getMovieStars(dbConnection, movieID));

		PosterScrapper posterScraper = new PosterScrapper(); // Too slow T_T
		resultData.addProperty("movie_poster", posterScraper.getIMDBPoster("https://www.imdb.com/title/" + movieID));
		
		resultData.addProperty("movie_runtime", "2h 22min");
		resultData.addProperty("movie_overview", "In Gotham City, mentally troubled comedian Arthur Fleck is disregarded and mistreated by society. He then embarks on a downward spiral of revolution and bloody crime. This path brings him face-to-face with his alter-ego: the Joker. ");
		resultData.addProperty("movie_price", "15.99");
		
		rSet.close();
		statement.close();
		
		return resultData;
	}
	
	private JsonArray getMovieStars(Connection dbConnection, String movieID) throws SQLException {
		String query = "";
		PreparedStatement statement = null;

		query = "SELECT star_id, name\n" + 
				"FROM stars_in_movies sim, stars s\n" + 
				"WHERE sim.movie_id = ? AND sim.star_id = s.id";

		// Prepare the statement
		statement = dbConnection.prepareStatement(query);
		statement.setString(1, movieID);

		// Execute the query
		ResultSet rSet = statement.executeQuery();

		// Create an JsonArray object to hold the data
		JsonArray jArray = new JsonArray();

		// Add the retrieved data to the array
		while(rSet.next()) {			
			// Create a JsonObject based on the data we retrieve from the result set
			JsonObject jObject = new JsonObject();
			jObject.addProperty("star_id", rSet.getString("star_id"));
			jObject.addProperty("star_name", rSet.getString("name"));
			jArray.add(jObject);
		}

		rSet.close();
		statement.close();
		return jArray;
	}

	private JsonArray getMovieGenres(Connection dbConnection, String movieID) throws SQLException {
		String query = "";
		PreparedStatement statement = null;

		query = "SELECT name\n" + 
				"FROM genres_in_movies gim, genres g\n" + 
				"WHERE gim.movie_id = ? AND gim.genre_id = g.id";

		// Prepare the statement
		statement = dbConnection.prepareStatement(query);
		statement.setString(1, movieID);

		// Execute the query
		ResultSet rSet = statement.executeQuery();

		// Create an JsonArray object to hold the data
		JsonArray jArray = new JsonArray();

		// Add the retrieved data to the array
		while(rSet.next()) {			
			// Create a JsonObject based on the data we retrieve from the result set
			JsonObject jObject = new JsonObject();
			jObject.addProperty("genre_name", rSet.getString("name"));
			jArray.add(jObject);
		}

		rSet.close();
		statement.close();
		return jArray;
	}
}