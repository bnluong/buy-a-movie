package com.buyamovie.api.cart;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CartAddServlet
 */
@WebServlet(name = "/CartAddServlet", urlPatterns = "/api/cart/add")
public class CartAddServlet extends HttpServlet {
	private static final long serialVersionUID = 14L;
       
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String title = request.getParameter("title");
        String price = request.getParameter("price");
        String quantity = request.getParameter("quantity");
        
        return;
	}

}
