package recipeConfig;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.BeanReceta;
import dbConnection.Connect;
import dbConnection.SimpleQuery;

@WebServlet("/uploadRecipe")
public class uploadRecipe extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.getSession().setAttribute("curPage", "recipeMod");
		request.getRequestDispatcher("createRecipe").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO: if save -> add the recipe to the database with the hidden status
		// TODO: if a recipe with hidden status already exists -> load that recipe
		// TODO: if upload now -> check all data and insert into database -> profile

		Connect con;
		SimpleQuery simpleQuery;

		String title = request.getParameter("title");
		String subtitle = request.getParameter("subtitle");
		String tags = request.getParameter("tags");
		String time = request.getParameter("time");
		String ingredients = request.getParameter("ingredients");
		String procedure = request.getParameter("procedure");
		String category = request.getParameter("category"); // TODO: Add the category selector

		// Either to save the recipe or to upload it
		String submitRecipe = request.getParameter("submitRecipe");
		Object resultado[][] = null;
		BeanReceta savedRecipe; // if save -> fulfill this bean
		Calendar auxCal;

		if (submitRecipe == "Publicar") {

			// TODO: IMPORTANT - Test upload the recipe
			
			

		} else if (submitRecipe == "Guardar") {

			// TODO: IMPORTANT - Test save recipe

			// TODO: Create a post with status hidden and add the data to that post
			con = new Connect("a21_jortnu", "a21_jortnu", "a21_jortnu");
			con.openConnection();
			simpleQuery = new SimpleQuery(con.getConexion());

			// resultado = simpleQuery.selectOne("cookit.recipe", "id", "estado =
			// 'guardado'");
			resultado = simpleQuery.select("cookit.publicacion", new String[] { "id", "fecha" }, "estado = 'guardado'",
					"", 0, 0);

			if (resultado != null) {

				// a saved recipe exists -> load the new recipe onto the saved one
				simpleQuery.update("cookit.publicacion", new String[] { "titulo", "subtitulo" },
						new String[] { "string", "string" }, new Object[] { title, subtitle },
						"id = " + (int) resultado[0][0]);

				simpleQuery.update("cookit.receta", new String[] { "procedimiento", "tiempo", "ingredientes", "tags" },
						new String[] { "string", "int", "string", "string" },
						new Object[] { procedure, time, ingredients, tags },
						"id_publicacion = " + (int) resultado[0][0]);

				con.closeConnection();

				auxCal = new GregorianCalendar();
				auxCal.setTime((java.sql.Date) resultado[0][2]);

			} else {

				// no saved recipes exists -> insert the new recipe
				simpleQuery.insert("cookit.publicacion",
						new String[] { "titulo", "subtitulo", "destacado", "fecha", "estado" },
						new String[] { "string", "string", "boolean", "date", "string" },
						new Object[] { title, subtitle, false, Calendar.getInstance(), "guardado" });

				// get the ID of the freshly inserted post
				int tempId = (int) simpleQuery.selectOne("cookit.publicacion", "id", "titulo = '" + title + "'");

				simpleQuery.insert("cookit.receta",
						new String[] { "id_publicacion", "id_categoria", "procedimiento", "tiempo", "ingredientes",
								"tags" },
						new String[] { "int", "int", "string", "int", "string", "string" },
						new Object[] { tempId, category, procedure, time, ingredients, tags });

				auxCal = Calendar.getInstance();

				con.closeConnection();
			}

			savedRecipe = new BeanReceta();

			savedRecipe.setTitulo(title);
			savedRecipe.setSubtitulo(subtitle);
			savedRecipe.setFecha(auxCal);
			savedRecipe.setId_categoria(Integer.valueOf(category));
			savedRecipe.setProcedimiento(procedure);
			savedRecipe.setTiempo(Integer.valueOf(time));
			savedRecipe.setIngredientes(ingredients);
			savedRecipe.setTags(tags);

			// TODO: CHeck if saving the recipe in an attribute is neccesary to not lose it
			// After saving the recipe, load it again in the form
			request.setAttribute("savedRecipe", savedRecipe);

		} else {
			// no save / no upload -> do nothing
		}

		doGet(request, response);
	}

}
