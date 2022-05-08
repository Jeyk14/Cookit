package toolkit;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbConnection.Connect;
import dbConnection.SimpleQuery;

@WebServlet("/loadImg")
public class LoadImg extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connect con;
		SimpleQuery modelo;
		byte[] imagenBytes = null; // Imagen en forma de array de Bytes (recien sacado de la BD)
		OutputStream out; // Para devolver la imagen (piensa que es un return)
		
		String id = request.getParameter("id");
		String target = request.getParameter("target");
		
		if( id != null ) {
			
			con = new Connect("a21_jortnu", "a21_jortnu", "a21_jortnu");
			con.openConnection();
			modelo = new SimpleQuery(con.getConexion());
						
			switch (target.toLowerCase()) {
			case "recipe":

				imagenBytes = (byte[]) modelo.selectOne("cookit.receta", "img", "id = " + id);
				break;
				
			case "user":
			case "usr":
				imagenBytes = (byte[]) modelo.selectOne("cookit.usuario", "img", "id = " + id);
				break;

			default:
				// no target -> do nothing
				break;
			}
			
			con.closeConnection();
			
		}

		out = response.getOutputStream();

		out.write(imagenBytes); // return the image
		out.flush();
	}
	
}
