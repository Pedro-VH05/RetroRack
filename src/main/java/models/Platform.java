package models;

public class Platform {
	private int id;
	private String name;
	private String slug;
	private String image;
	private Integer yearEnd;
	private Integer yearStart;
	private int gamesCount;
	private String imageBackground;

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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getYearEnd() {
		return yearEnd;
	}

	public void setYearEnd(Integer yearEnd) {
		this.yearEnd = yearEnd;
	}

	public Integer getYearStart() {
		return yearStart;
	}

	public void setYearStart(Integer yearStart) {
		this.yearStart = yearStart;
	}

	public int getGamesCount() {
		return gamesCount;
	}

	public void setGamesCount(int gamesCount) {
		this.gamesCount = gamesCount;
	}

	public String getImageBackground() {
		return imageBackground;
	}

	public void setImageBackground(String imageBackground) {
		this.imageBackground = imageBackground;
	}

	@Override
	public String toString() {
		return name;
	}

}
