import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
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

/**
 * Servlet implementation class MovieListServlet which maps to url "/api/movie-list"
 */
@WebServlet(name = "/MovieListServlet", urlPatterns = "/api/movie-list")
public class MovieListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

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

            // Declare our statement
            Statement statement = dbConnection.createStatement();
            
            // Declare the query
            String query = "SELECT * FROM movies INNER JOIN ratings ON movies.id = ratings.movie_id ORDER BY ratings.rating DESC LIMIT 20;";

            // Perform the query
            ResultSet resultSet = statement.executeQuery(query);
            
            // Declare a jsonArray to store the result of the query
            JsonArray jsonArray = new JsonArray();

            // Iterate through each row of resultSet
            while(resultSet.next()) {
            	String movieID = resultSet.getString("id");
                String movieRating = resultSet.getString("rating");
                String movieTitle = resultSet.getString("title");
                String movieYear = resultSet.getString("year");
                String movieDirector = resultSet.getString("director");
                
                // Process query for list of stars and genres
                String moviestars = getStarsFromMovie(movieID, dbConnection);
                String movieGenres = getGenresFromMovie(movieID, dbConnection);

                // Create a JsonObject based on the data we retrieve from the result set
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("movie_id", movieID);
                jsonObject.addProperty("movie_rating", movieRating);
                jsonObject.addProperty("movie_title", movieTitle);
                jsonObject.addProperty("movie_year", movieYear);
                jsonObject.addProperty("movie_director", movieDirector);
                jsonObject.addProperty("movie_stars", moviestars);
                jsonObject.addProperty("movie_genres", movieGenres);
                jsonArray.add(jsonObject);
            }
            
            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

            resultSet.close();
            statement.close();
            dbConnection.close();
        } catch (Exception e) {
			// write error message JSON object to output
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			out.write(jsonObject.toString());

			// set reponse status to 500 (Internal Server Error)
			response.setStatus(500);
        }
        out.close();
	}
	
	private String getStarsFromMovie(String movieID, Connection dbConnection) throws SQLException {
        String result = "";        
        String query = 
        		"SELECT GROUP_CONCAT(DISTINCT stars.name SEPARATOR ', ') as names \n" + 
        		"FROM stars \n" + 
        		"INNER JOIN stars_in_movies \n" + 
        		"ON stars_in_movies.movie_id = '" + movieID + "'\n" + 
        		"WHERE stars.id = stars_in_movies.star_id;";
        
        Statement statement = dbConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next())
        	result = resultSet.getString("names");
        
        resultSet.close();
        statement.close();
		return result;
	}
	private String getGenresFromMovie(String movieID, Connection dbConnection) throws SQLException {
        String result = "";        
        String query = 
        		"SELECT GROUP_CONCAT(DISTINCT genres.name SEPARATOR ', ') as names \n" + 
        		"FROM genres \n" + 
        		"INNER JOIN genres_in_movies \n" + 
        		"ON genres_in_movies.movie_id = '" + movieID + "'\n" + 
        		"WHERE genres.id = genres_in_movies.genre_id;";
        
        Statement statement = dbConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while(resultSet.next())
        	result = resultSet.getString("names");
        
        resultSet.close();
        statement.close();
		return result;
	}
}