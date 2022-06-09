package clientServlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import crypt.CryptSha512;
import data.BeanUsuario;
import dbConnection.SimpleQuery;
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
			request.setAttribute("tempMsg", "Ya has iniciado sesión");
			request.setAttribute("success", "false");
			request.setAttribute("showMsg", true);
		} 
		else if(request.getAttribute("changed") != null){
			// enters if the user has successfully changed its password
			header = "login";
			// Tell the user to check the email
			request.setAttribute("tempMessage", (String) request.getAttribute("changed") );
			request.setAttribute("success", "true");
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
		SimpleQuery sq = new SimpleQuery("a21_jortnu", "a21_jortnu", "a21_jortnu");
		CryptSha512 crypt;
		Object[][] foundUser = null;
		YahooEmail yemail;
		BeanUsuario tempUsr = null;
		
		String newPass = "";
		String newCryptPass = "";
		String newSalt = "";
		
		String from;
		String to;
		String pass;
		String subject;
		String message;
		
		if(submit.equals("ENVIAR CÓDIGO")) {
			// User clicked on send confirmation code to email
			
			if(targetEmail == null || targetEmail.equals("")) {
				// Email is wrong or empty -> show message and do nothing
				
				request.setAttribute("tempMsg", targetEmail+" no es dirección de correo válida");
				request.setAttribute("success", "false");
				request.setAttribute("showMsg", true);
				
			} else {
				// email seems OK -> send mail to user
				
				String mailCode = getCode(6);
				
				foundUser = sq.select("cookit.usuario", new String[] {"id"}, "email like '"+targetEmail+"'", "", 1, 0);
				
				System.out.println(sq.getLastQuery());
				
				if(foundUser.length > 0) {
					// user with given email exists in the app
				
					request.getSession().setAttribute("mailCode", mailCode);
					
					from = "jeykantonio@yahoo.com";
					pass = "ngimadoxqciwjumi";
					to = targetEmail;
					subject = "¿Olvidaste tu contraseña en Cookit?";
					message = "Alguien está intentando reestablecer tu contraseña en cookit, si no eres tu ignora este mensaje.<br>Tu código para restablecer la contraseña es "+mailCode+"<br>No responda a este correo";
					
					yemail = new YahooEmail(from, pass, to, subject, message);
					yemail.sendMail();
										
					if(yemail.getError().length() < 1) {
						// No error were returned -> ask the user to check their mail and proceed
						
						request.setAttribute("tempMsg", "Se ha enviado un código de confirmación a "+targetEmail);
						request.setAttribute("success", "true");
						request.setAttribute("showMsg", true);
						
						tempUsr = new BeanUsuario();
						tempUsr.setEmail(targetEmail);
						tempUsr.setId((int) foundUser[0][0]);
						
						request.getSession().setAttribute("tempUsr", tempUsr);
						
					} else {
						// Something went wrong -> inform the user and ask them to retry or contact admin
						
						request.setAttribute("tempMsg", "No se ha enviado un código de confirmación a "+targetEmail+"&#13;Vuelva a intentarlo. Si el error persiste pongase en contacto con un administrador en support@cookit.com");
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
			
			
			
		} else if(submit.equals("RESTAURAR MI CONTRASEÑA")) {
			// user clicked on confirm email
			
			if (recoveryCode == null){
				// the user gave no recovery code -> show message and do nothing
				
				request.setAttribute("tempMessage", "No se ha enviado un código de confirmación a "+targetEmail+"&#13;Vuelva a intentarlo. Si el error persiste pongase en contacto con un administrador en support@cookit.com");
				request.setAttribute("success", "false");
				request.setAttribute("showMsg", true);				
				
			} else {
				// the user gave a valid (maybe) code -> test if the code received matches the code sent
				
				if(request.getSession().getAttribute("mailCode") == null) {
					// The user doesn't have a confirmation code yet -> inform and do nothing
					
					request.setAttribute("tempMessage", "No se ha enviado un código de confirmación todavía&#13;Escriba su dirección de correo en la parte superior y confirmelo");
					request.setAttribute("success", "false");
					request.setAttribute("showMsg", true);	
					
					
				} else {
					
					if(!recoveryCode.toUpperCase().equals((String) request.getSession().getAttribute("mailCode"))) {
						// The code given does not match the code sent -> inform and do nothing
						
						request.setAttribute("tempMessage", "El código que ha escrito no es correcto");
						request.setAttribute("success", "false");
						request.setAttribute("showMsg", true);
						
					} else {
						// The code given matches -> send the user it's new password via email
												
						crypt = new CryptSha512();
						
						newPass = getCode(10);
						newSalt = getCode(10);
						newCryptPass = crypt.encrypt(newPass, newSalt).get();
						
						tempUsr = (BeanUsuario) request.getSession().getAttribute("tempUsr");
						
						sq.updateOne("cookit.usuario", "salt", "string", newSalt, "id = "+tempUsr.getId());
						sq.updateOne("cookit.usuario", "pass", "string", newCryptPass, "id = "+tempUsr.getId());
						
						from = "jeykantonio@yahoo.com";
						pass = "ngimadoxqciwjumi";
						to = tempUsr.getEmail();
						subject = "Tu nueva contraseña en Cookit!";
						message = "Hace poco decidiste cambiar tu contraseña en Cookit. Tu nueva contraseña es "+newPass+"\n¡No la olvides!";
						
						yemail = new YahooEmail(from, pass, to, subject, message);
						yemail.sendMail();
												
						request.setAttribute("changed", "Se ha enviado un correo electrónico a "+tempUsr.getEmail()+" con tu nueva y flamante contraseña de Cookit!");
//						request.setAttribute("tempMessage", "Se ha enviado un correo electrónico a "+tempUsr.getEmail()+" con tu nueva y flamante contraseña de Cookit!" );
//						request.setAttribute("success", "true");
//						request.setAttribute("showMsg", true);
						
						// remove the now unused attributes
						request.getSession().removeAttribute("tempUsr");
						if(request.getSession().getAttribute("mailCode") != null) {
							request.getSession().removeAttribute("mailCode");
						}
						
//						request.getRequestDispatcher("login").forward(request, response);
						
					}
					
				}
				
			}
			
		}
		
		doGet(request, response);
	}
	
	private String getCode(int length) {
		String code = "";
		String template = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

		for (int i = 0; i < length; i++) {
			code += template.charAt((int) Math.floor(Math.random() * template.length()));
		}

		return code;
	}

}
