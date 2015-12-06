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

	@RequestMapping("/indexar")
	public List<Movie> index() {
		System.out.println("indexar");

		List<Movie> movies = apiService.getMovies();
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
			@RequestParam(value = "runtime", required = false) Integer runtime) {
		System.out.println("buscar:" + query); 

		List<Movie> movies = new ArrayList<Movie>();
		if (query == null || query.equals("")) {
			// Si la consulta es vacía, mostramos todos los elementos
			query = "*:*";
		}
		movies = Searcher.buscar(query, title, description, year, yearInit, yearEnd, minVoteAverage, runtime);
		return movies;
	}

}
