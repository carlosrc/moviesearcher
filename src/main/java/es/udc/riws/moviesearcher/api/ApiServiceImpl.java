package es.udc.riws.moviesearcher.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.udc.riws.moviesearcher.api.apis.TmdbApiServiceImpl;
import es.udc.riws.moviesearcher.model.Movie;

@Service
public class ApiServiceImpl implements ApiService {

//	@Autowired
//	private ImdbApiServiceImpl imdbApiService;
	
	@Autowired
	private TmdbApiServiceImpl tmdbApiService;
	
	@Override
	public List<Movie> getMovies() {
		
		return tmdbApiService.getMovies();
	}

}
