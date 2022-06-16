package data;

import java.util.Calendar;

public class BeanComentario {
	
	private int id;
	private int id_publicacion;
	private int id_usuario;
	private Calendar fecha;
	private String texto;
	private String estado;
	private boolean editado;
	private int minuto;
	private int hora;
	
	public BeanComentario() {
		id = 0;
		id_publicacion = 0;
		id_usuario = 0;
		fecha = null;
		texto = "";
		estado = "";
		editado = false;	
		hora = 0;
		minuto = 0;
	}
	
	public BeanComentario(int id, int id_publicacion, int id_usuario, Calendar fecha, String texto, String estado, boolean editado, int hora, int minuto) {
		this.id = id;
		this.id_publicacion = id_publicacion;
		this.id_usuario = id_usuario;
		this.fecha = fecha;
		this.texto = texto;
		this.estado = estado;
		this.editado = editado;
		this.hora = hora;
		this.minuto = minuto;
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_publicacion() {
		return id_publicacion;
	}

	public void setId_publicacion(int id_publicacion) {
		this.id_publicacion = id_publicacion;
	}

	public int getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}

	public Calendar getFecha() {
		return fecha;
	}

	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String editable) {
		this.estado = editable;
	}

	public boolean isEditado() {
		return editado;
	}

	public void setEditado(boolean editado) {
		this.editado = editado;
	}

	public int getMinuto() {
		return minuto;
	}

	public void setMinuto(int minuto) {
		this.minuto = minuto;
	}

	public int getHora() {
		return hora;
	}

	public void setHora(int hora) {
		this.hora = hora;
	}

}
