package models;

public class Genre {
	private int id;
	private String nombre;
	private String slug;
	private String imagenDeFondo;

	public Genre(int id, String nombre, String slug, String imagenDeFondo) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.slug = slug;
		this.imagenDeFondo = imagenDeFondo;
	}

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

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getImagenDeFondo() {
		return imagenDeFondo;
	}

	public void setImagenDeFondo(String imagenDeFondo) {
		this.imagenDeFondo = imagenDeFondo;
	}
}
