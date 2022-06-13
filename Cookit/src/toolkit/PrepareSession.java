package toolkit;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.BeanCategoria;
import data.BeanEspecial;
import data.BeanUsuario;
import dbConnection.SimpleQuery;

public class PrepareSession {

	public static void prepare(HttpServletRequest req, HttpServletResponse res) {

		HttpSession sess = req.getSession();

		BeanUsuario myself;
		SimpleQuery sq = new SimpleQuery("a21_jortnu", "a21_jortnu", "a21_jortnu");
		Calendar auxCal;

		BeanCategoria[] categories;
		BeanEspecial special;

		Cookie[] cookies = req.getCookies();
		Cookie cookie;

		Object[][] results = null;
		Object result = null;

		String email = "";
		String pass = "";

		// Search for Cookit related cookies
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				cookie = cookies[i];

				switch (cookie.getName()) {
				case "cookitEmail":
					email = cookie.getValue();
					break;
				case "cookitPass":
					pass = cookie.getValue();
					break;
				}

			}
		}

		// Log in automatically if the user have the log data in the cookies
		if (!email.isEmpty() && !pass.isEmpty()) {
			results = sq.select("cookit.usuario", new String[] { "id", "nombre", "email", "pass", "edad", "dieta",
					"nacionalidad", "creacion", "confirmado" }, "email like '" + email + "'", "", 1, 0);

			if (results != null) {
				// result not null -> user with given email is found

				if (pass.equals((String) results[0][3])) {
					// email and pass matches the user in DB -> automatic login
					myself = new BeanUsuario();

					myself.setId((int) results[0][0]);
					myself.setNombre((String) results[0][1]);
					myself.setEmail((String) results[0][2]);
					myself.setEdad((int) results[0][4]);
					myself.setDieta((String) results[0][5]);
					myself.setNacionalidad((String) results[0][6]);
					auxCal = new GregorianCalendar();
					auxCal.setTime((java.sql.Date) results[0][7]);
					myself.setCreacion(auxCal);
					myself.setConfirmado((boolean) results[0][8]);
					
					req.getSession().setAttribute("cookieMsg", false);

					sess.setAttribute("myself", myself);
					sess.setAttribute("cookieMsg", false);

				}

			}

		}

		// Get the categories
		if (sess.getAttribute("categories") == null) {

			results = sq.select("cookit.categoria", new String[] { "id", "nombre", "descripcion" }, "", "", 0, 0);

			categories = new BeanCategoria[results.length];

			for (int i = 0; i < results.length; i++) {
				categories[i] = new BeanCategoria();
				categories[i].setId((int) results[i][0]);
				categories[i].setNombre((String) results[i][1]);
				categories[i].setDescripcion((String) results[i][2]);
			}

			sess.setAttribute("categories", categories);

		}

		// Get the special dish from the database
		if (sess.getAttribute("special") == null) {

			result = sq.selectOne("cookit.especial", "id_publicacion ", "", "anadido desc", 1, 0);

			if (result != null) {
				// a recipe exists

				results = sq.select(
						"cookit.receta AS rec INNER JOIN cookit.publicacion AS pub ON rec.id_publicacion = pub.id INNER JOIN cookit.usuario AS usu ON pub.id_usuario = usu.id",
						new String[] { "rec.id", "pub.id", "usu.id", "usu.nombre", "pub.titulo", "pub.estrellas",
								"rec.tags" },
						"pub.id = " + (int) result, "", 1, 0);

				if (results != null) {

					special = new BeanEspecial();

					/*
					 * int idUsuario; int idPublicacion; int idReceta; String titulo; String autor;
					 * String tags; int estrellas;
					 */

					special.setIdReceta((int) results[0][0]);
					special.setIdPublicacion((int) results[0][1]);
					special.setIdUsuario((int) results[0][2]);
					special.setAutor((String) results[0][3]);
					special.setTitulo((String) results[0][4]);
					special.setEstrellas((int) results[0][5]);
					special.setTags((String) results[0][6]);

					sess.setAttribute("special", special);

				}

			}

		}

		sq.closeConnection();

	}

}
