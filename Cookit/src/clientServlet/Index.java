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
import dbConnection.SimpleQuery;
import toolkit.typeCkecker;

/**
 * Servlet implementation class inicio
 */
@WebServlet("/index")
public class Index extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/*
	 * TODO: Add a block list for users to block eachother. That's another table in the database
	 * TODO: Add the page buttons: next page, previous page and the page indicator
	 * TODO: Make the "Volver a inicio" button float on the side of the page ONLY when needed
	 * TODO: Fix the tempMsg not showing anywhere
	 * 
	 * TODO: Implement tempMsg, success and showMsg as session attributes
	 * TODO: Implement curPage as a session attribute
	 * 
	 * TODO: (IMPORTANT) Add an import to a jsp that contains a check for all session attributes. If null, initiate attributes
	 * */
	
	/* Session attributes
	 * myself : BeanUsuario		The logged user. If null -> anonimous user
	 * savedRecipe : BeanReceta	A recipe the user saved previously while creating one.
	 * 
	 * pag : int				The current page on index
	 * searchtype : String		Indicates what fiels is being searched in the index Ej: titulo, ingredientes, usuario, etc
	 * searchValue : String		Indicates the value searched in the field that searchtype indicates. Ej: cebolla, pastel, etc
	 * order : String			Indicates the order of the search in the index
	 * categories : Object[][]	The whole category table. 
	 * 
	 * curPage : String			The page the user is in
	 *  
	 * tempMsg : String			A message to show the user
	 * success : boolean		Determines if tempMessge is a succes message or a failure message
	 * showMsg : boolean		If true, then show tempMsg and turn back to false
	 * 
	 * */
	
	/*
	 * curPage values:
	 * 
	 * index - index page
	 * log - login, confirm email, reset password
	 * profile - someone's profile page
	 * myprofile - logged user's profile
	 * profileMod - modify profile
	 * recipe - view recipe
	 * recipeMod - edit recipe, create recipe
	 * 
	 * */
	
	/*-
	 
	 Stars system 5-(((dislikes*100)/likes)/10)
	 	The rating is ceil rounded
	 	Anything below 0 count as 0 stars
	 	Anything above 0 count as 1 star
	 	etc
	 	Anything abobe 4 count as 5 starsceiling
	 
	 SELECT usu.id, usu.nombre, pub.fecha, cat.nombre, rec.img, rec.procedimiento, rec.tiempo, rec.ingredientes, rec.tags
	FROM cookit.publicacion AS pub INNER JOIN cookit.receta as rec ON pub.id = rec.id_publicacion
	INNER JOIN cookit.usuario AS usu ON pub.id_usuario = usu.id
	INNER JOIN cookit.categoria AS cat ON rec.id_categoria = cat.id
	 */
	
	/*
	 * Prepare the page using the search values stored on the session
	 * Session attribute are created if unexistent
	 * 
	 * Search by: recipe title, username, stars
	 * Order by: date, title, likes, time
	 * 
	 * */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession sesion = request.getSession();
		//Conexion con = new Conexion("a21_jortnu", "a21_jortnu", "a21_jortnu");
		ConsultaAbierta consult = new ConsultaAbierta();
		SimpleQuery simpleQuery = new SimpleQuery("a21_jortnu", "a21_jortnu", "a21_jortnu");
		
		// The beans that will be used to transport the data
		BeanUsuario[] userList = new BeanUsuario[12];
		BeanReceta[] recipeList = new BeanReceta[12];
		//BeanPublicacion[] indexPost = new BeanPublicacion[12];
		BeanCategoria[] catList = new BeanCategoria[12];
		
		Object[][] auxCategory; // The list of categories
		BeanCategoria[] categories = null;
		
		// Var used for the search
		String searchtype = "title"; //Where's being searched
		String searchValue = ""; // What's beng searched
		
		String order = "datedesc"; // The order of the search
		
		String getPag = request.getParameter("pag");
		int pag = 1;
		int offset = 0;
		
		int elemCount = 12;
		
		// TODO: If estado == oculto // guardado // bloqueado -> not show in list
		// Select the necessary field to be used
		String query = "SELECT usu.id, rec.id, pub.id, usu.nombre, pub.fecha, cat.nombre, pub.titulo, rec.tiempo, rec.tags " + 
				"FROM cookit.publicacion AS pub INNER JOIN cookit.receta as rec ON pub.id = rec.id_publicacion " + 
				"INNER JOIN cookit.usuario AS usu ON pub.id_usuario = usu.id " + 
				"INNER JOIN cookit.categoria AS cat ON rec.id_categoria = cat.id WHERE pub.estado NOT LIKE 'guardado' AND pub.estado NOT LIKE 'bloqueado' ";
		
		String likesQuery;
		String dislikesQuery;
		
		Object[][] result = null; // Will store the result of the query
		Calendar aux;
		
		// -----------------------------------------------------------------------------
		
		sesion.setAttribute("curPage", "index");
		
		if(getPag != null && typeCkecker.isInt(getPag)) {
			pag = Integer.valueOf(getPag);
		}
		
		System.out.println("pag: "+pag);
		
		if(request.getSession().getAttribute("categories") == null) {
			
			auxCategory = simpleQuery.select("cookit.categoria", 
					new String[] {"id", "nombre", "descripcion"}, "", "", 0, 0);
			
			categories = new BeanCategoria[auxCategory.length];
			
			for (int i = 0; i < auxCategory.length; i++) {
				categories[i] = new BeanCategoria();
				
				categories[i].setId((int) auxCategory[i][0]);
				categories[i].setNombre((String) auxCategory[i][1]);
				categories[i].setDescripcion((String) auxCategory[i][2]);
			}
			
			request.getSession().setAttribute("categories", categories);
			
		}
		
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
					query += " AND rec.titulo LIKE '%"+searchValue+"%' ";
				}
				break;
				
			case "username":
				if(searchValue.length() < 3) {
					// not enough words for a search -> do nothing
				} else {
					query += " AND usu.nombre LIKE '%"+searchValue+"%' ";
				}
				break;
				
			case "star":
				query += " AND (SELECT (5-((dislikes * 100)/likes)/10) FROM cookit.publicacion WHERE id = pub.id) > "+ (Integer.valueOf(searchValue) - 1)+" ";
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
			result = consult.select(simpleQuery.getConnection(), query, 9);
			
			if(result.length < 12) {
				elemCount = result.length;
			}
						
			//Load the beans
			/*
			 * String query = "SELECT usu.id, pub.id, usu.nombre, pub.fecha, cat.nombre, pub.titulo, rec.tiempo, rec.tags " + 
				"FROM cookit.publicacion AS pub INNER JOIN cookit.receta as rec ON pub.id = rec.id_publicacion " + 
				"INNER JOIN cookit.usuario AS usu ON pub.id_usuario = usu.id " + 
				"INNER JOIN cookit.categoria AS cat ON rec.id_categoria = cat.id "
			 * */
			
			System.out.println("query en index: "+query);
			System.out.println("result.length: "+result.length);
			System.out.println("elemCount: "+elemCount);
			
			
			for (int i = 0; i < elemCount; i++) {
				
				int auxLikes = 0;
				int auxDislikes = 0;
				Long auxLong = 0L;
				
				auxLong = (long) simpleQuery.selectOne("cookit.likes", "count(*)", "tipo LIKE 'l' AND id_publicacion = "+(int) result[i][1], "", 0, 0);
				auxLikes = auxLong.intValue();
				auxLong = (long) simpleQuery.selectOne("cookit.likes", "count(*)", "tipo LIKE 'd' AND id_publicacion = "+(int) result[i][1], "", 0, 0);
				auxDislikes = auxLong.intValue();
				
				userList[i] = new BeanUsuario();
				recipeList[i] = new BeanReceta();
				catList[i] = new BeanCategoria();
				
				userList[i].setId((int) result[i][0]);
				recipeList[i].setId((int) result[i][1]);
				recipeList[i].setIdPublicacion((int) result[i][2] );
				userList[i].setNombre((String)result[i][3]);
					aux = new GregorianCalendar();
					aux.setTime((java.sql.Date)result[i][4]);
				recipeList[i].setFecha(aux);
				catList[i].setNombre((String) result[i][5]);
				recipeList[i].setTitulo((String) result[i][6]);
				recipeList[i].setTiempo((int) result[i][7]);
				recipeList[i].setTags((String) result[i][8]);
				
				recipeList[i].setLikes(auxLikes);
				recipeList[i].setDislikes(auxDislikes);
			}
			
			request.setAttribute("userList", userList);
			request.setAttribute("catList", catList);			
			request.setAttribute("recipeList", recipeList);
			
			request.getSession().removeAttribute("viewedPost");
						
			simpleQuery.closeConnection();
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
