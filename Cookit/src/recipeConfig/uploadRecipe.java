package recipeConfig;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.BeanReceta;
import data.BeanUsuario;
import dbConnection.Connect;
import dbConnection.SimpleQuery;
import toolkit.typeCkecker;

@MultipartConfig
@WebServlet("/uploadRecipe")
public class uploadRecipe extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String header = "createRecipe";

		request.getSession().setAttribute("curPage", "recipeMod");

		if (request.getSession().getAttribute("myself") == null) {
			header = "index";
		}

		request.getRequestDispatcher(header).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// if save -> add the recipe to the database with the hidden status
		// TODO: if upload now -> check all data and insert into database -> profile
		// TODO: upload image as well

		SimpleQuery simpleQuery;
		
		BeanReceta savedRecipe = null;

		String title = request.getParameter("title");
		String subtitle = request.getParameter("subtitle");
		String tags = request.getParameter("tags");
		String time = request.getParameter("time");
		int timeInt = 1;
		String ingredients = request.getParameter("ingredients");
		String procedure = request.getParameter("procedure");
		String category = request.getParameter("category");
		int categoryInt = 0;

		// Used to check if the save query was succesful or not
		int insertedPost = 0;
		int updatedPost = 0;
		int lastId = 0; // The ID of the last post
		
		boolean success = true;
		int problemsFound = 0;
		String tempMsg = "";

		if (typeCkecker.isInt(time)) {
			timeInt = Integer.valueOf(time);
		}

		if (typeCkecker.isInt(category)) {
			categoryInt = Integer.valueOf(category);
		}
		
		if (request.getSession().getAttribute("savedRecipe") != null) {
			savedRecipe = (BeanReceta) request.getSession().getAttribute("savedRecipe");
		}

		// Either to save the recipe or to upload it
		String submitRecipe = request.getParameter("submitRecipe");
		Object resultado[][] = null;
		Calendar auxCal;

		BeanUsuario myself = (BeanUsuario) request.getSession().getAttribute("myself");

		if (submitRecipe == "Publicar") {

			// TODO: IMPORTANT - Test upload the recipe
			
			if(title.length() > 30 || title.length() < 2) {
				success = false;
				tempMsg = "El título debe tener entre 2 y 30 caracteres";
			}
			
			if(subtitle.length() > 80) {
				success = false;
				tempMsg = "El subtítulo no debe tener más de 30 caracteres";
			}
			
			if(tags.length() > 200) {
				success = false;
				tempMsg = "Las palabras clave superan el límite de caracteres";
			}
			
			if(!typeCkecker.isInt(time)) {
				success = false;
				tempMsg = "El tiempo de preparación debe ser un número";
			}
			
			if(ingredients.length() > 400 || ingredients.length() < 0) {
				success = false;
				tempMsg = "Los ingredientes superan el límite de 400 caracteres";
			}
			
			if(procedure.length() > 500) {
				success = false;
				tempMsg = "El procedimiento supera el límite de 400 caracteres";
			}
			
			if(!typeCkecker.isInt(category)) {
				success = false;
				tempMsg = "Hay un problema con la categoría elegida, vuelva a intentarlo";
			} else if (Integer.valueOf(category) < 1 || Integer.valueOf(category) > 6 ) {
				success = false;
				tempMsg = "La categoría no es correcta";
			}
			
			//TODO: if all data correct -> decide either to upload or to update
			if(success) {
				
				if(savedRecipe != null) {
					// No saved recipe -> insert
					
					
				} else {
					// saved recipe -> update
					
				}
				
			}

		} else if (submitRecipe.equals("Guardar")) {
			// Create a post with status hidden and add the data to that post

			simpleQuery = new SimpleQuery("a21_jortnu", "a21_jortnu", "a21_jortnu");

			resultado = simpleQuery.select("cookit.publicacion", new String[] { "id", "fecha" }, "estado = 'guardado'",
					"", 0, 0);

			if (resultado.length > 0) {
				// an already saved post exists -> load the new recipe onto the saved one

				// updtate post
				updatedPost = simpleQuery.update("cookit.publicacion", new String[] { "titulo", "subtitulo" },
						new String[] { "string", "string" }, new Object[] { title, subtitle },
						"id = " + (int) resultado[0][0]);
				
				System.out.println(simpleQuery.getLastQuery());

				Object aux = simpleQuery.selectOne(
						"cookit.receta as rec INNER JOIN cookit.publicacion as pub on rec.id_publicacion = pub.id ",
						"rec.id = " + (int) resultado[0][0], "", "", 0, 0);

				if (aux != null) {
					// a post and it's recipe exists -> update recipe
					
					simpleQuery.update("cookit.receta",
							new String[] { "procedimiento", "tiempo", "ingredientes", "tags" },
							new String[] { "string", "int", "string", "string" },
							new Object[] { procedure, timeInt, ingredients, tags },
							"id_publicacion = " + (int) resultado[0][0]);
					
					System.out.println("A "+ simpleQuery.getLastQuery());
				} else {
					// a saved post exists without it's recipe -> insert the recipe
					simpleQuery.insert("cookit.receta",
							new String[] { "id_publicacion", "id_categoria", "procedimiento", "tiempo", "ingredientes",
									"tags" },
							new String[] { "int", "int", "string", "int", "string", "string" },
							new Object[] { lastId, categoryInt, procedure, timeInt, ingredients, tags });
					
					System.out.println("B "+simpleQuery.getLastQuery());
				}

				simpleQuery.closeConnection();

				auxCal = new GregorianCalendar();
				auxCal.setTime((java.sql.Date) resultado[0][1]);

			} else {
				// no saved recipes exists -> insert the new recipe

				auxCal = Calendar.getInstance(); // Creation date - today

				// Get the last ID
				lastId = (int) simpleQuery.selectOne("cookit.publicacion", "id", "", "id desc", 0, 0);
				lastId++;

				insertedPost = simpleQuery.insert("cookit.publicacion",
						new String[] { "id", "id_usuario", "titulo", "subtitulo", "destacado", "fecha", "estado" },
						new String[] { "int", "int", "string", "string", "boolean", "calendar", "string" },
						new Object[] { lastId, myself.getId(), title, subtitle, false, auxCal, "guardado" });

				System.out.println(simpleQuery.getLastQuery());

				if (insertedPost > 0) {
					// If the post was inserted successfully

					// Got the ID of the last inserted post -> insert the recipe as well

					simpleQuery.insert("cookit.receta",
							new String[] { "id_publicacion", "id_categoria", "procedimiento", "tiempo", "ingredientes",
									"tags" },
							new String[] { "int", "int", "string", "int", "string", "string" },
							new Object[] { lastId, categoryInt, procedure, timeInt, ingredients, tags });

					simpleQuery.closeConnection();

				}

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

			// TODO: Check if saving the recipe in an attribute is necessary to not lose it
			// After saving the recipe, load it again in the form
			request.setAttribute("savedRecipe", savedRecipe);

		} else {
			// no save / no upload -> do nothing
		}

		doGet(request, response);
	}

}
