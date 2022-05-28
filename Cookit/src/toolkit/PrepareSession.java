package toolkit;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import crypt.CryptSha512;
import data.BeanCategoria;
import data.BeanEspecial;
import data.BeanReceta;
import data.BeanUsuario;
import dbConnection.SimpleQuery;

public class PrepareSession {

	public static void prepareSession(HttpServletRequest req, HttpServletResponse res) {

		HttpSession sess = req.getSession();

		BeanUsuario myself;
		CryptSha512 crypt = new CryptSha512();
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

		// Log in automatically if the user have the log data in the cookies
		if (!email.isEmpty() && !pass.isEmpty()) {
			results = sq.select("cookit.usuario", new String[] { "id", "nombre", "email", "pass", "edad", "dieta",
					"nacionalidad", "creacion", "salt" }, "email like " + email, "", 1, 0);

			if (results != null) {
				// result not null -> user with given email is found

				if (crypt.check(pass, (String) results[0][3], (String) results[0][7])) {
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

					sess.setAttribute("myself", myself);
					sess.setAttribute("cookieMsg", false);

				}

			}

		}

		// Get the categories
		if (sess.getAttribute("categories") == null) {

		}
		try {
			results = sq.select("cookit.recipe", new String[] { "nombre", "descripcion" }, "", "", 0, 0);

			categories = new BeanCategoria[results.length];

			for (int i = 0; i < results.length; i++) {
				categories[i].setNombre((String) results[i][0]);
				categories[i].setDescripcion((String) results[i][1]);
			}

			req.setAttribute("categories", categories);
		} catch (Exception sqle) {
			// The data couldn't be retrieved
			req.getSession().setAttribute("tempMsg",
					"Parece que no se ha podido obtener cierta información del sercidor");
			req.getSession().setAttribute("success", false);
		}

		// Get the special dish from the database
		if(sess.getAttribute("special") == null) {
			
			result = sq.selectOne("cookit.destacado", "id", "", "anadido desc", 1, 0);
			
			if(result != null) {
				// a recipe exists
				
				results = sq.select("cookit.receta AS rec INNER JOIN cookit.publicacion AS pub ON rec.id_publicacion = pub.id INNER JOIN cookit.usuario AS usu ON pub.id_Usuario = usu.id",
						new String[] {"rec.id", "pub.id", "usu.id", "usu.nombre", "pub.title", "pub.stars", "rec.tags"}, "", "", 1, 0);
				
				if(results != null) {
					
					special = new BeanEspecial();
					
					/*
					 * 	int idUsuario;
						int idPublicacion;
						int idReceta;
						String titulo;
						String autor;
						String tags;
						int estrellas;
					 * */
					
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
