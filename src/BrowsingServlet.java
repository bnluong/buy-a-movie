import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.buyamovie.utilities.PosterScrapper;

/**
 * Servlet implementation class BrowsingServlet
 */
@WebServlet(name = "BrowsingServlet", urlPatterns = "/api/browsing")
public class BrowsingServlet extends HttpServlet {
	private static final long serialVersionUID = 10L;

	// Create a dataSource which registered in web.xml
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		String titleParam = request.getParameter("title");
		String genreParam = request.getParameter("genre");
		String sortParam = request.getParameter("sortBy");
		String orderParam = request.getParameter("order");
		String numResultsParam = request.getParameter("numResults");
		String offsetParam = request.getParameter("offset");

		try {
			// Get a connection from dataSource
			Connection dbConnection = dataSource.getConnection();
			
			String totalMovies = getTotalMovies(dbConnection, titleParam, genreParam);
			
			JsonArray resultData = getMoviesList(dbConnection, titleParam, genreParam, sortParam, orderParam, numResultsParam, offsetParam);
			
			PosterScrapper posterScraper = new PosterScrapper(); // Too slow T_T
			
			for(int i = 0; i < resultData.size(); i++) {
				String movieID = resultData.get(i).getAsJsonObject().get("movie_id").getAsString();
				JsonArray movieGenres = getMovieGenres(dbConnection, (String) movieID);
				JsonArray movieStars = getMovieStars(dbConnection, (String) movieID);
				resultData.get(i).getAsJsonObject().add("movie_genres", movieGenres);
				resultData.get(i).getAsJsonObject().add("movie_stars", movieStars);
				//String moviePoster = posterScraper.getIMDBPoster("https://www.imdb.com/title/" + movieID);
				String moviePoster = "";
				resultData.get(i).getAsJsonObject().addProperty("movie_poster", moviePoster);
			}

			resultData.add(totalMovies);
			
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

	private String getTotalMovies(Connection dbConnection, String titleParam, String genreParam) throws SQLException {
		String totalMovies = "";
		String query = "";
		PreparedStatement statement = null;

		if(!titleParam.equalsIgnoreCase("None")) {
			// TODO: Implement "Browse by Title"
		}
		if(!genreParam.equalsIgnoreCase("None")) {
			query = "SELECT COUNT(*) as total_movies\n" + 
					"FROM movies m, ratings r\n" + 
					"WHERE m.id IN ( SELECT gim.movie_id\n" + 
					"				FROM genres g, genres_in_movies gim\n" + 
					"				WHERE g.name = ? AND g.id = gim.genre_id )\n" + 
					"			AND m.id = r.movie_id";

			// Prepare the statement
			statement = dbConnection.prepareStatement(query);
			statement.setString(1, genreParam);
		}

		// Execute the query
		ResultSet rSet = statement.executeQuery();

		while(rSet.next())	
			totalMovies = rSet.getString("total_movies");
		
		
		rSet.close();
		statement.close();
		
		return totalMovies;
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

	private JsonArray getMoviesList(Connection dbConnection, String titleParam, String genreParam, String sortParam, String orderParam,
			String numResultsParam, String offsetParam) throws SQLException {

		String query = "";
		PreparedStatement statement = null;

		if(!titleParam.equalsIgnoreCase("None")) {
			// TODO: Implement "Browse by Title"
		}
		if(!genreParam.equalsIgnoreCase("None")) {
			query = 
					"SELECT m.*, r.rating\n" + 
							"FROM movies m, ratings r\n" + 
							"WHERE m.id IN ( SELECT gim.movie_id\n" + 
							"				FROM genres g, genres_in_movies gim\n" + 
							"				WHERE g.name = ? AND g.id = gim.genre_id )\n" + 
							"			AND m.id = r.movie_id\n" + 
							"ORDER BY " + sortParam + " " + orderParam + "\n" + 
							"LIMIT ? OFFSET ?";

			// Prepare the statement
			statement = dbConnection.prepareStatement(query);
			statement.setString(1, genreParam);
			statement.setInt(2, Integer.parseInt(numResultsParam));
			statement.setInt(3, Integer.parseInt(offsetParam));
		}

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
			jObject.addProperty("movie_director", rSet.getString("director"));
			jObject.addProperty("movie_rating", rSet.getString("rating"));
			jArray.add(jObject);
		}

		rSet.close();
		statement.close();
		return jArray;
	}
}