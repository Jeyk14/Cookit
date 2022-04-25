package data;

import java.sql.*;

public class Conexion {

	Connection con;
	String url = "jdbc:postgresql://ns3034756.ip-91-121-81.eu/a21_jortnu";
	String usuario = "a21_jortnu";
	String pass = "a21_jortnu";

	public void abrirConexion() {
		try {

			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(url, usuario, pass);

		} catch (SQLException sqle) {
			System.err.println("Hubo un error durante la conexión a la base de datos a IES Mar de Cádiz. \n Error log:");
			sqle.printStackTrace(); 

		} catch (ClassNotFoundException cnfex) {
			System.err.println("No se ha encontrado el controlador de la base de datos en IES Mar de Cádiz. \n Error log:");
			cnfex.printStackTrace();
		}
	}

	/*
	 * Cierra la conexión abierta
	 */
	public void cerrarConexion() {
		try {
			con.close();
		} catch (SQLException sqle) {
			System.err.println("Hubo un error durante el cierre de la conexión a la base de datos de IES Mar de Cádiz. \n Error log:");
			sqle.printStackTrace();
		} catch (Exception ex) {
			System.err.println("Hubo un error desconocido durante el cierre de la conexión a la base de datos de IES Mar de Cádiz. \n Error log:");
			ex.printStackTrace();
		}
	}

	/*
	 * Retorna por parámetro la Conexión
	 */
	public Connection getConexion() {
		return con;
	}
	
	public void cambiarConexion(String nuevaUrl, String username, String password) {
		
		url = nuevaUrl;
		usuario = username;
		pass = password;
		
	}

}
