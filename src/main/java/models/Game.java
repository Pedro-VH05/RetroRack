package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un juego.
 */
public class Game {
	private int id;
	private String slug;
	private String name;
	private String releaseDate;
	private boolean tba;
	private String background_image;
	private double rating; // Rating de la comunidad
	private int ratingTop;
	private List<Rating> ratings;
	private int ratingsCount;
	private int reviewsCount;
	private int added;
	private int playtime;
	private Integer metacritic; // Puntuación de Metacritic
	private String esrbRating;
	private List<PlatformWrapper> platforms;
	private List<Genre> genres;
	private List<Shop> shops;
	private List<Tag> tags;
	private List<String> screenshots;
	private String description_raw;

	public Game(int id, String slug, String name, String releaseDate, boolean tba, String background_image,
			String description_raw, double rating, int ratingTop, List<Rating> ratings, int ratingsCount,
			int reviewsCount, int added, int playtime, Integer metacritic, String esrbRating,
			List<PlatformWrapper> platforms, List<Genre> genres, List<Shop> shops, List<Tag> tags,
			List<String> screenshots) {
		this.id = id;
		this.slug = slug;
		this.name = name;
		this.releaseDate = releaseDate;
		this.tba = tba;
		this.background_image = background_image;
		this.description_raw = description_raw;
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
		this.shops = shops;
		this.tags = tags;
		this.screenshots = screenshots;
	}

	// Getters y Setters

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

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
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

	public Integer getMetacritic() {
		return metacritic;
	}

	public void setMetacritic(Integer metacritic) {
		this.metacritic = metacritic;
	}

	public String getEsrbRating() {
		return esrbRating;
	}

	public void setEsrbRating(String esrbRating) {
		this.esrbRating = esrbRating;
	}

	public List<PlatformWrapper> getPlatforms() {
		return platforms != null ? platforms : new ArrayList<>();
	}

	public void setPlatforms(List<PlatformWrapper> platforms) {
		this.platforms = platforms;
	}

	public List<Genre> getGenres() {
		return genres;
	}

	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}

	public List<Shop> getShops() {
		return shops;
	}

	public void setShops(List<Shop> shops) {
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

	public String getDescription() {
		return description_raw;
	}

	public void setDescription(String description) {
		this.description_raw = description;
	}
}