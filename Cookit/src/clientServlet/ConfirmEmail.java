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
import dbConnection.SimpleQuery;
import toolkit.YahooEmail;

@WebServlet("/confirmEmail")
public class ConfirmEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// A user with an unconfirmed email logged -> ask to confirm email until they do confirm it
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession sess = request.getSession();
		YahooEmail mail = new YahooEmail();
		String mesage = "";
		BeanUsuario myself;
		String code = "";

		String header = "WEB-INF/confirmEmail.jsp";

		if (sess.getAttribute("myself") == null) {
			// User is not logged -> index
			header = "index";
			request.setAttribute("tempMsg", "Debes estar registrado para utilizar esta función");
			request.setAttribute("success", "false");
			request.setAttribute("showMsg", true);
		} else {
			
			myself = (BeanUsuario) sess.getAttribute("myself");
			
			// The user is logged but is not confirmed -> send mail -> confirmEmail.jsp
			mail.setFrom("jeykantonio@yahoo.com");
			mail.setPassword("ngimadoxqciwjumi");
			mail.setTo(myself.getEmail());
			mail.setSubject("Cookit! - Confirmación de correo");

			code = randCode();
			sess.setAttribute("code", code);
			mesage = "Bienvenido a Cookit!\n Su código de confirmaci&oacute;n es:\n\n" + code;

			mail.setMessage(mesage);

			mail.sendMail();
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
				String recoveryCode = request.getParameter("recovery-code");
				code = (String) sess.getAttribute("code");

				// Compare if the given code is the same code in the session attribute
				if (recoveryCode.equalsIgnoreCase(code)) {
					
					// Both codes are the same -> confirm mail
					
					consult = new SimpleQuery("a21_jortnu", "a21_jortnu", "a21_jortnu");
					consult.updateOne("cookit.usuario", "confirmado", "boolean", true, "id = "+myself.getId());
					consult.closeConnection();
					
					myself.setConfirmado(true);

					header = "index";
					
					request.setAttribute("tempMsg", "¡Ha confirmado su correo electrónico!&#13;Disfrute de todas las funciones de Cookit!");
					request.setAttribute("success", "true");
					request.setAttribute("showMsg", true);

				} else {

					// Code doesn't match -> message + re-send code and retry
					request.setAttribute("tempMsg", "El código escrito no coincide, se le ha enviado otro código");
					request.setAttribute("success", "false");
					request.setAttribute("showMsg", true);

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

		for (int i = 0; i < 6; i++) {
			code += characters.charAt((int) Math.floor(Math.random() * characters.length()));
		}

		return code;

	}

}
