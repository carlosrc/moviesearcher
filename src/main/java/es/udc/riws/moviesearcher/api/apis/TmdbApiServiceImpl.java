package es.udc.riws.moviesearcher.api.apis;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import es.udc.riws.moviesearcher.model.Movie;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

@Service
public class TmdbApiServiceImpl {

	public static final String API_KEY = "956651bac38135dfba7377945f6809a9";
	
	private static final int NUM_PAGINAS = 2;

	public List<Movie> getMovies() {

		TmdbMovies moviesTmdb = new TmdbApi(API_KEY).getMovies();

		// Creamos una lista de películas
		List<Movie> movies = new ArrayList<Movie>();
		// Recuperamos las películas en español
		for (int i = 1; i <= NUM_PAGINAS; i++) {
			MovieResultsPage results = moviesTmdb.getPopularMovieList("es", i);
			for (MovieDb result : results) {
				
				// FIXME: Primero comprobar que no existe la película. Redefinir el método equals en Movie
				
				// Creamos una lista de géneros
				List<String> genres = new ArrayList<String>();
				if (result.getGenres() != null) {
					for (Genre genre : result.getGenres()) {
						genres.add(genre.getName());
					}
				}
				// Añadimos las películas recuperadas a la lista de películas
				movies.add(new Movie(result.getTitle(), result.getOverview(), result.getPosterPath(),
						result.getVoteAverage(), genres));

				// Comprobamos se estamos capturando géneros de películas
				// FIXME: No está devolviendo géneros la api de TMDb
				if (result.getGenres() != null && result.getGenres().size() > 0)
					System.out.print(result.getGenres().get(0).getName());
			}
		}

		return movies;

	}

}
