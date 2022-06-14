package clientServlet;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import crypt.CryptSha512;
import data.BeanUsuario;
import data.ConsultaAbierta;
import dbConnection.Connect;
import dbConnection.SimpleQuery;
import toolkit.RandNum;
import toolkit.YahooEmail;

@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		System.out.println("login get");

		String header = "WEB-INF/login.jsp";
		HttpSession sess = request.getSession();

		// If user is already logged -> index
		if (sess.getAttribute("myself") != null) {

			header = "index";

		} else {
			sess.setAttribute("curPage", "log");
		}

		if (request.getSession().getAttribute("changed") != null) {
			// the user comes with a freshly changed password -> show them a message to
			// check the email

			SimpleQuery sq = new SimpleQuery("a21_jortnu", "a21_jortnu", "a21_jortnu");
			CryptSha512 crypt = new CryptSha512();
			String newPass = "";
			String newCryptPass = "";
			String newSalt = "";
			YahooEmail yemail = new YahooEmail();

			newPass = RandNum.getCode(10);
			newSalt = RandNum.getCode(10);
			newCryptPass = crypt.encrypt(newPass, newSalt).get();

			BeanUsuario tempUsr = null;
			tempUsr = (BeanUsuario) request.getSession().getAttribute("tempUsr");
			
			System.out.println("TempUsr es null? "+tempUsr == null);
			
			sq.updateOne("cookit.usuario", "salt", "string", newSalt, "id = " + tempUsr.getId());
			sq.updateOne("cookit.usuario", "pass", "string", newCryptPass, "id = " + tempUsr.getId());

			String from = "jeykantonio@yahoo.com";
			String pass = "ngimadoxqciwjumi";
			String to = tempUsr.getEmail();
			String subject = "Tu nueva contrase\u00f1a en Cookit!";
			String message = "Hace poco decidiste cambiar tu contrase\u00f1a en Cookit.\nTu nueva contrase\u00f1a es: "
					+ newPass;

			yemail = new YahooEmail(from, pass, to, subject, message);
			yemail.sendMail();

			// tell the user to check their email
			request.setAttribute("tempMsg", "Se ha enviado un correo electr&oacute;nico a " + tempUsr.getEmail()
					+ " con tu nueva y flamante contrase&ntilde;a de Cookit!");
			request.setAttribute("success", "true");
			request.setAttribute("showMsg", true);

			request.getSession().removeAttribute("tempUsr");

			request.getSession().removeAttribute("mailCode");

			request.getSession().removeAttribute("changed");

		}

		request.getRequestDispatcher(header).forward(request, response);
	}

	/*-
	 * Take the given username and pass, compare the username and pass with the pass in the database using the salt
	 * 
	 * if mathches, log the user in and prepare a session for him; if not, send back to login
	 * */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		System.out.println("login post");

		HttpSession sesion = request.getSession();
		Connect con = new Connect("a21_jortnu", "a21_jortnu", "a21_jortnu");
		ConsultaAbierta consult = new ConsultaAbierta();
		Object[][] result;
		String queryGetUsr = "SELECT id, nombre, email, edad, nacionalidad, dieta, creacion, pass, salt, confirmado FROM cookit.usuario WHERE email = ";
		CryptSha512 crypt = new CryptSha512();
		BeanUsuario myself; // The logged user data will be stored here

		String header = "doget"; // where the user will be sent at the end of doPost

		String email = request.getParameter("email");
		String pass = request.getParameter("pass") + "";
		String rememberMe = request.getParameter("rememberme"); // null = unchecked

		Calendar auxCal = null;

		// Check if user is trying to log in
		if (email == null || email.isEmpty() || pass == null || pass.isEmpty()) {

			// No data given -> do nothing
			if (request.getAttribute("tempMsg") != null) {

			}

		} else {

			// form fields correctly fulfilled
			con.openConnection();
			queryGetUsr += "'" + email + "'";
			result = consult.select(con.getConexion(), queryGetUsr, 10);

			// Check if email is found in the db
			if (result.length < 1) {

				request.setAttribute("tempMsg", "El correo electr&oacute;nico o la contrase&ntilde;a no son correctos");
				request.setAttribute("success", "false");
				request.setAttribute("showMsg", true);

			} else {

				// email exists -> check if the given pass is equals to the encrypted pass in
				// the DB
				if (crypt.check(pass, (String) result[0][7], (String) result[0][8])) {

					// everything ok -> log in + add important user info to session

					myself = new BeanUsuario();

					// id, nombre, email, edad, nacionalidad, dieta, creacion, pass, salt,
					// confirmado
					myself.setId((int) result[0][0]);
					myself.setNombre((String) result[0][1]);
					myself.setEmail((String) result[0][2]);
					myself.setEdad((int) result[0][3]);
					myself.setNacionalidad((String) result[0][4]);
					myself.setDieta((String) result[0][5]);
					auxCal = new GregorianCalendar();
					auxCal.setTime((java.sql.Date) result[0][6]);
					myself.setCreacion(auxCal);

					myself.setConfirmado((boolean) result[0][9]);

					sesion.setAttribute("myself", myself);

					if (rememberMe != null) {
						// Add email and password to the cookie if user clicked remember-me
						response.addCookie(new Cookie("cookitEmail", myself.getEmail()));
						response.addCookie(new Cookie("cookitPass", (String) result[0][7]));
					}

					request.getSession().setAttribute("cookieMsg", false);

					if (myself.isConfirmado() == false) {
						header = "confirmEmail";
					} else {
						header = "index";
					}

				} else {

					// pass != pass in DB -> retry
					request.setAttribute("tempMsg", "El correo electr&oacute;nico o la contrase&ntilde;a no son correctos");
					request.setAttribute("success", "false");
					request.setAttribute("showMsg", true);

				}

			}

		}

		// Send user either back to the login or logged into index
		if (header != "doget") {
			request.getRequestDispatcher(header).forward(request, response);
		} else {
			doGet(request, response);
		}

	}

}
