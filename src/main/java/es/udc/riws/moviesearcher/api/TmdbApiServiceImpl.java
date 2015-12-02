package es.udc.riws.moviesearcher.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import es.udc.riws.moviesearcher.model.Movie;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

@Service
public class TmdbApiServiceImpl {

	public static final String API_KEY = "956651bac38135dfba7377945f6809a9";

	public List<Movie> getMovies() {

		TmdbMovies moviesTmdb = new TmdbApi(API_KEY).getMovies();
		// MovieDb moviedb = movies.getMovie(5353, "es");

		List<Movie> movies = new ArrayList<Movie>();
		MovieResultsPage results = moviesTmdb.getPopularMovieList("es", 1);
		for (MovieDb result : results) {
			movies.add(new Movie(result.getTitle(), result.getOverview()));
		}

		return movies;

	}

}
