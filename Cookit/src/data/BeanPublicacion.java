package data;

import java.util.Calendar;

public class BeanPublicacion {
	
	private int id_publicacion;
	private int id_usuario;
	private String titulo;
	private String subtitulo;
	private int likes;
	private int dislikes;
	private boolean destacado;
	private Calendar fecha;
	private char estado;
	
	public BeanPublicacion() {
		id_publicacion = 0;
		id_usuario = 0;
		titulo = "";
		subtitulo = "";
		likes = 0;
		dislikes = 0;
		destacado = false;
		fecha = null;
		estado = 'v';
	}
	
	public BeanPublicacion(int id_publicacion, int id_usuario, String titulo, String subtitulo, int likes, int dislikes,
			boolean destacado, Calendar fecha, char estado) {
		super();
		this.id_publicacion = id_publicacion;
		this.id_usuario = id_usuario;
		this.titulo = titulo;
		this.subtitulo = subtitulo;
		this.likes = likes;
		this.dislikes = dislikes;
		this.destacado = destacado;
		this.fecha = fecha;
		this.estado = estado;
	}
	
	public int getStars() {
		
		//5-(((dislikes×100)÷likes)÷10)
		
		int starRate = 0;
		
		if(likes != 0) {
			starRate = dislikes * 100;
			starRate = starRate / likes;
			starRate = starRate / 10;
			starRate = 5 - starRate;
		}
		
		
		return starRate;
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

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getDislikes() {
		return dislikes;
	}

	public void setDislikes(int dislikes) {
		this.dislikes = dislikes;
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

	public char getEstado() {
		return estado;
	}

	public void setEstado(char estado) {
		this.estado = estado;
	}

}
