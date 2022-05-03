package clientServlet;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import conexionBD.Conexion;
import data.BeanCategoria;
import data.BeanReceta;
import data.BeanUsuario;
import data.ConsultaAbierta;
import toolkit.typeCkecker;

/**
 * Servlet implementation class profile
 */
@WebServlet("/profile")
public class profile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// TODO: search the user every time? Save the last searched user in a sess attr to reuse the data and reduce the db acces?
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession sess = request.getSession();
		Conexion con;
		ConsultaAbierta openQuery;
		
		String id = request.getParameter("id"); // The id of the user's profile
		int pag = 1;
		int offset = 0;
		String header = "WEB-INF/profile.jsp";
		
		BeanUsuario myself; // In case this is the logged user's profile
		BeanUsuario user = new BeanUsuario(); // The data of the owner of this profile
		String queryUsr = "SELECT id, nombre, edad, dieta, nacionalidad, creacion FROM cookit.usuario WHERE id = ";
		Calendar auxCal;

		BeanReceta[] recipeList = new BeanReceta[9]; // this user's recipes
		BeanCategoria[] catList = new BeanCategoria[9];
		String queryRecipe = "SELECT rec.id, pub.fecha, cat.nombre, pub.titulo, pub.likes, pub.dislikes, rec.tiempo, rec.tags " + 
				"FROM cookit.publicacion AS pub INNER JOIN cookit.receta as rec ON pub.id = rec.id_publicacion " + 
				"INNER JOIN cookit.usuario AS usu ON pub.id_usuario = usu.id " + 
				"INNER JOIN cookit.categoria AS cat ON rec.id_categoria = cat.id WHERE usu.id = ";
		
		Object[][] result;
		
		// --------------------------------------------------------------------
		// GETTING THE PROFILE'S DATA
		
		if(id != null) {
			
			if(typeCkecker.isInt(id)) {
			// if id received -> look for the user in the db
			
			con = new Conexion("a21_jortnu", "a21_jortnu", "a21_jortnu");
			con.abrirConexion();
			openQuery = new ConsultaAbierta();
			
			result = openQuery.select(con.getConexion(), queryUsr + id, 6);
			
			user = new BeanUsuario();
			
			user.setId((int) result[0][0]);
			user.setNombre((String) result[0][1]);
			user.setEdad((int) result[0][2]);
			user.setDieta((String) result[0][3]);
			user.setNacionalidad((String) result[0][4]);
			auxCal = new GregorianCalendar();
			auxCal.setTime((java.sql.Date) result[0][5]);
			user.setCreacion(auxCal);
			
			} else {
				// user typed something that isn't a number -> index
				header = "index";
				request.setAttribute("tempMsg", "Oops... Parece que ha habido un problema buscando el usuario con ID "+id);
			}
			
		} else if(sess.getAttribute("myself") != null) {
			// given id = null || nothing -> use myself id
			
			myself = (BeanUsuario) sess.getAttribute("myself");
			
			con = new Conexion("a21_jortnu", "a21_jortnu", "a21_jortnu");
			con.abrirConexion();
			openQuery = new ConsultaAbierta();
			
			result = openQuery.select(con.getConexion(), queryUsr + myself.getId(), 8);
			
			user = new BeanUsuario();
			
			user.setId((int) result[0][0]);
			user.setNombre((String) result[0][1]);
			user.setEdad((int) result[0][2]);
			user.setDieta((String) result[0][3]);
			user.setNacionalidad((String) result[0][4]);
			auxCal = new GregorianCalendar();
			auxCal.setTime((java.sql.Date) result[0][5]);
			user.setCreacion(auxCal); 
			
		} else {
			
			// no given ID && not logged in -> do nothing
			header = "index";
			request.setAttribute("tempMsg", "No se ha podido mostrar el perfil...<br />El usuario no existe");
			
		}
		
		// --------------------------------------------------------------------
		// SEARCHED'S USER 9 LAST RECIPES
		
		if(user.getNombre() != "" && user.getId() != 0) {
		// If a user is found, search for thas user's recipes
			
			queryRecipe += user.getId() + " "; // Add the user's ID to the search of recipes
			
			if(pag > 1) {
				offset = (pag - 1) * 9;
			}
			
			queryRecipe += " ORDER BY pub.fecha DESC LIMIT 12 OFFSET "+offset;
			
			// Like the index query, but 9 elements (3 rows) and no special search
			
			con = new Conexion("a21_jortnu", "a21_jortnu", "a21_jortnu");
			con.abrirConexion();
			openQuery = new ConsultaAbierta();
			result = openQuery.select(con.getConexion(), queryRecipe, 8);
			
			for (int i = 0; i < result.length; i++) {
				
				recipeList[i] = new BeanReceta();
				catList[i] = new BeanCategoria();
				
				recipeList[i].setId( (int) result[i][0]);
					auxCal = new GregorianCalendar();
					auxCal.setTime((java.sql.Date) result[i][1]);
				recipeList[i].setFecha(auxCal);
				catList[i].setNombre( (String) result[i][2]);
				recipeList[i].setTitulo( (String) result[i][3]);
				recipeList[i].setLikes( (int) result[i][4]);
				recipeList[i].setDislikes( (int) result[i][5]);
				recipeList[i].setTiempo( (int) result[i][6]);
				recipeList[i].setTags( (String) result[i][7]);
				
			}
		}
		
		request.setAttribute("recipeList", recipeList);
		request.setAttribute("catList", catList);
		request.setAttribute("user", user);
		
		
		request.getRequestDispatcher(header).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
