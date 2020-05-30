package com.buyamovie.apis;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.buyamovie.usersession.UserSession;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class SessionServlet
 */
@WebServlet(name = "SessionServlet", urlPatterns = "/api/session")
public class SessionServlet extends HttpServlet {
	private static final long serialVersionUID = 7L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		try {
			JsonObject resultData = new JsonObject();

			HttpSession session = request.getSession();
			UserSession currentUser = (UserSession) session.getAttribute("user_session");

			if(currentUser != null) {
				resultData.addProperty("user_name", currentUser.getUserName());
			} else {
				resultData.add("user_name", null);
			}

			// Write JSON string to output
			out.write(resultData.toString());
			// Set response status to 200 (OK)
			response.setStatus(200);
		} catch (Exception e) {
			// Write error message JSON object to output
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			out.write(jsonObject.toString());

			// Set reponse status to 500 (Internal Server Error)
			response.setStatus(500);		
		}
	}
}
