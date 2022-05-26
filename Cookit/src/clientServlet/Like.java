package clientServlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.BeanUsuario;
import dbConnection.SimpleQuery;
import toolkit.typeCkecker;

/**
 * Servlet implementation class Like
 */
@WebServlet("/like")
public class Like extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		BeanUsuario myself = null;
		int myId;

		String user = request.getParameter("user");
		String post = request.getParameter("post");
		String type = request.getParameter("type");
		
		String header = "recipe?id=" + post;

		Object result = null;
		SimpleQuery sq;
		
		System.out.println(user+" "+post+" "+type);

		if (session.getAttribute("myself") != null) {
			
			myself = (BeanUsuario) session.getAttribute("myself");
			myId = myself.getId();

			if (typeCkecker.isInt(user) && typeCkecker.isInt(post) && typeCkecker.isInt(type)) {

				if (myId == Integer.valueOf(user)) {

					sq = new SimpleQuery("a21_jortnu", "a21_jortnu", "a21_jortnu");

					result = sq.selectOne("cookit.likes", "tipo",
							"id_usuario = " + myId + " AND id_publicacion = " + post, "", 1, 0);

					if (result != null) {
						// User has liked/disliked this post -> update
						
						System.out.println("user enter update");

						switch (type) {
						case "1":

							sq.updateOne("cookit.likes", "tipo", "stirng", "L",
									"id_usuario = " + myId + " AND id_publicacion = " + post);
							System.out.println("Update and dislike "+sq.getLastQuery());

							break;
						case "0":

							sq.updateOne("cookit.likes", "tipo", "string", "D",
									"id_usuario = " + myId + " AND id_publicacion = " + post);
							System.out.println("Update and dislike "+sq.getLastQuery());

							break;

						default:
							// type is not 1 or 2 -> do nothing
							break;
						}

					} else {
						// User haven't liked or disliked this post yet -> insert
						
						System.out.println("user enter insert");

						switch (type) {
						case "1":

							sq.insert("cookit.likes", new String[] {"id_usuario", "id_publicacion", "tipo"},
									new String[] {"int", "int", "string"},
									new Object[] {Integer.valueOf(user), Integer.valueOf(post), "L"});
							
							System.out.println("Insert and dislike "+sq.getLastQuery());

							break;
						case "0":

							sq.insert("cookit.likes", new String[] {"id_usuario", "id_publicacion", "tipo"},
									new String[] {"int", "int", "string"},
									new Object[] {Integer.valueOf(user), Integer.valueOf(post), "D"});
							
							System.out.println("Insert and dislike "+sq.getLastQuery());

							break;

						default:
							// type is not 1 or 2 -> do nothing
							break;
						}

					}

				} // The user launching the like is not the logged user -> do nothing

			} // user ID or type of operation != number -> do nothing
		} else {
			header = "index";
		}

		request.getRequestDispatcher(header).forward(request, response);

	}
}
