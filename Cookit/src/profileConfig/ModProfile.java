package profileConfig;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import conexionBD.Conexion;
import data.BeanUsuario;
import dbConnection.Connect;
import toolkit.typeCkecker;

@WebServlet("/modProfile")
public class ModProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		BeanUsuario myself = new BeanUsuario();
		
		String id = request.getParameter("id");
		
		
		if(request.getSession().getAttribute("myself") != null) {
			myself = (BeanUsuario) request.getSession().getAttribute("myself");
		}
		
		// If myself if != given ID -> show profile instead of mod profile
		if(id.equals(Integer.toString(myself.getId()))) {
			request.setAttribute("id", id);
			request.getRequestDispatcher("WEB-INF/modProfile.jsp").forward(request, response);
		} else {
			request.getRequestDispatcher("profile?id="+myself.getId()).forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String queryMod = "UPDATE cookit.usuario set ";
		String changes = "";
		boolean coma = true;
		
		String newName = request.getParameter("newName");
		String newEmail = request.getParameter("newEmail");
		String newAge = request.getParameter("newAge");
		String newDiet = request.getParameter("newDiet");
		String newNation = request.getParameter("newNation");
		
		Connect con = new Connect();
		
		
		if (newName != null && newName.length() > 1) {
			if(!coma) { changes += ", ";}
			newName = newName.replace("'", "\'");
			changes += " nombre = '"+newName+"'";
			coma = false;
		}
		
		if (newEmail!= null && newEmail.length() > 1) {
			if (!coma) { changes += ", ";}
			newEmail = newEmail.replace("'", "\'");
			changes += " email = '"+newEmail+"'";
			coma = false;
		}
		
		if (newAge != null && newAge.length() > 1) {
			if(typeCkecker.isInt(newAge)){
				if(!coma) {changes += ", ";}
				changes += ", edad = ";
				coma = false;
			}
		}
		
		if (newDiet != null && newDiet.length() > 1) {
			if(!coma) { changes += ", "; }
			newDiet = newDiet.replace("'", "\'");
			changes += "dieta = '"+newDiet+"'";
			coma = false;
		}
		
		if (newName != null && newName.length() > 1) {
			if(!coma) { changes += ", "; }
			newName = newName.replace("'", "\'");
			changes += "nombre = '"+newName+"'";
			coma = false;
		}
		
		if(changes.length() > 2) {
			
			
			
		}
		
		doGet(request, response);
	}

}
