package toolkit;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class YahooEmail {

	String host;
	String from;
	String password;
	String to;
	String subject;
	String message;

	String log = "";

	public YahooEmail() {
		host = "smtp.mail.yahoo.com";
		from = "";
		password = "";
		to = "";
		subject = "";
		message = "";
	}

	public YahooEmail(String remitente, String contrasena, String destino, String asunto, String mensaje) {
		this.from = remitente;
		this.password = contrasena;
		this.to = destino;
		this.subject = asunto;
		this.message = mensaje;
	}

	public YahooEmail(String host, String remitente, String contrasena, String destino, String asunto, String mensaje) {
		this.host = host;
		this.from = remitente;
		this.password = contrasena;
		this.to = destino;
		this.subject = asunto;
		this.message = mensaje;
	}

	/**
	 * Build and send a Yahoo email
	 */
	public void sendMail() {

		Session sesion;
		MimeMessage mensajeMime;
		Transport transport;
		Properties propiedades;


		try {

			propiedades = System.getProperties();

			propiedades.put("mail.smtp.starttls.enable", "true");
			propiedades.setProperty("mail.smtp.ssl.trust", "smtp.mail.yahoo.com"); // Que trustee
			propiedades.put("mail.smtp.user", from);
			propiedades.put("mail.smtp.clave", password);
			propiedades.put("mail.smtp.port", "587");
			propiedades.put("mail.smtp.auth", "true");
			propiedades.put("mail.smtp.starttls.required", "true");
			propiedades.put("mail.smtp.ssl.protocols", "TLSv1.2");

			sesion = Session.getDefaultInstance(propiedades);
			mensajeMime = new MimeMessage(sesion);

			mensajeMime.setFrom(new InternetAddress(from));

			mensajeMime.addRecipients(Message.RecipientType.TO, to);

			mensajeMime.setSubject(subject);
			mensajeMime.setText(message);

			transport = sesion.getTransport("smtp");
			transport.connect("smtp.mail.yahoo.com", from, password);
			transport.sendMessage(mensajeMime, mensajeMime.getAllRecipients()); // Envia el mensaje
			transport.close();

		} catch (MessagingException messex) {

			System.err.print(messex);
			log += messex.toString();

		} catch (Exception ex) {

			System.err.println(ex);
			log += ex.toString();

		}

	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getFom() {
		return from;
	}

	public void setFrom(String remitente) {
		this.from = remitente;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String contrasena) {
		this.password = contrasena;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String destino) {
		this.to = destino;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String asunto) {
		this.subject = asunto;
	}

	public String getMessage () {
		return message;
	}

	public void setMessage (String mensaje) {
		this.message = mensaje;
	}

	public String getError() {
		return log;
	}
}
