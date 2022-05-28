package data;

public class BeanEspecial{
	
	int idUsuario;
	int idPublicacion;
	int idReceta;
	String titulo;
	String autor;
	String tags;
	int estrellas;
	
	public BeanEspecial () {
		idUsuario = 0;
		idPublicacion = 0;
		idReceta = 0;
		titulo = "";
		autor = "";
		tags = "";
		estrellas = 0;
	}
	
	public BeanEspecial(int idUsuario, int idPublicacion, int idReceta, String titulo, String autor, String tags,
			int estrellas) {
		this.idUsuario = idUsuario;
		this.idPublicacion = idPublicacion;
		this.idReceta = idReceta;
		this.titulo = titulo;
		this.autor = autor;
		this.tags = tags;
		this.estrellas = estrellas;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public int getIdPublicacion() {
		return idPublicacion;
	}

	public void setIdPublicacion(int idPublicacion) {
		this.idPublicacion = idPublicacion;
	}

	public int getIdReceta() {
		return idReceta;
	}

	public void setIdReceta(int idReceta) {
		this.idReceta = idReceta;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public int getEstrellas() {
		return estrellas;
	}

	public void setEstrellas(int estrellas) {
		this.estrellas = estrellas;
	}	

}
