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

import data.BeanComentario;
import data.BeanReceta;
import data.BeanUsuario;
import dbConnection.SimpleQuery;
import toolkit.PrepareSession;

@WebServlet("/recipe")
public class Recipe extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		PrepareSession.prepare(request, response);
		
		String target = "WEB-INF/recipe.jsp";

		String id = request.getParameter("id");
		SimpleQuery simpleQuery = new SimpleQuery("a21_jortnu", "a21_jortnu", "a21_jortnu");
		HttpSession session = request.getSession();

		BeanReceta recipe;
		BeanComentario[] comments;
		BeanUsuario[] users;
		BeanUsuario myself;
		BeanUsuario author;
		Object[][] results;
		Object result = null;
		Calendar auxCal;
		
		String aux;
		
		if (id == null) {

			target = "index";
		} else {
			// if id give -> check if ID exist

			result = simpleQuery.selectOne("cookit.publicacion", "titulo", "id = " + id, "", 0, 0);

			if (result == null) {

				target = "index";

			} else {
				// post with given ID exist -> load recipe and comments
				
				// if user logge -> check if the post is already liked
				if( session.getAttribute("myself") != null) {
					
					myself = (BeanUsuario) session.getAttribute("myself");
					
					System.out.println("id_usuario = "+myself.getId()+" AND id_publicacion = "+id);
					
					result = simpleQuery.selectOne("cookit.likes", "tipo", "id_usuario = "+myself.getId()+" AND id_publicacion = "+id, "", 1, 0);
					
					if(result != null) {
						
						System.out.println("Liked?: "+(String) result);
						
						aux = (String) result;
						
						if(aux.equals("L")) {
								request.setAttribute("liked", 'L');
								System.out.println("liked");
							}else {
								request.setAttribute("liked", 'D');
								System.out.println("disliked");
							}
						
					} else {System.out.println("neither");}
					
					result = null;
				}

				// TODO: Add a sorter for the comments (fecha - my comments first)
				// Comments on pages of 20 each

				users = new BeanUsuario[20];
				comments = new BeanComentario[20];

				results = simpleQuery.select(
						"cookit.comentario AS com INNER JOIN cookit.usuario AS usu ON com.id_usuario = usu.id",
						new String[] { "com.id", "usu.nombre", "usu.id", "com.fecha", "com.estado", "com.texto",
								"com.editado", "com.hora", "com.minuto" },
						"com.id_publicacion = " + id, "com.fecha desc, com.hora desc, com.minuto desc", 20, 0);

				System.out.println(simpleQuery.getLastQuery());

				for (int i = 0; i < results.length; i++) {

					users[i] = new BeanUsuario();
					comments[i] = new BeanComentario();

					comments[i].setId((int) results[i][0]);
					users[i].setNombre((String) results[i][1]);
					users[i].setId((int) results[i][2]);
					auxCal = new GregorianCalendar();
					auxCal.setTime((java.sql.Date) results[i][3]);
					comments[i].setFecha(auxCal);
					comments[i].setEstado((String) results[i][4]);
					comments[i].setTexto((String) results[i][5]);
					comments[i].setEditado((boolean) results[i][6]);
					comments[i].setHora((int) results[i][7]);
					comments[i].setMinuto((int) results[i][8]);

				}

				request.setAttribute("comments", comments);
				request.setAttribute("users", users);

					recipe = new BeanReceta();
					results = simpleQuery.select(
							"cookit.receta AS rec INNER JOIN cookit.publicacion AS pub ON pub.id = rec.id_publicacion",
							new String[] { "pub.id", "pub.titulo", "pub.subtitulo", "pub.fecha", "rec.procedimiento",
									"rec.tiempo", "rec.ingredientes", "rec.tags", "rec.id_categoria", "rec.id", "pub.estrellas", "pub.id_usuario" },
							"pub.id = " + id, "", 1, 0);

					recipe.setIdPublicacion((int) results[0][0]);
					recipe.setTitulo((String) results[0][1]);
					recipe.setSubtitulo((String) results[0][2]);
					auxCal = new GregorianCalendar();
					auxCal.setTime((java.sql.Date) results[0][3]);
					recipe.setFecha(auxCal);
					recipe.setProcedimiento((String) results[0][4]);
					recipe.setTiempo((int) results[0][5]);
					recipe.setIngredientes((String) results[0][6]);
					recipe.setTags((String) results[0][7]);
					recipe.setId_categoria((int) results[0][8]);
					recipe.setId((int) results[0][9]);
					recipe.setEstrellas((int) results[0][10]);

					request.setAttribute("viewedPost", recipe);
					
					author = new BeanUsuario();
					
					results = simpleQuery.select("cookit.usuario", new String[] {"id", "nombre"}, "id = "+(int) results[0][11], "", 1, 0);
					
					author.setId((int) results[0][0]);
					author.setNombre((String) results[0][1]);
					
					request.setAttribute("author", author);

					simpleQuery.closeConnection();

			}

			simpleQuery.closeConnection();

		}

		session.setAttribute("curPage", "recipe");
		
		request.getRequestDispatcher(target).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
