package data;

import java.util.Calendar;

public class BeanListanegra {

	private int id;
	private int id_usuario;
	private Calendar fecha;
	private Calendar alta;
	
	public BeanListanegra() {
		id = 0;
		id_usuario = 0;
		fecha = null;
		alta = null;
	}
	
	public BeanListanegra(int id, int id_usuario, Calendar fecha, Calendar alta) {
		
		this.id = id;
		this.id_usuario = id_usuario;
		this.fecha = fecha;
		this.alta = alta;
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Calendar getAlta() {
		return alta;
	}

	public void setAlta(Calendar alta) {
		this.alta = alta;
	}
	
}
