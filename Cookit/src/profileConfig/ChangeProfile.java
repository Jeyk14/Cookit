package profileConfig;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import conexionBD.Conexion;
import data.BeanUsuario;
import dbConnection.Connect;
import dbConnection.SimpleQuery;
import toolkit.typeCkecker;

@WebServlet("/changeProfile")
public class ChangeProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getSession().setAttribute("curPage", "profile");
		request.getRequestDispatcher("profile").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String newName = request.getParameter("newName");
		String newEmail = request.getParameter("newEmail");
		String newAge = request.getParameter("newAge");
		String newDiet = request.getParameter("newDiet");
		String newNation = request.getParameter("newNation");
		String[] fields = null;
		String[] types = null;
		Object[] values = null;
		String changes = ""; // 00000 length = 4
		int count = 0;
		int changeCount = 0;
		int returnCode = 0;

		BeanUsuario myself = (BeanUsuario) request.getSession().getAttribute("myself");
		
		System.out.println(myself.getId());
		

		String tempMsg = "";
		Connect con = new Connect("a21_jortnu", "a21_jortnu", "a21_jortnu");
		con.openConnection();
		SimpleQuery simpleQuery = new SimpleQuery(con.getConexion());

		if (newName != null && newName.length() > 1 && !newName.equals(myself.getNombre())) {
			changes += "1";
			changeCount ++;
		} else {
			changes += "0";
		}

		if (newEmail != null && newEmail.length() > 1 && !newEmail.equals(myself.getEmail())) {
			changes += "1";
			changeCount ++;
		} else {
			changes += "0";
		}

		if (newAge != null && typeCkecker.isInt(newAge) && !newAge.equals(Integer.toString(myself.getEdad()))) {
			changes += "1";
			changeCount ++;
		} else {
			changes += "0";
		}

		if (newDiet != null && newDiet.length() > 1 && !newDiet.equals(myself.getDieta())) {
			changes += "1";
			changeCount ++;
		} else {
			changes += "0";
		}

		if (newNation != null && newNation.length() > 1 && !newNation.equals(myself.getNacionalidad())) {
			changes += "1";
			changeCount ++;
		} else {
			changes += "0";
		}
		
		fields = new String[changeCount];
		types = new String[changeCount];
		values = new Object[changeCount];
		
		//System.out.println("N. cambios "+changeCount);

		for (int i = 0; i < changes.length(); i++) {
			if (changes.charAt(i) == '1') {
				switch (i) {
				case 0:
					fields[count] = "nombre";
					types[count] = "string";
					values[count] = newName;
					count ++;
					break;
				case 1:
					fields[count] = "email";
					types[count] = "string";
					values[count] = newEmail;
					count ++;
					break;
				case 2:
					fields[count] = "edad";
					types[count] = "int";
					values[count] = Integer.valueOf(newAge);
					count ++;
					break;
				case 3:
					fields[count] = "dieta";
					types[count] = "string";
					values[count] = newDiet;
					count ++;
					break;
				case 4:
					fields[count] = "nacionalidad";
					types[count] = "string";
					values[count] = newNation;
					count ++;
					break;
				}
			}
		}

		if (changes.length() > 0) {
			returnCode = simpleQuery.update("cookit.usuario", fields, types, values, " id = " + myself.getId());
			con.closeConnection();
		}

		switch (returnCode) {
		case -1:
			// -1 = the values were wrong
			tempMsg = "Parece que ha habido un problema<br/>Contacte con un administrador para que le podamos ayudar";
			request.setAttribute("success", false);

			break;
		case 0:
			// 0 = no changes / error
			tempMsg = "Parece que ha habido un problema.<br/>No hemos podido actualizar su información personal, inténtelo más tarde";
			request.setAttribute("success", false);
			break;
		default:
			// Changes -> success
			tempMsg = "Se han actualizado sus datos.<br/>Es posible que su información tarden unos segundos en hacerse visible";
			request.setAttribute("success", true);
			myself = updateBean(values, changes, myself); // Update the bean
			break;
		}

		System.out.println(returnCode);

		request.setAttribute("tempMsg", tempMsg);
		request.setAttribute("id", Integer.toString(myself.getId()));

		doGet(request, response);
	}
	
	private BeanUsuario updateBean(Object[] values, String changes, BeanUsuario myself) {
		
		int count = 0;
		
		for (int i = 0; i < changes.length(); i++) {
			if (changes.charAt(i) == '1') {
				switch (i) {
				case 0:
					myself.setNombre( (String) values[count]);
					count ++;
					break;
				case 1:
					myself.setEmail( (String) values[count]);
					count++;
					break;
				case 2:
					myself.setEdad( (int) values[count]);
					count ++;
					break;
				case 3:
					myself.setDieta( (String) values[count]);
					count ++;
					break;
				case 4:
					myself.setNacionalidad( (String) values[count]);
					count ++;
					break;
				}
			}
		}
		
		return myself;
		
	}

}
