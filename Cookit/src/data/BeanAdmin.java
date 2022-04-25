package data;

public class BeanAdmin {

	private int id;
	private String nombre;
	private String email;
	private String pass;
	private String salt;

	// --------------------------------------------------------------

	public BeanAdmin() {
		id = 0;
		nombre = "";
		email = "";
		pass = "";
		salt = "";
	}

	public BeanAdmin(int id, String nombre, String email, String pass, String salt) {
		this.id = id;
		this.nombre = nombre;
		this.email = email;
		this.pass = pass;
		this.salt = salt;
	}

	// --------------------------------------------------------------

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

	// --------------------------------------------------------------

}
