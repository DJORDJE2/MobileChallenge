package stankovic.djordje.mobilechallenge;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoviesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoviesFragment extends Fragment {
    private static final String BASE_URL_IMAGES = "http://image.tmdb.org/t/p/w185/";
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String BASE_URL_GENRE = "https://api.themoviedb.org/3/genre/movie/list";
    private static final String BASE_URL_TOP_RATED_PART = "top_rated";
    private static final String BASE_URL_MOST_POPULAR_PART = "popular";
    private static final String BASE_URL_API_KEY = "c22d755514350d9836b3f9b173b3d763";
    private static final String BASE_URL_API_PART = "?api_key=" + BASE_URL_API_KEY;
    private static final String BASE_URL_LANGUAGE_PART = "&language=en-US";
    private static final String BASE_URL_PAGE_PART = "&page="; // + page number which is dynamic.

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TITLE = "title";
    private static final String ARG_PAGE = "page";

    private String title;
    private int page;
    private JsonTask task;

    private List<GenreModel> genres;
    private List<MovieModel> movies;
    private Adapter adapter;

    public MoviesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title Parameter 1.
     * @return A new instance of fragment MoviesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoviesFragment newInstance(String title) {
        MoviesFragment fragment = new MoviesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putInt(ARG_PAGE, 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            page = getArguments().getInt(ARG_PAGE);
        }

        genres = new ArrayList<>();
        movies = new ArrayList<>();
        String urlStringGenre = generateGenreJsonURLString();
        String urlString = generateJsonURLString(page);
        URL urlGenre = new HttpHelper().createUrl(urlStringGenre);
        URL url = new HttpHelper().createUrl(urlString);
        if(task == null) task = new JsonTask();
        task.execute(urlGenre, url);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        adapter = new Adapter(getContext(), movies);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }

    private String generateJsonURLString(int page){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(BASE_URL);
        if(title.equals(MainActivity.TITLE_TOP_RATED)) stringBuilder.append(BASE_URL_TOP_RATED_PART);
        else if(title.equals(MainActivity.TITLE_MOST_POPULAR)) stringBuilder.append(BASE_URL_MOST_POPULAR_PART);
        stringBuilder.append(BASE_URL_API_PART);
        stringBuilder.append(BASE_URL_LANGUAGE_PART);
        stringBuilder.append(BASE_URL_PAGE_PART);
        stringBuilder.append(String.valueOf(page));
        return stringBuilder.toString();
    }

    private String generateImageURLString(String imagePath){
        return BASE_URL_IMAGES + imagePath;
    }

    private String generateGenreJsonURLString(){
        return BASE_URL_GENRE + BASE_URL_API_PART + BASE_URL_LANGUAGE_PART;
    }

    private String findGenreName(int id){
        for(int i=0; i<genres.size(); i++){
            if(genres.get(i).getId() == id) return genres.get(i).getName();
        }
        return "No genre";
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView genre;
        public TextView rating;
        public ImageView image;

        ViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.item_card, viewGroup, false));
            name = itemView.findViewById(R.id.card_name);
            genre = itemView.findViewById(R.id.card_genre);
            rating = itemView.findViewById(R.id.card_rating);
            image = itemView.findViewById(R.id.card_image);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    //TODO
                    /*
                        Kreirao bih MovieDetailsActivity kome bih prosledjivao MOVIE_ID koji je selektovan.
                        Ili bih prosirio selektovani CardView uz animaciju i prikazao dodatne podatke.
                        Prikupio bih informacije sa servera ako nemam zapisane na uredjaju ili su promenjeni na serveru.
                     */
                    //Context context = view.getContext();
                    //Intent intent = new Intent(context, MovieDetailsActivity.class);
                    //intent.putExtra(MovieDetailsActivity.EXTRA_MOVIE_ID, id);
                    //context.startActivity(intent);
                }
            });

            ImageButton imageButtonFavourite = itemView.findViewById(R.id.card_favourite);
            imageButtonFavourite.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Added to Favorite.", Snackbar.LENGTH_LONG).show();
                    //TODO
                    /*
                        Ovde bih lokalno na uredjaju cuvao informacije za film koje je dodat u favorites.
                        Postojao bih FavoritesActivity koji prikazuje sve omiljene filmove
                        i kome bi se pristupalo sa glavno ekrana aplikacije.
                        U zavisnosti od klika vrsilo bi se dodavanje ili uklanjanje omiljenog filma iz memorije.
                        Promena ikone bila bi vidljiva korisniku i odgovarajuce obavestenje takodje.

                        Verovatno bih umesto celog FavoritesActivity-a,
                        stavio feature u SearchBar-u na vrhu aplikacije da prikazuje samo favorites,
                        i tako pojednostavio compleksnost korisniku ne ubacujuci dodatni Activity.
                     */
                }
            });
        }
    }

    public class Adapter extends RecyclerView.Adapter<ViewHolder>{
        private final List<MovieModel> movies;

        Adapter(Context context, List<MovieModel> movies){
            this.movies = movies;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()), viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.name.setText(movies.get(i).getName());
            viewHolder.genre.setText(findGenreName(movies.get(i).getGenres().get(0)));
            String rating = "(" + String.valueOf(movies.get(i).getRating()) + ")";
            viewHolder.rating.setText(rating);
            viewHolder.image.setImageResource(R.mipmap.ic_launcher);
            String imageURL = generateImageURLString(movies.get(i).getImagePath());
            new DownloadImageTask(viewHolder.image).execute(imageURL);
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }
    }

    private class JsonTask extends AsyncTask<URL, Void, Void> {
        HttpHelper httpHelper = new HttpHelper();

        @Override
        protected Void doInBackground(URL... urls) {
            URL urlGenre = urls[0];
            URL url = urls[1];
            String jsonResponse = "";
            String jsonResponseGenre = "";
            try{
                jsonResponseGenre = httpHelper.makeHttpRequest(urlGenre);
                jsonResponse = httpHelper.makeHttpRequest(url);
            }catch (IOException exception){
                System.out.println("Error: While making HTTP Request." + exception.toString());
            }
            List<GenreModel> genresList = httpHelper.extractGenreModel(jsonResponseGenre);
            PageModel pageModel = httpHelper.extractPageModel(jsonResponse);
            genres.clear();
            genres.addAll(genresList);
            movies.clear();
            movies.addAll(pageModel.Movies());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            MoviesFragment.this.adapter.notifyDataSetChanged();
            /*
                Kako u tekstu yadatka noje potrebno vrsiti neku napredniju obradu podataka i izracunavanja,
                Lokalno skladistenje bih vrsio snimanjem kompletnih JSON fajlova,
                posebno za TOP_RATED i MOST_POPULAR JSON objekte na internu zasticenu memoriju,
                Slike bih stavljao u Cache ili takodje na internu memoriju yavisno od potrebe i mogucnosti.
             */
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{
        ImageView imageView;

        DownloadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String url = strings[0];
            Bitmap bitmap = null;
            try{
                InputStream inputStream = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            }catch (IOException exception){
                System.out.println("Error: Image Download Failed.");
            }
            return  bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(imageView != null) imageView.setImageBitmap(bitmap);
        }
    }
}
