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
    public List<Movie> search(@RequestParam(value="q", defaultValue="") String query) {
        System.out.println("buscar:" + query);
        
        List<Movie> movies = new ArrayList<Movie>();
        if (query != null && !query.equals("")) {
        	movies = Searcher.buscar(query);
        } else {
        	// Si no hay ninguna consulta se devuelven todas las películas
//        	movies = apiService.getMovies();
        }
        return movies;
    }
    
    @RequestMapping("/generateIndex")
    public boolean generateIndex() {
    	
    	return true;
    }
    
}
