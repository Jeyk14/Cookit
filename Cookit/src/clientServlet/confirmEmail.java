package clientServlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import conexionBD.Conexion;
import conexionBD.ModeloSimple;
import data.BeanUsuario;
import data.ConsultaAbierta;
import mail.Email;

@WebServlet("/confirmEmail")
public class confirmEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String header = "WEB-INF/confirmEmail.jsp";
		HttpSession sess = request.getSession();
		
		Email mail = new Email();
		String code = ""; //The mail confirmation code
		BeanUsuario myself = (BeanUsuario) sess.getAttribute("myself");
		String mesage = "";
		
		if(myself == null) {
			// User is not logged -> index
			header = "WEB-INF/index.jsp";
		} else {
			// The user is logged
			if(myself.isConfirmado()) {
				// The user's mail is already confirmed -> index
				header = "WEB-INF/index.jsp";
			} else {
				// The user is logged but is not confirmed -> send mail -> confirmEmail.jsp
				mail.setRemitente("TFGJeyfer2022IESMDC@gmail.com"); //TODO: Borrar contraseña
				mail.setContrasena("a21_jortnu"); // TODO: Borrar usuario
				mail.setDestino(myself.getEmail());
				mail.setAsunto("Cookit! - Confirmación de correo");
				
				code = randCode();
				mesage = "Bienvenido a Cookit!\n Su código de confirmación es:\n\n"+code;
				sess.setAttribute("code", code); // Save the confirmation code for later
				
				mail.setMensaje(mesage);

				mail.enviarCorreo();
			
			}
		}
		
		request.getRequestDispatcher(header).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession sess = request.getSession();
		ModeloSimple consult;
		Conexion con;
		String recoveryCode = request.getParameter("recovery-code");
		String header = "doget";
		
		String code = "";
		if(sess.getAttribute("code") != null) {
			code = (String) sess.getAttribute("code");
		}
		
		// Compare if the given code is the same code in the session attribute
		if(recoveryCode.equals(code)) {
			// Both codes are the same -> confirm mail
			
			con = new Conexion("a21_jortnu", "a21_jortnu", "a21_jortnu");
			consult = new ModeloSimple(con.getConexion());
			consult.modificar("cookit.usuario", "confirmado", "boolean", true);
			
			header = "index";
			
		} else {
			// Code doesn't match -> message + re-send code and retry
			// TODO: is it better to send the mail once and ask the user to re-send it?
			request.setAttribute("tempMsg", "El codigo que ha introducido no coincide\nSe le ha enviado un nuevo código de inicio");
		}
		// Given code = code in email -> SQL(confirmado = true) -> index
		
		doGet(request, response);
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
