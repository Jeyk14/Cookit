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

//import conexionBD.*;
import data.BeanCategoria;
import data.BeanReceta;
import data.BeanUsuario;
import data.ConsultaAbierta;
import dbConnection.Connect;

/**
 * Servlet implementation class inicio
 */
@WebServlet("/index")
public class Index extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*
	 * Prepare the page using the search values stored on the session
	 * Session attribute are created if unexistent
	 * 
	 * Search by: recipe title, username, stars
	 * Order by: date, title, likes, time
	 * 
	 * */
	
	/*-
	 
	 Stars system 5-(((dislikes×100)÷likes)÷10)
	 	The rating is celi rounded
	 	Anything below 0 count as 0 stars
	 	Anything above 0 count as 1 star
	 	etc
	 	Anything abobe 4 count as 5 starsceiling
	 
	 SELECT usu.id, usu.nombre, pub.fecha, cat.nombre, rec.img, rec.procedimiento, rec.tiempo, rec.ingredientes, rec.tags
FROM cookit.publicacion AS pub INNER JOIN cookit.receta as rec ON pub.id = rec.id_publicacion
INNER JOIN cookit.usuario AS usu ON pub.id_usuario = usu.id
INNER JOIN cookit.categoria AS cat ON rec.id_categoria = cat.id
	 
	 
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession sesion = request.getSession();
		//Conexion con = new Conexion("a21_jortnu", "a21_jortnu", "a21_jortnu");
		ConsultaAbierta consult = new ConsultaAbierta();
		Connect con = new Connect("a21_jortnu", "a21_jortnu", "a21_jortnu");
		
		// The beans that will be used to transport the data
		BeanUsuario[] userList = new BeanUsuario[12];
		BeanReceta[] recipeList = new BeanReceta[12];
		//BeanPublicacion[] indexPost = new BeanPublicacion[12];
		BeanCategoria[] catList = new BeanCategoria[12];
		
		// Var used for the search
		String searchtype = "title"; //Where's being searched
		String searchValue = ""; // What's beng searched
		
		String order = "datedesc"; // The order of the search
		
		int pag = 1;
		int offset = 0;
		
		// Select the necessary field to be used
		String query = "SELECT usu.id, rec.id, usu.nombre, pub.fecha, cat.nombre, pub.titulo, pub.likes, pub.dislikes, rec.tiempo, rec.tags " + 
				"FROM cookit.publicacion AS pub INNER JOIN cookit.receta as rec ON pub.id = rec.id_publicacion " + 
				"INNER JOIN cookit.usuario AS usu ON pub.id_usuario = usu.id " + 
				"INNER JOIN cookit.categoria AS cat ON rec.id_categoria = cat.id ";
		
		Object[][] result = null; // Will store the result of the query
		Calendar aux;
		
		// -----------------------------------------------------------------------------
		
		// The values aren't in the session -> add them to the session
		if(sesion.getAttribute("searchtype") != null) {
			searchtype = (String) sesion.getAttribute("searchtype");
		} else {
			sesion.setAttribute("searchtype", searchtype);
		}
		
		if(sesion.getAttribute("searchValue") != null) {
			searchValue = (String) sesion.getAttribute("searchValue");
		} else {
			sesion.setAttribute("searchValue", searchValue);
		}
		
		if(sesion.getAttribute("order") != null) {
			searchValue = (String) sesion.getAttribute("order");
		} else {
			sesion.setAttribute("searchValue", searchValue);
		}
		
		// -----------------------------------------------------------------------------
		
		// Add the search type and value to the query
			
			switch (searchtype) {
			
			case "title":
				if(searchValue.length() < 3) {
					// not enough words for a search -> do nothing
				} else {
					query += " WHERE rec.titulo LIKE '%"+searchValue+"%' ";
				}
				break;
				
			case "username":
				if(searchValue.length() < 3) {
					// not enough words for a search -> do nothing
				} else {
					query += " WHERE usu.nombre LIKE '%"+searchValue+"%' ";
				}
				break;
				
			case "star":
				query += " WHERE (SELECT (5-((dislikes * 100)/likes)/10) FROM cookit.publicacion WHERE id = pub.id) > "+ (Integer.valueOf(searchValue) - 1)+" ";
				break;
			default:
				// the type is invalid -> do nothing
				break;
			}

			// Add the order to 
			switch (order) {
			case "none":
				// do nothing
				break;
			case "dateasc":
				query += " ORDER BY pub.fecha DESC ";
				break;
			case "datedesc":
				query += " ORDER BY pub.fecha DESC ";
				break;
			case "titledesc":
				query += " ORDER BY rec.titulo DESC ";
				break;
			case "titleasc":
				query += " ORDER BY rec.titulo ASC ";
				break;
			case "likesdesc":
				query += " ORDER BY pub.likes DESC ";
				break;
			case "likesasc":
				query += " ORDER BY pub.likes ASC ";
				break;
			case "timedesc":
				query += " ORDER BY pub.fecha DESC ";
				break;
			case "likeasc":
				query += " ORDER BY pub.fecha ASC ";
				break;

			default:
				// do nothing
				break;
			}
			
			// -----------------------------------------------------------------------------
			
			// add the page offset to the query
			if(pag > 1) {
				offset = (pag - 1) * 12;
			}
			
			query += " LIMIT 12 OFFSET "+offset;
			
			// run the query
			con.openConnection();
			result = consult.select(con.getConexion(), query, 10);
						
			//Load the beans
			//  usu.id, rec.id, usu.nombre, pub.fecha, cat.nombre,
			//  pub.titulo, pub.likes, pub.dislikes, rec.tiempo, rec.tags
			for (int i = 0; i < result.length; i++) {
				userList[i] = new BeanUsuario();
				recipeList[i] = new BeanReceta();
				catList[i] = new BeanCategoria();
				
				userList[i].setId((int) result[i][0]);
				recipeList[i].setId((int) result[i][1]);
				userList[i].setNombre((String)result[i][2]);
					aux = new GregorianCalendar();
					aux.setTime((java.sql.Date)result[i][3]);
				recipeList[i].setFecha(aux);
				catList[i].setNombre((String) result[i][4]);
				recipeList[i].setTitulo((String) result[i][5]);
				recipeList[i].setLikes((int) result[i][6]);
				recipeList[i].setDislikes((int) result[i][7]);
				recipeList[i].setTiempo((int) result[i][8]);
				recipeList[i].setTags((String) result[i][9]);
//				System.out.println("Bucle "+i);
			}
			
			request.setAttribute("userList", userList);
			request.setAttribute("catList", catList);			
			request.setAttribute("recipeList", recipeList);
						
			request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession sesion = request.getSession();
		
		if(request.getAttribute("searchType") != null) {
			sesion.setAttribute("searchType", request.getAttribute("searchType"));
		}
		
		if(request.getAttribute("searchValue") != null) {
			sesion.setAttribute("searchValue", request.getAttribute("searchValue"));
		}
		
		if(request.getAttribute("order") != null) {
			sesion.setAttribute("order", request.getAttribute("order"));
		}
		
		if(request.getAttribute("page") != null) {
			sesion.setAttribute("page", request.getAttribute("page"));
		}
		
		doGet(request, response);
	}

}
