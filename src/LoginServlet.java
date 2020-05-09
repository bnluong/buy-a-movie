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

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 6L;

	// Create a dataSource which registered in web.xml
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource;
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");		// TODO: Implement remember me when we learn more about it
        
		try {
			// Get a connection from dataSource
			Connection dbConnection = dataSource.getConnection();
			
			JsonObject resultData = new JsonObject();
			
			String query = 
					"SELECT first_name, email, password FROM moviedb.customers\n" + 
					"WHERE email = ?";

			// Prepare the statement
			PreparedStatement statement = dbConnection.prepareStatement(query);
			statement.setString(1, email);

			// Execute the query
			ResultSet rSet = statement.executeQuery();

			if (rSet.next() == false) {
				resultData.addProperty("status", "fail");
				resultData.addProperty("message", "Account with the email address: " + email + " doesn't exist");
			} else {
				if(!rSet.getString("password").equals(password)) {
					resultData.addProperty("status", "fail");
					resultData.addProperty("message", "Incorect password");
				} else {
		            request.getSession().setAttribute("user", rSet.getString("first_name"));
		            resultData.addProperty("status", "success");
		            resultData.addProperty("message", "success");
				}
			}	

			// Write JSON string to output
			out.write(resultData.toString());
			// Set response status to 200 (OK)
			response.setStatus(200);
			// Close everything
			dbConnection.close();
			rSet.close();
			statement.close();
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
}