package clientServlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import conexionBD.Conexion;
import contraseña.Encriptacion;
import data.BeanUsuario;
import data.ConsultaAbierta;
import dbConnection.Connect;

@WebServlet("/login")
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String header = "WEB-INF/login.jsp";
		HttpSession sess = request.getSession();
		
		// TODO: Check for cookies. If login cookies exists, fill the form automatically
		
		// If user is already logged -> index
		if(sess.getAttribute("myself") != null) {
			
			header = "index";
			
		}
		
		request.getRequestDispatcher(header).forward(request, response);
	}

	/*-
	 * Take the given username and pass, compare the username and pass with the pass in the database using the salt
	 * 
	 * if mathches, log the user in and prepare a session for him; if not, send back to login
	 * */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession sesion = request.getSession();
		Connect con = new Connect("a21_jortnu", "a21_jortnu", "a21_jortnu");
		ConsultaAbierta consult = new ConsultaAbierta();
		Object[][] result;
		String queryGetUsr = "SELECT id, nombre, email, pass, salt, confirmado FROM cookit.usuario WHERE email = ";
		Encriptacion crypt = new Encriptacion();
		BeanUsuario myself; // The logged user data will be stored here
		
		String header = "doget"; // where the user will be sent at the end of doPost
		String tempMsg = "";
		
		String email = request.getParameter("email");
		String pass = request.getParameter("pass");
		String rememberMe = request.getParameter("rememberme"); // null = unchecked
		
		// Check if user is trying to log in
		if(email == null || email.isEmpty() || pass == null || pass.isEmpty()) {
			
			tempMsg = "Parece que el formulario no está correctamente rellenado";
			
		} else {

			// form fields correctly fulfilled
			con.abrirConexion();
			queryGetUsr += "'"+email+"'";
			result = consult.select(con.getConexion(), queryGetUsr, 6);
			
			// Check if email is found in the db
			if(result.length < 1) {
				
				tempMsg = "El correo electrónico o la contraseña no son correctos";
				System.out.println("result.length < 1");
				
			} else {
				
				// email exists -> check if the given pass is equals to the encrypted pass in the DB
				if(crypt.comprobarContraseña(pass, (String) result[0][3], (String) result[0][4]) ) {
					
					// everything ok -> log in + add important user info to session
					
					myself = new BeanUsuario();
					
					myself.setId((int)result[0][0]);
					myself.setNombre((String)result[0][1]);
					myself.setEmail((String)result[0][2]);
					myself.setConfirmado((boolean)result[0][5]);
					
					System.out.println((boolean)result[0][5]);
					
					sesion.setAttribute("myself", myself);
					
					if(myself.isConfirmado() == false) {
						header = "confirmEmail";
					} else {
						header = "index";
					}
					
					if(rememberMe != null) {
						// TODO: Add the email and pass to a cookie
					}
					
				} else {
					
					// pass != pass in DB -> retry
					tempMsg = "El correo electrónico o la contraseña no son correctos";
					System.out.println("Pass:"+pass+" / PassBD: "+(String) result[0][3]+" / Salt: "+(String) result[0][4]+"\n"
							+ " / "+crypt.comprobarContraseña(pass, (String) result[0][3], (String) result[0][4]));
					
				}
				
			}
			
		}
		
		// Send user either back to the login or logged into index
		if(header != "doget") {
			request.getRequestDispatcher(header).forward(request, response);
		} else {
			if(tempMsg.length() > 1) {
				request.setAttribute("tempMsg", tempMsg);
			}
			doGet(request, response);
		}
		
	}

}
