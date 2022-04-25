package data;

import java.util.Calendar;

public class BeanUsuario {

	private int id;
	private String nombre;
	private String email;
	private int edad;
	private String dieta;
	private String nacionalidad;
	private String pass;
	private String salt;
	private boolean confirmado;
	private byte[] img;
	private Calendar creacion;
	
	//--------------------------------------------------------------

	public BeanUsuario() {
		id = 0;
		nombre = "";
		email = "";
		edad = 0;
		dieta = "";
		nacionalidad = "";
		pass = "";
		salt = "";
		confirmado = false;
		img = null;
		creacion = null;
	}

	public BeanUsuario(int id, String nombre, String email, int edad, String dieta, String nacionalidad, String pass,
			String salt, boolean confirmado, byte[] img, Calendar creacion) {
		this.id = id;
		this.nombre = nombre;
		this.email = email;
		this.edad = edad;
		this.dieta = dieta;
		this.nacionalidad = nacionalidad;
		this.pass = pass;
		this.salt = salt;
		this.confirmado = confirmado;
		this.img = img;
		this.creacion = creacion;
	}
	
	//--------------------------------------------------------------

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getDieta() {
		return dieta;
	}

	public void setDieta(String dieta) {
		this.dieta = dieta;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public boolean isConfirmado() {
		return confirmado;
	}

	public void setConfirmado(boolean confirmado) {
		this.confirmado = confirmado;
	}

	public byte[] getImg() {
		return img;
	}

	public void setImg(byte[] img) {
		this.img = img;
	}

	public Calendar getCreacion() {
		return creacion;
	}

	public void setCreacion(Calendar creacion) {
		this.creacion = creacion;
	}
	
	//--------------------------------------------------------------
	
	

}
