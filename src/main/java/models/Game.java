package models;

import java.util.List;

/**
 * Clase Juego
 */
public class Game {
	private int id;
	private String slug;
	private String name;
	private String releaseDate;
	private boolean tba;
	private String background_image;
	private double rating;
	private int ratingTop;
	private List<Rating> ratings;
	private int ratingsCount;
	private int reviewsCount;
	private int added;
	private int playtime;
	private String metacritic;
	private String esrbRating;
	private List<Platform> platforms;
	private List<Genre> genres;
	private List<Shop> shops;
	private List<Tag> tags;
	private List<String> screenshots;

	public Game(int id, String slug, String name, String releaseDate, boolean tba, String background_image,
			double rating, int ratingTop, List<Rating> ratings, int ratingsCount, int reviewsCount, int added,
			int playtime, String metacritic, String esrbRating, List<Platform> platforms, List<Genre> genres,
			List<Shop> tiendas, List<Tag> tags, List<String> screenshots) {
		super();
		this.id = id;
		this.slug = slug;
		this.name = name;
		this.releaseDate = releaseDate;
		this.tba = tba;
		this.background_image = background_image;
		this.rating = rating;
		this.ratingTop = ratingTop;
		this.ratings = ratings;
		this.ratingsCount = ratingsCount;
		this.reviewsCount = reviewsCount;
		this.added = added;
		this.playtime = playtime;
		this.metacritic = metacritic;
		this.esrbRating = esrbRating;
		this.platforms = platforms;
		this.genres = genres;
		this.shops = tiendas;
		this.tags = tags;
		this.screenshots = screenshots;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setRelease(String fechaLanzamiento) {
		this.releaseDate = fechaLanzamiento;
	}

	public boolean isTba() {
		return tba;
	}

	public void setTba(boolean tba) {
		this.tba = tba;
	}

	public String getBackgroundImage() {
		return background_image;
	}

	public void setBackgroundImage(String background_image) {
		this.background_image = background_image;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public int getRatingTop() {
		return ratingTop;
	}

	public void setRatingTop(int ratingTop) {
		this.ratingTop = ratingTop;
	}

	public List<Rating> getRatings() {
		return ratings;
	}

	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}

	public int getRatingsCount() {
		return ratingsCount;
	}

	public void setRatingsCount(int ratingsCount) {
		this.ratingsCount = ratingsCount;
	}

	public int getReviewsCount() {
		return reviewsCount;
	}

	public void setReviewsCount(int reviewsCount) {
		this.reviewsCount = reviewsCount;
	}

	public int getAdded() {
		return added;
	}

	public void setAdded(int added) {
		this.added = added;
	}

	public int getPlaytime() {
		return playtime;
	}

	public void setPlaytime(int playtime) {
		this.playtime = playtime;
	}

	public String getMetacritic() {
		return metacritic;
	}

	public void setMetacritic(String metacritic) {
		this.metacritic = metacritic;
	}

	public String getEsrbRating() {
		return esrbRating;
	}

	public void setEsrbRating(String esrbRating) {
		this.esrbRating = esrbRating;
	}

	public List<Platform> getPlatforms() {
		return platforms;
	}

	public void setPlatforms(List<Platform> plataformas) {
		this.platforms = plataformas;
	}

	public List<Genre> getGeneros() {
		return genres;
	}

	public void setGeneros(List<Genre> genres) {
		this.genres = genres;
	}

	public List<Shop> getShops() {
		return shops;
	}

	public void setTiendas(List<Shop> shops) {
		this.shops = shops;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public List<String> getScreenshots() {
		return screenshots;
	}

	public void setScreenshots(List<String> screenshots) {
		this.screenshots = screenshots;
	}
}