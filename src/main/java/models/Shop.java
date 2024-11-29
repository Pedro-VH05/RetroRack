package models;

public class Shop {
	private int id;
	private String nombre;
	private String slug;
	private String dominio;
	private String imagenDeFondo;

	public Shop(int id, String nombre, String slug, String dominio, String imagenDeFondo) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.slug = slug;
		this.dominio = dominio;
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

	public String getDominio() {
		return dominio;
	}

	public void setDominio(String dominio) {
		this.dominio = dominio;
	}

	public String getImagenDeFondo() {
		return imagenDeFondo;
	}

	public void setImagenDeFondo(String imagenDeFondo) {
		this.imagenDeFondo = imagenDeFondo;
	}
}
