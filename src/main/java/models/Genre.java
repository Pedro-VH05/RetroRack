package models;

public class Genre {
    private int id;
    private String name; 
    private String slug;
    private String image_background;

    public Genre(int id, String name, String slug, String image_background) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.image_background = image_background;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getImageBackground() {
        return image_background;
    }

    public void setImageBackground(String image_background) {
        this.image_background = image_background;
    }
}