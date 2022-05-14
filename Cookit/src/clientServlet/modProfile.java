package clientServlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.BeanUsuario;
import dbConnection.Connect;
import dbConnection.SimpleQuery;

@WebServlet("/modProfile")
public class modProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		BeanUsuario myself = new BeanUsuario();
		
		String id = "0";

		if(request.getParameter("id") != null) {
			id = request.getParameter("id");
		} else if(request.getAttribute("id") != null) {
			id = (String) request.getAttribute("id");
		}

		if (request.getSession().getAttribute("myself") != null) {
			myself = (BeanUsuario) request.getSession().getAttribute("myself");
		}

		// If myself ID != given ID -> show profile instead of mod profile
		if (id.equals(Integer.toString(myself.getId()))) {
			// myID = given ID -> allow modProfile
			request.setAttribute("id", id);
			request.getSession().setAttribute("curPage", "profileMod");
			request.getRequestDispatcher("WEB-INF/modProfile.jsp").forward(request, response);
		} else {
			// myID != given ID -> show profile
			request.getSession().setAttribute("curPage", "profile");
			request.getRequestDispatcher("profile?id=" + myself.getId()).forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}