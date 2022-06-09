package clientServlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import toolkit.YahooEmail;

@WebServlet("/restorePass")
public class restorePass extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/*
		 * Restore password:
		 * 	In login, user clicks in "i forgot my password"
		 * 	User is asked an email to send a temporal code
		 * 	If an account with said email exist, a code is sent to the email
		 * 	The user can insert the code, if correct -> change password
		 * 		If the user abandon the password change page, the process starts over
		 * */
		
		String header = "WEB-INF/restorePass.jsp";
		
		if(request.getSession().getAttribute("myself") == null) {
			// uses is not logged -> index
			
			header = "index";
			request.setAttribute("tempMessage", "No puedes confirmar el correo si no tienes una cuenta antes");
			request.setAttribute("success", "false");
			request.setAttribute("showMsg", true);
		}
		
		request.getSession().setAttribute("curPage", "log");

		request.getRequestDispatcher(header).forward(request, response);
	}
	
	// TODO: Make the user confirm the email and then show the password change in the same jsp 
	// Show/hide the pass change fields with a sess attribute

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String submit = request.getParameter("submit");
		String targetEmail = request.getParameter("target-email");
		String recoveryCode = request.getParameter("recovery-code");
		YahooEmail yemail;
		
		String from;
		String to;
		String pass;
		String subject;
		String message;
		
		if(submit.equals("ENVIAR CÓDIGO")) {
			// User clicked on send confirmation code to email
			
			if(targetEmail == null || targetEmail.equals("")) {
				// Email is wrong or empty -> show message and do nothing
				request.setAttribute("tempMessage", targetEmail+" no es dirección de correo válida");
				request.setAttribute("success", "false");
				request.setAttribute("showMsg", true);
			} else {
				// email seems OK -> send mail to user
				
				String mailCode = getCode();
				
				request.getSession().setAttribute("mailCode", mailCode);
				
				from = "jeykantonio@yahoo.com";
				pass = "ngimadoxqciwjumi";
				to = targetEmail;
				subject = "¡Tu código de confirmación de Cookit!";
				message = "Tu código para confirmar el correo electrónico es "+mailCode+"&#13;&#13;No responda a este correo";
				
				yemail = new YahooEmail(from, pass, to, subject, message);
				yemail.sendMail();
				
				if(yemail.getError().length() < 1) {
					// No error were returned -> ask the user to check their mail and proceed
					
					request.setAttribute("tempMessage", "Se ha enviado un código de confirmación a "+targetEmail);
					request.setAttribute("success", "true");
					request.setAttribute("showMsg", true);
					
				} else {
					// Something went wrong -> inform the user and ask them to retry or contact admin
					
					request.setAttribute("tempMessage", "No se ha enviado un código de confirmación a "+targetEmail+"&#13;Vuelva a intentarlo. Si el error persiste pongase en contacto con un administrador en support@cookit.com");
					request.setAttribute("success", "false");
					request.setAttribute("showMsg", true);
					
				}
				
			}
			
			
			
		} else if(submit.equals("CONFIRMAR CORREO")) {
			// user clicked con confirm email
			
			if (recoveryCode == null){
				// the user gave no recovery code -> show message and do nothing
				
				request.setAttribute("tempMessage", "No se ha enviado un código de confirmación a "+targetEmail+"&#13;Vuelva a intentarlo. Si el error persiste pongase en contacto con un administrador en support@cookit.com");
				request.setAttribute("success", "false");
				request.setAttribute("showMsg", true);				
				
			} else {
				// the user gave a valid (maybe) code -> text if the code received matches the code sent
				
				if(request.getSession().getAttribute("mailCode") == null) {
					// The user doesn't have a confirmation code yet -> inform and do nothing
					
					request.setAttribute("tempMessage", "No se ha enviado un código de confirmación todavía&#13;Escriba su dirección de correo en la parte superior y confirmelo");
					request.setAttribute("success", "false");
					request.setAttribute("showMsg", true);	
					
					
				} else {
					
					if(!recoveryCode.toUpperCase().equals((String) request.getSession().getAttribute("mailCode"))) {
						// The code given does not match the code sent -> inform and do nothing
					} else {
						// The code given matches -> change the user to confirmed
					}
					
				}
				
			}
			
		}
		
		doGet(request, response);
	}
	
	private String getCode() {
		String code = "";
		String template = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

		for (int i = 0; i < 4; i++) {
			code += Integer.toString((int) Math.floor(Math.random() * 9));
		}

		return code;
	}

}
