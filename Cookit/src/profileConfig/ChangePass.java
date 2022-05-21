package profileConfig;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import crypt.CryptSha512;
import data.BeanUsuario;
import dbConnection.Connect;
import dbConnection.SimpleQuery;

@WebServlet("/changePass")
public class ChangePass extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String header = "profile";
		
		// if something went wrong with the pass -> back to mod
		// else -> back to profile
		if(request.getAttribute("success") != null) {
			if((boolean) request.getAttribute("success") == false) {
				header = "modProfile";
				request.getSession().setAttribute("curPage", "profileMod");
			}
		}
		
		request.getRequestDispatcher(header).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Connect con = new Connect("a21_jortnu", "a21_jortnu", "a21_jortnu");
		con.openConnection();
		SimpleQuery simpleQuery = new SimpleQuery(con.getConexion());

		BeanUsuario myself = (BeanUsuario) request.getSession().getAttribute("myself");

		String oldPass = request.getParameter("oldPass");
		String newPass = request.getParameter("newPass");
		String repepass = request.getParameter("newPass2");
		
		System.out.println(oldPass+" "+newPass+" "+repepass);

		String oldStored = "";
		String oldSalt = "";
		String newSalt = "";
		CryptSha512 crypt = new CryptSha512();

		if (oldPass != null && newPass != null && repepass != null) {
			if (oldPass.isEmpty() || newPass.isEmpty() || repepass.isEmpty()) {
				// A neccessary field is empty -> do nothing
				request.setAttribute("tempMsg", "Uno de los campos de contraseña está vacío");
				request.setAttribute("success", false);
			} else {

				oldStored = (String) simpleQuery.selectOne("cookit.usuario", "pass", "id = " + myself.getId(), "", 0, 0);
				oldSalt = (String) simpleQuery.selectOne("cookit.usuario", "salt", "id = " + myself.getId(), "", 0, 0);

				System.out.println("Coinciden: "+crypt.check(oldPass, oldStored, oldSalt));
				if (crypt.check(oldPass, oldStored, oldSalt)) {
					// The password given and the old password matches -> check new password

					System.out.println("Son iguales: "+newPass.equals(repepass));
					if (newPass.equals(repepass)) {
						// new pass = repeated pass -> change user password
						newSalt = prepareNewSalt();
						simpleQuery.updateOne("cookit.usuario", "pass", "string",
								crypt.encrypt(newPass, newSalt).get(), " id = " + myself.getId());
						simpleQuery.updateOne("cookit.usuario", "salt", "string", newSalt, " id = " + myself.getId());

						System.out.println("newsalt " + newSalt);

						request.setAttribute("tempMsg", "contraseña actualizada con éxito");
						request.setAttribute("success", true);

					} else {
						// repeated password != new password -> do nothing
						request.setAttribute("tempMsg", "Las contraseñas no coinciden");
						request.setAttribute("success", false);
					}

				} else {
					// actual pass != pass given -> do nothing
					request.setAttribute("tempMsg", "La contraseña proporcionada no es corrects");
					request.setAttribute("success", false);
				}

			}
		} else {
			// The field are null -> do nothing
			request.setAttribute("tempMsg", "Ha ocurrido un problema, vuelva a intentarlo");
			request.setAttribute("success", false);
		}

		if (!newPass.equals(repepass))

			con.closeConnection();
		doGet(request, response);
	}

	private String prepareNewSalt() {

		String salt = "";
		Calendar cal = Calendar.getInstance();
		String chars = "abcdefghijklmnopqsrtuvwxyz123456789123456789";

		String prefix = "";
		String subfix = cal.get(Calendar.DATE) + cal.get(Calendar.MONTH) + cal.get(Calendar.YEAR) + "";

		for (int i = 0; i < 6; i++) {
			prefix += chars.charAt((int) Math.floor(Math.random() * chars.length()));
		}

		salt = prefix + "$" + subfix;

		return salt;

	}

}
