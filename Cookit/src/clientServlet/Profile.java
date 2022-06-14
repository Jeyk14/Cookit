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

//import conexionBD.Conexion;
import data.BeanCategoria;
import data.BeanReceta;
import data.BeanUsuario;
import data.ConsultaAbierta;
import dbConnection.Connect;
import dbConnection.SimpleQuery;
import toolkit.PrepareSession;
import toolkit.typeCkecker;

/**
 * Servlet implementation class profile
 */
@WebServlet("/profile")
public class Profile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrepareSession.prepare(request, response);

		HttpSession sess = request.getSession();
		Connect con = new Connect("a21_jortnu", "a21_jortnu", "a21_jortnu");
		ConsultaAbierta openQuery;
		SimpleQuery sq = new SimpleQuery("a21_jortnu", "a21_jortnu", "a21_jortnu");

		String id = request.getParameter("id"); // The id of the user's profile
		int pag = 1;
		int offset = 0;
		String header = "WEB-INF/profile.jsp";
		String getPag = request.getParameter("pag");

		BeanUsuario myself = new BeanUsuario(); // In case this is the logged user's profile
		BeanUsuario user = new BeanUsuario(); // The data of the owner of this profile
		String queryUsr = "SELECT id, nombre, edad, dieta, nacionalidad, creacion FROM cookit.usuario WHERE id = ";
		Calendar auxCal;

		boolean isMyself = false;
		boolean isFound = false;

		int elemCount = 9;

		BeanReceta[] recipeList = new BeanReceta[9]; // this user's recipes
		BeanCategoria[] catList = new BeanCategoria[9];
//		String queryRecipe = "SELECT rec.id, pub.fecha, cat.nombre, pub.titulo, rec.tiempo, rec.tags, pub.id, pub.estrellas, pub.estado "
//				+ "FROM cookit.publicacion AS pub INNER JOIN cookit.receta as rec ON pub.id = rec.id_publicacion "
//				+ "INNER JOIN cookit.usuario AS usu ON pub.id_usuario = usu.id "
//				+ "INNER JOIN cookit.categoria AS cat ON rec.id_categoria = cat.id WHERE usu.id = ";

		Object[][] result = null;

		// --------------------------------------------------------------------
		// GETTING THE PROFILE'S DATA

		if (request.getSession().getAttribute("myself") != null) {
			myself = (BeanUsuario) request.getSession().getAttribute("myself");
		}

		if (getPag != null && typeCkecker.isInt(getPag)) {
			pag = Integer.valueOf(getPag);
		}
		
		if (id != null) {
			// an user ID was given
			
			if (typeCkecker.isInt(id)) {
				// the id goven is a number
				
				if(Integer.parseInt(id) == myself.getId()) {
					// id is myself id -> load myself
					
					user.setId(myself.getId());
					user.setNombre(myself.getNombre());
					user.setEdad(myself.getEdad());
					user.setDieta(myself.getDieta());
					user.setNacionalidad(myself.getNacionalidad());
					user.setCreacion(myself.getCreacion());
					
					isFound = true;
					isMyself = true;
					
				} else {
					// id is not myswlf id -> load from DB
					
					con.openConnection();
					openQuery = new ConsultaAbierta();
			
					result = openQuery.select(con.getConexion(), queryUsr + id, 6);
					con.closeConnection();
			
					if (result.length > 0) {
						
						user = new BeanUsuario();
			
						user.setId((int) result[0][0]);
						user.setNombre((String) result[0][1]);
						user.setEdad((int) result[0][2]);
						user.setDieta((String) result[0][3]);
						user.setNacionalidad((String) result[0][4]);
						auxCal = new GregorianCalendar();
						auxCal.setTime((java.sql.Date) result[0][5]);
						user.setCreacion(auxCal);
			
						isFound = true;
						con.closeConnection();
						
					} else {
						// no user found with given id
						isFound = false;
					}
				}
				
			} else {
				// id is not a valid number -> show message
				header = "index";
				isFound = false;
			}
			
		} else {
			// no user ID given -> load myself
			
			if(!myself.getNombre().isEmpty()) {
				// myself exists -> load myself
				
				user.setId(myself.getId());
				user.setNombre(myself.getNombre());
				user.setEdad(myself.getEdad());
				user.setDieta(myself.getDieta());
				user.setNacionalidad(myself.getNacionalidad());
				user.setCreacion(myself.getCreacion());
				
				isFound = true;
				isMyself = true;
				
			} else {
				// myself does not exist -> do nothing
				isFound = false;
			}
			
		}

		// --------------------------------------------------------------------
		// SEARCHED'S USER 9 LAST RECIPES

		if (isFound) {
			// If a user is found, search for thas user's recipes

//			queryRecipe += user.getId() + " "; // Add the user's ID to the search of recipes

			if (pag > 1) {
				offset = (pag - 1) * 9;
			}

			// Like the index query, but 9 elements (3 rows) and no special search

			result = sq.select("cookit.publicacion AS pub INNER JOIN cookit.receta as rec ON pub.id = rec.id_publicacion INNER JOIN cookit.usuario AS usu ON pub.id_usuario = usu.id INNER JOIN cookit.categoria AS cat ON rec.id_categoria = cat.id",
					new String[] {"rec.id", "pub.fecha", "cat.nombre", "pub.titulo", "rec.tiempo", "rec.tags", "pub.id", "pub.estrellas", "pub.estado"},
					"usu.id = "+user.getId(), 
					"pub.fecha DESC", 9, offset);
			
			System.out.println("result length "+result.length);

			if (result.length < 9) {
				elemCount = result.length;
			}

			if (result.length < 1) {
				request.setAttribute("noRecipe", "No se han encontrado recetas");
			}
			
			System.out.println("elem count "+elemCount);
			
			for (int i = 0; i < elemCount; i++) {
				
				// If logged -> can see your blocked/hidden posts
				
				recipeList[i] = new BeanReceta();
				catList[i] = new BeanCategoria();

				recipeList[i].setId((int) result[i][0]);
				auxCal = new GregorianCalendar();
				auxCal.setTime((java.sql.Date) result[i][1]);
				recipeList[i].setFecha(auxCal);
				catList[i].setNombre((String) result[i][2]);
				recipeList[i].setTitulo((String) result[i][3]);
				recipeList[i].setTiempo((int) result[i][4]);
				recipeList[i].setTags((String) result[i][5]);
				recipeList[i].setIdPublicacion((int) result[i][6]);
				recipeList[i].setEstrellas((int) result[i][7]);
				recipeList[i].setEstado((String) result[i][8]);

			}
			
		}

		System.out.println("Soy yo? "+isMyself);
		
		request.setAttribute("isFound", isFound);
		request.setAttribute("isMyself", isMyself);
		
		request.setAttribute("recipeList", recipeList);
		request.setAttribute("catList", catList);
		request.setAttribute("user", user);
		sess.setAttribute("curPage", "profile");
		sq.closeConnection();

		request.getRequestDispatcher(header).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
