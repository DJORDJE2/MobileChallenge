package stankovic.djordje.mobilechallenge;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class HttpHelper {

    public URL createUrl(String stringUrl) {
        URL url;
        try{
            url = new URL(stringUrl);
        }catch (MalformedURLException exception){
            System.out.println(exception.toString());
            return null;
        }
        return url;
    }

    public String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if(url == null) return jsonResponse;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try{
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.connect();

            if(httpURLConnection.getResponseCode() == 200){
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readInputStream(inputStream);
            }else{
                System.out.println("Error: Response Code: " + httpURLConnection.getResponseCode());
            }
        }catch (IOException exception){
            System.out.println("Error: While retrieving json data." + exception.toString());
        }finally {
            if(httpURLConnection != null){
                if(inputStream != null) inputStream.close();
                httpURLConnection.disconnect();
            }
        }
        return jsonResponse;
    }

    private String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null){
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }

    PageModel extractPageModel(String json){
        if(TextUtils.isEmpty(json)) return null;
        try{
            JSONObject base = new JSONObject(json);
            String page = base.getString(PageModel.JSON_PAGE);
            List<MovieModel> movies = new ArrayList<>();
            JSONArray results = base.getJSONArray(PageModel.JSON_MOVIES);
            for(int i=0; i<results.length(); i++){

                JSONObject result = results.getJSONObject(i);
                String id = result.getString(MovieModel.JSON_ID);
                String name = result.getString(MovieModel.JSON_NAME);
                String imagePath = result.getString(MovieModel.JSON_IMAGE);
                String rating = result.getString(MovieModel.JSON_RATING);

                List<Integer> genreIds = new ArrayList<>();
                JSONArray genres = result.getJSONArray(MovieModel.JSON_GENRES);
                for(int i2=0; i2<genres.length(); i2++){
                    int genre = Integer.parseInt(genres.getString(i2));
                    genreIds.add(genre);
                }

                MovieModel movieModel = new MovieModel(
                        Integer.parseInt(id), name, imagePath,
                        Float.parseFloat(rating), genreIds);
                movies.add(movieModel);
            }

            return new PageModel(Integer.parseInt(page), movies);
        }catch (JSONException exception){
            System.out.println("Error: While parsing json from inputStream.");
        }
        return null;
    }

    List<GenreModel> extractGenreModel(String json){
        if(TextUtils.isEmpty(json)) return null;
        try{
            JSONObject base = new JSONObject(json);
            List<GenreModel> genres = new ArrayList<>();
            JSONArray genresList = base.getJSONArray(GenreModel.JSON_GENRES);
            for(int i=0; i<genresList.length(); i++){

                JSONObject genre = genresList.getJSONObject(i);
                String id = genre.getString(GenreModel.JSON_ID);
                String name = genre.getString(GenreModel.JSON_NAME);

                GenreModel genreModel = new GenreModel(
                        Integer.parseInt(id), name);
                genres.add(genreModel);
            }

            return genres;
        }catch (JSONException exception){
            System.out.println("Error: While parsing genre json from inputStream.");
        }
        return null;
    }
}
