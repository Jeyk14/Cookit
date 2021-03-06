package data;

import java.util.Calendar;

public class BeanComentario {
	
	private int id;
	private int id_publicacion;
	private int id_usuario;
	private Calendar fecha;
	private String texto;
	private boolean editable;
	private boolean editado;
	
	public BeanComentario() {
		id = 0;
		id_publicacion = 0;
		id_usuario = 0;
		fecha = null;
		texto = "";
		editable = false;
		editado = false;	
	}
	
	public BeanComentario(int id, int id_publicacion, int id_usuario, Calendar fecha, String texto, boolean editable, boolean editado) {
		this.id = id;
		this.id_publicacion = id_publicacion;
		this.id_usuario = id_usuario;
		this.fecha = fecha;
		this.texto = texto;
		this.editable = editable;
		this.editado = editado;
		
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

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isEditado() {
		return editado;
	}

	public void setEditado(boolean editado) {
		this.editado = editado;
	}
	

}
