package es.udc.riws.moviesearcher.api.apis;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import es.udc.riws.moviesearcher.model.Movie;
import es.udc.riws.moviesearcher.util.JsonUtil;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

@Service
public class ImdbApiServiceImpl {

	public static final String API_KEY = "";
	
	public List<Movie> getMovies() {
		
		try {
			JSONObject object = JsonUtil.readJsonFromUrl("http://www.imdb.com/xml/find?json=1&nr=1&nm=on&q=jeniffer+garner");
			if (object != null) {
				System.out.println(object.toString());
//				String titulo = object.getString("title");
//				String descripcion = object.getString("description");
//				Movie movie = new Movie(titulo, descripcion);
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	

}
