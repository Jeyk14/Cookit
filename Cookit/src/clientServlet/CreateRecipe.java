package clientServlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.BeanReceta;
import data.BeanUsuario;
import dbConnection.SimpleQuery;

@WebServlet("/createRecipe")
public class CreateRecipe extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// TODO: IMPORTANT The post is inserted before the recipe, it may cause troubles

		String header = "WEB-INF/createRecipe.jsp";
		Object[][] auxResult;
		Object auxObj = null;
		BeanReceta savedRecipe;
		BeanUsuario myself;
		
		SimpleQuery simpleQuery;
				
		int lastId = 0; // The last recipe ID to autoincrement
		int idPost = 0; // The ID of a post withoout recipe

		if (request.getSession().getAttribute("myself") == null) {
			// user not logged -> index
			header = "index";
		} else {
			
			myself = (BeanUsuario) request.getSession().getAttribute("myself");

			if (request.getSession().getAttribute("savedRecipe") == null) {
				// No saved recipe loaded in an sess attr

				// Get the values of an existing saved recipe and load the data
				simpleQuery = new SimpleQuery("a21_jortnu", "a21_jortnu", "a21_jortnu");

				auxResult = simpleQuery.select(
						"cookit.receta as rec INNER JOIN cookit.publicacion as pub ON rec.id_publicacion = pub.id",
						new String[] { "rec.id", "pub.id", "pub.estado", "pub.titulo", "pub.subtitulo", "rec.id_categoria",
								"rec.tags", "rec.tiempo", "rec.ingredientes", "rec.procedimiento", "rec.img" },
						"estado LIKE 'guardado' AND pub.id_usuario = "+myself.getId(), "", 0, 0);

				if (auxResult.length > 0) {
					// A saved post+recipe was found -> load the bean
					savedRecipe = new BeanReceta();
					savedRecipe.setId((int) auxResult[0][0]);
					savedRecipe.setIdPublicacion((int) auxResult[0][1]);
					savedRecipe.setEstado((String) auxResult[0][2]);
					savedRecipe.setTitulo((String) auxResult[0][3]);
					savedRecipe.setSubtitulo((String) auxResult[0][4]);
					savedRecipe.setId_categoria((int) auxResult[0][5]);
					savedRecipe.setTags((String) auxResult[0][6]);
					savedRecipe.setTiempo((int) auxResult[0][7]);
					savedRecipe.setIngredientes((String) auxResult[0][8]);
					savedRecipe.setProcedimiento((String) auxResult[0][9]);
					savedRecipe.setImg((byte[])  auxResult[0][10] );

					request.getSession().setAttribute("savedRecipe", savedRecipe);

				} else {
					// no post+recipe found -> search for post without recipe (it may happen)
					
					auxObj = simpleQuery.selectOne("cookit.publicacion", "id", "estado LIKE 'guardado'", "", 0, 0);
					
					if(auxObj != null) { idPost = (int) auxObj; };
					
					System.out.println("Auxint: "+idPost);
					
					if(idPost != 0) {
						// A post exists without a recipe -> DISGUSTING -> create a recipe for said post
						
						lastId = (int) simpleQuery.selectOne("cookit.receta", "id", "", "id desc", 1, 0);
						
						simpleQuery.insert("cookit.receta",
								new String[] {"id", "id_publicacion ", "id_categoria", "procedimiento", "tiempo", "ingredientes", "tags"}, 
								new String[] {"int", "int", "int", "string", "int", "string", "string"},
								new Object[] {lastId +1, idPost, 1, "", 10, "", ""});
						
					} // else -> no post, no recipe -> proceed without loading anything
					
				}
				
				simpleQuery.closeConnection();
				
			}
		}

		request.getSession().setAttribute("curPage", "recipeMod");

		request.getRequestDispatcher(header).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
