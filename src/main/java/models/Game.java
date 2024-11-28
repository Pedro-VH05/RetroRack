package models;

import java.util.List;

public class Game {
    private String name;
    private String released;
    private float rating;
    private List<Platform> platforms;
    private List<Tag> tags;
    private String background_image;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<Platform> platforms) {
        this.platforms = platforms;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getBackgroundImage() {
        return background_image;
    }

    public void setBackgroundImage(String background_image) {
        this.background_image = background_image;
    }
}
