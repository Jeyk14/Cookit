package clientServlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/createRecipe")
public class CreateRecipe extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String header = "WEB-INF/createRecipe.jsp";
		
		if(request.getSession().getAttribute("myself") == null) {
			header = "index";
		}
		
		/*
		 * TODO: if user has a unpublished recipe, load this recipe instead
		 * For this, change the field oculto to estado
		 * estado 0 = not published (editing)
		 * estado 1 = published and visible
		 * estado 2 = published but blocked
		 */
		
		request.getSession().setAttribute("curPage", "recipeMod");
		
		request.getRequestDispatcher(header).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
