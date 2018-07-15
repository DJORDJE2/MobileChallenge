package stankovic.djordje.mobilechallenge;

import android.graphics.drawable.Drawable;

import java.util.List;

public class MovieModel {
    public static final String JSON_ID = "id";
    public static final String JSON_NAME = "title";
    public static final String JSON_IMAGE = "backdrop_path";
    public static final String JSON_RATING = "vote_average";
    public static final String JSON_GENRES = "genre_ids";

    private final int id;
    private final String name;
    private final String imagePath;
    private final float rating;
    private final List<Integer> genres;

    MovieModel(int id, String name, String imagePath,
            float rating, List<Integer> genres){
        this.id = id;
        this.name = name;
        this.imagePath = imagePath;
        this.rating = rating;
        this.genres = genres;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public float getRating() {
        return rating;
    }

    public List<Integer> getGenres() {
        return genres;
    }
}
