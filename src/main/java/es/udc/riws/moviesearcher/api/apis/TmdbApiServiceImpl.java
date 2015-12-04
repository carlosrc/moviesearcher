package es.udc.riws.moviesearcher.api.apis;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import es.udc.riws.moviesearcher.model.Movie;
import es.udc.riws.moviesearcher.model.Person;
import es.udc.riws.moviesearcher.model.Person.TypePerson;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.people.PersonCast;

@Service
public class TmdbApiServiceImpl {

	public static final String API_KEY = "956651bac38135dfba7377945f6809a9";

	private static final int NUM_PAGINAS = 1;

	public List<Movie> getMovies() {

		TmdbMovies moviesTmdb = new TmdbApi(API_KEY).getMovies();

		// Creamos una lista de películas
		List<Movie> movies = new ArrayList<Movie>();
		// Recuperamos las películas en español
		for (int i = 1; i <= NUM_PAGINAS; i++) {
			MovieResultsPage results = moviesTmdb.getPopularMovieList("es", i);
			for (MovieDb result : results) {

				MovieDb movie = moviesTmdb.getMovie(result.getId(), "es");

				// FIXME: Primero comprobar que no existe la película.
				// Redefinir
				// el método equals en Movie

				// Creamos una lista de géneros
				List<String> genres = new ArrayList<String>();
				if (movie.getGenres() != null) {
					for (Genre genre : movie.getGenres()) {
						genres.add(genre.getName());
					}
				}

				// Actores
				List<Person> people = new ArrayList<>();
				if (movie.getCast() != null) {
					for (PersonCast personCast : movie.getCast()) {
						people.add(new Person(personCast.getName(), personCast.getCharacter(), personCast.getOrder(),
								TypePerson.CAST));
					}
				}

				// TODO: Directores y escritores. Añadir en people

				// Añadimos las películas recuperadas a la lista de
				// películas
				movies.add(new Movie(movie.getTitle(), movie.getOverview(), movie.getPosterPath(),
						movie.getVoteAverage(), movie.getReleaseDate(), movie.getRuntime(), people, genres, null));
			}
		}

		return movies;

	}

}
