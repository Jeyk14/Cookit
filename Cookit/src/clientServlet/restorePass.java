package clientServlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.BeanUsuario;
import dbConnection.SimpleQuery;
import toolkit.RandNum;
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
		
		if(request.getSession().getAttribute("myself") != null) {
			// uses is not logged -> index
			
			header = "index";
			request.setAttribute("tempMsg", "Ya has iniciado sesi&oacute;n");
			request.setAttribute("success", "false");
			request.setAttribute("showMsg", true);
		} 
		else if(request.getSession().getAttribute("changed") != null){
			// enters if the user has successfully changed its password
			header = "login";
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
		SimpleQuery sq = new SimpleQuery("a21_jortnu", "a21_jortnu", "a21_jortnu");
		Object[][] foundUser = null;
		YahooEmail yemail;
		BeanUsuario tempUsr = null;
		
		String from;
		String to;
		String pass;
		String subject;
		String message;
		
		String savedCode;
		
		if(submit.toLowerCase().contains("enviar")) {
			// User clicked on send confirmation code to email
			
			if(targetEmail == null || targetEmail.equals("")) {
				// Email is wrong or empty -> show message and do nothing
				
				request.setAttribute("tempMsg", targetEmail+" no es direcci&oacute;n de correo v&aacute;lida");
				request.setAttribute("success", "false");
				request.setAttribute("showMsg", true);
				
			} else {
				// email seems OK -> send mail to user
				
				String mailCode = RandNum.getCode(6);
				
				foundUser = sq.select("cookit.usuario", new String[] {"id", "email"}, "email LIKE '"+targetEmail+"'", "", 1, 0);
								
				if(foundUser.length > 0) {
					// user with given email exists in the app
				
					request.getSession().setAttribute("mailCode", mailCode);
					
					from = "jeykantonio@yahoo.com";
					pass = "ngimadoxqciwjumi";
					to = targetEmail;
					subject = "¿Olvidaste tu contrase\u00f1a en Cookit?";
					message = "Alguien est\u00e1 intentando reestablecer tu contrase\u00f1a en Cookit!, SI NO ERES TÚ ignora este mensaje.\nTu c\u00f3digo para restablecer la contrase\u00f1a es el siguiente\n\n"+mailCode+"\n\nNo responda a este correo\nMuchas gracias";
					
					yemail = new YahooEmail(from, pass, to, subject, message);
					yemail.sendMail();
										
					if(yemail.getError().length() < 1) {
						// No error were returned -> ask the user to check their mail and proceed
						
						request.setAttribute("tempMsg", "Se ha enviado un c&oacute;digo de confirmaci&oacute;n a "+targetEmail);
						request.setAttribute("success", "true");
						request.setAttribute("showMsg", true);
						
						tempUsr = new BeanUsuario();
						tempUsr.setEmail((String) foundUser[0][1]);
						tempUsr.setId((int) foundUser[0][0]);
						
						System.out.println("Found "+tempUsr.getEmail()+" con ID "+tempUsr.getId());
						
						request.getSession().setAttribute("tempUsr", tempUsr);
						
					} else {
						// Something went wrong -> inform the user and ask them to retry or contact admin
						
						request.setAttribute("tempMsg", "No se ha enviado un c&oacute;digo de confirmaci&oacute;n a "+targetEmail+"&#13;Vuelva a intentarlo. Si el error persiste pongase en contacto con un administrador en support@cookit.com");
						request.setAttribute("success", "false");
						request.setAttribute("showMsg", true);
						
					}
					
				} else {
					// No user with given email exists in the app -> show message and do nothing
					
					request.setAttribute("tempMsg", "No existe el usuario con ese email");
					request.setAttribute("success", "false");
					request.setAttribute("showMsg", true);
					
				}
				
			}
			
			
			
		} else if(submit.toLowerCase().contains("restaurar")) {
			// user clicked on confirm email
			
			savedCode = (String) request.getSession().getAttribute("mailCode");
			
			if (recoveryCode == null){
				// the user gave no recovery code -> show message and do nothing
				
				request.setAttribute("tempMsg", "No se ha enviado un c&oacute;digo de confirmaci&oacute;n a "+targetEmail+"&#13;Vuelva a intentarlo. Si el error persiste pongase en contacto con un administrador en support@cookit.com");
				request.setAttribute("success", "false");
				request.setAttribute("showMsg", true);				
				
			} else {
				// the user gave a code -> test if the code received matches the code sent
				
				if(savedCode == null) {
					// The user doesn't have a confirmation code yet -> inform and do nothing
					
					request.setAttribute("tempMsg", "No se ha enviado un c&oacute;digo de confirmaci&oacute;n todav&iacute;a&#13;Escriba su direcci&oacute;n de correo en la parte superior y conf&iacute;rmelo");
					request.setAttribute("success", "false");
					request.setAttribute("showMsg", true);	
					
					
				} else {
					// the code is not null
					
					if(!recoveryCode.toUpperCase().equals(savedCode.toUpperCase())) {
						// The code given does not match the code sent -> inform and do nothing
						
						request.setAttribute("tempMsg", "El c&oacute;digo que ha escrito no es correcto");
						request.setAttribute("success", "false");
						request.setAttribute("showMsg", true);
						
					} else {
						// The code given matches -> send the user it's new password via email

						tempUsr = (BeanUsuario) request.getSession().getAttribute("tempUsr");
												
						request.getSession().setAttribute("changed", "Se ha enviado un correo electr&oacute;nico a "+tempUsr.getEmail()+" con tu nueva y flamante contrase&oacute;a de Cookit!");
						System.out.println("Llega aqui "+tempUsr == null);
						
						// remove the now unused attributes
						if(request.getSession().getAttribute("mailCode") != null) {
							request.getSession().removeAttribute("mailCode");
						}
						
						
					}
					
				}
				
			}
			
		}
		
		doGet(request, response);
	}

}
