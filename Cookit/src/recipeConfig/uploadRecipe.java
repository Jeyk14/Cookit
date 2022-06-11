package recipeConfig;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import data.BeanReceta;
import data.BeanUsuario;
import dbConnection.SimpleQuery;
import toolkit.Convert;
import toolkit.typeCkecker;

@MultipartConfig
@WebServlet("/uploadRecipe")
public class uploadRecipe extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String header = "createRecipe";

		request.getSession().setAttribute("curPage", "recipeMod");

		if (request.getSession().getAttribute("myself") == null ) {
			header = "index";
		} else if(request.getSession().getAttribute("allDone") != null) {
			// TODO: If post uploaded succesfully -> send user to post page
			header = "index";
			request.getSession().removeAttribute("allDone");
		}

		request.getRequestDispatcher(header).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// if save -> add the recipe to the database with the hidden status
		// TODO: if upload now -> check all data and insert into database -> profile

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

		InputStream image = null;
		String formType = "";
		Part filePart;
		String fileName;
		int maxSize = 5000 * 1024; // max file size
		int fileSize = 0;
		String mimeType;

		// Either to save the recipe or to upload it
		String submitRecipe = request.getParameter("submitRecipe");

		// Used to check if the save query was succesful or not
		int inserted = 0;
		int lastId = 0; // The ID of the last post
		int lastIdAux;

		boolean success = true;
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

		Object resultado[][] = null;
		Calendar auxCal;

		BeanUsuario myself = (BeanUsuario) request.getSession().getAttribute("myself");

		// -------------------------------------------------------------------------------------------------
		
		// the image
		formType = request.getContentType();

		if ((formType.indexOf("multipart/form-data") >= 0)) {

			filePart = request.getPart("img");
			fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
			image = filePart.getInputStream();
			fileSize = image.available();
			mimeType = getServletContext().getMimeType(fileName);

			if (mimeType != null) {

				if (mimeType.startsWith("image/")) {
					// The uploaded file is an image

					if (fileSize > maxSize) {
						// The size of the IMG is under the max permitted size
						tempMsg = "El archivo es demasiado grande (tama&ntilde;o m&aacute;ximo: " + (maxSize / 1000) + "kB)";
						success = false;
						image = null;
					}

				} else {
					// The file is not an image
					tempMsg = "El fichero no es una imagen";
					success = false;
					image = null;
				}
			}
		}

		// -------------------------------------------------------------------------------------------------

		if (submitRecipe.equals("Publicar")) {

			if (title.length() > 30 || title.length() < 2) {
				success = false;
				tempMsg = "El t&iacute;tulo debe tener entre 2 y 30 caracteres";
			}

			if (subtitle.length() > 80) {
				success = false;
				tempMsg = "El subt&iacute;tulo no debe tener m&aacute;s de 30 caracteres";
			}

			if (tags.length() > 200) {
				success = false;
				tempMsg = "Las palabras clave superan el l&iacute;mite de caracteres";
			}

			if (!typeCkecker.isInt(time)) {
				success = false;
				tempMsg = "El tiempo de preparaci&iacute;n debe ser un n&uacute;mero";
			}

			if (ingredients.length() > 400 || ingredients.length() < 0) {
				success = false;
				tempMsg = "Los ingredientes superan el l&iacute;mite de 400 caracteres";
			}

			if (procedure.length() > 500) {
				success = false;
				tempMsg = "El procedimiento supera el l&iacute;mite de 400 caracteres";
			}

			if (!typeCkecker.isInt(category)) {
				success = false;
				tempMsg = "Hay un problema con la categor&iacute;a elegida, vuelva a intentarlo";
			} else if (Integer.valueOf(category) < 1 || Integer.valueOf(category) > 6) {
				success = false;
				tempMsg = "La categor&iacute;a no es correcta";
			}

			// if all data correct -> decide either to upload or to update
			if (success) {

				if (savedRecipe == null) {
					// No saved recipe -> insert
					
					simpleQuery = new SimpleQuery("a21_jortnu", "a21_jortnu", "a21_jortnu");

					lastId = (int) simpleQuery.selectOne("cookit.publicacion", "id", "", "id desc", 1, 0);
					lastIdAux = lastId + 1;
					
					auxCal = Calendar.getInstance();

					inserted = simpleQuery.insert("cookit.publicacion",
							new String[] { "id", "id_usuario", "titulo", "subtitulo", "destacado", "fecha", "estado" },
							new String[] { "int", "int", "string", "string", "boolean", "calendar", "string" },
							new Object[] { lastIdAux, myself.getId(), title, subtitle, false, auxCal, "publicado" });
					
					if (inserted > 0) {
						// the post was inserted -> insert the recipe

						lastId = (int) simpleQuery.selectOne("cookit.receta", "id", "", "id desc", 0, 0);
						lastId++;
						
						if(fileSize > 0) {
							simpleQuery.insert("cookit.receta",
									new String[] { "id", "id_publicacion", "id_categoria", "img", "procedimiento", "tiempo", "ingredientes", "tags" },
									new String[] { "int", "int", "int", "stream", "string", "int", "string", "string" },
									new Object[] { lastIdAux, lastIdAux, categoryInt, image, procedure, timeInt, ingredients,
											tags });
						} else {
							simpleQuery.insert("cookit.receta",
									new String[] { "id", "id_publicacion", "id_categoria", "procedimiento", "tiempo", "ingredientes", "tags" },
									new String[] { "int", "int", "int", "string", "int", "string", "string" },
									new Object[] { lastIdAux, lastIdAux, categoryInt, procedure, timeInt, ingredients,
											tags });
							}

						
						request.getSession().removeAttribute("savedRecipe");
						
						request.getSession().setAttribute("allDone", 1);

					}

					simpleQuery.closeConnection();

				} else {
					// saved recipe exists -> update

					simpleQuery = new SimpleQuery("a21_jortnu", "a21_jortnu", "a21_jortnu");

					simpleQuery.update("cookit.publicacion", new String[] { "titulo", "subtitulo", "estado" },
							new String[] { "string", "string", "string" },
							new Object[] { title, subtitle, "publicado" }, "id = " + savedRecipe.getIdPublicacion());

					if(fileSize > 0) {
						
						simpleQuery.update("cookit.receta",
								new String[] { "id_categoria", "img", "procedimiento", "tiempo", "ingredientes", "tags" },
								new String[] { "int", "stream", "string", "int", "string", "string" },
								new Object[] { categoryInt, image, procedure, timeInt, ingredients, tags },
								"id_publicacion = " + savedRecipe.getIdPublicacion());
						
					}else {
						
						simpleQuery.update("cookit.receta",
								new String[] { "id_categoria", "procedimiento", "tiempo", "ingredientes", "tags" },
								new String[] { "int", "string", "int", "string", "string" },
								new Object[] { categoryInt, procedure, timeInt, ingredients, tags },
								"id_publicacion = " + savedRecipe.getIdPublicacion());

					}


					simpleQuery.closeConnection();
					
					request.getSession().removeAttribute("savedRecipe");
					request.getSession().setAttribute("allDone", 1);

				}

			} else {
				// Something went wrong -> do nothing + show message to the user

				request.setAttribute("tempMsg", tempMsg);
				request.setAttribute("success", success);

			}

		} else if (submitRecipe.equals("Guardar")) {
			// Create a post with status hidden and add the data to that post
			
			simpleQuery = new SimpleQuery("a21_jortnu", "a21_jortnu", "a21_jortnu");

			resultado = simpleQuery.select("cookit.publicacion", new String[] { "id", "fecha" }, "estado = 'guardado'",
					"", 0, 0);

			if (resultado.length > 0) {
				// an already saved post exists -> load the new recipe onto the saved one
				
				// update post
				simpleQuery.update("cookit.publicacion", new String[] { "titulo", "subtitulo" },
						new String[] { "string", "string" }, new Object[] { title, subtitle },
						"id = " + (int) resultado[0][0]);
				
				if(fileSize > 0) {

					simpleQuery.update("cookit.receta",
							new String[] { "id_categoria", "img", "procedimiento", "tiempo", "ingredientes", "tags" },
							new String[] { "int", "stream", "string", "int", "string", "string" },
							new Object[] { categoryInt, image, procedure, timeInt, ingredients, tags },
							"id_publicacion = " + (int) resultado[0][0]);

				} else {

					simpleQuery.update("cookit.receta",
							new String[] { "id_categoria", "procedimiento", "tiempo", "ingredientes", "tags" },
							new String[] { "int", "string", "int", "string", "string" },
							new Object[] { categoryInt, procedure, timeInt, ingredients, tags },
							"id_publicacion = " + (int) resultado[0][0]);

				}

				simpleQuery.closeConnection();

				auxCal = new GregorianCalendar();
				auxCal.setTime((java.sql.Date) resultado[0][1]);
				
				savedRecipe = new BeanReceta();
				
				savedRecipe.setId((int) resultado[0][0]);
				savedRecipe.setTitulo(title);
				savedRecipe.setSubtitulo(subtitle);
				savedRecipe.setFecha(auxCal);
				savedRecipe.setId_categoria(Integer.valueOf(category));
				savedRecipe.setProcedimiento(procedure);
				savedRecipe.setTiempo(Integer.valueOf(time));
				savedRecipe.setIngredientes(ingredients);
				savedRecipe.setTags(tags);
				savedRecipe.setImg(Convert.toByteArray(image, fileSize));
				savedRecipe.setIdPublicacion((int) resultado[0][0]);
				
				request.getSession().setAttribute("savedRecipe", savedRecipe);

			} else {
				// no saved recipes exists -> insert the new recipe

				auxCal = Calendar.getInstance(); // Creation date - today

				// Get the last ID
				lastId = (int) simpleQuery.selectOne("cookit.publicacion", "id", "", "id desc", 0, 0);
				lastId++;


				inserted = simpleQuery.insert("cookit.publicacion",
						new String[] { "id", "id_usuario", "titulo", "subtitulo", "destacado", "fecha", "estado" },
						new String[] { "int", "int", "string", "string", "boolean", "calendar", "string" },
						new Object[] { lastId, myself.getId(), title, subtitle, false, auxCal, "guardado" });
				
				if (inserted > 0) {
					// If the post was inserted successfully

					// Got the ID of the last inserted post -> insert the recipe as well

					if(fileSize > 0) {
						simpleQuery.insert("cookit.receta",
								new String[] { "id", "id_publicacion", "img", "id_categoria", "procedimiento", "tiempo",
										"ingredientes", "tags" },
								new String[] { "int", "int", "stream", "int", "string", "int", "string", "string" },
								new Object[] { lastId, lastId, image, categoryInt, procedure, timeInt, ingredients, tags });
					} else {
						simpleQuery.insert("cookit.receta",
								new String[] { "id", "id_publicacion", "id_categoria", "procedimiento", "tiempo",
										"ingredientes", "tags" },
								new String[] { "int", "int", "int", "string", "int", "string", "string" },
								new Object[] { lastId, lastId, categoryInt, procedure, timeInt, ingredients, tags });
					}

					simpleQuery.closeConnection();
					
					savedRecipe = new BeanReceta();
					
					savedRecipe.setId(lastId);
					savedRecipe.setTitulo(title);
					savedRecipe.setSubtitulo(subtitle);
					savedRecipe.setFecha(auxCal);
					savedRecipe.setId_categoria(Integer.valueOf(category));
					savedRecipe.setProcedimiento(procedure);
					savedRecipe.setTiempo(Integer.valueOf(time));
					savedRecipe.setIngredientes(ingredients);
					savedRecipe.setTags(tags);
					savedRecipe.setImg(Convert.toByteArray(image, fileSize));
					savedRecipe.setIdPublicacion(lastId);
					
					request.getSession().setAttribute("savedRecipe", savedRecipe);

				}

			}
//
//			savedRecipe = new BeanReceta();
//
//			savedRecipe.setTitulo(title);
//			savedRecipe.setSubtitulo(subtitle);
//			savedRecipe.setFecha(auxCal);
//			savedRecipe.setId_categoria(Integer.valueOf(category));
//			savedRecipe.setProcedimiento(procedure);
//			savedRecipe.setTiempo(Integer.valueOf(time));
//			savedRecipe.setIngredientes(ingredients);
//			savedRecipe.setTags(tags);
//			savedRecipe.setImg(Convert.toByteArray(image, fileSize));

			// TODO: Check if saving the recipe in an attribute is necessary to not lose it
			// After saving the recipe, load it again in the form
//			request.setAttribute("savedRecipe", savedRecipe);

		} else {
			// no save / no upload -> do nothing
		}

		doGet(request, response);
	}

}
