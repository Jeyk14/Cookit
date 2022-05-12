package clientServlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import conexionBD.Conexion;
//import conexionBD.ModeloSimple;
import data.BeanUsuario;
import dbConnection.Connect;
import dbConnection.SimpleQuery;
import mail.Email;

@WebServlet("/confirmEmail")
public class confirmEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession sess = request.getSession();
		Email mail = new Email();
		String mesage = "";
		BeanUsuario myself;
		String code = "";

		String header = "WEB-INF/confirmEmail.jsp";

		if (sess.getAttribute("myself") == null) {
			// User is not logged -> index
			header = "index";
		} else {
			
			myself = (BeanUsuario) sess.getAttribute("myself");
			
			// The user is logged but is not confirmed -> send mail -> confirmEmail.jsp
			mail.setRemitente("TFGJeyfer2022IESMDC@gmail.com");
			mail.setContrasena("a21_jortnu");
			mail.setDestino(myself.getEmail());
			mail.setAsunto("Cookit! - Confirmación de correo");

			code = randCode();
			sess.setAttribute("code", code);
			mesage = "Bienvenido a Cookit!\n Su código de confirmación es:\n\n" + code;

			mail.setMensaje(mesage);

			mail.enviarCorreo();
			header = "WEB-INF/confirmEmail.jsp";
			
		}

		sess.setAttribute("curPage", "log");
		
		request.getRequestDispatcher(header).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		String code = ""; // The mail confirmation code
		HttpSession sess = request.getSession();
		String header = "doget";

		if (sess.getAttribute("myself") != null && sess.getAttribute("code") != null) {

			if (request.getParameter("recovery-code") != null && !request.getParameter("recovery-code").isEmpty()) {

				BeanUsuario myself = (BeanUsuario) sess.getAttribute("myself");
				SimpleQuery consult;
				Connect con;
				String recoveryCode = request.getParameter("recovery-code");
				code = (String) sess.getAttribute("code");

//				System.out.println("code: "+code+" recoveryCode: "+recoveryCode+"\nEquals? "+recoveryCode.equalsIgnoreCase(code));
				// Compare if the given code is the same code in the session attribute
				if (recoveryCode.equalsIgnoreCase(code)) {
					
					// Both codes are the same -> confirm mail
					con = new Connect("a21_jortnu", "a21_jortnu", "a21_jortnu");
					con.openConnection();
					
					consult = new SimpleQuery(con.getConexion());
					consult.updateOne("cookit.usuario", "confirmado", "boolean", true, "id = "+myself.getId());

					header = "index";

				} else {

					// Code doesn't match -> message + re-send code and retry
					// TODO: Give the user 3 email confirm tries. fail -> wait 60 sec
					request.setAttribute("tempMsg",
							"El codigo que ha introducido no coincide\nSe le va a enviar un nuevo código de inicio");

				}
				
			} // no recovery-code -> do nothing
			
		} else {
			// User is not logged -> doget -> index
		}

		if(header != "doget") {
			request.getRequestDispatcher(header).forward(request, response);
		} else {
			doGet(request, response);
		}

	}

	private String randCode() {

		String code = "";

		String characters = "123456789ABCDEFGHIJTLMNOPQRSTUVWXYZ123456789";

		for (int i = 0; i < 5; i++) {
			code += characters.charAt((int) Math.floor(Math.random() * characters.length()));
		}

		return code;

	}

}
