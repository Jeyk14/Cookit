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
import toolkit.PrepareSession;

@WebServlet("/modProfile")
public class ModProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		BeanUsuario myself = new BeanUsuario();
		
		PrepareSession.prepare(request, response);
		
		String id = "0";

		if (request.getSession().getAttribute("myself") != null) {
			myself = (BeanUsuario) request.getSession().getAttribute("myself");
			id = Integer.toString(myself.getId());
			request.setAttribute("id", id);
			request.getSession().setAttribute("curPage", "profileMod");
			request.getRequestDispatcher("WEB-INF/modProfile.jsp").forward(request, response);
		} else {
			// not logged -> no mod
			request.getSession().setAttribute("curPage", "profile");
			request.getRequestDispatcher("profile?id=" + myself.getId()).forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
