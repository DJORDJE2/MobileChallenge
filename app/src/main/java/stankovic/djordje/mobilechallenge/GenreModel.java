package stankovic.djordje.mobilechallenge;

public class GenreModel {
    public static final String JSON_GENRES = "genres";
    public static final String JSON_ID = "id";
    public static final String JSON_NAME = "name";

    private final int id;
    private final String name;

    GenreModel(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }
}
