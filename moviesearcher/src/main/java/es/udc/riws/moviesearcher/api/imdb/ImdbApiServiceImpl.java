package es.udc.riws.moviesearcher.api.imdb;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import es.udc.riws.moviesearcher.model.Movie;
import es.udc.riws.moviesearcher.util.JsonUtil;

@Service
public class ImdbApiServiceImpl {

	public List<Movie> getMovies() {
		
		try {
			JSONObject object = JsonUtil.readJsonFromUrl("http://www.imdb.com/xml/find?json=1&nr=1&nm=on&q=jeniffer+garner");
			if (object != null) {
				System.out.println(object.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	

}
