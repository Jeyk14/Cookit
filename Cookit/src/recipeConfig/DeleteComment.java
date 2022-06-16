package recipeConfig;

import java.io.IOException;
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
 * Servlet implementation class DeleteComment
 */
@WebServlet("/deleteComment")
public class DeleteComment extends HttpServlet {
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

				header = "recipe?id=" + id_post + "#comment";

			}
		}

		request.getRequestDispatcher(header).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String id_del = request.getParameter("id-delete");
		BeanUsuario myself = (BeanUsuario) request.getAttribute("myself");
		SimpleQuery sq;
		Object result;
		int deleted = 0;

		// Only delete if the owner of the comment is the logged user
		if (myself == null) {

			request.setAttribute("tempMsg", "Debe iniciar sesi&oacute;n para borrar un comentario");
			request.setAttribute("success", "false");
			request.setAttribute("showMsg", true);

		} else if (id_del == null) {

			request.setAttribute("tempMsg", "No se ha podido procesar su solicitud, vuelva a intentarlo");
			request.setAttribute("success", "false");
			request.setAttribute("showMsg", true);

		} else if (!typeCkecker.isInt(id_del)) {

			request.setAttribute("tempMsg", "No se ha podido procesar su solicitud, vuelva a intentarlo");
			request.setAttribute("success", "false");
			request.setAttribute("showMsg", true);

		} else {

			sq = new SimpleQuery("a21_jortnu", "a21_jortnu", "a21_jortnu");
			result = sq.selectOne("cookit.comentario", "id_usuario", "id = " + id_del, "", 1, 0);

			if (result == null) {

				request.setAttribute("tempMsg", "No se ha podido encontrado el comentario");
				request.setAttribute("success", "false");
				request.setAttribute("showMsg", true);

			} else {

				if ((int) result != myself.getId()) {
					
					request.setAttribute("tempMsg", "No puede borrar este comentario");
					request.setAttribute("success", "false");
					request.setAttribute("showMsg", true);
					
				} else {

					deleted = sq.delete("cookit.comentario", "id = " + (int) result);

					if (deleted < 1) {

						request.setAttribute("tempMsg", "No se ha podio borrar el comentario");
						request.setAttribute("success", "false");
						request.setAttribute("showMsg", true);

					} else {

						request.setAttribute("tempMsg", "Comentario borrado con &eacute;xito");
						request.setAttribute("success", "true");
						request.setAttribute("showMsg", true);

					}

				}

			}

		}

		doGet(request, response);
	}

}
