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

@WebServlet("/login")
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String header = "WEB-INF/login.jsp";
		HttpSession sess = request.getSession();

		// TODO: Check for cookies. If login cookies exists, fill the form automatically

		// If user is already logged -> index
		if (sess.getAttribute("myself") != null) {

			header = "index";

		} else {
			sess.setAttribute("curPage", "log");
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

		HttpSession sesion = request.getSession();
		Connect con = new Connect("a21_jortnu", "a21_jortnu", "a21_jortnu");
		ConsultaAbierta consult = new ConsultaAbierta();
		Object[][] result;
		String queryGetUsr = "SELECT id, nombre, email, edad, nacionalidad, dieta, creacion, pass, salt, confirmado FROM cookit.usuario WHERE email = ";
		CryptSha512 crypt = new CryptSha512();
		BeanUsuario myself; // The logged user data will be stored here
		
		String header = "doget"; // where the user will be sent at the end of doPost
		String tempMsg = "";
		boolean success = true;

		String email = request.getParameter("email");
		String pass = request.getParameter("pass")+"";
		String rememberMe = request.getParameter("rememberme"); // null = unchecked

		Calendar auxCal = null;

		// Check if user is trying to log in
		if (email == null || email.isEmpty() || pass == null || pass.isEmpty()) {

			tempMsg = "Parece que el formulario no está correctamente rellenado";

		} else {

			// form fields correctly fulfilled
			con.openConnection();
			queryGetUsr += "'" + email + "'";
			result = consult.select(con.getConexion(), queryGetUsr, 10); // TODO: don't get the whole query to check pass+salt

			// Check if email is found in the db
			if (result.length < 1) {

				success = false;
				tempMsg = "El correo electrónico o la contraseña no son correctos";
				System.out.println("result.length < 1");

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
					
					// Add email and password to the cookie
					response.addCookie(new Cookie("cookitEmail", myself.getEmail()));
					response.addCookie(new Cookie("cookitPass", (String) result[0][7]));
					
					request.setAttribute("cookieMsg", false);

					if (myself.isConfirmado() == false) {
						header = "confirmEmail";
					} else {
						header = "index";
					}

					if (rememberMe != null) {
						// TODO: Add the email and pass to a cookie
					}

				} else {

					// pass != pass in DB -> retry
					request.setAttribute("tempMsg", "El correo electrónico o la contraseña no son correctos");
					request.setAttribute("success", false);

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
