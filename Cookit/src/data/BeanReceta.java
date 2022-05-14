package data;

public class BeanReceta extends BeanPublicacion {
	private int id_receta;
	private int id_categoria;
	private byte[] img;
	private String procedimiento;
	private int tiempo;
	private String ingredientes;
	private String tags;
	
	public BeanReceta() {
		super();
		id_receta = 0;
		id_categoria = 0;
		img = null;
		procedimiento = "";
		tiempo = 0;
		ingredientes = "";
		tags = "";
	}
	
	public BeanReceta(int id, int id_categoria, byte[] img, String procedimiento, int tiempo, String ingredientes, String tags) {
		super();
		this.id_receta = id;
		this.id_categoria = id_categoria;
		this.img = img;
		this.procedimiento = procedimiento;
		this.tiempo = tiempo;
		this.ingredientes = ingredientes;
		this.tags = tags;
	}

	public int getIdReceta() {
		return id_receta;
	}

	public void setId(int id_receta) {
		this.id_receta = id_receta;
	}

//	public int getId_publicacion() {
//		return id_publicacion;
//	}
//
//	public void setId_publicacion(int id_publicacion) {
//		this.id_publicacion = id_publicacion;
//	}

	public int getId_categoria() {
		return id_categoria;
	}

	public void setId_categoria(int id_categoria) {
		this.id_categoria = id_categoria;
	}

	public byte[] getImg() {
		return img;
	}

	public void setImg(byte[] img) {
		this.img = img;
	}

	public String getProcedimiento() {
		return procedimiento;
	}

	public void setProcedimiento(String procedimiento) {
		this.procedimiento = procedimiento;
	}

	public int getTiempo() {
		return tiempo;
	}

	public void setTiempo(int tiempo) {
		this.tiempo = tiempo;
	}

	public String getIngredientes() {
		return ingredientes;
	}

	public void setIngredientes(String ingredientes) {
		this.ingredientes = ingredientes;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}
	
}