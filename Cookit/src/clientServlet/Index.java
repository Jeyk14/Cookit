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
import toolkit.PrepareSession;
import toolkit.typeCkecker;

@WebServlet("/index")
public class Index extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*-
	 * TODO: Add a block list for users to block eachother. That's another table in the database 	 * 
	 * TODO: Count the max number of pages to show "Página X de Y" on the page controls
	 * 
	 */

	/*
	 * Prepare the page using the search values stored on the session Session
	 * attribute are created if unexistent
	 * 
	 * Search by: recipe title, username, stars Order by: date, title, likes, time
	 * 
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Prepare the session attributes and check for cookies to log in
		PrepareSession.prepare(request, response);

		HttpSession sesion = request.getSession();
		ConsultaAbierta consult = new ConsultaAbierta();
		SimpleQuery simpleQuery = new SimpleQuery("a21_jortnu", "a21_jortnu", "a21_jortnu");

		// The beans that will be used to transport the data
		BeanUsuario[] userList = new BeanUsuario[12];
		BeanReceta[] recipeList = new BeanReceta[12];
		BeanCategoria[] catList = new BeanCategoria[12];

		// Var used for the search
		String searchtype = "title"; // Where's being searched
		String searchValue = ""; // What's beng searched

		String order = "datedesc"; // The order of the search

		String getPag = request.getParameter("pag");
		int pag = 1;
		int offset = 0;

		int elemCount = 12;

		// Select the necessary field to be used
		String query = "SELECT usu.id, rec.id, pub.id, usu.nombre, pub.fecha, cat.nombre, pub.titulo, rec.tiempo, rec.tags, pub.estrellas "
				+ "FROM cookit.publicacion AS pub INNER JOIN cookit.receta as rec ON pub.id = rec.id_publicacion "
				+ "INNER JOIN cookit.usuario AS usu ON pub.id_usuario = usu.id "
				+ "INNER JOIN cookit.categoria AS cat ON rec.id_categoria = cat.id WHERE pub.estado NOT LIKE 'guardado' AND pub.estado NOT LIKE 'bloqueado' ";

		Object[][] result = null; // Will store the result of the query
		Calendar aux;

		// -----------------------------------------------------------------------------

		sesion.setAttribute("curPage", "index");

		// Get the page of the index
		if (getPag != null && typeCkecker.isInt(getPag)) {
			pag = Integer.valueOf(getPag);
		}

		// The search values aren't in the session -> add them to the session
		if (sesion.getAttribute("searchtype") != null) {
			searchtype = (String) sesion.getAttribute("searchtype");
		} else {
			sesion.setAttribute("searchtype", searchtype);
		}

		if (sesion.getAttribute("searchValue") != null) {
			searchValue = (String) sesion.getAttribute("searchValue");
		} else {
			sesion.setAttribute("searchValue", searchValue);
		}

		if (sesion.getAttribute("order") != null) {
			searchValue = (String) sesion.getAttribute("order");
		} else {
			sesion.setAttribute("searchValue", searchValue);
		}

		// -----------------------------------------------------------------------------

		// Add the search type and value to the query
		switch (searchtype) {

		case "title":
			if (searchValue.length() < 3) {
				// not enough words for a search -> do nothing
			} else {
				query += " AND rec.titulo LIKE '%" + searchValue + "%' ";
			}
			break;

		case "username":
			if (searchValue.length() < 3) {
				// not enough words for a search -> do nothing
			} else {
				query += " AND usu.nombre LIKE '%" + searchValue + "%' ";
			}
			break;

		case "star":
			query += " AND pub.estrellas > " + (Integer.valueOf(searchValue) - 1) + " ";
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
		if (pag > 1) {
			offset = (pag - 1) * 12;
		}

		query += " LIMIT 12 OFFSET " + offset;

		// run the query to get the recipes
		result = consult.select(simpleQuery.getConnection(), query, 10);
		
		System.out.println(query);
		System.out.println("index length "+result.length);

		if (result.length < 12) {
			elemCount = result.length;
		}
		
		System.out.println("elem count "+elemCount);

		// Put the recipes in the bean
		for (int i = 0; i < elemCount; i++) {

			userList[i] = new BeanUsuario();
			recipeList[i] = new BeanReceta();
			catList[i] = new BeanCategoria();

			userList[i].setId((int) result[i][0]);
			recipeList[i].setId((int) result[i][1]);
			recipeList[i].setIdPublicacion((int) result[i][2]);
			userList[i].setNombre((String) result[i][3]);
			aux = new GregorianCalendar();
			aux.setTime((java.sql.Date) result[i][4]);
			recipeList[i].setFecha(aux);
			catList[i].setNombre((String) result[i][5]);
			recipeList[i].setTitulo((String) result[i][6]);
			recipeList[i].setTiempo((int) result[i][7]);
			recipeList[i].setTags((String) result[i][8]);

			recipeList[i].setEstrellas((int) result[i][9]);
		}

		// Search for a special recipe
		// TODO: Every week add a recipe with the most likes in the special table

		request.setAttribute("userList", userList);
		request.setAttribute("catList", catList);
		request.setAttribute("recipeList", recipeList);

		request.getSession().removeAttribute("viewedPost");

		simpleQuery.closeConnection();
		
		request.getRequestDispatcher("WEB-INF/index.jsp").forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession sesion = request.getSession();

		if (request.getAttribute("searchType") != null) {
			sesion.setAttribute("searchType", request.getAttribute("searchType"));
		}

		if (request.getAttribute("searchValue") != null) {
			sesion.setAttribute("searchValue", request.getAttribute("searchValue"));
		}

		if (request.getAttribute("order") != null) {
			sesion.setAttribute("order", request.getAttribute("order"));
		}

		if (request.getAttribute("page") != null) {
			sesion.setAttribute("page", request.getAttribute("page"));
		}

		doGet(request, response);
	}

}
