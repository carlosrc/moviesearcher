package es.udc.riws.moviesearcher.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.riws.moviesearcher.api.ApiService;
import es.udc.riws.moviesearcher.lucene.Indexer;
import es.udc.riws.moviesearcher.lucene.Searcher;
import es.udc.riws.moviesearcher.model.Movie;

@RestController
public class SearchRestController {

	@Autowired
	private ApiService apiService;

	@RequestMapping("/index")
	public List<Movie> index() {
		List<Movie> movies = new ArrayList<Movie>();
		// Obtenemos las películas
		movies = apiService.getMovies();

		// Y las indexamos
		Indexer.indexar(movies);

		return movies;
	}

	@RequestMapping("/search")
	public List<Movie> search(@RequestParam(value = "q", required = false) String query,
			@RequestParam(value = "tit", required = false) String title,
			@RequestParam(value = "desc", required = false) String description,
			@RequestParam(value = "year", required = false) Integer year,
			@RequestParam(value = "yearInit", required = false) Integer yearInit,
			@RequestParam(value = "yearEnd", required = false) Integer yearEnd,
			@RequestParam(value = "minVote", required = false) Float minVoteAverage,
			@RequestParam(value = "runtime", required = false) Integer runtime,
			@RequestParam(value = "genres", required = false) String[] genres,
			@RequestParam(value = "cast", required = false) String[] cast,
			@RequestParam(value = "director", required = false) String[] director) {

		List<Movie> movies = new ArrayList<Movie>();
		if (query == null || query.equals("")) {
			// Si la consulta es vacía, mostramos todos los elementos
			query = "*:*";
		}
		movies = Searcher.search(query, title, description, year, yearInit, yearEnd, minVoteAverage, runtime, genres,
				cast, director);
		return movies;
	}

	@RequestMapping("/findSimilar")
	public List<Movie> findSimilar(@RequestParam(value = "id", required = true) long id) {

		List<Movie> movies = new ArrayList<Movie>();

		movies = Searcher.findSimilar(id);
		return movies;
	}

}
