package es.udc.riws.moviesearcher.api.apis;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import es.udc.riws.moviesearcher.model.Movie;
import es.udc.riws.moviesearcher.model.Person;
import es.udc.riws.moviesearcher.model.Person.TypePerson;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.Credits;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.people.PersonCast;
import info.movito.themoviedbapi.model.people.PersonCrew;

/**
 * Librería: https://github.com/holgerbrandl/themoviedbapi/
 * 
 * @author Carlos
 *
 */
@Service
public class TmdbApiServiceImpl {

	public static final String API_KEY = "956651bac38135dfba7377945f6809a9";

	private static final int NUM_PAGINAS = 3;

	private static final int TIME_TO_SLEEP = 8000;

	public List<Movie> getMovies() {

		TmdbApi tmdbApi = new TmdbApi(API_KEY);

		TmdbMovies moviesTmdb = tmdbApi.getMovies();

		// Creamos una lista de películas
		List<Movie> movies = new ArrayList<Movie>();
		// Recuperamos las películas en español
		for (int i = 1; i <= NUM_PAGINAS; i++) {
			if (i > 1) {
				dormir();
			}
			MovieResultsPage results = moviesTmdb.getPopularMovieList("es", i);
			int processedMovies = 0;
			for (MovieDb result : results) {

				MovieDb movie = moviesTmdb.getMovie(result.getId(), "es");

				// FIXME: Primero comprobar que no existe la película.
				// Redefinir el método equals en Movie

				// Creamos una lista de géneros
				List<String> genres = new ArrayList<String>();
				if (movie.getGenres() != null) {
					for (Genre genre : movie.getGenres()) {
						genres.add(genre.getName());
					}
				}

				// Actores
				Credits credits = moviesTmdb.getCredits(result.getId());
				List<Person> people = new ArrayList<>();
				if (credits.getCast() != null) {
					for (PersonCast personCast : credits.getCast()) {
						people.add(new Person(personCast.getName(), personCast.getCharacter(), personCast.getOrder(),
								TypePerson.CAST));
					}
				}

				// Directores y escritores
				if (credits.getCrew() != null) {
					for (PersonCrew personCast : credits.getCrew()) {
						if (personCast.getJob().equals("Director")) {
							people.add(
									new Person(personCast.getName(), personCast.getJob(), null, TypePerson.DIRECTOR));
						} else if (personCast.getJob().equals("Screenplay")) {
							people.add(new Person(personCast.getName(), personCast.getJob(), null, TypePerson.WRITER));
						}
					}
				}

				// Añadimos las películas recuperadas a la lista de
				// películas
				movies.add(new Movie(movie.getTitle(), movie.getOverview(), movie.getPosterPath(),
						movie.getVoteAverage(), movie.getReleaseDate(), movie.getRuntime(), people, genres, null));

				processedMovies++;
				if (processedMovies == 10) {
					dormir();
				}
			}
		}

		return movies;

	}

	private void dormir() {
		try {
			System.out.println("Durmiendo " + TIME_TO_SLEEP + "ms");
			Thread.sleep(TIME_TO_SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
