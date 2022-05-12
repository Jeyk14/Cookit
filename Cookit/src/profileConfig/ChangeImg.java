package profileConfig;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import data.BeanUsuario;
import dbConnection.Connect;
import dbConnection.SimpleQuery;
import toolkit.typeCkecker;

@MultipartConfig
@WebServlet("/changeImg")
public class ChangeImg extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.getSession().setAttribute("curPage", "profile");
		request.getRequestDispatcher("profile").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Part parteFichero;
		String nombreFichero;
		InputStream stream;

		int tamanoMax = 5000 * 1024; // max file size
		int tamanoFichero;
		String tipoMime;
		String tipoForm;

		int isExito = 0;

		Connect con;
		SimpleQuery query;
		String target = request.getParameter("target");
		String id = request.getParameter("id");
		BeanUsuario myself = new BeanUsuario();

		if (request.getSession().getAttribute("myself") != null) {
			myself = (BeanUsuario) request.getSession().getAttribute("myself");
		}

		// ---------------------------------------

		if (target != null && id != null && request.getPart("image") != null) {
			// target and ID are not null

			if (checkTarget(target) && typeCkecker.isInt(id)) {
				// target exist + id is an number

				if (id.equals(Integer.toString(myself.getId()))) {
					// must be logged in to upload anything

					tipoForm = request.getContentType();

					if ((tipoForm.indexOf("multipart/form-data") >= 0)) {
						// check for the form "multipart/form-data"

						// obtain the file information
						parteFichero = request.getPart("image");
						nombreFichero = Paths.get(parteFichero.getSubmittedFileName()).getFileName().toString();
						stream = parteFichero.getInputStream();
						tamanoFichero = stream.available();
						tipoMime = getServletContext().getMimeType(nombreFichero);

						if (tipoMime.startsWith("image/")) {
							// The uploaded file is an image

							if (tamanoFichero > tamanoMax) {
								// The size of the IMG is under the max permitted size
								request.setAttribute("tempMsg",
										"El archivo es demasiado grande (tamaño máximo: " + (tamanoMax / 1000) + "kB");
								request.setAttribute("success", false);
							} else {

								// isExito = modeloImg.modificar("imagen", stream, bean.getId());
								con = new Connect("a21_jortnu", "a21_jortnu", "a21_jortnu");
								con.openConnection();
								query = new SimpleQuery(con.getConexion());
								isExito = query.updateOne("cookit." + target, "img", "inputStream", stream,
										" id = " + id);
								con.closeConnection();

								if (isExito > 0) {
									// image uploaded
									request.setAttribute("tempMsg", "Se ha subido la imagen con éxito");
									request.setAttribute("success", true);
								} else {
									// image not uploaded
									request.setAttribute("tempMsg", "Ha ocurrido un error en la BD");
									request.setAttribute("success", false);
								}

							}

						} else {

							// The file is not an image
							request.getSession().setAttribute("mensajeError", "El archivo NO es una imagen");

						}

					} // (tipoForm.indexOf("multipart/form-data") >= 0)

				} // id.equals(Integer.toString(myself.getId()))

			} // checkTarget(target) && typeCkecker.isInt(id)

		} // target != null && id != null

		doGet(request, response);
	}

	private boolean checkTarget(String target) {
		boolean valid = false;

		if (target != null) {
			switch (target) {
			case "usuario":
			case "receta":
				valid = true;
				break;
			default:
				valid = false;
				break;
			}
		} else {
			valid = false;
		}

		return valid;
	}

}
