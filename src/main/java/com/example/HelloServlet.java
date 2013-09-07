/**
 * 
 */
package com.example;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author aster
 * 
 */
public class HelloServlet extends HttpServlet {

	private static final long serialVersionUID = -1789675035507615582L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println("<h1>Hello Servlet</h1>");
		out.println("</body>");
		out.println("</html>");
	}

}
