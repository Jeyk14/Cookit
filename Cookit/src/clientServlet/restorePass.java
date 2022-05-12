package clientServlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/restorePass")
public class restorePass extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// TODO: Fix the mail library and add the password restoration process
		/*
		 * Restore password:
		 * 	In login, user clicks in "i forgot my password"
		 * 	User is asked an email to send a temporal code
		 * 	If an account with said email exist, a code is sent to the email
		 * 	The user can insert the code, if correct -> change password
		 * 		If the user abandon the password change page, the process starts over
		 * */
		request.getSession().setAttribute("curPage", "log");
		request.getRequestDispatcher("WEB-INF/restorePass.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO: Get an email -> send a code to said email + code to session attribute
		// TODO: Get a code -> check if code = code in session. If true -> change pass
		//														If false -> retry
		doGet(request, response);
	}

}
