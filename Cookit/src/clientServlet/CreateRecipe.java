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

		String header = "WEB-INF/createRecipe.jsp";
		Object[][] auxObj;
		BeanReceta savedRecipe;
		BeanUsuario myself;
		
		SimpleQuery simpleQuery;
		
		//TODO: If there is an ID URL parameter -> modify recipe if it belongs to the logged user 
		
		int lastId; // The last recipe ID to autoincrement
		int idPost; // The ID of a post withoout recipe

		if (request.getSession().getAttribute("myself") == null) {
			// user not logged -> index
			header = "index";
		} else {
			
			myself = (BeanUsuario) request.getSession().getAttribute("myself");

			if (request.getSession().getAttribute("savedRecipe") == null) {
				// No saved recipe loaded in an sess attr

				// Get the values of an existing saved recipe and load the data
				simpleQuery = new SimpleQuery("a21_jortnu", "a21_jortnu", "a21_jortnu");

				auxObj = simpleQuery.select(
						"cookit.receta as rec INNER JOIN cookit.publicacion as pub ON rec.id_publicacion = pub.id",
						new String[] { "rec.id", "pub.id", "pub.estado", "pub.titulo", "pub.subtitulo", "rec.id_categoria",
								"rec.tags", "rec.tiempo", "rec.ingredientes", "rec.procedimiento" },
						"estado LIKE 'guardado' AND pub.id_usuario = "+myself.getId(), "", 0, 0);

				if (auxObj.length > 0) {
					// A saved post+recipe was found -> load the bean
					savedRecipe = new BeanReceta();
					savedRecipe.setId((int) auxObj[0][0]);
					savedRecipe.setIdPublicacion((int) auxObj[0][1]);
					savedRecipe.setEstado((String) auxObj[0][2]);
					savedRecipe.setTitulo((String) auxObj[0][3]);
					savedRecipe.setSubtitulo((String) auxObj[0][4]);
					savedRecipe.setId_categoria((int) auxObj[0][5]);
					savedRecipe.setTags((String) auxObj[0][6]);
					savedRecipe.setTiempo((int) auxObj[0][7]);
					savedRecipe.setIngredientes((String) auxObj[0][8]);
					savedRecipe.setProcedimiento((String) auxObj[0][9]);

					request.getSession().setAttribute("savedRecipe", savedRecipe);

				} else {
					// no post+recipe found -> search for post without recipe (it may happen)
					
					idPost = (int) simpleQuery.selectOne("cookit.publicacion", "id", "estado LIKE 'guardado'", "", 0, 0);
					System.out.println("Auxint: "+idPost);
					
					if(idPost != 0) {
						// A post exists without a recipe -> DISGUSTING -> create a recipe for said post
						lastId = (int) simpleQuery.selectOne("cookit.receta", "id", "", "id desc", 1, 0);
						simpleQuery.insert("cookit.receta",
								new String[] {"id", "id_publicacion ", "id_categoria", "procedimiento", "tiempo", "ingredientes", "tags"}, 
								new String[] {"int", "int", "int", "string", "int", "string", "string"},
								new Object[] {lastId +1, idPost, 1, "", 10, "", ""});
					}
					// if no post nor recipe -> proceed withoud loading anything
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
