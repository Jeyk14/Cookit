package clientServlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/logoff")
public class logoff extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Cookie[] cookies = request.getCookies();
		Cookie cookie;
		
		// Remove the log cookies
		for (int i = 0; i < cookies.length; i++) {
			cookie = cookies[i];
			
			switch (cookie.getName()) {
			case "cookitEmail":
				cookie.setValue(null);
				cookie.setMaxAge(0);
				response.addCookie(cookie);
				break;
			case "cookitPass":
				cookie.setValue(null);
				cookie.setMaxAge(0);
				response.addCookie(cookie);
				break;
			}
			
		}
		
		request.getSession().invalidate();
		
		response.sendRedirect("index");
	}

}
