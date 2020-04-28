import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Servlet implementation class MovieInfoServlet
 */
@WebServlet(name = "MovieInfoServlet", urlPatterns = "/api/movie-info")
public class MovieInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 2L;
	
	// Setup the DataSource for the API
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		// Set response type -> json
		response.setContentType("application/json");
		
		// Retrieve parameter id from url request
		String id = request.getParameter("id");
		
		// Get a PrintWriter object that can send character text to the client
		PrintWriter out = response.getWriter();
		
		// API logic
		try {
			// Get db connection from DataSource
			Connection dbConnection = dataSource.getConnection();
			
			// Prepare a parameterized query represented by "?"
			String query = 
					"SELECT m.id, m.title, m.year, m.director, "
						+ "GROUP_CONCAT(DISTINCT g.name ORDER BY g.name ASC SEPARATOR ', ') as genres, "
						+ "r.rating\n" + 
					"FROM movies m\n" + 
					"INNER JOIN genres_in_movies gim ON m.id = gim.movie_id\n" + 
					"INNER JOIN genres g ON gim.genre_id = g.id\n" + 
					"INNER JOIN ratings r ON m.id = r.movie_id\n" + 
					"WHERE m.id = ?\n" + 
					"GROUP BY m.id, r.rating";
			PreparedStatement statement = dbConnection.prepareStatement(query);
			
			// Prepare the statement by changing the "?" from the id from the url
			statement.setString(1, id); // 1 represents the 1st "?" from the query
			
			// Execute the query and retrieve the data
			ResultSet rSet = statement.executeQuery();
			
			// Retrieve stars data
			JsonArray moviestars = getStarsFromMovie(id, dbConnection);
			
			// Create an JsonArray object to hold the data
			JsonArray jArray = new JsonArray();
			
			// Populate the data into the array
			while(rSet.next()) {
				// Create a JsonObject to hold the movie data
				JsonObject jObject = new JsonObject();
				jObject.addProperty("movie_id", rSet.getString("id"));
				jObject.addProperty("movie_title", rSet.getString("title"));
				jObject.addProperty("movie_year", rSet.getString("year"));
				jObject.addProperty("movie_director", rSet.getString("director"));
				jObject.addProperty("movie_genres", rSet.getString("genres"));
				jObject.add("movie_stars", moviestars);
				jObject.addProperty("movie_rating", rSet.getString("rating"));
				
				// Add the data to the array
				jArray.add(jObject);
			}
			
			// Write the JsonArray to the output 
			// i.e pack the jArray into a single string and send it to the client
			out.write(jArray.toString());
			
			// Set the response statuse to OK (OK 200)
			response.setStatus(200);
			
			// Close everything
			rSet.close();
			statement.close();
			dbConnection.close();
		} catch (Exception e) {
			// Write error message to JSON output
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage: ", e.getMessage());
			out.write(jsonObject.toString());
			
			// Set response status error (Internal Server Error 500)
			response.setStatus(500);
		}
		// Close output writer
		out.close();
	}

	/*
	 * Retrieve stars info (stars' names and stars' ids) from a movie identified by movieID
	 * Return: JsonArray object that contains the stars' info
	 */
	private JsonArray getStarsFromMovie(String movieID, Connection dbConnection) throws SQLException {
        String query = 
        		"SELECT s.id as star_id, s.name as star_name \n" + 
        		"FROM stars s \n" + 
        		"INNER JOIN stars_in_movies sim ON s.id = sim.star_id \n" + 
        		"WHERE sim.movie_id = ? \n" + 
        		"ORDER BY star_name ASC";
        
        // Prepare the parameterized statement
        PreparedStatement statement = dbConnection.prepareStatement(query);
        // Change the parameter to movieID
        statement.setString(1, movieID); // 1 represents the 1st "?" from the query
        
        // Create an JsonArray object to hold the data
     	JsonArray jArray = new JsonArray();
     	
     	// Execute the query
        ResultSet rSet = statement.executeQuery();
        
        // Prepare the retrieved data
        while(rSet.next()) {
			JsonObject jObject = new JsonObject();
			jObject.addProperty("star_id", rSet.getString("star_id"));
			jObject.addProperty("star_name", rSet.getString("star_name"));
			
			jArray.add(jObject);
        }
        
        rSet.close();
        statement.close();
		return jArray;
	}
}