package stankovic.djordje.mobilechallenge;

import java.util.List;

public class PageModel {
    public static final String JSON_PAGE = "page";
    public static final String JSON_MOVIES = "results";

    private final int page;
    private final List<MovieModel> movies;

    public PageModel(int page, List<MovieModel> movies){
        this.page = page;
        this.movies = movies;
    }

    public int Page() {
        return page;
    }

    public List<MovieModel> Movies(){
        return movies;
    }
}
