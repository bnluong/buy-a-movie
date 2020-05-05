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
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setContentType("application/json"); // Response mime type

		// Output stream to STDOUT
		PrintWriter out = response.getWriter();
		try {
			// Get a connection from dataSource
			Connection dbConnection = dataSource.getConnection();

			// Declare our statement
			Statement statement = dbConnection.createStatement();

			// Declare the query
			String query = 
					"SELECT title, year\n" + 
					"FROM movies AS m1 \n" + 
					"JOIN (SELECT id FROM movies ORDER BY RAND() LIMIT 10) as m2 ON m1.id = m2.id \n" + 
					"LIMIT 3";

			// Perform the query
			ResultSet resultSet = statement.executeQuery(query);

			// Declare a jsonArray to store the result of the query
			JsonArray jsonArray = new JsonArray();

			// Iterate through each row of resultSet
			while(resultSet.next()) {
				// Create a JsonObject based on the data we retrieve from the result set
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("title", resultSet.getString("title"));
				jsonObject.addProperty("year", resultSet.getString("year"));
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
