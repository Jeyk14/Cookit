package recipeConfig;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.BeanUsuario;
import dbConnection.SimpleQuery;
import toolkit.PrepareSession;
import toolkit.typeCkecker;

/**
 * Servlet implementation class uploadComment
 */
@WebServlet("/uploadComment")
public class UploadComment extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrepareSession.prepare(request, response);
		String header = "index";
		String id_post = "";

		if (request.getSession().getAttribute("myself") == null) {
			// user is not logged -> GTFO dude
			header = "index";
		} else {

			id_post = request.getParameter("post");

			if (id_post == null) {
				
				// no post ID provided -> index
				
				header = "index";

			} else {

				header = "recipe?id=" + id_post;

				if (request.getAttribute("retry") != null) {
					
					if ((boolean) request.getAttribute("retry") == true) {
						
						header = "recipe?id=" + id_post + "#comment";
						
					}
				}
			}
		}

		request.getRequestDispatcher(header).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO: Add a commnet to the post ID received from param. Use myself data to
		// upload comment

		String id_post = request.getParameter("post");
		String comment = request.getParameter("comment");

		BeanUsuario myself = (BeanUsuario) request.getSession().getAttribute("myself");
		Calendar hoy = Calendar.getInstance();

		SimpleQuery sq = new SimpleQuery("a21_jortnu", "a21_jortnu", "a21_jortnu");
		Object result = null;
		int oldId = 0;
		int inserted = 0;

		boolean retry = false;

		if (myself == null) {

			request.setAttribute("tempMsg", "Debe iniciar sesi&oacute;n para escribir un comentario");
			request.setAttribute("success", "false");
			request.setAttribute("showMsg", true);

		} else {

			if (comment == null || id_post == null) {

				request.setAttribute("tempMsg", "Hubo un problema procesando su comentario");
				request.setAttribute("success", "false");
				request.setAttribute("showMsg", true);

			} else {

				if (comment.length() > 500) {

					request.setAttribute("tempMsg", "Su comentario supera el l&iacute;mite de 500 caracteres");
					request.setAttribute("success", "false");
					request.setAttribute("showMsg", true);
					retry = true;

				} else if (comment.length() < 1) {

					request.setAttribute("tempMsg", "Su comentario es demasiado corto");
					request.setAttribute("success", "false");
					request.setAttribute("showMsg", true);
					retry = true;

				} else if (!typeCkecker.isInt(id_post)) {

					request.setAttribute("tempMsg", "No se ha encontrado la publicaci&oacute;n");
					request.setAttribute("success", "false");
					request.setAttribute("showMsg", true);

				} else {
					// el comentario existe y tiene una longitud correcta

					result = sq.selectOne("cookit.comentario", "id", "", "id desc", 1, 0);

					if (result == null) {

						request.setAttribute("tempMsg",
								"No se ha podido publicar su comentario, int&eacute;ntelo otra vez");
						request.setAttribute("success", "false");
						request.setAttribute("showMsg", true);
						retry = true;

					} else {

						oldId = (int) result;

						inserted = sq.insert("cookit.comentario",
								new String[] { "id", "id_publicacion", "id_usuario", "fecha", "texto", "estado",
										"editado", "hora", "minuto" },
								new String[] { "int", "int", "int", "calendar", "string", "string", "boolean", "int", "int" },
								new Object[] { oldId+1, Integer.parseInt(id_post), myself.getId(), hoy, comment,
										"publicado", false, hoy.get(Calendar.HOUR_OF_DAY), hoy.get(Calendar.MINUTE) });

						if (inserted < 1) {

							request.setAttribute("tempMsg",
									"No se ha podido publicar su comentario, int&eacute;ntelo otra vez");
							request.setAttribute("success", "false");
							request.setAttribute("showMsg", true);
							retry = true;

						} else {
							request.setAttribute("tempMsg", "Comentario a&ntilde;adido con &eacute;xito");
							request.setAttribute("success", "true");
							request.setAttribute("showMsg", true);
						}

					}

				}

			}
		}

		request.setAttribute("retry", retry);

		doGet(request, response);
	}

}
