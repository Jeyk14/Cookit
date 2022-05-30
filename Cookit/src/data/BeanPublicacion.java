package data;

import java.util.Calendar;

public class BeanPublicacion {
	
	private int id_publicacion;
	private int id_usuario;
	private String titulo;
	private String subtitulo;
	private int estrellas;
	private boolean destacado;
	private Calendar fecha;
	private String estado;
	
	public BeanPublicacion() {
		id_publicacion = 0;
		id_usuario = 0;
		titulo = "";
		subtitulo = "";
		estrellas = 0;
		destacado = false;
		fecha = null;
		estado = "";
	}
	
	public BeanPublicacion(int id_publicacion, int id_usuario, String titulo, String subtitulo, int estrellas,
			boolean destacado, Calendar fecha, String estado) {
		super();
		this.id_publicacion = id_publicacion;
		this.id_usuario = id_usuario;
		this.titulo = titulo;
		this.subtitulo = subtitulo;
		this.estrellas = estrellas;
		this.destacado = destacado;
		this.fecha = fecha;
		this.estado = estado;
	}

	public int getIdPublicacion() {
		return id_publicacion;
	}

	public void setIdPublicacion(int id_receta) {
		this.id_publicacion = id_receta;
	}

	public int getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getSubtitulo() {
		return subtitulo;
	}

	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
	}

	public int getEstrellas() {
		return estrellas;
	}

	public void setEstrellas(int estrellas) {
		this.estrellas = estrellas;
	}

	public boolean isDestacado() {
		return destacado;
	}

	public void setDestacado(boolean destacado) {
		this.destacado = destacado;
	}

	public Calendar getFecha() {
		return fecha;
	}

	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

}
